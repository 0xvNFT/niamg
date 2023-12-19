package com.play.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.Agent;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点代理商信息表
 *
 * @author admin
 *
 */
@Repository
public class AgentDao extends JdbcRepository<Agent> {
	private static final String CACHE_KEY_PROMOCODE = "promo:";
	private static final String CACHE_KEY_ID = "id:";

	public Page<Agent> page(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return queryByPage("select * from agent where station_id=:stationId order by id desc", map);
	}

	public boolean existName(Long stationId, String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("stationId", stationId);
		return queryForInt("select count(*) from agent where name=:name and station_id=:stationId", map) > 0;
	}

	public boolean existPromoCode(Long stationId, String promoCode) {
		Map<String, Object> map = new HashMap<>();
		map.put("promoCode", promoCode);
		map.put("stationId", stationId);
		return queryForInt("select count(*) from agent where promo_code=:promoCode and station_id=:stationId", map) > 0;
	}

	@Override
	public Agent save(Agent t) {
		t = super.save(t);
		CacheUtil.delCache(CacheKey.AGENT, CACHE_KEY_ID + t.getId());
		return t;
	}

	@Override
	public Agent findOne(Long id, Long stationId) {
		if (id == null || id <= 0) {
			return null;
		}
		Agent a = CacheUtil.getCache(CacheKey.AGENT, CACHE_KEY_ID + id, Agent.class);
		if (a == null || !a.getStationId().equals(stationId)) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("stationId", stationId);
			a = findOne("select * from agent where id=:id and station_id=:stationId", map);
			addCache(a);
		}
		return a;
	}

	private void addCache(Agent a) {
		if (a != null) {
			StringBuilder pkey = new StringBuilder(CACHE_KEY_PROMOCODE);
			pkey.append(a.getPromoCode()).append(":s:").append(a.getStationId());
			CacheUtil.addCache(CacheKey.AGENT, pkey.toString(), a.getId());
			CacheUtil.addCache(CacheKey.AGENT, CACHE_KEY_ID + a.getId(), a);
		}
	}

	public Agent findOneByPromoCode(String promoCode, Long stationId) {
		if (StringUtils.isEmpty(promoCode)) {
			return null;
		}
		Agent a = null;
		StringBuilder pkey = new StringBuilder(CACHE_KEY_PROMOCODE);
		pkey.append(promoCode).append(":s:").append(stationId);
		Long id = CacheUtil.getCache(CacheKey.AGENT, pkey.toString(), Long.class);
		if (id != null) {
			a = CacheUtil.getCache(CacheKey.AGENT, CACHE_KEY_ID + id, Agent.class);
		}
		if (a == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("promoCode", promoCode.trim().toLowerCase());
			map.put("stationId", stationId);
			a = findOne("select * from agent where promo_code=:promoCode and station_id=:stationId", map);
			addCache(a);
		}
		return a;
	}

	public void updateInfo(Agent agent) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", agent.getId());
		map.put("stationId", agent.getStationId());
		map.put("name", agent.getName());
		map.put("promoCode", agent.getPromoCode());
		map.put("ap", agent.getAccessPage());
		map.put("remark", agent.getRemark());
		update("update agent set remark=:remark,name=:name,promo_code=:promoCode,access_page=:ap where id=:id and station_id=:stationId",
				map);
		CacheUtil.delCache(CacheKey.AGENT, CACHE_KEY_ID + agent.getId());
	}

	@Override
	public int deleteById(Serializable id) {
		int i = super.deleteById(id);
		CacheUtil.delCache(CacheKey.AGENT, CACHE_KEY_ID + id);
		return i;
	}

	public void changeStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		map.put("stationId", stationId);
		update("update agent set status=:status where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.AGENT, CACHE_KEY_ID + id);
	}

	public List<Agent> find(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return find("select * from agent where station_id=:stationId order by id desc", map);
	}

	public Agent findByName(String name, Long stationId) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("name", name);
		return findOne("select * from agent where station_id=:stationId and name=:name", map);
	}
}
