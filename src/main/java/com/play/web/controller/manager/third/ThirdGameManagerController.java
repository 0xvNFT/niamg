package com.play.web.controller.manager.third;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.service.StationService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/thirdGame")
public class ThirdGameManagerController extends ManagerBaseController {
	@Autowired
	private StationService stationService;
	@Autowired
	private ThirdGameService thirdGameService;

	@Permission("zk:thirdGame")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/third/thirdGameIndex");
	}

	@Permission("zk:thirdGame")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "游戏开关管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId) {
		renderJSON(thirdGameService.page(stationId));
	}

	@Permission("zk:thirdGame:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long stationId, String type, Integer status) {
		thirdGameService.changeStatus(stationId, type, status);
		renderSuccess();
	}

}
