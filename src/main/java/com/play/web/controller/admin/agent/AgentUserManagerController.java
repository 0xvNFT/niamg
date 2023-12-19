package com.play.web.controller.admin.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.model.AgentUser;
import com.play.service.AgentService;
import com.play.service.AgentUserService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/agentUser")
public class AgentUserManagerController extends AdminBaseController {
	@Autowired
	private AgentService agentService;
	@Autowired
	private AgentUserService agentUserService;

	@Permission("admin:agentUser")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("agents", agentService.getAll(SystemUtil.getStationId()));
		return returnPage("/agent/user/agentUserIndex");
	}

	@Permission("admin:agentUser")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(Long agentId, String username) {
		renderPage(agentUserService.page(SystemUtil.getStationId(), agentId, username));
	}

	/**
	 * 新增用户
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:agentUser:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		map.put("agents", agentService.getAll(SystemUtil.getStationId()));
		return returnPage("/agent/user/agentUserAdd");
	}

	/**
	 * 新增用户
	 *
	 * @param user
	 */
	@Permission("admin:agentUser:add")
	@ResponseBody
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public void doAdd(AgentUser user) {
		LoginAdminUtil.checkPerm();
		// 默认type为管理员
		user.setStationId(SystemUtil.getStationId());
		agentUserService.save(user);
		renderSuccess();
	}

	/**
	 * 修改用户真实姓名
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:agentUser:realName")
	@RequestMapping(value = "/showModifyRealName", method = RequestMethod.GET)
	public String showModifyRealName(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("agentUser", agentUserService.findOneById(id, SystemUtil.getStationId()));
		return returnPage("/agent/user/agentUserModifyRealName");
	}

	/**
	 * 修改真实姓名
	 *
	 * @param user
	 */
	@Permission("admin:agentUser:realName")
	@ResponseBody
	@RequestMapping(value = "/doModifyRealName", method = RequestMethod.POST)
	public void doModifyRealName(Long id, String realName) {
		LoginAdminUtil.checkPerm();
		agentUserService.modifyRealName(id, realName, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改用户备注
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:agentUser:remark")
	@RequestMapping(value = "/showModifyRemark", method = RequestMethod.GET)
	public String showModifyRemark(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("agentUser", agentUserService.findOneById(id, SystemUtil.getStationId()));
		return returnPage("/agent/user/agentUserModifyRemark");
	}

	/**
	 * 修改备注
	 *
	 * @param user
	 */
	@Permission("admin:agentUser:remark")
	@ResponseBody
	@RequestMapping(value = "/doModifyRemark", method = RequestMethod.POST)
	public void doModifyRemark(Long id, String remark) {
		LoginAdminUtil.checkPerm();
		agentUserService.modifyRemark(id, remark, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 */
	@Permission("admin:agentUser:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		LoginAdminUtil.checkPerm();
		agentUserService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改用户可用状态
	 *
	 * @param id
	 * @param status
	 */
	@Permission("admin:agentUser:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		LoginAdminUtil.checkPerm();
		agentUserService.changeStatus(id, status, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 重置密码
	 *
	 * @param id
	 */
	@Permission("admin:agentUser:pwd")
	@ResponseBody
	@RequestMapping(value = "/resetPwd")
	public void resetPwd(Long id) {
		LoginAdminUtil.checkPerm();
		agentUserService.resetPwd(id, SystemUtil.getStationId());
		renderSuccess("密码重置为Aa123456");
	}
}
