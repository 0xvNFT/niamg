package com.play.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.core.ScoreRecordTypeEnum;
import com.play.model.SysUserScoreHistory;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 积分变动记录表
 *
 * @author admin
 *
 */
@Repository
public class SysUserScoreHistoryDao extends JdbcRepository<SysUserScoreHistory> {

	public Page<SysUserScoreHistory> getRecordPage(Long stationId, String username, Date begin, Date end, Integer type,
			Long userId) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM sys_user_score_history WHERE 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null && stationId > 0) {
			sql_sb.append(" AND station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql_sb.append(" AND username = :username");
			paramMap.put("username", username);
		}
		if (type != null) {
			sql_sb.append(" AND type = :type");
			paramMap.put("type", type);
		}
		if (begin != null) {
			sql_sb.append(" AND create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sql_sb.append(" AND create_datetime < :end");
			paramMap.put("end", end);
		}
		if (userId != null) {
			sql_sb.append(" AND user_id =:userId");
			paramMap.put("userId", userId);
		}
		sql_sb.append(" ORDER BY create_datetime DESC");
		return super.queryByPage(sql_sb.toString(), paramMap);
	}

	public void batchScoreToZero(Long stationId, String remark) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("WITH upd AS (SELECT a.username,a.id,a.partner_id,");
		sb.append("b.score FROM sys_user a left join sys_user_score b ON a.id=b.user_id");
		sb.append(" WHERE a.station_id =:stationId)");
		sb.append(" INSERT INTO sys_user_score_history(before_score,score,after_score,type,");
		sb.append("create_datetime,user_id,username,station_id,partner_id,remark )SELECT ");
		sb.append("score,score,:afterScore,:type,:createDatetime,id,username,:stationId,");
		sb.append("partner_id,:remark FROM upd");
		params.put("afterScore", BigDecimal.ZERO);
		params.put("type", ScoreRecordTypeEnum.SUB_ARTIFICIAL.getType());
		params.put("createDatetime", new Date());
		params.put("stationId", stationId);
		params.put("remark", remark);
		super.update(sb.toString(), params);
	}
}
