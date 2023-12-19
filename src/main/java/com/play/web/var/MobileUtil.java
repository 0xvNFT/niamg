package com.play.web.var;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class MobileUtil {

	/**
	 * 检查访问方式是否为移动端
	 * 
	 * @Title: isMoblie
	 * @throws IOException
	 */
	public static boolean isMoblie(HttpServletRequest request) {
		boolean isFromMobile = false;
		// 检查是否已经记录访问方式（移动端或pc端）
		try {
			// 获取ua，用来判断是否为移动端访问
			String userAgent = request.getHeader("User-Agent");
			if (null == userAgent) {
				userAgent = "";
			}
			isFromMobile = CheckMobile.check(userAgent.toLowerCase());
		} catch (Exception e) {
		}
		return isFromMobile;
	}

	public static class CheckMobile {

		// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔), 字符串在编译时会被转码一次,所以是 "\\b"
		// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
		static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
				+ "|windows (phone|ce)|blackberry|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
				+ "|laystation portable)|nokia|fennec|htc[-_]|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
		static String padReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

		// 移动设备正则匹配：手机端、平板
		static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
		static Pattern padPat = Pattern.compile(padReg, Pattern.CASE_INSENSITIVE);

		/**
		 * 检测是否是移动设备访问
		 * 
		 * @Title: check
		 * @Date : 2014-7-7 下午01:29:07
		 * @param userAgent 浏览器标识
		 * @return true:移动设备接入，false:pc端接入
		 */
		public static boolean check(String userAgent) {
			if (null == userAgent) {
				userAgent = "";
			}
			// 匹配
			Matcher matcherPhone = phonePat.matcher(userAgent);
			Matcher matcherTable = padPat.matcher(userAgent);
			if (matcherPhone.find() || matcherTable.find()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean judgeIsRNApp(HttpServletRequest request) {
		if (request.getHeader("User-Agent") != null) {
			String agent = request.getHeader("User-Agent");
			if (agent.startsWith("android/") || agent.startsWith("ios/") || agent.startsWith("wap/")) {
				return true;
			}
		}
		return false;
	}

	public static boolean judgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "ios", "iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java",
				"opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
				"dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola",
				"foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad",
				"webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips",
				"sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			String agent = request.getHeader("User-Agent");
			for (String mobileAgent : mobileAgents) {
				if (agent.toLowerCase().indexOf(mobileAgent) >= 0 && agent.toLowerCase().indexOf("windows nt") <= 0
						&& agent.toLowerCase().indexOf("macintosh") <= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
}
