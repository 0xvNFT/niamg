package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationFloatFrameSetting;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class StationFloatFrameSettingDao extends JdbcRepository<StationFloatFrameSetting> {

	public List<StationFloatFrameSetting> find(Long affId) {
		Map<String, Object> map = new HashMap<>();
		map.put("affId", affId);
		return super.find(
				"select * from station_float_frame_setting where afr_id=:affId  order by img_sort desc",
				map);
	}

	public void deleteByAffId(Long affId ) {
		Map<String, Object> map = new HashMap<>();
		map.put("affId", affId);
		super.update("delete from station_float_frame_setting where afr_id=:affId", map);
	}
}
