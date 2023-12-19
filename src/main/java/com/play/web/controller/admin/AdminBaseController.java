package com.play.web.controller.admin;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.play.common.SystemConfig;
import com.play.core.LanguageEnum;
import com.play.core.StationConfigEnum;
import com.play.web.controller.BaseController;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

public class AdminBaseController extends BaseController {

	public String returnPage(String pagePath) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (ServletUtils.isAjaxInvoie(request)) {
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append(pagePath).toString();
		} else {
			request.setAttribute("languages", LanguageEnum.values());
			Long stationId = SystemUtil.getStationId();
			request.setAttribute("depositVoice",
					StationConfigUtil.get(stationId, StationConfigEnum.deposit_voice_path));
			request.setAttribute("withdrawVoice",
					StationConfigUtil.get(stationId, StationConfigEnum.withdraw_voice_path));
			request.setAttribute("language", SystemUtil.getLanguage());
			request.setAttribute("loginUser", LoginAdminUtil.currentUser());
			if (!pagePath.equals("/index") && !pagePath.equals("/login")) {
				request.setAttribute("content_page", pagePath);
				String url = request.getRequestURI();
				url = url.substring(request.getContextPath().length());
				request.setAttribute("content_url", url);
				return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append("/index").toString();
			}
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append(pagePath).toString();
		}
	}
	

	public Set<Long> getSet(String ids) {
		Set<Long> set = new HashSet<>();
		if (ids != null) {
			for (String id : ids.split(",")) {
				set.add(NumberUtils.toLong(id));
			}
		}
		return set;
	}
}
