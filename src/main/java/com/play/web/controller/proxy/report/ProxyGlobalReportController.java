package com.play.web.controller.proxy.report;


import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.SysUser;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserService;
import com.play.service.ThirdGameService;
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
 * 全局报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/globalReport")
public class ProxyGlobalReportController extends ProxyBaseController {

	@Autowired
	private SysUserDailyMoneyService sysUserDailyMoneyService;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ThirdGameService gameService;

	//@Permission("admin:globalReport")
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
	//@Permission("admin:globalReport")
	@RequestMapping("/list")
	@NeedLogView("全局报表")
	public void list(String startDate, String endDate, String proxyName, String username, String agentUser,
			String userRemark, String degreeIds, Integer level, Integer userType) {
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
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(sysUserDailyMoneyService.globalReport(SystemUtil.getStationId(), begin, end, proxyName, username,
					agentUser, userRemark, degreeIds, level, userType));

	}

}
