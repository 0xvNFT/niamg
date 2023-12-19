package com.play.web.controller.admin.adminUser;

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
import com.play.service.AdminMenuService;
import com.play.service.AdminUserGroupAuthService;
import com.play.service.AdminUserGroupService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/adminUserGroup")
public class AdminUserGroupManagerController extends AdminBaseController {
	@Autowired
	private AdminMenuService adminMenuService;
	@Autowired
	private AdminUserGroupService adminUserGroupService;
	@Autowired
	private AdminUserGroupAuthService userGroupAuthService;

	/**
	 * 后台组别管理首页
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:adminUserGroup")
	@RequestMapping("/index")
	public String groupIndex(Map<String, Object> map) {
		map.put("loginUser", LoginAdminUtil.currentUser());
		return returnPage("/adminuser/group/adminUserGroupIndex");
	}

	/**
	 * 后台组别管理列表
	 *
	 * @param map
	 */
	@Permission("admin:adminUserGroup")
	@RequestMapping("/list")
	@ResponseBody
	public void groupList(Map<String, Object> map) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
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
		// 过滤数据
		renderJSON(adminUserGroupService.page(null, SystemUtil.getStationId(), minType));
	}

	/**
	 * 新增分组
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:adminUserGroup:add")
	@RequestMapping("/add")
	public String groupAdd(Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		map.put("loginUser", LoginAdminUtil.currentUser());
		return returnPage("/adminuser/group/adminUserGrouAdd");
	}

	@Permission("admin:adminUserGroup:add")
	@ResponseBody
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public void doAdd(AdminUserGroup userGroup) {
		LoginAdminUtil.checkPerm();
		userGroup.setStationId(SystemUtil.getStationId());
		adminUserGroupService.save(userGroup);
		renderSuccess();
	}

	@Permission("admin:adminUserGroup:update")
	@RequestMapping("/update")
	public String groupUpdate(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("group", adminUserGroupService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/adminuser/group/adminUserGrouUpdate");
	}

	@Permission("admin:adminUserGroup:update")
	@RequestMapping("/doUpdate")
	@ResponseBody
	public void doUpdate(AdminUserGroup group) {
		LoginAdminUtil.checkPerm();
		group.setStationId(SystemUtil.getStationId());
		adminUserGroupService.modify(group);
		renderSuccess();
	}

	@Permission("admin:adminUserGroup:delete")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteGroup(Long id) {
		LoginAdminUtil.checkPerm();
		adminUserGroupService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:adminUserGroup:auth")
	@RequestMapping(value = "/auth")
	public String groupAuth(Long id, Map<String, Object> map) {
		AdminUser user = LoginAdminUtil.currentUser();
		Long stationId = user.getStationId();
		map.put("group", adminUserGroupService.findOne(id, stationId));
		map.put("menus", adminMenuService.getStationMenu(stationId, user.getGroupId()));
		map.put("menuIds", userGroupAuthService.getMenuIds(stationId, id));
		return returnPage("/adminuser/group/groupAuth");
	}

	@Permission("admin:adminUserGroup:auth")
	@ResponseBody
	@RequestMapping(value = "/addAuth", method = RequestMethod.POST)
	public void addAuth(Long groupId, Long[] menuId) {
		userGroupAuthService.setAuth(groupId, SystemUtil.getStationId(), menuId);
		renderSuccess();
	}

}
