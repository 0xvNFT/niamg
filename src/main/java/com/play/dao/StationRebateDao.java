package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationRebate;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点返点设置信息
 *
 * @author admin
 *
 */
@Repository
public class StationRebateDao extends JdbcRepository<StationRebate> {

	public Page<StationRebate> getPage(Long stationId, Integer userType, Integer type) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_rebate WHERE 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null) {
			sql_sb.append(" and station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (userType != null) {
			sql_sb.append(" AND user_type= :userType");
			paramMap.put("userType", userType);
		}
		if (type != null) {
			sql_sb.append(" AND type = :type");
			paramMap.put("type", type);
		}
		sql_sb.append(" ORDER BY id DESC");
		return super.queryByPage(sql_sb.toString(), paramMap);
	}

    /*public StationRebate getStationRebate(Long stationId){
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_rebate WHERE 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null) {
			sql_sb.append(" and station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		sql_sb.append(" AND user_type = 2");
		paramMap.put("userType", 2);
		sql_sb.append(" ORDER BY id DESC");
		return super.findOne(sql_sb.toString(), paramMap);
	}*/


	public StationRebate findOne(Long stationId, Integer userType) {
		String key = getKey(stationId, userType);
		StationRebate sr = CacheUtil.getCache(CacheKey.REBATE_SCALE, key, StationRebate.class);
		if (sr != null) {
			return sr;
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("userType", userType);
		sr = super.findOne("SELECT * FROM station_rebate WHERE station_id=:stationId AND user_type=:userType",
				paramMap);
		CacheUtil.addCache(CacheKey.REBATE_SCALE, key, sr);
		return sr;
	}

	private String getKey(Long stationId, Integer userType) {
		return new StringBuilder("sid").append(stationId).append(":ut:").append(userType).toString();
	}

	@Override
	public int update(StationRebate t) {
		int i = super.update(t);
		CacheUtil.delCache(CacheKey.REBATE_SCALE, getKey(t.getStationId(), t.getUserType()));
		return i;
	}

}
