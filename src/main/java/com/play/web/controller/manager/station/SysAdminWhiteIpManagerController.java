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
import com.play.model.AdminWhiteIp;
import com.play.service.AdminWhiteIpService;
import com.play.service.StationService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.i18n.BaseI18nCode;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/adminWhiteIp")
public class SysAdminWhiteIpManagerController extends ManagerBaseController {
	@Autowired
	private AdminWhiteIpService adminWhiteIpService;
	@Autowired
	private StationService stationService;

	@Permission("zk:adminWhiteIp")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/ip/adminWhiteIpIndex");
	}

	@Permission("zk:adminWhiteIp")
	@NeedLogView(value = "站点后台IP白名单列表", type = LogTypeEnum.VIEW_LIST)
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(Long stationId, String ip, Integer type) {
		renderPage(adminWhiteIpService.page(null, stationId, ip, type));
	}

	@Permission("zk:adminWhiteIp:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		adminWhiteIpService.delete(id, null);
		renderSuccess();
	}

	@Permission("zk:adminWhiteIp:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/ip/adminWhiteIpAdd");
	}

	@Permission("zk:adminWhiteIp:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AdminWhiteIp ip) {
		String r = adminWhiteIpService.save(ip);
		if (StringUtils.isNotEmpty(r)) {
			renderSuccess(I18nTool.getMessage(BaseI18nCode.inputIpError, r));
			return;
		}
		renderSuccess();
	}
}
