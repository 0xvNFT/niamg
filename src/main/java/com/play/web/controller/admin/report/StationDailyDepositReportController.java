package com.play.web.controller.admin.report;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.StationDailyDepositService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 存款报表管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/depositReport")
public class StationDailyDepositReportController extends AdminBaseController {

	@Autowired
	private StationDailyDepositService dailyDepositService;

	@Permission("admin:depositReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/report/depositReport");
	}

	@ResponseBody
	@Permission("admin:depositReport")
	@RequestMapping("/list")
	@NeedLogView("充值报表")
	public void list(String startDate, String endDate, String payName, String sortName, String sortOrder,Integer depositType) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(startDate)) {
//		if (!hasViewAll && StringUtils.isEmpty(payName)) {
			renderJSON(new JSONObject());
		} else {
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(dailyDepositService.page(SystemUtil.getStationId(), begin, end, payName, sortName, sortOrder, depositType));
		}
	}

	@ResponseBody
	@Permission("admin:depositReport:export")
	@RequestMapping("/export")
	public void export(String startDate, String endDate, String payName,Integer depositType) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		dailyDepositService.depositReportExport(SystemUtil.getStationId(), begin, end, payName,depositType);
	}

}
