package com.play.web.controller.manager.third;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.core.PlatformType;
import com.play.service.StationService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/thirdPlatform")
public class ThirdPlatformManagerController extends ManagerBaseController {
	@Autowired
	private StationService stationService;
	@Autowired
	private ThirdPlatformService thirdPlatformService;

	@Permission("zk:thirdPlatform")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		map.put("platforms", PlatformType.values());
		return returnPage("/third/thirdPlatformIndex");
	}

	@Permission("zk:thirdPlatform")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "三方平台开关管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId, Integer platform) {
		renderJSON(thirdPlatformService.page(stationId, platform));
	}

	@Permission("zk:thirdPlatform:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		thirdPlatformService.changeStatus(id, status);
		renderSuccess();
	}
}
