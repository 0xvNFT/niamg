package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationRegisterConfig;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 站点注册页面有哪些字段
 *
 * @author admin
 *
 */
@Repository
public class StationRegisterConfigDao extends JdbcRepository<StationRegisterConfig> {

	public List<StationRegisterConfig> find(Long stationId, Integer platform, Integer show) {
		String key = new StringBuilder("sid:").append(stationId).append(":p:").append(platform).append(":show:")
				.append(show).toString();
		String json = CacheUtil.getCache(CacheKey.REGISTER_CONFIG, key);
		StringBuilder sb = new StringBuilder();
		if (json != null) {
			return JSON.parseArray(json, StationRegisterConfig.class);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("platform", platform);
		sb.append("select * from station_register_config where station_id=:stationId and platform=:platform ");
		if (show != null) {
			map.put("show", show);
			sb.append(" and show=:show  ");
		}
		sb.append(" order by sort_no desc ");
		List<StationRegisterConfig> list = find(sb.toString(), map);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.REGISTER_CONFIG, key, list);
		}
		return list;
	}

	public void updateProp(Long id, Long stationId, String prop, Integer value) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("id", id);
		map.put("value", value);
		update("update station_register_config set " + prop + "=:value where id=:id and station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.REGISTER_CONFIG);
	}

	public void updateSortNo(Long id, Long stationId, Long sortNo, String name, String tips) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("id", id);
		map.put("sortNo", sortNo);
		StringBuilder sb = new StringBuilder("update station_register_config set sort_no=:sortNo");
		if (StringUtils.isNotEmpty(name)) {
			sb.append(" ,name=:name");
			map.put("name", name);
		}
//        if(StringUtils.isNotEmpty(tips)) {
		sb.append(" ,tips=:tips");
		map.put("tips", tips);
//        }
		sb.append(" where id=:id and station_id=:stationId");
		update(sb.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.REGISTER_CONFIG);
	}

	@Override
	public StationRegisterConfig save(StationRegisterConfig t) {
		t = super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.REGISTER_CONFIG);
		return t;
	}
}
