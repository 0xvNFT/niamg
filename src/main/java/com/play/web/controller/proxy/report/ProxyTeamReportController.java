package com.play.web.controller.proxy.report;


import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.SysUser;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserService;
import com.play.web.annotation.NeedLogView;

import com.play.web.controller.proxy.ProxyBaseController;

import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * 团队报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/teamReport")
public class ProxyTeamReportController extends ProxyBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserService sysUserService;

	//@Permission("admin:teamReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("lowestLevel", userService.getLowestLevel(stationId, null));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/report/teamReport");
	}

	@ResponseBody
	//@Permission("admin:teamReport")
	@RequestMapping("/list")
	@NeedLogView("团队报表")
	public void list(String startDate, String endDate, String proxyName, String username, Integer pageSize,
			Integer pageNumber, Integer level, String agentName, String degreeIds, String userRemark) {

			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);

		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);
			if (sysUser!=null&&!proxyName.equals(sysUser.getUsername())&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}else if (StringUtils.isEmpty(username)){
			proxyName = LoginMemberUtil.getUsername();
		}

		if (StringUtils.isNotEmpty(username)){
			SysUser sysUser = sysUserService.findOneByUsername(username,SystemUtil.getStationId(),null);
			if (sysUser!=null&&!username.equals(sysUser.getUsername())&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				username = LoginMemberUtil.getUsername();
			}
		}

			renderJSON(userDailyMoneyService.adminTeamReport(SystemUtil.getStationId(),begin, end, username, proxyName, pageNumber, pageSize,
					level, agentName, degreeIds, userRemark));

	}

	@ResponseBody
	//@Permission("admin:teamReport:report")
	@RequestMapping("/export")
	public void export(String startDate, String endDate, String proxyName, String username, Integer level,
			String agentName, String degreeIds, String userRemark) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		userDailyMoneyService.adminTeamReportExport(SystemUtil.getStationId(),begin, end, username, proxyName, level, agentName, degreeIds,
				userRemark);
	}

}
