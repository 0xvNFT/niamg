package com.play.web.controller.manager.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

/**
 * 在线统计
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/onlineStat")
public class StationOnlineStatController extends ManagerBaseController {

	@Autowired
	private StationService stationService;

	@Permission("zk:onlineStat")
	@RequestMapping("/index")
	public String index() {
		return returnPage("/data/onlineStat");
	}

	@ResponseBody
	@Permission("zk:onlineStat")
	@RequestMapping("/list")
	@NeedLogView(value = "在线统计列表", type = LogTypeEnum.VIEW_LIST)
	public void list() {
		renderJSON(stationService.getManagerOnlineNum());
	}

}
