package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationTurntable;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 转盘活动表
 *
 * @author admin
 *
 */
@Repository
public class StationTurntableDao extends JdbcRepository<StationTurntable> {

	public Page<StationTurntable> getPage(Long stationId, Date begin, Date end) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_turntable");
		sql_sb.append(" WHERE station_id = :stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		if (begin != null) {
			sql_sb.append(" AND create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sql_sb.append(" AND create_datetime < :end");
			paramMap.put("end", end);
		}
		sql_sb.append(" ORDER BY create_datetime DESC");
		return super.queryByPage(sql_sb.toString(), paramMap);
	}

	public List<StationTurntable> find(Long stationId, Integer joinType) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_turntable");
		sql_sb.append(" WHERE station_id =:stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		if (joinType != null) {
			sql_sb.append(" AND join_type=:joinType");
			paramMap.put("joinType", joinType);
		}
		return find(sql_sb.toString(), paramMap);
	}

	public void updateStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE station_turntable SET status=:status WHERE id =:id and station_id=:stationId", map);
	}

	public void updateAwardCount(Long id, Long stationId, Integer awardCount) {
		Map<String, Object> map = new HashMap<>();
		map.put("awardCount", awardCount);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE station_turntable SET award_count=:awardCount WHERE id =:id and station_id=:stationId", map);
	}

	public StationTurntable getProgress(Long stationId, Integer status, Long degreeId, Long groupId) {
		if (degreeId == null && groupId == null) {
			return null;
		}
		StringBuilder sql = new StringBuilder("SELECT * FROM station_turntable");
		sql.append(" WHERE station_id = :stationId ");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		if (status != null) {
			sql.append(" AND status = :status");
			paramMap.put("status", status);
		}
		if (degreeId != null) {
			sql.append(" AND degree_ids ~:degreeId");
			paramMap.put("degreeId", "," + degreeId + ",");
		}
		if (groupId != null) {
			sql.append(" AND group_ids ~:groupId");
			paramMap.put("groupId", "," + groupId + ",");
		}
		return super.findOne(sql.toString(), paramMap);
	}

	public List<StationTurntable> getList(Long stationId, Integer status, Long degreeId) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_turntable");
		sql_sb.append(" WHERE station_id = :stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		if (status != null) {
			sql_sb.append(" AND status = :status");
			paramMap.put("status", status);
		}
		if (degreeId != null) {
			sql_sb.append(" AND degree_ids ~:degreeId");
			paramMap.put("degreeId", "," + degreeId + ",");
		}
		return super.find(sql_sb.toString(), paramMap);
	}

	public Integer getOpenActiveNum(Integer status, Long stationId) {
		StringBuilder sql_sb = new StringBuilder("SELECT count(id) FROM station_turntable");
		sql_sb.append(" WHERE station_id = :stationId and status =:status ");
		return super.queryForInt(sql_sb.toString(), MapUtil.newHashMap("stationId", stationId, "status", status));
	}

}
