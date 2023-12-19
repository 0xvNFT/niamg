package com.play.web.controller.admin.report;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserService;
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
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/teamReport")
public class TeamReportController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserDegreeService userDegreeService;

	@Permission("admin:teamReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("lowestLevel", userService.getLowestLevel(stationId, null));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/report/teamReport");
	}

	@ResponseBody
	@Permission("admin:teamReport")
	@RequestMapping("/list")
	@NeedLogView("团队报表")
	public void list(String startDate, String endDate, String proxyName, String username, Integer pageSize,
			Integer pageNumber, Integer level, String agentName, String degreeIds, String userRemark) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new JSONObject());
		} else {
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminTeamReport(SystemUtil.getStationId(),begin, end, username, proxyName, pageNumber, pageSize,
					level, agentName, degreeIds, userRemark));
		}
	}

	@ResponseBody
	@Permission("admin:teamReport:report")
	@RequestMapping("/export")
	public void export(String startDate, String endDate, String proxyName, String username, Integer level,
			String agentName, String degreeIds, String userRemark) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		userDailyMoneyService.adminTeamReportExport(SystemUtil.getStationId(),begin, end, username, proxyName, level, agentName, degreeIds,
				userRemark);
	}

}
