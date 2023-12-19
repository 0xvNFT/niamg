package com.play.dao;

import com.play.model.StationMessage;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 站点站内信发送表
 *
 * @author admin
 *
 */
@Repository
public class StationMessageDao extends JdbcRepository<StationMessage> {
	public Page<StationMessage> getPage(Long stationId, Integer sendType, Integer receiveType, String title,
			String sendUser, Long sendUserId, String receiveUser, Date begin, Date end) {
		StringBuilder sb = new StringBuilder("SELECT * from station_message WHERE station_id = :stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and create_time >=:begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sb.append("  and create_time <=:end");
			paramMap.put("end", end);
		}
		if (sendType != null) {
			sb.append(" AND send_type =:sendType");
			paramMap.put("sendType", sendType);
		}
		if (receiveType != null) {
			sb.append(" AND receive_type =:receiveType");
			paramMap.put("receiveType", receiveType);
		}
		if (StringUtils.isNotEmpty(title)) {
			sb.append(" AND title LIKE :title");
			paramMap.put("title", title + "%");
		}
		if (StringUtils.isNotEmpty(sendUser)) {
			sb.append(" AND send_username like :sendUser");
			paramMap.put("sendUser", "%(" + sendUser + ")%");
		}
		if (sendUserId != null) {
			sb.append(" AND send_user_id =:sendUserId");
			paramMap.put("sendUserId", sendUserId);
		}
		if (StringUtils.isNotEmpty(receiveUser)) {
			sb.append(" and id in(select message_id from station_message_receive");
			sb.append(" where station_id=:stationId and username=:username)");
			paramMap.put("username", receiveUser);
		}
		sb.append(" ORDER BY id DESC");
		return super.queryByPage(sb.toString(), paramMap);
	}

	public Page<StationMessage> getMemberPage(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		sb.append("SELECT m.*,COALESCE(r.id,0) as receive_message_id ,COALESCE(r.status,1) as status ");
		sb.append(" FROM station_message m LEFT JOIN station_message_receive r on r.message_id = m.id");
		sb.append(" WHERE m.station_id =:stationId and r.user_id =:userId");
		sb.append(" order by r.status ASC,m.create_time DESC ");
		return queryByPage(sb.toString(), paramMap);
	}

	public List<StationMessage> getMessageList(Long stationId, Integer sendType, Integer receiveType, String title,
			String sendUser, Long sendUserId, String receiveUser, Date begin, Date end) {
		StringBuilder cols = new StringBuilder();
		StringBuilder tabs = new StringBuilder();
		StringBuilder whs = new StringBuilder();
		StringBuilder ods = new StringBuilder();
		cols.append("SELECT m.*");
		tabs.append(" FROM station_message m");
		whs.append(" WHERE 1=1");
		Map<String, Object> paramMap = MapUtil.newHashMap();
		if (begin != null) {
			whs.append(" and m.create_time >=:begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			whs.append("  and m.create_time <=:end");
			paramMap.put("end", end);
		}
		if (sendType != null) {
			whs.append(" AND m.send_type =:sendType");
			paramMap.put("sendType", sendType);
		}
		if (stationId != null) {
			whs.append(" AND m.station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(title)) {
			whs.append(" AND m.title LIKE :title");
			paramMap.put("title", title + "%");
		}
		if (StringUtils.isNotEmpty(sendUser)) {
			whs.append(" AND m.send_username like :sendUser ");
			paramMap.put("sendUser", "%(" + sendUser + ")%");
		}
		if (receiveType != null) {
			whs.append(" and m.receive_type =:receiveType");
			paramMap.put("receiveType", receiveType);
		}
		if (sendUserId != null) {
			whs.append(" AND m.send_user_id =:sendUserId ");
			paramMap.put("sendUserId", sendUserId);
		}
		if (receiveType != null
				&& (receiveType == StationMessage.TYPE_SELECT_USER || receiveType == StationMessage.TYPE_SINGLE)) {
			cols.append(",array_to_string(array_agg(r.username),',') as receive_user");
			tabs.append(" left join station_message_receive r on m.id = r.message_id");
			ods.append(" group by m.id");
			if (StringUtils.isNotEmpty(receiveUser)) {
				whs.append(" and r.username =:receiveUser");
				paramMap.put("receiveUser", receiveUser);
			}
		}
		ods.append(" ORDER BY  m.create_time DESC");
		return find(cols.append(tabs.append(whs.append(ods))).toString(), paramMap);
	}

	public long getMaxMessageId(Date date) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("date", date);
		return queryForLong(" select max(id) from station_message  where create_time <=:date", paramMap);
	}

	public void clearGenerateData(Date date) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("date", date);
		update(" delete from station_message  where create_time <=:date", paramMap);
	}

	public List<StationMessage> getPopMessageList(Long stationId, String receiveUser,String sort) {
		StringBuilder sql = new StringBuilder("SELECT m.* FROM station_message m");
		sql.append(" left join station_message_receive r on m.id = r.message_id");
		sql.append(" WHERE m.pop_status = 2 and r.status = 1 and m.send_type = 3");
		sql.append(" AND m.station_id = :stationId and r.username =:receiveUser");
		if (StringUtils.isNotEmpty(sort)) {
			sql.append(" group by m.id ORDER BY  m.create_time "+sort);
		}else{
			sql.append(" group by m.id ORDER BY  m.create_time ASC");
		}
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("stationId", stationId);
		paramMap.put("receiveUser", receiveUser);
		return find(sql.toString(), paramMap);
	}

	public List<StationMessage> getPopMessageList(Long stationId, String receiveUser) {
		return getPopMessageList(stationId, receiveUser, null);
	}
}
