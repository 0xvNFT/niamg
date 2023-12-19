package com.play.web.controller.manager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.play.common.SystemConfig;
import com.play.web.controller.BaseController;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginManagerUtil;

public class ManagerBaseController extends BaseController {
	
	public String returnPage(String pagePath) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (ServletUtils.isAjaxInvoie(request)) {
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append(pagePath).toString();
		} else {
			request.setAttribute("loginUser", LoginManagerUtil.currentUser());
			if (!pagePath.equals("/index") && !pagePath.equals("/login")) {
				request.setAttribute("content_page", pagePath);
				String url = request.getRequestURI();
				url = url.substring(request.getContextPath().length());
				request.setAttribute("content_url", url);
				return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append("/index").toString();
			}
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append(pagePath).toString();
		}
	}
}
