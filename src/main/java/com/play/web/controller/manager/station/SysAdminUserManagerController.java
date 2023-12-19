package com.play.web.controller.manager.station;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.service.AdminUserGroupService;
import com.play.service.AdminUserService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/adminUser")
public class SysAdminUserManagerController extends ManagerBaseController {
	@Autowired
	private StationService stationService;
	@Autowired
	private AdminUserService adminUserService;
	@Autowired
	private AdminUserGroupService userGroupService;

	@Permission("zk:adminUser")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		map.put("types", UserTypeEnum.getAdminTypes());
		return returnPage("/station/adminUser/adminUserIndex");
	}

	@Permission("zk:adminUser")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "站点管理员管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId, Long groupId, String username, Integer type) {
		renderJSON(adminUserService.pageForManager(stationId, groupId, username, type));
	}

	@Permission("zk:adminUser:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status, Long stationId) {
		adminUserService.changeStatus(id, status, stationId);
		renderSuccess();
	}

	@Permission("zk:adminUser:update")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "站点管理员管理修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showModify(Map<String, Object> map, Long id, Long stationId) {
		map.put("adminUser", adminUserService.findById(id, stationId));
		map.put("types", UserTypeEnum.getAdminTypes());
		map.put("groups", userGroupService.getAll(null, stationId, null));
		return returnPage("/station/adminUser/adminUserModify");
	}

	@Permission("zk:adminUser:update")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(AdminUser user) {
		adminUserService.update(user);
		renderSuccess();
	}

	@Permission("zk:adminUser:uppwd")
	@RequestMapping(value = "/showUpPwd")
	public String showUpPwd(Map<String, Object> map, Long id, Long stationId) {
		map.put("adminUser", adminUserService.findById(id, stationId));
		return returnPage("/station/adminUser/adminUserUpPwd");
	}

	@Permission("zk:adminUser:uppwd")
	@ResponseBody
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	public void updatePwd(Long id, Long stationId, String pwd, String rpwd) {
		adminUserService.updatePwd(id, stationId, pwd, rpwd);
		renderSuccess();
	}

	@Permission("zk:adminUser:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map, Long stationId) {
		map.put("stations", stationService.getCombo(null));
		map.put("types", UserTypeEnum.getAdminTypes());
		return returnPage("/station/adminUser/adminUserAdd");
	}

	@Permission("zk:adminUser:add")
	@ResponseBody
	@RequestMapping("/getGroups")
	public void getGroups(Long stationId) {
		renderJSON(userGroupService.getAll(null, stationId, null));
	}

	@Permission("zk:adminUser:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AdminUser user) {
		if (user.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()
				|| user.getType() == UserTypeEnum.ADMIN_PARTNER_SUPER.getType()) {
			throw new ParamException(BaseI18nCode.userTypeError);
		}
		adminUserService.save(user);
		renderSuccess();
	}
}
