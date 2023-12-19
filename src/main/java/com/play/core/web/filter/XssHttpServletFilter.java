package com.play.core.web.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class XssHttpServletFilter extends OncePerRequestFilter {
//	private String exclude = null; // 不需要过滤的路径集合
	private Pattern pattern = null; // 匹配不需要过滤路径的正则表达式

	public void setExclude(String exclude) {
//		this.exclude = exclude;
		pattern = Pattern.compile(getRegStr(exclude));
	}

	/**
	 * XSS过滤
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		if (StringUtils.isNotBlank(requestURI)) {
			requestURI = requestURI.replace(request.getContextPath(), "");
		}

		if (pattern!=null && pattern.matcher(requestURI).matches()) {
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(new XssHttpServletRequestWrapper(request), response);
		}
	}

	/**
	 * 将传递进来的不需要过滤得路径集合的字符串格式化成一系列的正则规则
	 * 
	 * @param str
	 *            不需要过滤的路径集合
	 * @return 正则表达式规则
	 */
	private String getRegStr(String str) {
		if (StringUtils.isNotBlank(str)) {
			String[] excludes = str.split(";"); // 以分号进行分割
			int length = excludes.length;
			for (int i = 0; i < length; i++) {
				String tmpExclude = excludes[i].trim();
				// 对点、反斜杠和星号进行转义
				tmpExclude = tmpExclude.replace("\\", "\\\\").replace(".", "\\.").replace("*", ".*");

				tmpExclude = "^" + tmpExclude + "$";
				excludes[i] = tmpExclude;
			}
			return StringUtils.join(excludes, "|");
		}
		return str;
	}
}