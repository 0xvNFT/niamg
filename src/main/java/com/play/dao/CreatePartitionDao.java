package com.play.dao;

import com.play.common.utils.DateUtil;
import com.play.orm.jdbc.JdbcRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CreatePartitionDao extends JdbcRepository {

	public int createLogPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_log_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_sys_log_partition(:tableName,:startDate ,:endDate)", map);
	}

	public int createDailyMoneyPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_user_daily_money_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_sys_user_daily_money_partition(:tableName,:startDate ,:endDate)", map);
	}

	public int createUserBonusPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_user_bonus_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_sys_user_bonus_partition(:tableName,:startDate ,:endDate)", map);
	}

	public int createMoneyHistoryPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_user_money_history_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_money_history_partition(:tableName,:startDate ,:endDate)", map);
	}

	public int createBetHistoryPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_user_bet_num_history_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_bet_num_history_partition(:tableName,:startDate ,:endDate)", map);		
	}

	public int createTronRecordPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "tron_trans_user_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_tron_record_partition(:tableName,:startDate ,:endDate)", map);
	}

	public int createScoreHistoryPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "sys_user_score_history_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_score_history_partition(:tableName,:startDate ,:endDate)", map);			
	}

	public int createKickbackRecordPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "station_kickback_record_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_station_kickback_record_partition(:tableName,:startDate ,:endDate)", map);	
	}

	public int createProxyDailyBetStatPartition(Date date) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", "proxy_daily_bet_stat_" + DateUtil.formatDate(date, "yyyyMM"));
		map.put("startDate", DateUtil.formatDate(date, "yyyy-MM-01 00:00:00"));
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		map.put("endDate", DateUtil.formatDate(c.getTime(), "yyyy-MM-01 00:00:00"));
		return queryForInt("select create_proxy_daily_bet_stat_partition(:tableName,:startDate ,:endDate)", map);			
	}
}
