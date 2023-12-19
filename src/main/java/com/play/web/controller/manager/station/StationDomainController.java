package com.play.web.controller.manager.station;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.model.ManagerUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDomainService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.var.LoginManagerUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/stationDomain")
public class StationDomainController extends ManagerBaseController {
	@Autowired
	private StationDomainService domainService;
	@Autowired
	private StationService stationService;

	@Permission("zk:stationDomain")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/domain/domainIndex");
	}

	@Permission("zk:stationDomain")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点域名列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId, String name, String proxyUsername, Integer type) {
		ManagerUser user = LoginManagerUtil.currentUser();
		if (user.getType() == UserTypeEnum.MANAGER.getType() && StringUtils.isEmpty(name)) {
			renderPage(new Page<>());
			return;
		}
		super.renderPage(domainService.getDomainPage(stationId, name, proxyUsername, type));
	}

	@Permission("zk:stationDomain:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		domainService.changeStatus(id, status);
		renderSuccess();
	}

	@Permission("zk:stationDomain:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/domain/domainAdd");
	}

	@Permission("zk:stationDomain:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(Long stationId, Integer type, String domains, Integer ipMode, String proxyUsername,
			String agentName, String defaultHome, String remark) {
		renderSuccess(domainService.addStationDomain(stationId, type, domains, ipMode, proxyUsername, agentName,
				defaultHome, 1L, remark));
	}

	@Permission("zk:stationDomain:update")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "站点域名管理修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showModify(Map<String, Object> map, Long id) {
		map.put("domain", domainService.findOneById(id));
		return returnPage("/station/domain/domainModify");
	}

	@Permission("zk:stationDomain:update")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(Long id, Integer type, Integer ipMode, String proxyUsername, String agentName,
			String defaultHome, String remark) {
		domainService.modifyDomain(id, type, ipMode, proxyUsername, agentName, defaultHome, remark);
		renderSuccess();
	}

	@Permission("zk:stationDomain:del")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(Long id) {
		domainService.delete(id);
		renderSuccess();
	}
}
