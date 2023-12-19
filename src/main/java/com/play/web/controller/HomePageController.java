package com.play.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.play.common.SystemConfig;
import com.play.model.StationDomain;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.ControllerRender;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;

@Controller
public class HomePageController {

	@NotNeedLogin
	@RequestMapping("/home")
	public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		switch (SystemUtil.getStationType()) {
		case MANAGER:
			return "forward:" + SystemConfig.CONTROL_PATH_MANAGER + "/index.do";
		case PARTNER:
			return "forward:" + SystemConfig.CONTROL_PATH_PARTNER + "/index.do";
		case ADMIN:
			return "forward:" + SystemConfig.CONTROL_PATH_ADMIN + "/index.do";
		case RECEIVE:
			ControllerRender.renderHtml("It is OK!");
			return null;
		case AGENT:
			return "forward:" + SystemConfig.CONTROL_PATH_AGENT + "/index.do";
		default:
		}
		//PC前台如果默认要访问手机端，则转发到手机路径
		boolean pcDefaultMobile = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.pc_default_visit_mobile);
		if (MobileUtil.isMoblie(request) || pcDefaultMobile) {
			return indexMobile(request, response);
		}
		return indexPage(response);
	}

	private String indexMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StationDomain domain = SystemUtil.getDomain();
		String defaultHome = domain.getDefaultHome();
		if (StringUtils.isNotEmpty(defaultHome) && !LoginMemberUtil.isLogined()) {
			return "forward:/" + defaultHome;
		}
		if (StringUtils.isNotEmpty(defaultHome) && defaultHome.startsWith("regpage")
				|| defaultHome.startsWith("/regpage")) {
			return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + defaultHome;
		}
		return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + "/index.do";
	}

	private String indexPage(HttpServletResponse response) throws IOException {
		StationDomain domain = SystemUtil.getDomain();
		String defaultHome = domain.getDefaultHome();
		if (StringUtils.isNotEmpty(defaultHome) && !LoginMemberUtil.isLogined()) {
			if (StringUtils.startsWith(defaultHome, "forward:") || StringUtils.startsWith(defaultHome, "redirect:")) {
				return defaultHome;
			}
			if ((StringUtils.equals("regpage.do", defaultHome) || StringUtils.equals("loginPage.do", defaultHome)
					|| StringUtils.equals("index/loginInterface.do", defaultHome)) && LoginMemberUtil.isLogined()) {
				return "forward:/index.do";
			}
			response.sendRedirect(ServletUtils.getDomain() + defaultHome);
			return null;
		}
		return "forward:/index.do";
	}
}
