package com.play.core.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class HomePageServlet extends HttpServlet {

	private static final Map<String, String> SOURCE_MAPPING = new HashMap<String, String>();

	static {
		// 总控
		SOURCE_MAPPING.put("/manager", "/manager/index.do");
		SOURCE_MAPPING.put("/manager/", "/manager/index.do");
		// 合作商后台
		SOURCE_MAPPING.put("/partner", "/partner/index.do");
		SOURCE_MAPPING.put("/partner/", "/partner/index.do");
		// 站点后台
		SOURCE_MAPPING.put("/admin", "/admin/index.do");
		SOURCE_MAPPING.put("/admin/", "/admin/index.do");
		// 站点代理商
		SOURCE_MAPPING.put("/agent", "/agent/index.do");
		SOURCE_MAPPING.put("/agent/", "/agent/index.do");
		//站点代理
		SOURCE_MAPPING.put("/proxy", "/proxy/index.do");
		SOURCE_MAPPING.put("/proxy/", "/proxy/index.do");
		// wap
		SOURCE_MAPPING.put("/m", "/m/index.do");
		SOURCE_MAPPING.put("/m/", "/m/index.do");

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String base = req.getContextPath();
		String uri = req.getRequestURI();
		String urlKey = StringUtils.substringAfter(uri, base);
		//cocos 版本手机wap特殊处理
		String path = null;
		if (StringUtils.isNotEmpty(urlKey) && urlKey.contains("/im/")) {
			path = "/im/index.do";
		}else{
			path = SOURCE_MAPPING.get(urlKey);
		}
		req.getRequestDispatcher(path).forward(req, resp);
	}
}
