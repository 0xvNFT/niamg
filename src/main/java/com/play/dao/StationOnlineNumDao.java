package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.common.utils.DateUtil;
import com.play.model.StationOnlineNum;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 *
 */
@Repository
public class StationOnlineNumDao extends JdbcRepository<StationOnlineNum> {
	public void updateNum(Date date, Long stationId, Integer onlineNum, int minuteLoginNum, int dayLoginNum) {
		StringBuilder sb = new StringBuilder("insert into station_online_num as a (station_id,");
		sb.append("report_datetime,stat_date,stat_minute,online_num,minute_login_num,day_login_num)");
		sb.append(" values(:stationId,:date,:statDate,:statMinute,:onlineNum,:minuteLoginNum,:dayLoginNum)");
		sb.append(" ON conflict(station_id,stat_date,stat_minute) DO update set ");
		sb.append("online_num=(case when a.online_num>:onlineNum then a.online_num else :onlineNum end)");
		sb.append(",report_datetime=(case when a.online_num>:onlineNum then a.report_datetime else :date end)");
		sb.append(",minute_login_num =:minuteLoginNum");
		sb.append(",day_login_num=(case when a.day_login_num>:dayLoginNum then a.day_login_num else :dayLoginNum end)");

		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("date", date);
		params.put("statDate", DateUtil.toDateStr(date));
		params.put("statMinute", DateUtil.formatDate(date, "HH:mm"));
		params.put("onlineNum", onlineNum);
		params.put("minuteLoginNum", minuteLoginNum);
		params.put("dayLoginNum", dayLoginNum);
		update(sb.toString(), params);
	}

	public int getOnlineNum(Date date, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("date", DateUtil.toDateStr(date));
		params.put("stationId", stationId);
		try {
			return queryForInt(
					"select max(online_num) from station_online_num where stat_date=:date and station_id=:stationId",
					params);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 按分钟，只统计一天内的数据
	 * 
	 * @param stationId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<StationOnlineNum> getDataByMinute(Long stationId, Date begin, Date end) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append("select * from station_online_num where 1=1");
		if (begin != null) {
			sb.append(" and report_datetime >=:begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and report_datetime <=:end");
			params.put("end", end);
		}
		if (stationId != null) {
			sb.append(" and station_id =:stationId");
			params.put("stationId", stationId);
		}
		sb.append(" order by stat_minute");
		return find(sb.toString(), params);
	}

	/**
	 * 按天来统计
	 * 
	 * @param stationId
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<StationOnlineNum> getDataByDay(Long stationId, Date begin, Date end) {
		StringBuilder sb = new StringBuilder("select station_id,max(online_num) as online_num,");
		sb.append("sum(minute_login_num) as minute_login_num,(COALESCE(day_login_num,0)) as day_login_num,");
		sb.append("stat_date from station_online_num where 1=1 ");
		Map<String, Object> params = new HashMap<>();
		if (begin != null) {
			sb.append(" and report_datetime >=:begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and report_datetime <=:end");
			params.put("end", end);
		}
		if (stationId != null) {
			sb.append(" and station_id =:stationId");
			params.put("stationId", stationId);
		}
		sb.append(" group by station_id,stat_date,day_login_num order by stat_date");
		return find(sb.toString(), params);
	}
}
