package com.play.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CookieHelper {

	public static String get(HttpServletRequest request, String name) {
		if (null == request || StringUtils.isEmpty(name)) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void set(HttpServletResponse response, String name, String value) {
		String path = "/";
		int maxAge = 60 * 60 * 24 * 30;
		set(response, name, value, path, maxAge);
	}

	public static void set(HttpServletResponse response, String name, String value, String path) {
		int maxAge = 60 * 60 * 24 * 30;
		set(response, name, value, path, maxAge);
	}

	public static void set(HttpServletResponse response, String name, String value, int maxAge) {
		String path = "/";
		set(response, name, value, path, maxAge);
	}

	public static void set(HttpServletResponse response, String name, String value, String path, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	public static void delete(HttpServletResponse response, String name) {
		if (StringUtils.isEmpty(name)) {
			return;
		}
		String path = "/";
		delete(response, name, path);
	}

	public static void delete(HttpServletResponse response, String name, String path) {
		Cookie cookie = new Cookie(name, name);
		cookie.setMaxAge(0);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
}
