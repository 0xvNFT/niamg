package com.play.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.UserTypeEnum;
import com.play.dao.ManagerMenuDao;
import com.play.dao.ManagerUserAuthDao;
import com.play.dao.ManagerUserDao;
import com.play.model.ManagerMenu;
import com.play.model.ManagerUser;
import com.play.model.ManagerUserAuth;
import com.play.model.vo.MenuVo;
import com.play.service.ManagerMenuService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginManagerUtil;

/**
 * 总控菜单
 *
 * @author admin
 *
 */
@Service
public class ManagerMenuServiceImpl implements ManagerMenuService {

	@Autowired
	private ManagerMenuDao managerMenuDao;
	@Autowired
	private ManagerUserAuthDao managerUserAuthDao;
	@Autowired
	private ManagerUserDao managerUserDao;
	private static final String cache_key = "cw:perm:uid:";
	private static final String nav_menu_key = "cw:manager:menus:";

	@Override
	public Set<String> getPermissionSet(Long id) {
		if (id == null || id <= 0) {
			return new HashSet<>();
		}
		String key = cache_key + id;
		Set<String> set = CacheUtil.getCache(CacheKey.MANAGER, key, Set.class);
		if (set != null) {
			return set;
		}
		List<ManagerMenu> menus = managerMenuDao.findByUserId(id);
		if (menus != null && !menus.isEmpty()) {
			set = new HashSet<>();
			for (ManagerMenu m : menus) {
				set.add(m.getPermName());
			}
			CacheUtil.addCache(CacheKey.MANAGER, key, set);
		}
		return set;
	}

	@Override
	public Set<Long> getPermissionIdSet(Long id) {
		if (id == null || id <= 0) {
			return new HashSet<>();
		}
		Set<Long> set = new HashSet<>();
		List<ManagerMenu> menus = managerMenuDao.findByUserId(id);
		if (menus != null && !menus.isEmpty()) {
			for (ManagerMenu m : menus) {
				set.add(m.getId());
			}
		}
		return set;
	}

	@Override
	public List<MenuVo> getAllMenuVo() {
		List<ManagerMenu> menu = managerMenuDao.findAllActive();
		if (menu != null && !menu.isEmpty()) {
			return toNavMenuVo(menu, true);
		}
		return null;
	}

	@Override
	public List<MenuVo> getNavMenuVo() {
		List<MenuVo> menuVos = null;
		ManagerUser u = LoginManagerUtil.currentUser();
		String key = nav_menu_key + u.getId();
		String json = CacheUtil.getCache(CacheKey.MANAGER, key);
		if (json != null) {
			menuVos = JSON.parseArray(json, MenuVo.class);
		}
		if (menuVos == null || menuVos.isEmpty()) {
			List<ManagerMenu> menus = null;
			if (u.getType() != null && u.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
				menus = managerMenuDao.findAllActive();
			} else {
				menus = managerMenuDao.findByUserId(u.getId());
			}
			if (menus != null && !menus.isEmpty()) {
				menuVos = toNavMenuVo(menus, false);
				CacheUtil.addCache(CacheKey.MANAGER, key, menuVos);
			}
		}
		return menuVos;
	}

	private List<MenuVo> toNavMenuVo(List<ManagerMenu> menuList, boolean containOperate) {
		List<MenuVo> list = null;
		List<MenuVo> children = null;
		MenuVo mv = null;
		Map<Long, List<MenuVo>> listMap = new HashMap<>();
		for (ManagerMenu am : menuList) {
			if (!containOperate && am.getType() >= Constants.MENU_TYPE_OPERATE) {
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

	private MenuVo toMenuVo(ManagerMenu am) {
		MenuVo v = new MenuVo();
		v.setId(am.getId());
		v.setTitle(am.getTitle());
		v.setHref(am.getUrl());
		v.setIcon(am.getIcon());
		v.setSortNo(am.getSortNo());
		v.setSpread(false);
		v.setType(am.getType());
		return v;
	}

	@Override
	public void setAuth(Long userId, Long[] menuIds) {
		ManagerUser user = managerUserDao.findOneById(userId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userExist);
		}
		if (menuIds == null || menuIds.length == 0) {
			managerUserAuthDao.deleteByUserId(userId);
			LogUtils.delLog("清空管理员菜单权限" + user.getUsername());
			return;
		}
		Set<Long> set = getPermissionIdSet(userId);
		ManagerUserAuth ua = null;
		for (Long menuId : menuIds) {
			if (set.contains(menuId)) {
				set.remove(menuId);
				continue;
			}
			ua = new ManagerUserAuth();
			ua.setMenuId(menuId);
			ua.setUserId(userId);
			managerUserAuthDao.save(ua);
		}
		managerUserAuthDao.deleteBatch(userId, set);
		CacheUtil.delCache(CacheKey.MANAGER, cache_key + userId);
		CacheUtil.delCache(CacheKey.MANAGER, nav_menu_key + userId);
		LogUtils.modifyLog("设置管理员菜单权限" + user.getUsername());
	}
}
