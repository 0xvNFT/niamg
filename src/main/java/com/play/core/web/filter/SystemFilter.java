package com.play.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.core.StationConfigEnum;
import com.play.model.StationDomain;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import com.play.common.SystemConfig;
import com.play.common.utils.SpringUtils;
import com.play.model.Station;
import com.play.service.StationService;
import com.play.web.utils.ServletUtils;
import org.slf4j.LoggerFactory;

public class SystemFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (!isStaticFile(request)) {
			chain.doFilter(request, response);
			return;
		}
		//这里对手机静态链接:index.jsp进行处理，如果当前是PC访问则强制跳到PC主页，防止电脑访问手机index.jsp时展示的是手机界面
//		boolean pcDefaultMobile = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.pc_default_visit_mobile);
		if (request.getRequestURL().toString().endsWith("index.jsp")) {
			if (!MobileUtil.isMoblie(request)) {
				indexPage(response);
				return;
			}
		}

		/**
		 * 公共资源目录
		 */
		if (isNotMemberSource(request)) {
			chain.doFilter(request, response);
			return;
		}
		String filePath = getPostUrl(request);
		if (filePath != null) {
			request.getRequestDispatcher(filePath).forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/common/error/404.html");
		}
	}

	private void indexPage(HttpServletResponse response) throws IOException {
		response.sendRedirect(ServletUtils.getDomain());
	}

//	private static String[] exts = new String[] { ".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".html", ".htm",
//			".ico", ".swf", ".mp3", ".htc", ".mp4",".jsp",".properties"};
	private static String[] exts = new String[] { ".js", ".css", ".png", ".jpg", ".jpeg", ".gif", ".html", ".htm",
			".ico", ".swf", ".mp3", ".htc", ".mp4",".jsp"};

	private boolean isStaticFile(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (StringUtils.endsWithAny(uri, exts)) {
			return true;
		}
		return false;
	}

	private String getPostUrl(HttpServletRequest request) {
		String basePath = request.getContextPath();
		Station s = SpringUtils.getBean(StationService.class)
				.findByDomain(ServletUtils.getDomainName(request.getRequestURL().toString()));
		if (s == null) {
			return null;
		}
		String url = request.getRequestURI();
		return new StringBuilder("/member/").append(s.getCode()).append(url.substring(basePath.length()))
				.toString();
	}

	private boolean isNotMemberSource(HttpServletRequest request) {
		String url = request.getRequestURI();
		String basePath = request.getContextPath();
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_COMMON)) {// 公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_MOBILE)) {// mobile公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_WAP)) {// wap公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_M)) {// m公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.CONTROL_PATH_GAMES)) {// 游戏公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_NATIVE)) {// native公共目录
			return true;
		}
		if (url.startsWith(basePath + SystemConfig.SOURCE_FOLDER_MANAGER)) {// 后台总管理目录
			return true;
		}
		if (url.startsWith(basePath + "/.well-known/")) {
			return true;
		}
		if (url.startsWith(basePath + "/res/")) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {

	}
}
