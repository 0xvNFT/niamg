package com.play.web.controller.admin.report;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.StationDailyRegisterService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

/**
 * 每日注册报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/dailyRegisterReport")
public class DailyRegisterReportController extends AdminBaseController {

	@Autowired
	private StationDailyRegisterService stationDailyRegisterService;

	@Permission("admin:dailyRegisterReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Date now = new Date();
		map.put("endDate", DateUtil.toDateStr(now));
		map.put("startDate", DateUtil.toDateStr(DateUtils.addDays(now, -30)));
		return returnPage("/report/dailyRegisterReport");
	}

	@Permission("admin:dailyRegisterReport")
	@RequestMapping("/list")
	@NeedLogView("每日注册报表")
	@ResponseBody
	public void list(String startDate, String endDate, String proxyName, String agentName) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		if (StringUtils.isEmpty(proxyName) && StringUtils.isEmpty(agentName)) {
			renderJSON(stationDailyRegisterService.getPage(SystemUtil.getStationId(), begin, end));
		} else {
			renderJSON(stationDailyRegisterService.getPageByproxyName(SystemUtil.getStationId(), begin, end, proxyName,
					agentName));
		}

	}

	@Permission("admin:dailyRegisterReport:export")
	@RequestMapping("/export")
	@ResponseBody
	public void export(String startDate, String endDate, String proxyName, String agentName) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		stationDailyRegisterService.export(SystemUtil.getStationId(), begin, end);
		renderSuccess();
	}
}
