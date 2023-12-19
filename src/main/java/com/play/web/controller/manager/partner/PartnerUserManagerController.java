package com.play.web.controller.manager.partner;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.PartnerUser;
import com.play.service.PartnerMenuService;
import com.play.service.PartnerService;
import com.play.service.PartnerUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/partnerUser")
public class PartnerUserManagerController extends ManagerBaseController {
	@Autowired
	private PartnerService partnerService;
	@Autowired
	private PartnerUserService partnerUserService;
	@Autowired
	private PartnerMenuService partnerMenuService;

	@Permission("zk:partnerUser")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/user/partnerUserIndex");
	}

	@Permission("zk:partnerUser")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@NeedLogView(value = "合作商管理员管理列表", type = LogTypeEnum.VIEW_LIST)
	@ResponseBody
	public void list(Long partnerId, String username) {
		renderPage(partnerUserService.page(partnerId, username));
	}

	@Permission("zk:partnerUser:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		partnerUserService.changeStatus(id, null, status);
		renderSuccess();
	}

	@Permission("zk:partnerUser:pwd")
	@RequestMapping(value = "/updatePwd", method = RequestMethod.GET)
	public String updatePwd(Map<String, Object> map, Long id) {
		map.put("user", partnerUserService.findOne(id, null));
		return returnPage("/partner/user/updatePwd");
	}

	@Permission("zk:partnerUser:pwd")
	@ResponseBody
	@RequestMapping(value = "/doUpdatePwd", method = RequestMethod.POST)
	public void doUpdatePwd(Long id, String pwd, String pwd2) {
		partnerUserService.updatePwd(id, null, pwd, pwd2);
		renderSuccess();
	}

	@Permission("zk:partnerUser:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		partnerUserService.delete(id, null);
		renderSuccess();
	}

	@Permission("zk:partnerUser:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/user/addUser");
	}

	@Permission("zk:partnerUser:add")
	@ResponseBody
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public void addUser(PartnerUser user) {
		partnerUserService.save(user);
		renderSuccess();
	}

	@Permission("zk:partnerUser:auth")
	@RequestMapping(value = "/showSetAuth", method = RequestMethod.GET)
	public String showSetAuth(Map<String, Object> map, Long id) {
		map.put("user", partnerUserService.findOne(id, null));
		map.put("menus", partnerMenuService.getAllMenuVo(null));
		map.put("sets", partnerMenuService.getPermissionIdSet(id));
		return returnPage("/partner/user/setAuth");
	}

	@Permission("zk:partnerUser:auth")
	@ResponseBody
	@RequestMapping(value = "/setAuth", method = RequestMethod.POST)
	public void setAuth(Long userId, Long[] menuId) {
		partnerMenuService.setAuth(userId, null, null, menuId);
		renderSuccess();
	}

	@Permission("zk:partnerUser:setpwd2")
	@RequestMapping(value = "/setPwd2", method = RequestMethod.GET)
	public String setPwd2(Map<String, Object> map, Long id) {
		map.put("user", partnerUserService.findOne(id, null));
		return returnPage("/partner/user/setPwd2");
	}

	@Permission("zk:partnerUser:setpwd2")
	@ResponseBody
	@RequestMapping(value = "/doSetPwd2", method = RequestMethod.POST)
	public void doSetPwd2(Long id, String pwd, String pwd2) {
		partnerUserService.doSetPwd2(id, null, pwd, pwd2);
		renderSuccess();
	}

}
