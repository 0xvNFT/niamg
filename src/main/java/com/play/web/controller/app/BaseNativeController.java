package com.play.web.controller.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

import ch.qos.logback.core.joran.action.ActionUtil;

public class BaseNativeController extends ActionUtil {

	/**
	 * 获取站点资源目录
	 * 
	 * @return
	 */
	public String getDomainFolder() {
		return SystemConfig.CONTROL_PATH_NATIVE;
	}

	/**
	 * 网站是否维护中
	 * 
	 * @return
	 */
	public Map<String, Object> maintaince() {
		Long stationId = SystemUtil.getStationId();
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.maintenance_switch)) {
			String cause = StationConfigUtil.get(stationId, StationConfigEnum.maintenance_cause);
			Map<String, Object> json = new HashMap<>();
			json.put("success", false);
			json.put("code", 20001);
			json.put("msg", StringUtils.isNotEmpty(cause) ? cause : "网站正在维护中...");
			return json;
		}
		return null;
	}

	public String goPage(String jspPath) {
		return getDomainFolder() + "/" + jspPath;
	}

}
