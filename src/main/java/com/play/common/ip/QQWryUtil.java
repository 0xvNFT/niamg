package com.play.common.ip;

import java.io.IOException;

import com.play.web.exception.BaseException;

public class QQWryUtil {
	private static QQWry qqwry;

	private static QQWry getQqwry() {
		if (qqwry == null) {
			synchronized (QQWryUtil.class) {
				if (qqwry == null) {
					try {
						qqwry = new QQWry();
					} catch (IOException e) {
						throw new BaseException(e);
					}
				}
			}
		}
		return qqwry;
	}

	public static String getCountry(String ip) {
		IPZone zone = getQqwry().findIP(ip);
		if (zone != null) {
			return zone.getMainInfo();
		}
		return "";
	}
}
