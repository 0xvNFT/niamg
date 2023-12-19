package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
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
public class SysDomainDao extends JdbcRepository<StationDomain> {

	public StationDomain findByDomain(String domain) {
		StationDomain ssb = CacheUtil.getCache(CacheKey.STATION_DOMAIN, ":domain:" + domain, StationDomain.class);
		if (ssb != null) {
			return ssb;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name,status,type,ip_mode,customer_id");
		sql.append(" from sys_domain where name=:domainName");
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put("domainName", domain);
		ssb = findOne(sql.toString(), paramMap);
		if (ssb != null) {
			CacheUtil.addCache(CacheKey.STATION_DOMAIN, ":domain:" + domain, ssb);
		}
		return ssb;
	}

	public Page<StationDomain> getPage(Long customerId, String name, Integer type) {
		StringBuilder sql = new StringBuilder("select * from sys_domain where 1=1");
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(name)) {
			sql.append(" and name=:name");
			map.put("name", name);
		}
		if (customerId != null) {
			sql.append(" and customer_id=:customerId");
			map.put("customerId", customerId);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public void changeStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update sys_domain set status=:status where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	@Override
	public StationDomain save(StationDomain t) {
		t = super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
		return t;
	}

	public boolean exist(String name, Long escapeId) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_domain ");
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
		update("update sys_domain set type=:type,ip_mode=:ipMode,remark=:remark where id=:id", map);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_DOMAIN);
	}

	public List<StationDomain> findByCustomerId(Long customerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("customerId", customerId);
		return find("select * from sys_domain where customer_id=:customerId order by type", map);
	}

	public String getByCustomerId(Long customerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("customerId", customerId);
		map.put("type", StationDomain.TYPE_FRONT);
		map.put("status", Constants.STATUS_ENABLE);
		return queryForString(
				"select name from sys_domain where customer_id=:customerId and type=:type and status=:status limit 1",
				map);
	}

}
