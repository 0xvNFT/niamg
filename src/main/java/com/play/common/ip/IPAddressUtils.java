
package com.play.common.ip;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

public class IPAddressUtils {
	// 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
	private static Map<String, String> ipCache = new ConcurrentHashMap<>();

	/**
	 * 私有构造函数
	 */
	private IPAddressUtils() {
	}

	public static String getCountry(String ip) {
		if (StringUtils.isEmpty(ip)) {
			return "";
		}
		if (ipCache.containsKey(ip)) {
			return ipCache.get(ip);
		}
		String addr = null;
		if (IPV6Utils.isValidIpv6Addr(ip)) {
			addr = IPV6Utils.getInstance().getCountry(ip);
		} else {
			addr = QQWryUtil.getCountry(ip);
		}

		if (addr.equals("IANA")) {
			addr = " ";
		}
		if (StringUtils.isNotEmpty(addr)) {
			ipCache.put(ip, addr);
			return addr;
		}
		return "";
	}

}