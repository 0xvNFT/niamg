package com.play.web.controller.admin.report;

import java.util.Date;
import java.util.Map;

import com.play.core.StationTimezoneEnum;
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
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 全局报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/globalReport")
public class GlobalReportController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService sysUserDailyMoneyService;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ThirdGameService gameService;

	@Permission("admin:globalReport")
	@RequestMapping("/index")
	public String index(String username, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("username", username);
		map.put("degrees", degreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("lowestLevel", sysUserService.getLowestLevel(stationId, null));
		map.put("game", gameService.findOne(stationId));
		return returnPage("/report/globalReport");
	}

	@ResponseBody
	@Permission("admin:globalReport")
	@RequestMapping("/list")
	@NeedLogView("全局报表")
	public void list(String startDate, String endDate, String proxyName, String username, String agentUser,
			String userRemark, String degreeIds, Integer level, Integer userType) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new JSONObject());
		} else {
			// 当前三方真人中心(mlangthird)部署为北京时区，写入到平台的数据为北京时间，平台打码量记录需按北京时区查询
			Date begin = DateUtil.getTzDateTime(startDate + " 00:00:00", StationTimezoneEnum.China.getTimezone1());
			Date end = DateUtil.getTzDateTime(endDate + " 00:00:00", StationTimezoneEnum.China.getTimezone1());
			renderJSON(sysUserDailyMoneyService.globalReport(SystemUtil.getStationId(), begin, end, proxyName, username,
					agentUser, userRemark, degreeIds, level, userType));
		}
	}

}
