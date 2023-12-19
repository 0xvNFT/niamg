package com.play.web.controller.proxy.report;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.SysUser;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserService;

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
 * 代理报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/proxyReport")
public class ProxyReportProxyController extends ProxyBaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;

	//@Permission("admin:proxyReport")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/report/proxyReport");
	}

	@ResponseBody
	//@Permission("admin:proxyReport")
	@RequestMapping("/list")
	public void list(String startDate, String endDate,String proxyName) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		if (StringUtils.isEmpty(proxyName)){
			proxyName = LoginMemberUtil.getUsername();
		}else {
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);
			if (sysUser!=null&&!proxyName.equals(sysUser.getUsername())&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}
		//代理会员查看报表只查看和统计直属下级的数据，不统计直属下级的所有弟归下级数据；用于投放时代理线查看投放情况
		renderJSON(userDailyMoneyService.adminProxyReport(SystemUtil.getStationId(), begin, end, proxyName,true));
	}

}
