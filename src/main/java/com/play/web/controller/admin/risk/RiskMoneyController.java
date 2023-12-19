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
import com.play.model.vo.RiskReportVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/riskMoney")
public class RiskMoneyController extends AdminBaseController {
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserDegreeService userDegreeService;

	@Permission("admin:riskMoney")
	@RequestMapping("/index")
	public String index(String username, Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("username", username);
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/risk/riskMoney");
	}

	@ResponseBody
	@Permission("admin:riskMoney")
	@RequestMapping("/list")
	public void list(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(SystemUtil.getStationId(), "money", begin, end, username,
					proxyName, degreeIds, agentName, sortName, sortOrder));
		}
	}
}
