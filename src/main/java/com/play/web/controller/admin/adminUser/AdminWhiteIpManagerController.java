package com.play.web.controller.admin.adminUser;

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
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/adminWhiteIp")
public class AdminWhiteIpManagerController extends AdminBaseController {
	@Autowired
	private AdminWhiteIpService adminWhiteIpService;

	@Permission("admin:adminWhiteIp")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/adminuser/ip/adminWhiteIpIndex");
	}

	@Permission("admin:adminWhiteIp")
	@NeedLogView(value = "站点后台IP白名单列表", type = LogTypeEnum.VIEW_LIST)
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list(String ip, Integer type) {
		renderPage(adminWhiteIpService.page(null, SystemUtil.getStationId(), ip, type));
	}

	@Permission("admin:adminWhiteIp:del")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		adminWhiteIpService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:adminWhiteIp:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		return returnPage("/adminuser/ip/adminWhiteIpAdd");
	}

	@Permission("admin:adminWhiteIp:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AdminWhiteIp ip) {
		ip.setStationId(SystemUtil.getStationId());
		String r = adminWhiteIpService.save(ip);
		if (StringUtils.isNotEmpty(r)) {
			renderSuccess(I18nTool.getMessage(BaseI18nCode.inputIpError, r));
			return;
		}
		renderSuccess();
	}
}
