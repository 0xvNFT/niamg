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
import com.play.service.SysUserDailyMoneyService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 代理报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/proxyReport")
public class ProxyReportController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;

	@Permission("admin:proxyReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/report/proxyReport");
	}

	@ResponseBody
	@Permission("admin:proxyReport")
	@RequestMapping("/list")
	public void list(String startDate, String endDate, String proxyName) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(proxyName)) {
			renderJSON(new JSONObject());
		} else {
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminProxyReport(SystemUtil.getStationId(), begin, end, proxyName,true));
		}
	}

}
