package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.model.StationConfig;
import com.play.model.vo.StationConfigVo;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 站点配置信息
 *
 * @author admin
 *
 */
@Repository
public class StationConfigDao extends JdbcRepository<StationConfig> {

	public StationConfig findOne(Long stationId, String key) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("key", key);
		return findOne("select * from station_config where station_id=:stationId and key=:key", map);
	}

	public List<StationConfig> getAll(Long stationId, Integer visible) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select * from station_config where station_id=:stationId");
		if (visible != null) {
			map.put("visible", visible);
			sb.append(" and visible=:visible");
		}
		sb.append(" order by sort_no desc");
		return find(sb.toString(), map);
	}

	public void deleteByStationId(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		update("delete from station_config where station_id=:stationId", map);
	}

	public void updateForBitch(StationConfig c) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", c.getStationId());
		map.put("key", c.getKey());
		map.put("title", c.getTitle());
		map.put("groupName", c.getGroupName());
		map.put("eleType", c.getEleType());
		map.put("visible", c.getVisible());
		map.put("sortNo", c.getSortNo());
		update("update station_config set title=:title,group_name=:groupName,ele_type=:eleType,visible=:visible,sort_no=:sortNo where station_id=:stationId and key=:key",
				map);
	}

	public void deleteForBatch(Long stationId, Set<String> set) {
		if (set == null || set.isEmpty()) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("delete from station_config where station_id=");
		sql.append(stationId).append(" and key in(");
		for (String key : set) {
			sql.append("'").append(key).append("',");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		update(sql.toString());
	}

	public void update(Long stationId, String key, String value) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("key", key);
		map.put("value", value);
		update("update station_config set value=:value where station_id=:stationId and key=:key", map);
	}
	

	public List<StationConfigVo> findAllConfig(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return find("select key,value from station_config where station_id=:stationId",
				new BeanPropertyRowMapper<StationConfigVo>(StationConfigVo.class), map);
	}
}
