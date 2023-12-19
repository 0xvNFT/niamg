package com.play.web.controller.admin.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.RandomStringUtils;
import com.play.model.Agent;
import com.play.service.AgentService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/agent")
public class AgentManagerController extends AdminBaseController {
	@Autowired
	private AgentService agentService;

	@Permission("admin:agent")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return returnPage("/agent/agentIndex");
	}

	@Permission("admin:agent")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list() {
		renderPage(agentService.page(SystemUtil.getStationId()));
	}

	/**
	 * 新增用户
	 *
	 * @param map
	 * @return
	 */
	@Permission("admin:agent:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		 map.put("promoCode", RandomStringUtils.randomInt(8));
		return returnPage("/agent/agentAdd");
	}

	/**
	 * 新增用户
	 *
	 * @param user
	 */
	@Permission("admin:agent:add")
	@ResponseBody
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public void doAdd(Agent agent) {
		LoginAdminUtil.checkPerm();
		// 默认type为管理员
		agent.setStationId(SystemUtil.getStationId());
		agent.setPartnerId(SystemUtil.getPartnerId());
		agentService.save(agent);
		renderSuccess();
	}

	/**
	 * 修改用户
	 *
	 * @param map
	 * @param id
	 * @return
	 */
	@Permission("admin:agent:update")
	@RequestMapping(value = "/showModify", method = RequestMethod.GET)
	public String showModify(Map<String, Object> map, Long id) {
		LoginAdminUtil.checkPerm();
		map.put("agent", agentService.findById(id, SystemUtil.getStationId()));
		return returnPage("/agent/agentModify");
	}

	/**
	 * 修改
	 *
	 * @param user
	 */
	@Permission("admin:agent:update")
	@ResponseBody
	@RequestMapping(value = "/doModify", method = RequestMethod.POST)
	public void doModify(Agent agent) {
		LoginAdminUtil.checkPerm();
		agent.setStationId(SystemUtil.getStationId());
		agentService.update(agent);
		renderSuccess();
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 */
	@Permission("admin:agent:delete")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		LoginAdminUtil.checkPerm();
		agentService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改用户可用状态
	 *
	 * @param id
	 * @param status
	 */
	@Permission("admin:agent:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		LoginAdminUtil.checkPerm();
		agentService.changeStatus(id, status, SystemUtil.getStationId());
		renderSuccess();
	}
}
