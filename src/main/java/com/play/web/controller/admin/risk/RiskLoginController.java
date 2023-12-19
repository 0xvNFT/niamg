package com.play.web.controller.admin.risk;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.SysUserDailyMoney;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 系统风险评估
 * 运营分析
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/riskLogin")
public class RiskLoginController extends AdminBaseController {
	@Autowired
	private SysUserService userService;

	@Autowired
	private SysUserDegreeService userDegreeService;

	@Permission("admin:riskLogin")
	@RequestMapping("/index")
	public String index(String username, Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("username", username);
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/risk/riskLogin");
	}

	@ResponseBody
	@Permission("admin:riskLogin")
	@RequestMapping("/list")
	@SortMapping(mapping = { "createDatetime=create_datetime", "lastLoginDatetime=last_login_datetime" })
	public void list(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<SysUserDailyMoney>());
		} else {
			startDate=startDate+" 00:00:00";
			endDate=endDate+" 23:59:59";
			Date begin = DateUtil.toDatetime(startDate);
			Date end = DateUtil.toDatetime(endDate);
			renderJSON(userService.adminRiskReport(SystemUtil.getStationId(), begin, end, proxyName, username,
					degreeIds, agentName));
		}
	}
}
