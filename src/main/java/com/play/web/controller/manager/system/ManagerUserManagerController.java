package com.play.web.controller.manager.system;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.ManagerUser;
import com.play.service.ManagerMenuService;
import com.play.service.ManagerUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/user")
public class ManagerUserManagerController extends ManagerBaseController {
	@Autowired
	private ManagerUserService managerUserService;
	@Autowired
	private ManagerMenuService menuService;

	@Permission("zk:user")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/user/userIndex");
	}

	@Permission("zk:user")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@NeedLogView(value = "用户管理列表", type = LogTypeEnum.VIEW_LIST)
	@ResponseBody
	public void list() {
		renderPage(managerUserService.page());
	}

	@Permission("zk:user:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		managerUserService.changeStatus(id, status);
		renderSuccess();
	}

	@Permission("zk:user:reset:pwd")
	@ResponseBody
	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	public void resetPwd(Long id) {
		managerUserService.resetPwd(id);
		renderSuccess();
	}

	@Permission("zk:user:delete")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		managerUserService.delete(id);
		renderSuccess();
	}

	@Permission("zk:user:add")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Map<String, Object> map) {
		return returnPage("/user/addUser");
	}

	@Permission("zk:user:add")
	@ResponseBody
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public void addUser(ManagerUser user) {
		managerUserService.save(user);
		renderSuccess();
	}

	@Permission("zk:user:auth")
	@RequestMapping(value = "/showSetAuth", method = RequestMethod.GET)
	public String showSetAuth(Map<String, Object> map, Long id) {
		map.put("user", managerUserService.findById(id));
		map.put("menus", menuService.getAllMenuVo());
		map.put("sets", menuService.getPermissionIdSet(id));
		return returnPage("/user/setAuth");
	}

	@Permission("zk:user:auth")
	@ResponseBody
	@RequestMapping(value = "/setAuth", method = RequestMethod.POST)
	public void setAuth(Long userId, Long[] menuId) {
		menuService.setAuth(userId, menuId);
		renderSuccess();
	}

	@Permission("zk:user:setpwd2")
	@RequestMapping(value = "/setPwd2", method = RequestMethod.GET)
	public String setPwd2(Map<String, Object> map, Long id) {
		map.put("user", managerUserService.findById(id));
		return returnPage("/user/userPwd2");
	}

	@Permission("zk:user:setpwd2")
	@ResponseBody
	@RequestMapping(value = "/doSetPwd2", method = RequestMethod.POST)
	public void doSetPwd2(Long id, String pwd, String pwd2) {
		managerUserService.doSetPwd2(id, pwd, pwd2);
		renderSuccess();
	}

}
