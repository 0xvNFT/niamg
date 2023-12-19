package com.play.web.controller.admin.report;

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
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

/**
 * 最高在线人数分析
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/onlineNum")
public class OnlineNumController extends AdminBaseController {
	@Autowired
	private StationOnlineNumService onlineNumService;

	@Permission("admin:onlineNum")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/report/onlineNumReport");
	}

	@Permission("admin:onlineNum")
	@ResponseBody
	@RequestMapping("/data")
	public void onlineData(String startTime, String endTime) {
		Date begin = StringUtils.isNotEmpty(startTime) ? DateUtil.toDatetime(startTime) : null;
		Date end = StringUtils.isNotEmpty(endTime) ? DateUtil.toDatetime(endTime) : null;
		renderJSON(onlineNumService.adminEchartsData(SystemUtil.getStationId(), begin, end));
	}

}
