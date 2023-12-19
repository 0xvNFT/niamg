package com.play.web.controller.manager.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.service.StationConfigService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/stationConfig")
public class SysStationConfigManagerController extends ManagerBaseController {
	@Autowired
	private StationConfigService stationConfigService;
	@Autowired
	private StationService stationService;

	@Permission("zk:stationConfig")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@NeedLogView(value = "站点配置管理列表", type = LogTypeEnum.VIEW_LIST)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/configIndex");
	}

	@Permission("zk:stationConfig")
	@RequestMapping(value = "/getSettings", method = RequestMethod.GET)
	@ResponseBody
	@NeedLogView(value = "绑定站点配置列表", type = LogTypeEnum.VIEW_DETAIL)
	public void getSettings(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("configGroups", StationConfigEnum.getGroupNameList());
		map.put("configMap", StationConfigEnum.groupMap());
		map.put("stationConfigs", stationConfigService.getAllSet(stationId));
		renderJSON(map);
	}

	@Permission("zk:stationConfig")
	@RequestMapping(value = "/getConfigs", method = RequestMethod.GET)
	@ResponseBody
	@NeedLogView(value = "设置站点配置列表", type = LogTypeEnum.VIEW_DETAIL)
	public void getConfigs(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("configGroups", StationConfigEnum.getGroupNameList());
		map.put("configs", stationConfigService.getAll(stationId, null));
		renderJSON(map);
	}

	@Permission("zk:stationConfig:save")
	@ResponseBody
	@RequestMapping("/saveSettings")
	public void saveSettings(Long stationId, String keys) {
		stationConfigService.saveSettings(stationId, keys);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_CONFIG, "s:" + stationId + ":k:");
		super.renderSuccess();
	}

	@Permission("zk:stationConfig:save")
	@ResponseBody
	@RequestMapping("/saveConfig")
	public void saveConfig(Long stationId, String pk, String value) {
		stationConfigService.saveConfig(stationId, pk, value);
		CacheUtil.delCacheByPrefix(CacheKey.STATION_CONFIG, "s:" + stationId + ":k:" + pk);
		super.renderSuccess();
	}
}
