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

public class ALLIpTool {
	private static Logger logger = LoggerFactory.getLogger(ALLIpTool.class);
	private static final String ALL_IPS = "apnicIps.txt";

	private static final String ARIN_IPS = "arinIps.txt";

	private static final String AFRINIC_IPS = "afrinicIps.txt";

	private static final String LACNIC_IPS = "lacnicIps.txt";

	private static final String RIPE_IPS = "ripeIps.txt";

	/**
	 * 取模
	 */
	private static final long RANGE_SIZE_ALL = 10000000L;
	/**
	 * 分批存储
	 */
	private static Map<Integer, List<ALLIpRecord>> recordApnicMap;

	private static Map<Integer, List<ALLIpRecord>> recordArinMap;

	private static Map<Integer, List<ALLIpRecord>> recordAfrinicMap;

	private static Map<Integer, List<ALLIpRecord>> recordLacnicMap;

	private static Map<Integer, List<ALLIpRecord>> recordRipeMap;

	/**
	 * 系统启动时，必须先初始化
	 */
	public static void init() {
		try {
			recordApnicMap = new HashMap<>();
			List<ALLIpRecord> list = new ArrayList<>();
			String content = FileUtils.readFileToString(new ClassPathResource(ALL_IPS).getFile(), "UTF-8");
			String[] cs = content.split("\\|");
			String[] ips = null;
			for (String s : cs) {
				if (s == null) {
					continue;
				}
				ips = s.split(",");
				if (ips.length != 3) {
					continue;
				}
				long ipLong = Long.parseLong(ips[0]);
				int count = Integer.parseInt(ips[1]);
				String countryCode = ips[2];
				list.add(new ALLIpRecord(ipLong, count, countryCode));
			}
			list.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE_ALL);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE_ALL);
				List<ALLIpRecord> key1List = recordApnicMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordApnicMap.put(key1, key1List);
				if (key2 > key1) {
					List<ALLIpRecord> key2List = recordApnicMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordApnicMap.put(key2, key2List);
				}
			});

			recordArinMap = new HashMap<>();
			List<ALLIpRecord> arinList = new ArrayList<>();
			String arinContent = FileUtils.readFileToString(new ClassPathResource(ARIN_IPS).getFile(), "UTF-8");
			String[] arincs = arinContent.split("\\|");
			String[] arinips = null;
			for (String s : arincs) {
				if (s == null) {
					continue;
				}
				arinips = s.split(",");
				if (arinips.length != 3) {
					continue;
				}
				long ipLong = Long.parseLong(arinips[0]);
				int count = Integer.parseInt(arinips[1]);
				String countryCode = arinips[2];
				arinList.add(new ALLIpRecord(ipLong, count, countryCode));
			}
			arinList.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE_ALL);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE_ALL);
				List<ALLIpRecord> key1List = recordArinMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordArinMap.put(key1, key1List);
				if (key2 > key1) {
					List<ALLIpRecord> key2List = recordArinMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordArinMap.put(key2, key2List);
				}
			});

			recordAfrinicMap = new HashMap<>();
			List<ALLIpRecord> afrinicList = new ArrayList<>();
			String afrinicContent = FileUtils.readFileToString(new ClassPathResource(AFRINIC_IPS).getFile(), "UTF-8");
			String[] afriniccs = afrinicContent.split("\\|");
			String[] afrinicips = null;
			for (String s : afriniccs) {
				if (s == null) {
					continue;
				}
				afrinicips = s.split(",");
				if (afrinicips.length != 3) {
					continue;
				}
				long ipLong = Long.parseLong(afrinicips[0]);
				int count = Integer.parseInt(afrinicips[1]);
				String countryCode = afrinicips[2];
				afrinicList.add(new ALLIpRecord(ipLong, count, countryCode));
			}
			afrinicList.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE_ALL);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE_ALL);
				List<ALLIpRecord> key1List = recordAfrinicMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordAfrinicMap.put(key1, key1List);
				if (key2 > key1) {
					List<ALLIpRecord> key2List = recordAfrinicMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordAfrinicMap.put(key2, key2List);
				}
			});

			recordLacnicMap = new HashMap<>();
			List<ALLIpRecord> lacnicList = new ArrayList<>();
			String lacnicContent = FileUtils.readFileToString(new ClassPathResource(LACNIC_IPS).getFile(), "UTF-8");
			String[] lacniccs = lacnicContent.split("\\|");
			String[] lacnicips = null;
			for (String s : lacniccs) {
				if (s == null) {
					continue;
				}
				lacnicips = s.split(",");
				if (lacnicips.length != 3) {
					continue;
				}
				long ipLong = Long.parseLong(lacnicips[0]);
				int count = Integer.parseInt(lacnicips[1]);
				String countryCode = lacnicips[2];
				lacnicList.add(new ALLIpRecord(ipLong, count, countryCode));
			}
			lacnicList.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE_ALL);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE_ALL);
				List<ALLIpRecord> key1List = recordLacnicMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordLacnicMap.put(key1, key1List);
				if (key2 > key1) {
					List<ALLIpRecord> key2List = recordLacnicMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordLacnicMap.put(key2, key2List);
				}
			});

			recordRipeMap = new HashMap<>();
			List<ALLIpRecord> ripeList = new ArrayList<>();
			String ripeContent = FileUtils.readFileToString(new ClassPathResource(RIPE_IPS).getFile(), "UTF-8");
			String[] ripecs = ripeContent.split("\\|");
			String[] ripeips = null;
			for (String s : ripecs) {
				if (s == null) {
					continue;
				}
				ripeips = s.split(",");
				if (ripeips.length != 3) {
					continue;
				}
				long ipLong = Long.parseLong(ripeips[0]);
				int count = Integer.parseInt(ripeips[1]);
				String countryCode = ripeips[2];
				ripeList.add(new ALLIpRecord(ipLong, count, countryCode));
			}
			ripeList.forEach(r -> {
				int key1 = (int) (r.start / RANGE_SIZE_ALL);
				int key2 = (int) ((r.start + r.count) / RANGE_SIZE_ALL);
				List<ALLIpRecord> key1List = recordRipeMap.getOrDefault(key1, new ArrayList<>());
				key1List.add(r);
				recordRipeMap.put(key1, key1List);
				if (key2 > key1) {
					List<ALLIpRecord> key2List = recordRipeMap.getOrDefault(key2, new ArrayList<>());
					key2List.add(r);
					recordRipeMap.put(key2, key2List);
				}
			});

		} catch (Exception e) {
			logger.error("初始化国家ip范围发生错误", e);
		}
	}

	/**
	 * 返回国家代码
	 * 
	 * @param ip
	 * @return
	 * @return
	 */
	public static String getCountryCode(String ip) {
		String countryCodeStr = "";
		if (ip == null || ip.trim().isEmpty()) {
			return "";
		}
		if (StringUtils.equals("127.0.0.1", ip)) {
			return "";
		}
		if (isValidIpV4Address(ip)) {
			long value = ipToLong(ip);
			int key = (int) (value / RANGE_SIZE_ALL);
			if (recordApnicMap != null && recordApnicMap.containsKey(key)) {
				List<ALLIpRecord> list = recordApnicMap.get(key);
				for (ALLIpRecord record : list) {
					if (record.contains(value)) {
						countryCodeStr = record.countryCode;
						break;
					}

				}
			}
			if (recordArinMap != null && recordArinMap.containsKey(key)) {
				List<ALLIpRecord> list = recordArinMap.get(key);
				for (ALLIpRecord record : list) {
					if (record.contains(value)) {
						countryCodeStr = record.countryCode;
						break;
					}

				}
			}
			if (recordAfrinicMap != null && recordAfrinicMap.containsKey(key)) {
				List<ALLIpRecord> list = recordAfrinicMap.get(key);
				for (ALLIpRecord record : list) {
					if (record.contains(value)) {
						countryCodeStr = record.countryCode;
						break;
					}

				}
			}
			if (recordLacnicMap != null && recordLacnicMap.containsKey(key)) {
				List<ALLIpRecord> list = recordLacnicMap.get(key);
				for (ALLIpRecord record : list) {
					if (record.contains(value)) {
						countryCodeStr = record.countryCode;
						break;
					}

				}
			}
			if (recordRipeMap != null && recordRipeMap.containsKey(key)) {
				List<ALLIpRecord> list = recordRipeMap.get(key);
				for (ALLIpRecord record : list) {
					if (record.contains(value)) {
						countryCodeStr = record.countryCode;
						break;
					}

				}
			}
		}
		return countryCodeStr;
	}

	public static String getCountryName(String countryCode) {
		String countryName = "";
		switch (countryCode) {
		case "CN":
			countryName = "中国大陆";
			break;
		case "HK":
			countryName = "中国香港";
			break;
		case "TW":
			countryName = "中国台湾";
			break;
		case "PH":
			countryName = "菲律宾";
			break;
		case "KH":
			countryName = "柬埔寨";
			break;
		case "MY":
			countryName = "马来西亚";
			break;
		case "TH":
			countryName = "泰国";
			break;
		case "MM":
			countryName = "缅甸";
			break;
		case "LA":
			countryName = "老挝";
			break;
		case "BD":
			countryName = "孟加拉";
			break;
		case "IR":
			countryName = "伊朗";
			break;
		case "MX":
			countryName = "墨西哥";
			break;
		case "FR":
			countryName = "法国";
			break;
		case "BY":
			countryName = "白俄罗斯";
			break;
		case "SG":
			countryName = "新加坡";
			break;
		case "VN":
			countryName = "越南";
			break;
		case "IE":
			countryName = "爱尔兰";
			break;
		case "KR":
			countryName = "韩国";
			break;
		case "DE":
			countryName = "德国";
			break;
		case "GN":
			countryName = "几内亚";
			break;
		case "LB":
			countryName = "黎巴嫩";
			break;
		case "UA":
			countryName = "乌克兰";
			break;
		case "LV":
			countryName = "拉脱维亚";
			break;
		case "RU":
			countryName = "俄罗斯";
			break;
		case "US":
			countryName = "美国";
			break;
		case "SO":
			countryName = "索马里";
			break;
		case "BR":
			countryName = "巴西";
			break;
		case "ID":
			countryName = "印尼";
			break;
		case "JP":
			countryName = "日本";
			break;
		default:
			countryName = "";
			break;
		}
		return countryName;
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

	public static void main(String[] args) throws IOException {
		System.out.println(getCountryCode("52.128.240.34"));
		System.out.println(getCountryCode("144.52.196.94"));
		System.out.println(getCountryCode("112.67.217.17"));
		System.out.println(getCountryCode("14.27.39.155"));
		System.out.println(getCountryCode("113.66.54.79"));
		System.out.println(getCountryCode("117.61.245.234"));
		System.out.println(getCountryCode("116.10.140.160"));
		System.out.println(getCountryCode("14.24.164.83"));
		System.out.println(getCountryCode("223.104.66.167"));
		System.out.println(getCountryCode("182.104.53.183"));
		System.out.println(getCountryCode("117.136.12.18"));
		System.out.println(getCountryCode("223.73.21.60"));
		System.out.println(getCountryCode("125.95.74.145"));
		System.out.println(getCountryCode("117.136.12.18"));
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
