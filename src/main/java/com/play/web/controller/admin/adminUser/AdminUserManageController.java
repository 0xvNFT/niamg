package com.play.web.controller.admin.adminUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.model.AdminUser;
import com.play.model.AdminUserGroup;
import com.play.service.AdminUserGroupService;
import com.play.service.AdminUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

/**
 * 管理员管理
 *
 * @author
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/adminUser")
public class AdminUserManageController extends AdminBaseController {

	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserGroupService adminUserGroupService;

	/**
	 * 后台用户管理首页
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:adminUser")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
		Long stationId = adminUser.getStationId();
		Integer minType = null;
		switch (SystemUtil.getUserType()) {
		// 超级管理员可以新增所有
		case ADMIN_MASTER_SUPER:
			minType = AdminUserGroup.TYPE_MASTER;
			break;
		// 高级管理员可以新增：租户可编辑权限以及租户只看，不可编辑权限
		case ADMIN_MASTER:
		case ADMIN_PARTNER_SUPER:
			minType = AdminUserGroup.TYPE_PARTNER_VIEW;
		case ADMIN_PARTNER:
			minType = AdminUserGroup.TYPE_PARTNER_MODIFY;
			break;
		// 普通管理员只能新增租户可编辑权限
		default:
			if (adminUser.getOriginal() == Constants.USER_ORIGINAL) {
				minType = AdminUserGroup.TYPE_ADMIN_VIEW;
			} else {
				minType = AdminUserGroup.TYPE_ADMIN_MODIFY;
			}
		}
		map.put("loginUser", adminUser);
		map.put("groups", adminUserGroupService.getAll(null, stationId, minType));
		if (adminUser.getGroupId() == 0) {
			map.put("loginUserGroupType", "");
		} else {
			AdminUserGroup adminUserGroup = adminUserGroupService.findOne(adminUser.getGroupId(), stationId);
			if (null == adminUserGroup) {
				map.put("loginUserGroupType", "");
			} else {
				map.put("loginUserGroupType", adminUserGroup.getType());
			}
		}
		return returnPage("/adminuser/adminUserIndex");
	}

	/**
	 * 后台用户管理列表
	 */
	@Permission("admin:adminUser")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("后台管理员列表")
	public void list(String username, Long groupId, Boolean onlineStatus) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
//		// 获取当前登录用户属性
//		if (!PermissionForAdminUser.hadPermission("admin:user:online")) {
//			onlineStatus = false;
//		}
		renderJSON(adminUserService.adminPage(adminUser.getStationId(), adminUser.getType(), username, groupId,
				onlineStatus));
	}

	/**
	 * 新增用户
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:adminUser:add")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		AdminUser adminUser = LoginAdminUtil.currentUser();
		Long stationId = adminUser.getStationId();
		Integer minType = null;
		if (adminUser.getOriginal() == Constants.USER_ORIGINAL) {
			minType = AdminUserGroup.TYPE_ADMIN_VIEW;
		} else {
			minType = AdminUserGroup.TYPE_ADMIN_MODIFY;
		}
		List<AdminUserGroup> groups = adminUserGroupService.getAll(null, stationId, minType);
		if (groups == null || groups.isEmpty()) {
			throw new ParamException(BaseI18nCode.adminUserGroupEmpty);
		}
		map.put("groups", groups);
		return returnPage("/adminuser/adminUserAdd");
	}

	/**
	 * 新增用户
	 *
	 * @param user
	 */
	@Permission("admin:adminUser:add")
	@ResponseBody
	@RequestMapping(value = "/addAdminUser", method = RequestMethod.POST)
	public void addUser(AdminUser user) {
		LoginAdminUtil.checkPerm();
		if (user.getGroupId() == null || user.getGroupId() <= 0) {
			throw new ParamException(BaseI18nCode.adminUserGroupMust);
		}
		// 默认type为管理员
		user.setStationId(SystemUtil.getStationId());
		adminUserService.save(user);
		renderSuccess();
	}

	/**
	 * 修改用户
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:adminUser:update")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		map.put("user", adminUserService.findById(id, stationId));
		map.put("groups", adminUserGroupService.getAll(null, stationId, AdminUserGroup.TYPE_ADMIN_VIEW));
		return returnPage("/adminuser/adminUserUpdate");
	}

	/**
	 * 修改
	 *
	 * @param user
	 */
	@Permission("admin:adminUser:update")
	@ResponseBody
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public void updateUser(AdminUser user) {
		LoginAdminUtil.checkPerm();
		user.setStationId(SystemUtil.getStationId());
		adminUserService.update(user);
		renderSuccess();
	}

	/**
	 * 修改管理员加款上限
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:adminUser:updatemoneyLimit")
	@RequestMapping(value = "/moneyLimitModify")
	public String moneyLimitModify(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("user", adminUserService.findById(id, SystemUtil.getStationId()));
		return returnPage("/adminuser/moneyLimitModify");
	}

	/**
	 * 管理员加款上限修改保存
	 */
	@Permission("admin:adminUser:updatemoneyLimit")
	@ResponseBody
	@RequestMapping("/moneyLimitModifySave")
	public void moneyLimitModifySave(BigDecimal addMoneyLimit, Long id) {
		adminUserService.changeAddMoneyLimit(id, SystemUtil.getStationId(), addMoneyLimit);
		renderSuccess();
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 */
	@Permission("admin:adminUser:delete")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		LoginAdminUtil.checkPerm();
		checkPermission(id);
		adminUserService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改用户可用状态
	 *
	 * @param id
	 * @param status
	 */
	@Permission("admin:adminUser:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		LoginAdminUtil.checkPerm();
		adminUserService.changeStatus(id, status, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 重置密码
	 *
	 * @param id
	 */
	@Permission("admin:adminUser:reset:pwd")
	@ResponseBody
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public void resetPwd(Long id) {
		LoginAdminUtil.checkPerm();
		checkPermission(id);
		adminUserService.updatePwd(id, SystemUtil.getStationId(), "a123456", "a123456");
		renderSuccess();
	}

	/**
	 * 重置密码
	 *
	 * @param id
	 */
	@Permission("admin:adminUser:reset:pwd")
	@ResponseBody
	@RequestMapping(value = "/resetReceiptPwd", method = RequestMethod.POST)
	public void resetReceiptPwd(Long id) {
		LoginAdminUtil.checkPerm();
		checkPermission(id);
		adminUserService.resetReceiptPwd(id, SystemUtil.getStationId(), "a123456");
		renderSuccess();
	}

	private void checkPermission(Long resetUserId) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
		if (null == adminUser || adminUser.getGroupId() == 0) {
			return;
		}
		AdminUserGroup adminUserGroup = adminUserGroupService.findOne(adminUser.getGroupId(), adminUser.getStationId());
		if (null == adminUserGroup) {
			return;
		}
		Long stationId = adminUser.getStationId();
		AdminUser resetAdminUser = adminUserService.findById(resetUserId, stationId);
		if (null == resetAdminUser || resetAdminUser.getGroupId() == 0) {
			return;
		}
		AdminUserGroup resetUserGroup = adminUserGroupService.findOne(resetAdminUser.getGroupId(),
				adminUser.getStationId());
		if (null == resetUserGroup) {
			return;
		}
		if (adminUser.getType() > resetAdminUser.getType() || adminUserGroup.getType() > resetUserGroup.getType()) {// 低级别的不能修改高级别的用户
			throw new UnauthorizedException();
		}
	}

	/**
	 * 修改管理员处理单笔充值上限
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:adminUser:updateDepositLimit")
	@RequestMapping(value = "/depositLimitModify")
	public String depositLimitModify(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("user", adminUserService.findById(id, SystemUtil.getStationId()));
		return returnPage("/adminuser/depositLimitModify");
	}

	/**
	 * 管理员处理单笔充值修改保存
	 */
	@Permission("admin:adminUser:updateDepositLimit")
	@ResponseBody
	@RequestMapping("/depositLimitModifySave")
	public void depositLimitModifySave(BigDecimal depositLimit, Long id) {
		adminUserService.changeDepositLimit(id, SystemUtil.getStationId(), depositLimit);
		renderSuccess();
	}

	/**
	 * 修改管理员处理单笔提款上限
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:adminUser:updateWithdrawLimit")
	@RequestMapping(value = "/withdrawLimitModify")
	public String drawLimitModify(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("user", adminUserService.findById(id, SystemUtil.getStationId()));
		return returnPage("/adminuser/withdrawLimitModify");
	}

	/**
	 * 管理员处理单笔提款修改保存
	 */
	@Permission("admin:adminUser:updateWithdrawLimit")
	@ResponseBody
	@RequestMapping("/withdrawLimitModifySave")
	public void drawLimitModifySave(BigDecimal withdrawLimit, Long id) {
		adminUserService.changeWithdrawLimit(id,SystemUtil.getStationId(), withdrawLimit);
		renderSuccess();
	}

}
