package com.play.web.controller.manager.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.SysConfigEnum;
import com.play.core.LogTypeEnum;
import com.play.service.SysConfigService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/config")
public class SysConfigController extends ManagerBaseController {
	@Autowired
	private SysConfigService configService;

	@Permission("zk:config")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return returnPage("/config/configIndex");
	}

	@Permission("zk:config")
	@RequestMapping(value = "/getConfigs", method = RequestMethod.GET)
	@ResponseBody
	@NeedLogView(value = "系统配置列表",type = LogTypeEnum.VIEW_LIST)
	public void list() {
		Map<String, Object> map = new HashMap<>();
		map.put("configGroups", SysConfigEnum.getGroupNameList());
		map.put("configMap", SysConfigEnum.groupMap(configService.getAllMap()));
		renderJSON(map);
	}

	@Permission("zk:config:save")
	@ResponseBody
	@RequestMapping("/save")
	public void save(String pk, String value) {
		configService.save(pk, value);
		super.renderSuccess();
	}
}
