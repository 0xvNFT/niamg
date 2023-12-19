package com.play.service.impl;

import java.util.List;
import java.util.Objects;

import com.play.model.ManagerUser;
import com.play.web.var.LoginManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.UserTypeEnum;
import com.play.dao.AdminUserGroupAuthDao;
import com.play.dao.AdminUserGroupDao;
import com.play.model.AdminUserGroup;
import com.play.model.Station;
import com.play.orm.jdbc.page.Page;
import com.play.service.AdminUserGroupService;
import com.play.service.StationService;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * 站点管理员组别信息
 *
 * @author admin
 *
 */
@Service
public class AdminUserGroupServiceImpl implements AdminUserGroupService {

	@Autowired
	private AdminUserGroupDao adminUserGroupDao;
	@Autowired
	private AdminUserGroupAuthDao authDao;
	@Autowired
	private StationService stationService;

	// 站点可修改权限
	public static final int TYPE_ADMIN_GROUP_MODIFY = 5;

	@Override
	public AdminUserGroup saveDefaultGroup(Long stationId, Long partnerId, String name, int type) {
		AdminUserGroup u = new AdminUserGroup();
		u.setName(name);
		u.setStationId(stationId);
		u.setPartnerId(partnerId);
		u.setType(type);
		adminUserGroupDao.save(u);
		return u;
	}

	@Override
	public Page<AdminUserGroup> page(Long partnerId, Long stationId, Integer minType) {
		return adminUserGroupDao.page(partnerId, stationId, minType);
	}

	@Override
	public AdminUserGroup findOne(Long id, Long stationId) {
		return adminUserGroupDao.findOne(id, stationId);
	}

	@Override
	public void modify(AdminUserGroup userGroup) {
		if (userGroup == null) {
			return;
		}
		AdminUserGroup u = adminUserGroupDao.findOne(userGroup.getId(), userGroup.getStationId());
		if (u == null) {
			throw new ParamException(BaseI18nCode.groupUnExist);
		}
		adminUserGroupDao.updateName(u.getId(), userGroup.getName());
		LogUtils.modifyLog("修改站点管理员组别名称 " + u.getName() + " 为:" + userGroup.getName());
	}

	@Override
	public void save(AdminUserGroup userGroup) {
		Station station = stationService.findOneById(userGroup.getStationId(), null);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		if(Objects.isNull(userGroup.getType())){
			userGroup.setType(TYPE_ADMIN_GROUP_MODIFY);
		}
		userGroup.setPartnerId(station.getPartnerId());
		adminUserGroupDao.save(userGroup);
		LogUtils.addLog("新增站点 " + station.getName() + "[" + station.getCode() + "]管理员组别" + userGroup.getName());
	}

	@Override
	public void delete(Long id, Long stationId) {
		AdminUserGroup u = adminUserGroupDao.findOne(id, stationId);
		if (u == null) {
			throw new ParamException(BaseI18nCode.groupUnExist);
		}
		boolean isManagerUser = false;
		ManagerUser user = LoginManagerUtil.currentUser();
		if (user.getType() != null && user.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			isManagerUser = true;
		}
		int userType = SystemUtil.getUserTypeVaule();
		if ((userType <= UserTypeEnum.ADMIN_MASTER_SUPER.getType() &&
				(u.getType() == AdminUserGroup.TYPE_PARTNER_MODIFY || u.getType()==AdminUserGroup.TYPE_MASTER))
				|| u.getType() == AdminUserGroup.TYPE_ADMIN_MODIFY || isManagerUser) {
			adminUserGroupDao.deleteById(id);
			authDao.deleteByGroupId(stationId, id);
			LogUtils.addLog("删除站点管理员组别:" + u.getName());
		} else {
			throw new UnauthorizedException();
		}
	}

	@Override
	public List<AdminUserGroup> getAll(Long customerId, Long stationId, Integer minType) {
		return adminUserGroupDao.getAll(customerId, stationId, minType);
	}

	@Override
	public AdminUserGroup getAdminUsergroup(Long stationId, Long partnerId, String groupName, Integer groupType) {
		return adminUserGroupDao.getAdminUsergroup(stationId, partnerId, groupName, groupType);
	}

}
