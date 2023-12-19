package com.play.web.controller.admin.report;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

/**
 * 统计概况管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/monthReport")
public class MonthReportController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private ThirdGameService gameService;

	@Permission("admin:monthReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("game", gameService.findOne(SystemUtil.getStationId()));
		map.put("language",SystemUtil.getLanguage());
		return returnPage("/report/monthReport");
	}

	@ResponseBody
	@Permission("admin:monthReport")
	@RequestMapping("/list")
	@NeedLogView("统计概况")
	public void list() {
		renderJSON(userDailyMoneyService.adminMonthReport(SystemUtil.getStationId()));
	}
}
