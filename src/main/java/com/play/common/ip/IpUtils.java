
package com.play.common.ip;

import com.play.common.SystemConfig;
import com.play.model.StationDomain;
import com.play.web.var.SysThreadObject;
import com.play.web.var.SysThreadVariable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

public class IpUtils {
	private IpUtils() {
	}

	public static String getSafeIpAdrress(HttpServletRequest request) {
		String ip = request.getHeader("cocos-custom-ip");
		if (StringUtils.isNotEmpty(ip)) {
			LoggerFactory.getLogger("iputils").error("cocos custom ip ==== " + ip);
			return ip;
		}
		ip = request.getRemoteAddr();
		if (StringUtils.contains(SystemConfig.ALL_CDN_IP_LIST, "," + ip + ",")) {
			// 如果ip在cdn的ip内，则获取X-Forwarded-For
			ip = request.getHeader("X-Forwarded-For");
		}
		return safeIp(ip);
	}

	private static String safeIp(String ipAddress) {
		if (StringUtils.isEmpty(ipAddress)) {
			return "";
		}
		if (ipAddress.indexOf("<") > -1) {
			return "";
		}
		if (ipAddress.startsWith("::ffff:")) {
			ipAddress = ipAddress.substring(7);
		}
		return ipAddress;
	}

	public static String getIpAddr(HttpServletRequest request, Integer ipMode) {
		if (request == null) {
			return "unknown";
		}
		String appIp = request.getHeader("cocos-custom-ip");
		if (StringUtils.isNotEmpty(appIp)) {
			LoggerFactory.getLogger("iputils").error("cocos custom ip ==== " + appIp);
			return appIp;
		}
		if (ipMode != null && ipMode == StationDomain.MODE_SAFE) {// 使用安全模式获取ip
			return getSafeIpAdrress(request);
		}
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("x-forwarded-for");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Cdn-Src-Ip");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("HTTP_CLIENT_IP");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (StringUtils.equals(ipAddress, "127.0.0.1") || StringUtils.equals(ipAddress, "[::1]")
					|| StringUtils.equals(ipAddress, "0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
					if (inet != null) {
						ipAddress = inet.getHostAddress();
					}
				} catch (Exception e) {
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
			// = 15
			if (ipAddress.indexOf(",") > 0) {
				String[] ips = ipAddress.split(",");
				for (String ip : ips) {
					ip = StringUtils.trim(ip);
					if (StringUtils.isEmpty(ip) || ip.indexOf("<") > -1) {
						continue;
					}
					ipAddress = ip;
					break;
				}
			}
		}
		return safeIp(ipAddress);
	}

	public static String getIp() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getIp();
		}
		return "";

	}

	/**
	 * 获取计算机名
	 *
	 * @return
	 */
	public static String getName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			if (addr != null) {
				return addr.getHostName().toString();// 获得本机名称
			}
		} catch (Exception e) {
		}
		return "unknown";
	}

	/**
	 * 是否是总控IP白名单
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isManagerWhiteIp(String ip) {
		if (null != SystemConfig.IP_WHITE_MANAGER && !SystemConfig.IP_WHITE_MANAGER.isEmpty()) {
			for (String d : SystemConfig.IP_WHITE_MANAGER) {
				if (StringUtils.equals(d, ip)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

}
