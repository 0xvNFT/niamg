package com.play.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.model.AdminUserGroup;
import com.play.service.AdminUserGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AdminMenuDao;
import com.play.model.AdminMenu;
import com.play.model.vo.AdminMenuVo;
import com.play.service.AdminMenuService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.transaction.annotation.Transactional;

/**
 * admin user 权限表
 *
 * @author admin
 *
 */
@Service
public class AdminMenuServiceImpl implements AdminMenuService {

	private Logger logger = LoggerFactory.getLogger(AdminMenuServiceImpl.class);

	@Autowired
	private AdminMenuDao adminMenuDao;

	@Autowired
	private AdminUserGroupService adminUserGroupService;

	@Override
	public List<AdminMenuVo> getAll() {
		List<AdminMenu> list = adminMenuDao.getAll();
		List<AdminMenuVo> vList = new ArrayList<>();
		List<AdminMenuVo> rList = new ArrayList<>();
		Map<Long, AdminMenuVo> map = new HashMap<>();
		AdminMenuVo v = null;
		for (AdminMenu m : list) {
			v = new AdminMenuVo();
			v.setId(m.getId());
			v.setParentId(m.getParentId());
			if (StringUtils.isNotEmpty(m.getCode())) {
				v.setTitle(I18nTool.getMessage(m.getCode()));
			} else {
				v.setTitle(m.getTitle());
			}
			vList.add(v);
			map.put(m.getId(), v);
		}
		for (AdminMenuVo v1 : vList) {
			if (v1.getParentId() == 0) {
				rList.add(v1);
			} else {
				map.get(v1.getParentId()).addChild(v1);
			}
		}
		return rList;
	}

	@Override
	public List<AdminMenuVo> getStationMenu(Long stationId, Long groupId) {
		List<AdminMenu> list = adminMenuDao.findByGroupId(groupId, stationId);
		if (list == null) {
			throw new BaseException(BaseI18nCode.stationNoMenu);
		}
		List<AdminMenuVo> vList = new ArrayList<>();
		List<AdminMenuVo> rList = new ArrayList<>();
		Map<Long, AdminMenuVo> map = new HashMap<>();
		AdminMenuVo v = null;
		for (AdminMenu m : list) {
			v = new AdminMenuVo();
			v.setId(m.getId());
			v.setParentId(m.getParentId());
			if (StringUtils.isNotEmpty(m.getCode())) {
				v.setTitle(I18nTool.getMessage(m.getCode()));
			} else {
				v.setTitle(m.getTitle());
			}
			vList.add(v);
			map.put(m.getId(), v);
		}
		for (AdminMenuVo v1 : vList) {
			if (v1.getParentId() == 0) {
				rList.add(v1);
			} else {
				if (map.get(v1.getParentId()) != null) {
					map.get(v1.getParentId()).addChild(v1);
				}
			}
		}
		return rList;
	}

	@Override
	public Set<Long> getPermissionIdSet(Long groupId, Long stationId) {
		if (stationId == null || stationId <= 0) {
			return new HashSet<>();
		}
		Set<Long> set = new HashSet<>();
		List<AdminMenu> menus = adminMenuDao.findByGroupId(groupId, stationId);
		if (menus != null && !menus.isEmpty()) {
			for (AdminMenu m : menus) {
				set.add(m.getId());
			}
		}
		return set;
	}

	@Transactional
	@Override
	public void adminMenuRefresh(Long stationId, Long partnerId, Long[] adminMenuIds) {
		// 添加组别和权限
		try {
			adminMenuDao.adminMenuRefresh(stationId, partnerId, 0L, adminMenuIds);

			AdminUserGroup zg = adminUserGroupService.getAdminUsergroup(stationId, partnerId,
					BaseI18nCode.stationOwnerLeader.getMessage(), AdminUserGroup.TYPE_MASTER);
			adminMenuDao.adminMenuRefresh(stationId, partnerId, zg.getId(), adminMenuIds);

			AdminUserGroup kf = adminUserGroupService.getAdminUsergroup(stationId, partnerId,
					BaseI18nCode.stationClientService.getMessage(), AdminUserGroup.TYPE_MASTER);
			adminMenuDao.adminMenuRefresh(stationId, partnerId, kf.getId(), adminMenuIds);

			AdminUserGroup js = adminUserGroupService.getAdminUsergroup(stationId, partnerId,
					BaseI18nCode.stationTechService.getMessage(), AdminUserGroup.TYPE_MASTER);
			adminMenuDao.adminMenuRefresh(stationId, partnerId, js.getId(), adminMenuIds);

			if (partnerId != null && partnerId > 0) {// 有选择合作商，才需要添加
				AdminUserGroup partner = adminUserGroupService.getAdminUsergroup(stationId, partnerId,
						BaseI18nCode.stationPartnerAdmin.getMessage(), AdminUserGroup.TYPE_PARTNER_VIEW);
				adminMenuDao.adminMenuRefresh(stationId, partnerId, partner.getId(), adminMenuIds);

			}
			AdminUserGroup ug = adminUserGroupService.getAdminUsergroup(stationId, partnerId,
					BaseI18nCode.stationDefAdmin.getMessage(), AdminUserGroup.TYPE_ADMIN_VIEW);
			adminMenuDao.adminMenuRefresh(stationId, partnerId, ug.getId(), adminMenuIds);

			// 清除权限列表缓存
			CacheUtil.delCacheByPrefix(CacheKey.ADMIN, Constants.cache_key_admin_perm);
		} catch (Exception e) {
			logger.error("adminMenuRefresh service error :{}", e);
			throw new BaseException("adminMenuRefresh service error" + e);
		}
	}
}
