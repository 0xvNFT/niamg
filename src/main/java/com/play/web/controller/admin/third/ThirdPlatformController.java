package com.play.web.controller.admin.third;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/thirdPlatform")
public class ThirdPlatformController extends AdminBaseController {
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private ThirdPlatformService thirdPlatformService;

	@Permission("admin:thirdPlatform")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("game", thirdGameService.findOne(stationId));
		map.put("platforms", thirdPlatformService.find(stationId));
		return returnPage("/third/thirdPlatformIndex");
	}

	@Permission("admin:thirdPlatform:status")
	@ResponseBody
	@RequestMapping("/changeGameStatus")
	public void changeGameStatus(String type, Integer status) {
		thirdGameService.changeStatus(SystemUtil.getStationId(), type, status);
		super.renderSuccess();
	}

	@Permission("admin:thirdPlatform:status")
	@ResponseBody
	@RequestMapping("/changePlatformStatus")
	public void changePlatformStatus(Long id, Integer status) {
		thirdPlatformService.changeStatus(id, status);
		super.renderSuccess();
	}

}
