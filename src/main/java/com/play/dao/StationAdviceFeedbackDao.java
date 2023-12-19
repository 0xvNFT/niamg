package com.play.dao;

import com.play.model.StationAdviceFeedback;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 建议反馈
 *
 * @author admin
 */
@Repository
public class StationAdviceFeedbackDao extends JdbcRepository<StationAdviceFeedback> {

	public Page<StationAdviceFeedback> getPage(Long stationId, Integer sendType, String sendUsername, Long sendUserId,
			Date begin, Date end) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_advice_feedback");
		sb.append(" WHERE station_id=:stationId");
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
		if (StringUtils.isNotEmpty(sendUsername)) {
			sb.append(" AND send_username =:sendUsername ");
			paramMap.put("sendUsername", sendUsername);
		}
		if (sendUserId != null) {
			sb.append(" AND send_user_id =:sendUserId ");
			paramMap.put("sendUserId", sendUserId);
		}
		sb.append(" ORDER BY id DESC");
		return super.queryByPage(sb.toString(), paramMap);
	}

	public Page<StationAdviceFeedback> userCenterPage(Long userId, Long stationId, Date begin, Date end, Integer status) {
		StringBuilder sb = new StringBuilder("select * from station_advice_feedback");
		sb.append(" where station_id =:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and create_time >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_time <= :end");
			params.put("end", end);
		}
		if (userId != null) {
			sb.append(" and send_user_id=:userId");
			params.put("userId", userId);
		}
		if (status != null) {
			sb.append(" and status=:status");
			params.put("status", status);
		}
		sb.append(" order by id DESC");
		return queryByPage(sb.toString(), params);
	}

	public int countDailyAdviceNum(Date begin, Date end, Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(*) from station_advice_feedback");
		Map<String, Object> params = new HashMap<>();
		sb.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		if (userId != null) {
			sb.append(" and send_user_id =:userId");
			params.put("userId", userId);
		}
		if (end != null) {
			sb.append(" and create_time <= :end");
			params.put("end", end);
		}
		if (begin != null) {
			sb.append(" and create_time >= :begin");
			params.put("begin", begin);
		}
		return queryForInt(sb.toString(), params);
	}
}
