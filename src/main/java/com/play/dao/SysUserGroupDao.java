package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.model.SysUserGroup;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 会员组别信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserGroupDao extends JdbcRepository<SysUserGroup> {

	public Page<SysUserGroup> getPage(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return queryByPage("select * from sys_user_group where station_id=:stationId order by id asc", map);
	}

	@Override
	public SysUserGroup findOne(Long id, Long stationId) {
		if (id == null || stationId == null) {
			return null;
		}
		String key = ":id" + id + ":s" + stationId;
		SysUserGroup l = CacheUtil.getCache(CacheKey.USER_GROUP, key, SysUserGroup.class);
		if (l != null) {
			return l;
		}
		l = super.findOne(id, stationId);
		if (l != null) {
			BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(l.getName());
			if (baseI18nCode != null) {
				l.setName(I18nTool.getMessage(baseI18nCode));
			}
		}
        if (l != null) {
            CacheUtil.addCache(CacheKey.USER_GROUP, key, l);
        }
		return l;
	}


	public SysUserGroup findOne(Long id, Long stationId, String name) {
		StringBuilder sb = new StringBuilder("select * from sys_user_group");
		sb.append(" where station_id =:stationId and status=:status and name=:name");
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.STATUS_ENABLE);
		map.put("stationId", stationId);
		map.put("name", name);
		if (null!=id) {
			sb.append(" and id !=:id");
			map.put("id", id);
		}
		return findOne(sb.toString(), map);
	}

	@Override
	public SysUserGroup save(SysUserGroup t) {
		t = super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
		return t;
	}

	@Override
	public int update(String sql) {
		int i = super.update(sql);
		CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
		return i;
	}

	public void updateStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE sys_user_group SET status=:status WHERE id =:id and station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
	}

	public List<SysUserGroup> find(Long stationId, Integer status) {
		String key = new StringBuilder("s:").append(stationId).append(":s:").append(status).toString();
		String json = CacheUtil.getCache(CacheKey.USER_GROUP, key);
//		if (StringUtils.isNotEmpty(json)) {
//			return JSON.parseArray(json, SysUserGroup.class);
//		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from sys_user_group");
		sql.append(" where station_id=:stationId");
		if (status != null) {
			sql.append(" and status=:status");
			map.put("status", status);
		}
		sql.append(" order by id asc");
		List<SysUserGroup> list = find(sql.toString(), map);
		for (SysUserGroup sysUserGroup : list){
			BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(sysUserGroup.getName());
			if (baseI18nCode != null) {
				sysUserGroup.setName(I18nTool.getMessage(baseI18nCode));
			}
//			else {
//				sysUserGroup.setName(I18nTool.getMessage(BaseI18nCode.groupDefault));
//			}
		}
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.USER_GROUP, key, list);
		}
		return list;
	}

	public void restatMemberCount(Long stationId) {
		queryForInt("select restat_member_count(" + stationId + ")");
		CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
	}

	public SysUserGroup getDefault(Long stationId) {
		String key = "default:" + stationId;
		SysUserGroup g = CacheUtil.getCache(CacheKey.USER_GROUP, key, SysUserGroup.class);
		if (g != null) {
			return g;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.STATUS_ENABLE);
		map.put("stationId", stationId);
		map.put("orig", Constants.USER_ORIGINAL);
		g = super.findOne(
				"select * from sys_user_group where station_id=:stationId and status=:status and original=:orig", map);
		if (g != null) {
			CacheUtil.addCache(CacheKey.USER_GROUP, key, g);
		}
		BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(g.getName());
		if (baseI18nCode != null) {
			g.setName(I18nTool.getMessage(baseI18nCode));
		}
		return g;
	}

}
