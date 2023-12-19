package com.play.web.controller.admin.agent;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.AgentWhiteIp;
import com.play.service.AgentService;
import com.play.service.AgentWhiteIpService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/agentWhiteIp")
public class AgentWhiteIpManagerController extends AdminBaseController {
	@Autowired
	private AgentWhiteIpService agentWhiteIpService;
	@Autowired
	private AgentService agentService;

	@Permission("admin:agent:whiteIp")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("agents", agentService.getAll(SystemUtil.getStationId()));
		return returnPage("/agent/ip/agentWhiteIpIndex");
	}

	@Permission("admin:agent:whiteIp")
	@NeedLogView(value = "站点后台IP白名单列表", type = LogTypeEnum.VIEW_LIST)
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(Long agentId, String ip, Integer type) {
		renderPage(agentWhiteIpService.page(agentId, SystemUtil.getStationId(), ip, type));
	}

	@Permission("admin:agent:whiteIp:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		agentWhiteIpService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:agent:whiteIp:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		map.put("agents", agentService.getAll(SystemUtil.getStationId()));
		return returnPage("/agent/ip/agentWhiteIpAdd");
	}

	@Permission("admin:agent:whiteIp:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AgentWhiteIp ip) {
		ip.setStationId(SystemUtil.getStationId());
		String r = agentWhiteIpService.save(ip);
		if (StringUtils.isNotEmpty(r)) {
			renderSuccess(I18nTool.getMessage(BaseI18nCode.inputIpError, r));
			return;
		}
		renderSuccess();
	}
}
