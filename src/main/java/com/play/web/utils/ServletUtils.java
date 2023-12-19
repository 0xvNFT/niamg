/**
 * <pre>
 * Copyright:           Copyright(C) 2011-2012, ketayao.com
 * Filename:            com.ketayao.utils.ServletUtils.java
 * Class:                       Servlets
 * Date:                        2013-7-19
 * Author:                      <a href="mailto:ketayao@gmail.com">ketayao</a>
 * Version          3.1.0
 * Description:
 *
 * </pre>
 **/

package com.play.web.utils;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.common.utils.CookieHelper;
import com.play.model.SysUserLogin;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriUtils;

/**
 * @author <a href="mailto:ketayao@gmail.com">ketayao</a> Version 3.1.0
 * @since 2013-7-19 下午2:25:31
 */

public class ServletUtils {

	/**
	 * 设置让浏览器弹出下载对话框的Header,不同浏览器使用不同的编码方式.
	 *
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
			String fileName) {
		final String CONTENT_DISPOSITION = "Content-Disposition";

		try {
			String agent = request.getHeader("User-Agent");
			String encodedfileName = null;
			if (null != agent) {
				agent = agent.toLowerCase();
				if (agent.contains("firefox") || agent.contains("chrome") || agent.contains("safari")) {
					encodedfileName = "filename=\"" + new String(fileName.getBytes(), "ISO8859-1") + "\"";
				} else if (agent.contains("msie")) {
					encodedfileName = "filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";
				} else if (agent.contains("opera")) {
					encodedfileName = "filename*=UTF-8\"" + fileName + "\"";
				} else {
					encodedfileName = "filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"";
				}
			}

			response.setHeader(CONTENT_DISPOSITION, "attachment; " + encodedfileName);
		} catch (UnsupportedEncodingException e) {
		}
	}

	public static String getCurrentUrl() {
		RequestAttributes requestAttributes = null;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
		}
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getRequestURI();
		}
		return null;
	}

	public static boolean isAjaxInvoie() {
		RequestAttributes requestAttributes = null;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
		}
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return isAjaxInvoie(request);
		}
		return false;
	}

	public static boolean isAjaxInvoie(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		return (requestType != null && requestType.equals("XMLHttpRequest"));
	}

	public static String getPrefixUrl() {
		RequestAttributes requestAttributes = null;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
		}
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getHeader("Referer");
		}
		return null;
	}

	/**
	 * 获取当前用户的session
	 *
	 * @return
	 */
	public static HttpSession getSession() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		if (request != null) {
			return request.getSession();
		}
		return null;
	}

	/**
	 * 通过请求路径 获取主域名
	 *
	 * @param requestUrl
	 * @return
	 */
	public static String getDomainName(String requestUrl) {
		int index = requestUrl.indexOf("://");
		// 去掉http头
		if (index > -1) {
			requestUrl = requestUrl.substring(index + 3);
		}
		if ((index = requestUrl.indexOf("/")) > -1) {
			requestUrl = requestUrl.substring(0, index);
		}

		// 截掉端口号
		index = requestUrl.indexOf(":");
		if (index > -1) {
			requestUrl = requestUrl.substring(0, index);
		}
		return requestUrl;
	}

	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = null;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
			// ignore 如unit test
		}
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return null;
	}

	public static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = null;
		try {
			requestAttributes = RequestContextHolder.currentRequestAttributes();
		} catch (Exception e) {
			// ignore 如unit test
		}
		if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getResponse();
		}
		return null;
	}

	public static String getUserAgent() {
		HttpServletRequest request = getRequest();
		if (request == null) {
			return "";
		}
		return request.getHeader("User-Agent");
	}

	/**
	 * 获取操作系统,浏览器及浏览器版本信息
	 * 
	 * @return
	 */
	public static String getBrowserInfo() {
		String userAgent = getUserAgent();
		String user = userAgent.toLowerCase();
		String browser = "";
		// ===============Browser===========================
		if (user.contains("edge")) {
			browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("msie")) {
			String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
			browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
		} else if (user.contains("safari") && user.contains("version")) {
			browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
					+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
		} else if (user.contains("opr") || user.contains("opera")) {
			if (user.contains("opera")) {
				browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if (user.contains("opr")) {
				browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
						.replace("OPR", "Opera");
			}

		} else if (user.contains("chrome")) {
			browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
		} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
				|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
				|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
			browser = "Netscape-?";

		} else if (user.contains("firefox")) {
			browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
		} else if (user.contains("rv")) {
			String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
			browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
		} else {
			browser = "UnKnown, More-Info: " + userAgent;
		}

		return browser;
	}

	public static String getIndexDomain() {
		HttpServletRequest request = getRequest();
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		return scheme + "://" + serverName;
	}

	/**
	 * 获取域名
	 */
	public static String getDomain() {
		HttpServletRequest request = getRequest();
		String scheme = request.getScheme();
		int serverPort = request.getServerPort();

		StringBuilder currentUrl = new StringBuilder();
		currentUrl.append(scheme);
		currentUrl.append("://");
		currentUrl.append(request.getServerName());
		if (!(("http".equals(scheme) && serverPort == 80) || ("https".equals(scheme) && serverPort == 443))) {
			currentUrl.append(":").append(serverPort);
		}
		currentUrl.append(request.getContextPath());
		return currentUrl.toString();
	}

	public static String getRequestURI(HttpServletRequest request) {
		return clearRequestURI(request.getRequestURI());
	}

	public static String clearRequestURI(String uri) {
		uri = removeSemicolonContent(uri);
		uri = decodeRequestString(uri);
		uri = RegExUtils.replacePattern(uri, " +", "");
		uri = RegExUtils.replacePattern(uri, "/+", "/");
		return uri;
	}

	private static String removeSemicolonContent(String requestUri) {
		int semicolonIndex = requestUri.indexOf(';');
		while (semicolonIndex != -1) {
			int slashIndex = requestUri.indexOf('/', semicolonIndex);
			String start = requestUri.substring(0, semicolonIndex);
			requestUri = (slashIndex != -1) ? start + requestUri.substring(slashIndex) : start;
			semicolonIndex = requestUri.indexOf(';', semicolonIndex);
		}
		return requestUri;
	}

	public static String getFingerprint(int terminal) {
		HttpServletRequest request = getRequest();
		String fingerprint = CookieHelper.get(request,"SESSIONV");
		if (terminal == SysUserLogin.TERMINAL_WAP && StringUtils.isEmpty(fingerprint)) {
			fingerprint = request.getHeader("User-Broswer");
			if (StringUtils.isEmpty(fingerprint)) {
				//modify johnny @ 2023-10-18
				//如果wap获取不到浏览器指纹，则暂时使用手机浏览器用户的当前ip来替代.cocos wap 。因暂未提供获取浏览器指纹的方案
				fingerprint = IpUtils.getIp();
			}
			LoggerFactory.getLogger("aa").error("frinpring of wap browser = " + fingerprint);
		}
		if (StringUtils.isNotEmpty(fingerprint) && !ValidateUtil.isFingerprint(fingerprint) && terminal != SysUserLogin.TERMINAL_WAP) {
			fingerprint = null;
		}
		LoggerFactory.getLogger("aa").error("frinpring in final = " + fingerprint);
		return fingerprint;
	}

	private static String decodeRequestString(String uri) {
		return UriUtils.decode(uri, "utf-8");
	}
}
