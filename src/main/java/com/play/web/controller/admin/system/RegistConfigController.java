package com.play.web.controller.admin.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.play.model.StationRegisterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.service.StationRegisterConfigService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/registConfig")
public class RegistConfigController extends AdminBaseController {
	@Autowired
	private StationRegisterConfigService registerConfigService;

	@Permission("admin:registConfig")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/system/registerConfig/registerConfigIndex");
	}

	@Permission("admin:registConfig")
	@ResponseBody
	@RequestMapping("/list")
	@NeedLogView("注册配置列表")
	public void list(Integer platform) {
		Map<String, Object> map = new HashMap<>();
		List<StationRegisterConfig> list = registerConfigService.initAndGetList(SystemUtil.getStationId(), platform);
		list = list.stream()
				.filter(config -> !("qq".equals(config.getEleName()) || "wechat".equals(config.getEleName())))
				.collect(Collectors.toList());
		map.put("data", list);
		super.renderJSON(map);
	}

	@Permission("admin:registConfig:updateProp")
	@RequestMapping(value = "/updateProp")
	@ResponseBody
	public void updateProp(Long id, String prop, Integer value) {
		registerConfigService.updateProp(id, SystemUtil.getStationId(), prop, value);
		super.renderSuccess();
	}

	@Permission("admin:registConfig:updateSortNo")
	@RequestMapping(value = "/showModify", method = RequestMethod.GET)
	public String showModify(Map<String, Object> map, Long id) {
		map.put("config", registerConfigService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/system/registerConfig/registerConfigModify");
	}

	@Permission("admin:registConfig:updateSortNo")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(Long id, Long sortNo,String name,String tips) {
		registerConfigService.updateSortNo(id, SystemUtil.getStationId(), sortNo,name,tips);
		renderSuccess();
	}
}
