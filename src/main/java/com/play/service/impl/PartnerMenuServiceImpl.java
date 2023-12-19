package com.play.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.play.common.utils.LogUtils;
import com.play.core.UserTypeEnum;
import com.play.dao.PartnerMenuDao;
import com.play.dao.PartnerUserAuthDao;
import com.play.dao.PartnerUserDao;
import com.play.model.PartnerMenu;
import com.play.model.PartnerUser;
import com.play.model.PartnerUserAuth;
import com.play.model.vo.MenuVo;
import com.play.service.PartnerMenuService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginPartnerUtil;

/**
 * 合作商后台菜单
 *
 * @author admin
 *
 */
@Service
public class PartnerMenuServiceImpl implements PartnerMenuService {

	@Autowired
	private PartnerMenuDao partnerMenuDao;
	@Autowired
	private PartnerUserDao partnerUserDao;
	@Autowired
	private PartnerUserAuthDao partnerUserAuthDao;

	private static final String cache_key = "api1:perm:uid:";
	private static final String nav_menu_key = "api1:bg:menus:";

	@Override
	public Set<String> getPermissionSet(Long id) {
		if (id == null || id <= 0) {
			return new HashSet<>();
		}
		String key = cache_key + id;
		Set<String> set = CacheUtil.getCache(CacheKey.PARTNER, key, Set.class);
		if (set != null) {
			return set;
		}
		List<String> menus = partnerMenuDao.findPermByUserId(id);
		if (menus != null && !menus.isEmpty()) {
			set = new HashSet<>(menus);
			CacheUtil.addCache(CacheKey.PARTNER, key, set);
		}
		return set;
	}

	@Override
	public Set<Long> getPermissionIdSet(Long id) {
		if (id == null || id <= 0) {
			return new HashSet<>();
		}
		List<Long> menuIds = partnerMenuDao.findMenuIdByUserId(id);
		if (menuIds != null && !menuIds.isEmpty()) {
			return new HashSet<Long>(menuIds);
		}
		return new HashSet<>();
	}

	@Override
	public List<MenuVo> getAllMenuVo(Long userId) {
		List<PartnerMenu> menu = partnerMenuDao.getAll();
		if (menu != null && !menu.isEmpty()) {
			Set<Long> set = getPermissionIdSet(userId);
			if (!set.isEmpty()) {
				PartnerMenu m = null;
				for (Iterator<PartnerMenu> it = menu.iterator(); it.hasNext();) {
					m = it.next();
					if (!set.contains(m.getId())) {
						it.remove();
					}
				}
			}
			return toNavMenuVo(menu, true);
		}
		return null;
	}

	@Override
	public List<MenuVo> getNavMenuVo() {
		List<MenuVo> menuVos = null;
		PartnerUser u = LoginPartnerUtil.currentUser();
		String key = nav_menu_key + u.getId();
		String json = CacheUtil.getCache(CacheKey.PARTNER, key);
		if (json != null) {
			menuVos = JSON.parseArray(json, MenuVo.class);
		}
		if (menuVos == null || menuVos.isEmpty()) {
			List<PartnerMenu> menus = null;
			if (u.getType() != null && u.getType() == UserTypeEnum.PARTNER_SUPER.getType()) {
				menus = partnerMenuDao.getAll();
			} else {
				menus = partnerMenuDao.findByUserId(u.getId());
			}
			if (menus != null && !menus.isEmpty()) {
				menuVos = toNavMenuVo(menus, false);
				CacheUtil.addCache(CacheKey.PARTNER, key, menuVos);
			}
		}
		return menuVos;
	}

	private List<MenuVo> toNavMenuVo(List<PartnerMenu> menuList, boolean containOperate) {
		List<MenuVo> list = null;
		List<MenuVo> children = null;
		MenuVo mv = null;
		Map<Long, List<MenuVo>> listMap = new HashMap<>();
		for (PartnerMenu am : menuList) {
			if (!containOperate && am.getType() >= Constants.MENU_TYPE_OPERATE && StringUtils.isEmpty(am.getUrl())) {
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

	private MenuVo toMenuVo(PartnerMenu am) {
		MenuVo v = new MenuVo();
		v.setId(am.getId());
		if (StringUtils.isNotEmpty(am.getCode())) {
			v.setTitle(I18nTool.getMessage(am.getCode()));
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

	@Override
	public void setAuth(Long userId, Long currentUserId, Long partnerId, Long[] menuIds) {
		PartnerUser user = partnerUserDao.findOne(userId, partnerId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		if (menuIds == null || menuIds.length == 0) {
			partnerUserAuthDao.deleteByUserId(userId);
			LogUtils.delLog("清空合作商管理员菜单权限" + user.getUsername() + " partnerId=" + user.getPartnerId());
			return;
		}
		Set<Long> set = getPermissionIdSet(userId);
		Set<Long> currentUserPermSet = null;
		if (currentUserId != null) {
			currentUserPermSet = getPermissionIdSet(currentUserId);
		}
		PartnerUserAuth ua = null;
		for (Long menuId : menuIds) {
			if (currentUserPermSet != null && !currentUserPermSet.contains(menuId)) {
				continue;
			}
			if (set.contains(menuId)) {
				set.remove(menuId);
				continue;
			}
			ua = new PartnerUserAuth();
			ua.setMenuId(menuId);
			ua.setUserId(userId);
			ua.setPartnerId(user.getPartnerId());
			partnerUserAuthDao.save(ua);
		}
		partnerUserAuthDao.deleteBatch(userId, set);
		CacheUtil.delCache(CacheKey.PARTNER, cache_key + userId);
		CacheUtil.delCache(CacheKey.PARTNER, nav_menu_key + userId);
		LogUtils.modifyLog("设置合作商管理员菜单权限" + user.getUsername() + " partnerId=" + user.getPartnerId());
	}
}
