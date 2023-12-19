package com.play.common.ip.country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class CNIpTool {
	private static Logger logger = LoggerFactory.getLogger(CNIpTool.class);
	private static final String CHINA_IPS = "cnIps.txt";
	/**
	 * 取模
	 */
	private static final long RANGE_SIZE = 10000000L;
	/**
	 * 分批存储
	 */
	private static Map<Integer, List<CNIpRecord>> recordMap;

	/**
	 * 系统启动时，必须先初始化
	 */
	public static void init() {
		try {
			recordMap = new HashMap<>();
			List<CNIpRecord> list = new ArrayList<>();
			String content = FileUtils.readFileToString(new ClassPathResource(CHINA_IPS).getFile(), "UTF-8");
//			String content = getFileContent(CHINA_IPS);
			String[] cs = content.split("\\|");
			String[] ips = null;
			for (String s : cs) {
				if (s == null) {
					continue;
				}
				ips = s.split(",");
				if (ips.length != 2) {
					continue;
				}
				long ipLong = Long.parseLong(ips[0]);
				int count = Integer.parseInt(ips[1]);
				list.add(new CNIpRecord(ipLong, count));
			}
			list.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE);
				List<CNIpRecord> key1List = recordMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordMap.put(key1, key1List);
				if (key2 > key1) {
					List<CNIpRecord> key2List = recordMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordMap.put(key2, key2List);
				}
			});
		} catch (Exception e) {
			logger.error("初始化中国ip范围发生错误", e);
		}
	}

	/**
	 * 判断是否是大陆IP
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isCNIp(String ip) {
		if (ip == null || ip.trim().isEmpty()) {
			return false;
		}
		if (StringUtils.equals("127.0.0.1", ip)) {
			return true;
		}
		if (isValidIpV4Address(ip)) {
			long value = ipToLong(ip);
			int key = (int) (value / RANGE_SIZE);
			if (recordMap != null && recordMap.containsKey(key)) {
				List<CNIpRecord> list = recordMap.get(key);
				return list.stream().anyMatch((CNIpRecord r) -> r.contains(value));
			}
		}
		return false;
	}

	/**
	 * 判断字段串是否是有效的IPV4地址。
	 *
	 * @return true-IPV4地址
	 */
	public static boolean isValidIpV4Address(String value) {

		int periods = 0;
		int i;
		int length = value.length();

		if (length > 15) {
			return false;
		}
		char c;
		StringBuilder word = new StringBuilder();
		for (i = 0; i < length; i++) {
			c = value.charAt(i);
			if (c == '.') {
				periods++;
				if (periods > 3) {
					return false;
				}
				if (word.length() == 0) {
					return false;
				}
				if (Integer.parseInt(word.toString()) > 255) {
					return false;
				}
				word.delete(0, word.length());
			} else if (!Character.isDigit(c)) {
				return false;
			} else {
				if (word.length() > 2) {
					return false;
				}
				word.append(c);
			}
		}

		if (word.length() == 0 || Integer.parseInt(word.toString()) > 255) {
			return false;
		}

		return periods == 3;
	}
//
//	private static String getFileContent(String path) throws IOException {
//		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(CHINA_IPS);
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "utf-8"));
//		StringBuilder sb = new StringBuilder();
//		String lineTxt = null;
//		while ((lineTxt = bufferedReader.readLine()) != null) {
//			sb.append(lineTxt);
//		}
//		bufferedReader.close();
//		input.close();
//		return sb.toString();
//	}

	public static void main(String[] args) throws IOException {
		System.err.println(isCNIp("52.128.240.34"));
		System.err.println(isCNIp("144.52.196.94"));
		System.err.println(isCNIp("112.67.217.17"));
		System.err.println(isCNIp("14.27.39.155"));
		System.err.println(isCNIp("113.66.54.79"));
		System.err.println(isCNIp("117.61.245.234"));
		System.err.println(isCNIp("116.10.140.160"));
		System.err.println(isCNIp("14.24.164.83"));
		System.err.println(isCNIp("223.104.66.167"));
		System.err.println(isCNIp("182.104.53.183"));
		System.err.println(isCNIp("117.136.12.18"));
		System.err.println(isCNIp("223.73.21.60"));
		System.err.println(isCNIp("125.95.74.145"));
		System.err.println(isCNIp("117.136.12.18"));
	}

	public static long ipToLong(String ip) {
		if (ip == null || ip.isEmpty()) {
			return 0;
		}
		String[] values = ip.split("\\.");
		if (values.length != 4) {
			return 0;
		}
		long result = 0;
		for (String value : values) {
			result = result * 256 + Integer.parseInt(value);
		}
		return result;
	}
}
