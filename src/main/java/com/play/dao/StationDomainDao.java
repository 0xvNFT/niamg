package com.play.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationDomain;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点域名信息
 *
 * @author admin
 *
 */
@Repository
public class StationDomainDao extends JdbcRepository<StationDomain> {

	public Page<StationDomain> getPage(Long partnerId, Long stationId, String name, String proxyUsername, Integer type,
			Integer minType, Integer platform) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from station_domain where 1=1");
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" and station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(name)) {
			sql.append(" and name=:name");
			map.put("name", name);
		}
		if (StringUtils.isNotEmpty(proxyUsername)) {
			sql.append(" and proxy_username=:proxyUsername");
			map.put("proxyUsername", proxyUsername);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		if (minType != null) {
			sql.append(" and type>=:minType");
			map.put("minType", minType);
		}
		if (platform != null) {
			sql.append(" and platform=:platform");
			map.put("platform", platform);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public void changeStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update station_domain set status=:status where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public boolean exist(String name, Long escapeId) {
		StringBuilder sql = new StringBuilder("select count(*) from station_domain ");
		sql.append(" where name=:name");
		Map<String, Object> map = new HashMap<>();
		map.put("escapeId", escapeId);
		map.put("name", name);
		if (escapeId != null) {
			sql.append(" and id!=:escapeId");
		}
		return queryForLong(sql.toString(), map) > 0;
	}

	public void updateTypeAndRemark(Long id, Integer type, Integer ipMode, String remark) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("type", type);
		map.put("ipMode", ipMode);
		map.put("remark", remark);
		update("update station_domain set type=:type,ip_mode=:ipMode,remark=:remark where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public void modifyDomain(Long id, Integer type, Integer ipMode, Long proxyId, String proxyUsername,
			String agentPromoCode, String agentName, String defaultHome, Long srotNo, String remark) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("type", type);
		map.put("ipMode", ipMode);
		map.put("remark", remark);
		map.put("proxyId", proxyId);
		map.put("proxyUsername", proxyUsername);
		map.put("agentPromoCode", agentPromoCode);
		map.put("agentName", agentName);
		map.put("defaultHome", defaultHome);
		map.put("srotNo", srotNo);
		StringBuilder sql = new StringBuilder("update station_domain set type=:type,ip_mode=:ipMode,");
		sql.append("remark=:remark,agent_promo_code=:agentPromoCode,agent_name=:agentName,proxy_id=:proxyId,");
		sql.append("proxy_username=:proxyUsername,default_home=:defaultHome,sort_no=:srotNo where id=:id");
		update(sql.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public StationDomain findByDomain(String domain) {
		String key = new StringBuilder("domain:").append(domain).toString();
		StationDomain ssb = CacheUtil.getCache(CacheKey.STATION_DOMAIN, key, StationDomain.class);
		if (ssb != null) {
			return ssb;
		}
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put("domainName", domain);
		ssb = findOne("select * from station_domain where name=:domainName", paramMap);
		if (ssb != null) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, key, ssb);
		}
		return ssb;
	}

	public void updSwitchStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update station_domain set switch_domain=:status where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public void modifyRemark(Long id, Long stationId, String remark) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		map.put("remark", remark);
		update("update station_domain set remark=:remark where id=:id and station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public List<String> listDomainByStationId(Long stationId, Integer limit, Integer switchDomain) {
		StringBuilder sql = new StringBuilder("SELECT name FROM station_domain WHERE status=2");
		sql.append(" and station_id=:stationId and type=:type");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("type", StationDomain.TYPE_FRONT);
		if (switchDomain != null) {
			map.put("switchDomain", switchDomain);
			sql.append(" and switch_domain =:switchDomain");
		}
		sql.append(" order by  sort_no desc");
		if (limit != null) {
			sql.append(" limit ").append(limit);
		}
		return find(sql.toString(), map, String.class);
	}

	public Map<String, String> getAllStationDomain() {
		StringBuilder sql = new StringBuilder("SELECT a.name,b.code");
		sql.append(" FROM station_domain a left join station b on a.station_id=b.id");
		sql.append(" WHERE a.status=2 and b.status=2");
		sql.append(" and a.platform=:platform and b.code is not null");
		Map<String, Object> map = new HashMap<>();
		map.put("platform", StationDomain.PLATFORM_STATION);
		Map<String, String> rmap = new HashMap<String, String>();
		find(sql.toString(), new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				rmap.put(rs.getString(1), rs.getString(2));
				return null;
			}
		}, map);
		return rmap;
	}

}
