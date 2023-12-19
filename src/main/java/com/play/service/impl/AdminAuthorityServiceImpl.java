package com.play.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.dao.AdminMenuDao;
import com.play.model.AdminMenu;
import com.play.model.vo.MenuVo;
import com.play.service.AdminAuthorityService;
import com.play.spring.config.i18n.I18nTool;

@Service
public class AdminAuthorityServiceImpl implements AdminAuthorityService {
	@Autowired
	private AdminMenuDao adminMenuDao;

	@Override
	public Set<String> getPermissionSet(Long groupId, Long stationId) {
		if (groupId == null || groupId < 0) {
			return new HashSet<>();
		}
		String key = new StringBuilder(Constants.cache_key_admin_perm).append(groupId).append(":sset:")
				.append(stationId).toString();
		Set<String> set = CacheUtil.getCache(CacheKey.ADMIN, key, Set.class);
		if (set != null) {
			return set;
		}
		List<AdminMenu> menus = adminMenuDao.findByGroupId(groupId, stationId);
		if (menus != null && !menus.isEmpty()) {
			set = new HashSet<>();
			for (AdminMenu m : menus) {
				set.add(m.getPermName());
			}
			CacheUtil.addCache(CacheKey.ADMIN, key, set);
		}
		return set;
	}

	@Override
	public List<MenuVo> getNavMenuVo(Long groupId, Long stationId) {
		if (groupId == null) {
			return null;
		}
		String key = new StringBuilder(Constants.cache_key_admin_perm).append(groupId).append(":smvo:")
				.append(stationId).toString();
		List<MenuVo> menuVos = null;
		String json = CacheUtil.getCache(CacheKey.ADMIN, key);
		if (json != null) {
			menuVos = JSON.parseArray(json, MenuVo.class);
		}
		if (menuVos == null || menuVos.isEmpty()) {
			List<AdminMenu> menus = adminMenuDao.findByGroupId(groupId, stationId);
			if (menus != null && !menus.isEmpty()) {
				menuVos = toNavMenuVo(menus);
				CacheUtil.addCache(CacheKey.ADMIN, key, menuVos);
			}
		}
		return menuVos;
	}

	private List<MenuVo> toNavMenuVo(List<AdminMenu> menuList) {
		List<MenuVo> list = null;
		List<MenuVo> children = null;
		MenuVo mv = null;
		Map<Long, List<MenuVo>> listMap = new HashMap<>();
		for (AdminMenu am : menuList) {
			if (am.getType() >= Constants.MENU_TYPE_OPERATE && StringUtils.isEmpty(am.getUrl())) {
				continue;
			}
			list = listMap.get(am.getParentId().longValue());
			if (list == null) {
				list = new ArrayList<>();
				listMap.put(am.getParentId().longValue(), list);
			}
			mv = toMenuVo(am);
			children = listMap.get(am.getId().longValue());
			if (children == null) {
				children = new ArrayList<>();
				listMap.put(am.getId().longValue(), children);
			}
			mv.setChildren(children);
			list.add(mv);
		}
		for (Long l : listMap.keySet()) {
			list = listMap.get(l);
			Collections.sort(list);
		}
		list = listMap.get(0L);
		list.get(0).setSpread(true);
		return list;
	}

	private MenuVo toMenuVo(AdminMenu am) {
		MenuVo v = new MenuVo();
		v.setId(am.getId());
		if (StringUtils.isNotEmpty(am.getCode())) {
			v.setTitle(I18nTool.getMessage(am.getCode(), am.getTitle()));
		} else {
			v.setTitle(am.getTitle());
		}
		v.setHref(am.getUrl());
		v.setIcon(am.getIcon());
		v.setSortNo(am.getSortNo());
		v.setSpread(false);
		v.setType(am.getType());
		return v;
	}
}
