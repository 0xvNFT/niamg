package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.Station;
import com.play.model.SysUserLogin;
import com.play.model.vo.StationComboVo;
import com.play.model.vo.StationOnlineNumVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点信息
 *
 * @author admin
 */
@Repository
public class StationDao extends JdbcRepository<Station> {
	private static final String ALL_KEY = "ALL:KEY";

	public Page<Station> getPage(Long partnerId, String code, String name) {
		StringBuilder sql = new StringBuilder("select * from station where 1=1");
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(code)) {
			sql.append(" and code=:code");
			map.put("code", code);
		}
		if (StringUtils.isNotEmpty(name)) {
			sql.append(" and name=:name");
			map.put("name", name);
		}
		if (partnerId != null && partnerId > 0) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	@Override
	public Station save(Station t) {
		super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
		return t;
	}

	public List<Station> findAll() {
		String json = CacheUtil.getCache(CacheKey.STATION_DOMAIN, ALL_KEY);
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, Station.class);
		}
		List<Station> list = find("select * from station order by code asc");
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, ALL_KEY, list);
		}
		return list;
	}

	public void changeStatus(Long id, Long partnerId, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		StringBuilder sql = new StringBuilder("update station set status=:status where id=:id");
		if (partnerId != null && partnerId > 0) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		update(sql.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public void modify(Long id, String name, String language, String currency) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		map.put("language", language);
		map.put("currency", currency);
		update("update station set name=:name,language=:language,currency=:currency where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public Long getNextId() {
		return queryForLong("select nextval('station_id_seq')");
	}

	public void changeBgStatus(Long id, Long partnerId, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		StringBuilder sql = new StringBuilder("update station set bg_status=:status where id=:id");
		if (partnerId != null && partnerId > 0) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		update(sql.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public List<StationComboVo> getCombo(Long customerId, Long apiId) {
		String json = CacheUtil.getCache(CacheKey.STATION_DOMAIN, "station:combo");
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, StationComboVo.class);
		}
		StringBuilder sql = new StringBuilder("select * from sys_station where status=2");
		Map<String, Object> map = new HashMap<>();
		if (customerId != null) {
			sql.append(" and customer_id=:customerId");
			map.put("customerId", customerId);
		}
		if (apiId != null && apiId > 0) {
			sql.append(" and api_id=:apiId");
			map.put("apiId", apiId);
		}
		sql.append(" order by id desc");
		List<StationComboVo> list = find(sql.toString(),
				new BeanPropertyRowMapper<StationComboVo>(StationComboVo.class), map);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, "station:combo", list);
		}
		return list;
	}

	public Station findOneById(Long id, Long partnerId) {
		String key = new StringBuilder("id:").append(id).append(":").append(partnerId).toString();
		Station station = CacheUtil.getCache(CacheKey.STATION_DOMAIN, key, Station.class);
		if (station != null) {
			return station;
		}

		StringBuilder sql = new StringBuilder("select * from station where id=:id");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		if (partnerId != null && partnerId > 0) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		station = findOne(sql.toString(), map);
		if (station != null) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, key, station);
		}
		return station;
	}

	public Station findOneById(Long id) {
		String key = new StringBuilder("id:").append(id).toString();
		Station station = CacheUtil.getCache(CacheKey.STATION_DOMAIN, key, Station.class);
		if (station == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			station = super.findOne("select * from station where id=:id", map);
			if (station != null) {
				CacheUtil.addCache(CacheKey.STATION_DOMAIN, key, station);
			}
		}
		return station;
	}

	public Station findOneByCode(String code) {
		String key = new StringBuilder("c:").append(code).toString();
		Station station = CacheUtil.getCache(CacheKey.STATION_DOMAIN, key, Station.class);
		if (station == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("code", code);
			station = findOne("select * from station where code=:code", map);
			if (station != null) {
				CacheUtil.addCache(CacheKey.STATION_DOMAIN, key, station);
			}
		}
		return station;
	}

	public List<StationComboVo> getCombo(Long partnerId) {
		String json = CacheUtil.getCache(CacheKey.STATION_DOMAIN, "station:combo");
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, StationComboVo.class);
		}
		StringBuilder sql = new StringBuilder("select id,name||'['||code||']' as name");
		sql.append(" from station where status=2");
		Map<String, Object> map = new HashMap<>();
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		sql.append(" order by id desc");
		List<StationComboVo> list = find(sql.toString(),
				new BeanPropertyRowMapper<StationComboVo>(StationComboVo.class), map);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, "station:combo", list);
		}
		return list;
	}

	public boolean exist(String code) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		return count("select count(*) from station where code=:code", map) > 0;
	}

	public List<StationOnlineNumVo> getManagerOnlineNum() {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select b.code as code ,b.name as name,");
		sb.append(" (select count(a.user_id) from sys_user_login a where a.station_id = b.id and ");
		sb.append(" a.online_status =:onlineStatus)");
		sb.append(" as onlineNum");
		sb.append(" from station b");
		map.put("onlineStatus", SysUserLogin.STATUS_ONLINE_ON);
		return find(sb.toString(), new BeanPropertyRowMapper<StationOnlineNumVo>(StationOnlineNumVo.class), map);
	}

	public Station findByDomain(String domainName) {
		Station s = CacheUtil.getCache(CacheKey.STATION_DOMAIN, "station:" + domainName, Station.class);
		if (s == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("domain", domainName);
			s = findOne(
					"select s.* from station s left join station_domain d on s.id=d.station_id where d.name=:domain",
					map);
			if (s != null) {
				CacheUtil.addCache(CacheKey.STATION_DOMAIN, "station:" + domainName, s);
			}
		}
		return s;
	}

	public void modifyCode(Long id, String code) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("code", code);
		StringBuilder sql = new StringBuilder("update station set code=:code where id=:id");
		update(sql.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}
}
