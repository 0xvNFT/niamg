package com.play.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.AdminMenuDao;
import com.play.dao.AdminUserGroupAuthDao;
import com.play.dao.AdminUserGroupDao;
import com.play.dao.StationDao;
import com.play.model.AdminMenu;
import com.play.model.AdminUserGroup;
import com.play.model.AdminUserGroupAuth;
import com.play.model.Station;
import com.play.service.AdminUserGroupAuthService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 站点管理员组别权限信息
 *
 * @author admin
 *
 */
@Service
public class AdminUserGroupAuthServiceImpl implements AdminUserGroupAuthService {

	@Autowired
	private AdminUserGroupAuthDao adminUserGroupAuthDao;
	@Autowired
	private StationDao stationDao;
	@Autowired
	private AdminMenuDao menuDao;
	@Autowired
	private AdminUserGroupDao groupDao;

	@Override
	public List<Long> getMenuIds(Long stationId, Long groupId) {
		return adminUserGroupAuthDao.getMenuIds(stationId, groupId);
	}

	@Override
	public void save(Long stationId, String ids, Long groupId) {
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		String groupName =  BaseI18nCode.staionUserGroupDefault.getMessage();
		if (groupId != 0) {
			AdminUserGroup group = groupDao.findOneById(groupId);
			if (group == null) {
				throw new ParamException(BaseI18nCode.groupUnExist);
			}
			groupName = group.getName();
		}
		if (StringUtils.isEmpty(ids)) {
			adminUserGroupAuthDao.deleteByGroupId(stationId, groupId);
			CacheUtil.delCacheByPrefix(CacheKey.ADMIN,Constants.cache_key_admin_perm);
			LogUtils.addLog("清空站点" + station.getName() + "[" + station.getCode() + "]的管理组别:" + groupName + "的菜单权限");
			return;
		}
		AdminMenu m = null;
		Long menuId = null;
		AdminUserGroupAuth auth = null;
		List<Long> menuIds = getMenuIds(stationId, 0L);
		for (String k : ids.split(",")) {
			if (StringUtils.isEmpty(k)) {
				continue;
			}
			menuId = NumberUtils.toLong(k);
			if (menuId == 0) {
				continue;
			}
			m = menuDao.findOneById(menuId);
			if (m == null) {
				continue;
			}
			if (!adminUserGroupAuthDao.exist(stationId, groupId, menuId)) {
				auth = new AdminUserGroupAuth();
				auth.setGroupId(groupId);
				auth.setStationId(stationId);
				auth.setMenuId(menuId);
				auth.setPartnerId(station.getPartnerId());
				adminUserGroupAuthDao.save(auth);
			}
			menuIds.remove(menuId);
		}
		adminUserGroupAuthDao.deleteForBatch(stationId, groupId, menuIds);
		CacheUtil.delCacheByPrefix(CacheKey.ADMIN, Constants.cache_key_admin_perm);
		LogUtils.addLog("保存站点" + station.getName() + "[" + station.getCode() + "]的管理组别:" + groupName + "的菜单权限");
	}

	@Override
	public void setAuth(Long groupId, Long stationId, Long[] menuIds) {
		AdminUserGroup ag = groupDao.findOneById(groupId);
		if (ag == null) {
			throw new ParamException(BaseI18nCode.groupUnExist);
		}
		if (menuIds == null || menuIds.length == 0) {
			adminUserGroupAuthDao.deleteByGroupId(stationId, groupId);
			LogUtils.delLog("清空站点管理员组别菜单权限" + ag.getName());
			return;
		}
		List<Long> oldIds = getMenuIds(stationId, groupId);
		AdminUserGroupAuth ua = null;
		for (Long menuId : menuIds) {
			if (oldIds.contains(menuId)) {
				oldIds.remove(menuId);
				continue;
			}
			ua = new AdminUserGroupAuth();
			ua.setMenuId(menuId);
			ua.setGroupId(groupId);
			ua.setStationId(stationId);
			adminUserGroupAuthDao.save(ua);
		}
		adminUserGroupAuthDao.deleteForBatch(stationId, groupId, oldIds);
		CacheUtil.delCacheByPrefix(CacheKey.ADMIN, Constants.cache_key_admin_perm);
		LogUtils.modifyLog("设置站点管理员组别菜单权限" + ag.getName());
	}
}
