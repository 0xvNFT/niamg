package com.play.web.controller.manager.data;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.StationOnlineNumService;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/stationOnlineNum")
public class StationOnlineNumController extends ManagerBaseController {

	@Autowired
	private StationOnlineNumService stationOnlineNumService;

	@Permission("zk:stationOnlineNum")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentTime());
		return returnPage("/data/stationOnlineNum");
	}

	@ResponseBody
	@RequestMapping("/data")
	@Permission("zk:stationOnlineNum")
	public void data(String startTime, String endTime) {
		Date begin = StringUtils.isNotEmpty(startTime) ? DateUtil.toDatetime(startTime) : null;
		Date end = StringUtils.isNotEmpty(endTime) ? DateUtil.toDatetime(endTime) : null;
		renderJSON(stationOnlineNumService.managerEchartsData(begin, end));
	}

}
