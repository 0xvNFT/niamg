package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationFloatFrame;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class StationFloatFrameDao extends JdbcRepository<StationFloatFrame> {

	public Page<StationFloatFrame> page(Long stationId, String language, Integer showPage, Integer platform) {
		StringBuilder sb = new StringBuilder("select * from station_float_frame");
		sb.append(" where station_id = :stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (platform != null) {
			sb.append(" and platform=:platform");
			map.put("platform", platform);
		}
		if (StringUtils.isNotEmpty(language)) {
			sb.append(" and language=:language");
			map.put("language", language);
		}
		if (showPage != null) {
			sb.append(" and show_page=:showPage");
			map.put("showPage", showPage);
		}
		sb.append(" order by show_page asc");
		return super.queryByPage(sb.toString(), map);
	}

	public void delete(Long id, Long stationId) {
		super.update("delete from station_float_frame where id = :id and station_id = :stationId",
				MapUtil.newHashMap("id", id, "stationId", stationId));
	}

	public List<StationFloatFrame> find(Long stationId, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("status", status);
		return super.find(
				"select * from station_float_frame where station_id=:stationId and status=:status order by show_page asc",
				map);
	}

	public List<StationFloatFrame> find(Long stationId, Integer status, Integer showPage, Integer platform) {
		StringBuilder sb = new StringBuilder("select * from station_float_frame");
		sb.append(" where status = :status and station_id = :stationId and platform = :platform");
		Map<String, Object> map = new HashMap<>();
		if (platform == null) {
			platform = 1;
		}
		map.put("platform", platform);
		if (showPage != null) {
			sb.append(" and show_page = :showPage");
			map.put("showPage", showPage);
		}
		map.put("stationId", stationId);
		map.put("status", status);
		return super.find(sb.toString(), map);
	}

	public void updStatus(Long id, Integer status, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" update station_float_frame set status=:status,update_time=:time");
		sb.append(" where id =:id and station_id = :stationId");
		map.put("time", new Date());
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update(sb.toString(), map);
	}

}
