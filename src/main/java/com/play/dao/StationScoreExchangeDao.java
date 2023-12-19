package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationScoreExchange;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 *
 *
 * @author admin
 *
 */
@Repository
public class StationScoreExchangeDao extends JdbcRepository<StationScoreExchange> {
	public Page<StationScoreExchange> getPage(Integer type, String name, Long stationId) {
		StringBuilder sql = new StringBuilder("SELECT * FROM station_score_exchange");
		sql.append(" WHERE station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (type != null) {
			sql.append(" AND type = :type");
			paramMap.put("type", type);
		}
		if (name != null) {
			sql.append(" AND name LIKE :name");
			paramMap.put("name", name + "%");
		}
		sql.append(" ORDER BY id DESC");
		return super.queryByPage(sql.toString(), paramMap);
	}

	public List<StationScoreExchange> getProgressConfig(Integer type, Integer status, Long stationId) {
		StringBuilder sql = new StringBuilder("SELECT * FROM station_score_exchange");
		sql.append(" WHERE station_id = :stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (type != null) {
			sql.append(" AND type = :type");
			map.put("type", type);
		}
		if (status != null) {
			sql.append(" AND status = :status");
			map.put("status", status);
		}
		return super.find(sql.toString(), map);
	}

	public StationScoreExchange findOne(Integer type, Long stationId, Integer status) {
		StringBuilder sql = new StringBuilder("SELECT * FROM station_score_exchange");
		sql.append(" WHERE station_id = :stationId");
		sql.append(" AND type =:type AND status =:status order by id DESC limit 1");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("type", type);
		map.put("status", status);
		return findOne(sql.toString(), map);
	}

	public int updStatus(Long id, Integer status, Long stationId) {
		StringBuilder sb = new StringBuilder("update station_score_exchange set status=:status");
		sb.append(" where id =:id and station_id = :stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		return update(sb.toString(), map);
	}

	public List<StationScoreExchange> findByStationId(Long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		return find("select * from station_score_exchange where station_id=:stationId and status=2", params);
	}
}
