package com.play.web.controller.manager.station;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.AdminUserGroup;
import com.play.service.AdminMenuService;
import com.play.service.AdminUserGroupAuthService;
import com.play.service.AdminUserGroupService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/adminUserGroup")
public class SysAdminUserGroupManagerController extends ManagerBaseController {
	@Autowired
	private StationService stationService;
	@Autowired
	private AdminUserGroupService userGroupService;
	@Autowired
	private AdminUserGroupAuthService userGroupAuthService;
	@Autowired
	private AdminMenuService adminMenuService;

	@Permission("zk:adminUserGroup")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/admin/adminUserGroupIndex");
	}

	@Permission("zk:adminUserGroup")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "站点管理员组别管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId) {
		renderJSON(userGroupService.page(null, stationId, null));
	}

	@Permission("zk:adminUserGroup:update")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "站点管理员组别修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showAdd(Map<String, Object> map, Long id, Long stationId) {
		map.put("group", userGroupService.findOne(id, stationId));
		return returnPage("/station/admin/adminUserGroupModify");
	}

	@Permission("zk:adminUserGroup:update")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(AdminUserGroup userGroup) {
		userGroupService.modify(userGroup);
		renderSuccess();
	}

	@Permission("zk:adminUserGroup:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map, Long stationId) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/admin/adminUserGroupAdd");
	}

	@Permission("zk:adminUserGroup:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AdminUserGroup userGroup) {
		userGroupService.save(userGroup);
		renderSuccess();
	}

	@Permission("zk:adminUserGroup:del")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(Long id, Long stationId) {
		userGroupService.delete(id, stationId);
		renderSuccess();
	}

	@Permission("zk:adminUserGroup:auth")
	@RequestMapping(value = "/showAddAuth")
	public String showAddAuth(Map<String, Object> map, Long id, Long stationId) {
		map.put("group", userGroupService.findOne(id, stationId));
		map.put("menus", adminMenuService.getStationMenu(stationId, 0L));
		map.put("menuIds", userGroupAuthService.getMenuIds(stationId, id));
		return returnPage("/station/admin/adminUserGroupAddAuth");
	}

	@Permission("zk:adminUserGroup:auth")
	@ResponseBody
	@RequestMapping(value = "/addAuth")
	public void addAuth(Long groupId, Long stationId, Long[] menuId) {
		userGroupAuthService.setAuth(groupId, stationId, menuId);
		renderSuccess();
	}
}
