package com.play.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.SysConfig;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 系统配置信息
 *
 * @author admin
 *
 */
@Repository
public class SysConfigDao extends JdbcRepository<SysConfig> {
	@Override
	public SysConfig findOne(String key) {
		String ckey = "sys:config:" + key;
		String json = CacheUtil.getCache(CacheKey.SYS_CONFIG, ckey);
		if (json != null) {
			if (json.equals("")) {
				return null;
			}
			return JSON.parseObject(json, SysConfig.class);
		}
		SysConfig c = findOne("select * from sys_config where key=:key", "key", key);
		if (c != null) {
			CacheUtil.addCache(CacheKey.SYS_CONFIG, ckey, c);
		} else {
			CacheUtil.addCache(CacheKey.SYS_CONFIG, ckey, "");
		}
		return c;
	}

	public List<SysConfig> getAll() {
		return find("select * from sys_config");
	}

	@Override
	public SysConfig save(SysConfig t) {
		SysConfig c = super.save(t);
		CacheUtil.addCache(CacheKey.SYS_CONFIG, "sys:config:" + t.getKey(), c);
		return c;
	}

	public int update(SysConfig t) {
		int i = super.update(t);
		CacheUtil.addCache(CacheKey.SYS_CONFIG, "sys:config:" + t.getKey(), t);
		return i;
	}
}
