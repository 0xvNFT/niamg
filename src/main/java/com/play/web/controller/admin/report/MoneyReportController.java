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
import com.play.model.SysUserDailyMoney;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDailyMoneyService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 团队报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/moneyReport")
public class MoneyReportController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;

	@Permission("admin:moneyReport")
	@RequestMapping("/index")
	public String index(String username, Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("username", username);
		return returnPage("/report/moneyReport");
	}

	@ResponseBody
	@Permission("admin:moneyReport")
	@RequestMapping("/list")
	@NeedLogView("财务报表")
	public void list(String startDate, String endDate, String proxyName, String username, Integer pageSize,
			Integer pageNumber, String agentName) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<SysUserDailyMoney>());
		} else {
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.moneyReport(SystemUtil.getStationId(), begin, end, username, proxyName,
					pageNumber, pageSize, agentName));
		}
	}

	@ResponseBody
	@Permission("admin:moneyReport:export")
	@RequestMapping("/export")
	public void export(String startDate, String endDate, String proxyName, String username, String agentName) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		userDailyMoneyService.moneyReportExport(SystemUtil.getStationId(), begin, end, username, proxyName, agentName);
	}

}
