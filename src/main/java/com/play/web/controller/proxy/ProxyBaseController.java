package com.play.web.controller.proxy;

import javax.servlet.http.HttpServletRequest;

import com.play.core.LanguageEnum;

import com.play.web.var.SystemUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.play.common.SystemConfig;
import com.play.web.controller.BaseController;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;

public class ProxyBaseController extends BaseController {
	public String returnPage(String pagePath) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (ServletUtils.isAjaxInvoie(request)) {
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_PROXY).append(pagePath).toString();
		} else {
			request.setAttribute("languages", LanguageEnum.values());

			request.setAttribute("language", SystemUtil.getLanguage());
			request.setAttribute("loginUser", LoginMemberUtil.currentUser());
			if (!pagePath.equals("/index") && !pagePath.equals("/login")) {
				request.setAttribute("content_page", pagePath);
				String url = request.getRequestURI();
				url = url.substring(request.getContextPath().length());
				request.setAttribute("content_url", url);
				return new StringBuilder(SystemConfig.SOURCE_FOLDER_PROXY).append("/index").toString();
			}
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_PROXY).append(pagePath).toString();
		}
	}

}
