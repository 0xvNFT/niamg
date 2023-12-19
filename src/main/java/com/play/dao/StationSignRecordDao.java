package com.play.dao;

import com.play.common.utils.DateUtil;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationSignRecord;
import com.play.orm.jdbc.JdbcRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员签到日志表
 *
 * @author admin
 *
 */
@Repository
public class StationSignRecordDao extends JdbcRepository<StationSignRecord> {
	public Page<StationSignRecord> getPage(Long stationId, String username, String signIp, Date startDate,
			Date endDate) {
		Map<String, Object> paramMap = MapUtil.newHashMap();
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_sign_record WHERE 1=1");
		if (stationId != null) {
			sql_sb.append(" AND station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql_sb.append(" AND username = :username");
			paramMap.put("username", username);
		}
		if (StringUtils.isNotEmpty(signIp)) {
			sql_sb.append(" AND ip = :signIp");
			paramMap.put("signIp", signIp);
		}
		if (null != startDate) {
			sql_sb.append(" AND sign_date >= :startDate");
			paramMap.put("startDate", startDate);
		}
		if (null != endDate) {
			sql_sb.append(" AND sign_date < :endDate");
			paramMap.put("endDate", endDate);
		}
		sql_sb.append(" ORDER BY sign_date DESC");
		return super.queryByPage(sql_sb.toString(), paramMap);
	}

	public List<StationSignRecord> getMemberRecords(long userId, Date startDate, Date endDate) {
		Map<String, Object> paramMap = MapUtil.newHashMap("userId", userId, "startDate", startDate, "endDate", endDate);
		String sql = "select * from station_sign_record where user_id = :userId and sign_date > :startDate and sign_date < :endDate ";
		return super.find(sql, paramMap);
	}

	/**
	 * 获取用户当天签到数据
	 */
	public StationSignRecord getUserSignByDay(Long stationId, Long userId, Date date) {
		StringBuilder sb = new StringBuilder("select * from station_sign_record");
		sb.append(" where user_id = :userId and station_id =:stationId");
		sb.append(" and TO_CHAR(sign_date, 'YYYY-MM-DD') =:date");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("date", DateUtil.toDateStr(date));
		return findOne(sb.toString(), params);
	}

	/**
	 * 前端数据展示
	 * 
	 * @return
	 */
	public List<StationSignRecord> find(Long userId, Long stationId, Date startDate, Date endDate) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		StringBuilder sb = new StringBuilder("select * from station_sign_record");
		sb.append(" where user_id=:userId and station_id=:stationId");
		if (startDate != null) {
			map.put("startDate", startDate);
			sb.append(" and sign_date >= :startDate");
		}
		if (endDate != null) {
			map.put("endDate", endDate);
			sb.append(" and sign_date < :endDate");
		}
		sb.append(" order by sign_date desc");
		return super.find(sb.toString(), map);
	}

	/**
	 * 前端数据展示
	 * 
	 * @return
	 */
	public StationSignRecord findNewOne(Long userId, Long stationId, Date startDate, Date endDate) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		StringBuilder sb = new StringBuilder("select * from station_sign_record");
		sb.append(" where user_id=:userId and station_id=:stationId");
		if (startDate != null) {
			map.put("startDate", startDate);
			sb.append(" and sign_date > :startDate");
		}
		if (endDate != null) {
			map.put("endDate", endDate);
			sb.append(" and sign_date < :endDate");
		}
		sb.append(" order by id desc limit 1");
		return super.findOne(sb.toString(), map);
	}

	public int countUserSignNum(Date begin, Date end, String signIp, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(*) from station_sign_record where station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and sign_date>=:begin");
			map.put("begin", begin);
		}
		if (begin != null) {
			sb.append(" and sign_date <:end");
			map.put("end", end);
		}
		if (StringUtils.isNotEmpty(signIp)) {
			sb.append(" and ip =:ip");
			map.put("ip", signIp);
		}
		return queryForInt(sb.toString(), map);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder("delete from station_sign_record");
		sb.append(" where station_id =:stationId and sign_date <= :createTime");
		Map<String, Object> map = new HashMap<>();
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

}
