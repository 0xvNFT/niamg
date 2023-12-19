package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.Partner;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商信息表
 *
 * @author admin
 *
 */
@Repository
public class PartnerDao extends JdbcRepository<Partner> {

	public List<Partner> getAll() {
		String json = CacheUtil.getCache(CacheKey.PARTNER, "all");
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, Partner.class);
		}
		List<Partner> list = find("select * from partner order by id desc");
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.PARTNER, "all", list);
		}
		return list;
	}

	public Page<Partner> page() {
		return queryByPage("select * from partner order by id desc");
	}

	@Override
	public Partner save(Partner t) {
		t = super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.PARTNER);
		return t;
	}

	public Partner findOne(Long id) {
		String key = new StringBuilder("id:").append(id).toString();
		Partner p = CacheUtil.getCache(CacheKey.PARTNER, key, Partner.class);
		if (p != null) {
			return p;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		p = findOne("select * from partner where id=:id", map);
		if (p != null) {
			CacheUtil.addCache(CacheKey.PARTNER, key, p);
		}
		return p;
	}

	@Override
	public int update(Partner t) {
		int i = super.update(t);
		CacheUtil.delCacheByPrefix(CacheKey.PARTNER);
		return i;
	}
}
