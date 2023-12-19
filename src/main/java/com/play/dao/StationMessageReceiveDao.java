package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.common.Constants;
import com.play.model.StationMessageReceive;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

/**
 * 站点站内信接收表
 *
 * @author admin
 */
@Repository
public class StationMessageReceiveDao extends JdbcRepository<StationMessageReceive> {
	public void delByMessageId(Long messageId) {
		super.update("DELETE FROM station_message_receive WHERE message_id = :messageId",
				MapUtil.newHashMap("messageId", messageId));
	}

	/**
	 * 批量发送站内信
	 *
	 * @param messageId
	 * @param levelGroup
	 */
	public void batchSendMessage(Long messageId, Long stationId, Long degreeId, Long groupId, Integer sendType,
			Date lastLoginTime, String proxyName, Integer popStatus) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("WITH upd AS (SELECT id,username FROM sys_user");
		sb.append(" WHERE station_id =:stationId");
		sb.append(" and id in(select user_id from sys_user_login where station_id=:stationId");
		sb.append(" and last_login_datetime notnull and last_login_datetime>=:lastLoginTime)");
		params.put("lastLoginTime", lastLoginTime);
		params.put("stationId", stationId);
		if (degreeId != null) {
			sb.append(" AND degree_id =:degreeId ");
			params.put("degreeId", degreeId);
		}
		if (groupId != null) {
			sb.append(" AND group_id =:groupId ");
			params.put("groupId", groupId);
		}
		if (StringUtils.isNotEmpty(proxyName)) {
			sb.append(" and (username = :proxyName or parent_names like :proxyNameCn)");
			params.put("proxyName", proxyName);
			params.put("proxyNameCn", "%," + proxyName + ",%");
		}
		sb.append(" )");
		sb.append(" INSERT INTO station_message_receive(station_id,user_id,username,message_id,status,pop_status");
		sb.append(",send_type) SELECT :stationId,id,username,:messageId,:status,:popStatus,:sendType FROM upd");
		params.put("messageId", messageId);
		params.put("status", StationMessageReceive.STATUS_UNREAD);
		params.put("sendType", sendType);
		params.put("popStatus", popStatus);
		super.update(sb.toString(), params);
	}


    public int countUnreadMsgNum(Long userId, Long stationId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("select count(*) from station_message_receive");
        sb.append(" where user_id =:userId and pop_status = :popStatus");
        sb.append(" and station_id=:stationId and status=:status");
        params.put("userId", userId);
        params.put("stationId", stationId);
        params.put("status", StationMessageReceive.STATUS_UNREAD);
        params.put("popStatus", Constants.STATUS_ENABLE);
        return queryForInt(sb.toString(), params);
    }

	public int countUnreadMsgNumForApp(Long userId, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select count(*) from station_message_receive");
		sb.append(" where user_id =:userId ");
		sb.append(" and station_id=:stationId and status=:status");
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("status", StationMessageReceive.STATUS_UNREAD);
		return queryForInt(sb.toString(), params);
	}

    public int updateUnReadMsg(Long userId, Long stationId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("update station_message_receive set pop_status=:popStatus");
        sb.append(" where user_id =:userId and pop_status = :oldPopStatus");
        sb.append(" and station_id=:stationId and status=:status");
        params.put("userId", userId);
        params.put("stationId", stationId);
        params.put("status", StationMessageReceive.STATUS_UNREAD);
        params.put("popStatus", Constants.STATUS_DISABLE);
        params.put("oldPopStatus", Constants.STATUS_ENABLE);
        return update(sb.toString(), params);
    }

	public void clearGenerateData(Long maxMessageId) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("maxMessageId", maxMessageId);
		update(" delete from station_message_receive  where message_id <=:maxMessageId", paramMap);
	}

	public void allReceiveRead(Long userId, Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		update("update station_message_receive set status=2 where user_id=:userId and station_id=:stationId", paramMap);
	}

	public void allReceiveDelete(Long userId, Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		update("delete from station_message_receive where user_id=:userId and station_id=:stationId", paramMap);
	}

	public String findSingleReceiveUsername(Long messageId, Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("stationId", stationId);
		paramMap.put("messageId", messageId);
		return findObject(
				"select string_agg(username, ',') from station_message_receive where message_id=:messageId and station_id=:stationId limit 10",
				paramMap, String.class);
	}

	public void delete(Long id, Long userId, Long stationId) {
		if (id == null || id == 0) {
			return;
		}
		Map<String, Object> paramMap = MapUtil.newHashMap();
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		paramMap.put("id", id);
		update("delete from station_message_receive where id=:id and user_id=:userId and station_id=:stationId",
				paramMap);
	}
}
