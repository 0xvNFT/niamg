
package com.play.common.ip;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * https://www.zxinc.org/update.php?app=ipv6db&type=json查询ipv6数据库是否有更新(目前版本：20200506)
 * http://ip.zxinc.org/ipv6wry.db ipv6数据库下载地址
 */
public class IPV6Utils {
	private static Logger logger = LoggerFactory.getLogger(IPV6Utils.class);

	// 单一模式实例
	private static IPV6Utils instance = new IPV6Utils();

	// private RandomAccessFile randomAccessFile = null;
	private byte[] v6Data;
	// 偏移地址长度
	private int offsetLen;
	// 索引区第一条记录的偏移
	private long firstIndex;
	// 总记录数
	private long indexCount;

	// 随机文件访问类
	// private RandomAccessFile ipV6File;

	// 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
	// private Hashtable ipV6Cache;

	// 为提高效率而采用的临时变量
	// private IPV6Location loc;

	public long getIndexCount() {
		return indexCount;
	}

	private IPV6Utils() {
		InputStream in = null;
		try {
			in = IPV6Utils.class.getClassLoader().getResourceAsStream("/../../ipv6wry.db");
			ByteArrayOutputStream out = new ByteArrayOutputStream(10 * 1024 * 1024); // 10MB
			byte[] buffer = new byte[4096];
			while (true) {
				int r = in.read(buffer);
				if (r == -1) {
					break;
				}
				out.write(buffer, 0, r);
			}
			this.v6Data = out.toByteArray();
			// 获取偏移地址长度
			byte[] bytes = readBytes(6, 1);
			this.offsetLen = bytes[0];
			// 索引区第一条记录的偏移
			bytes = readBytes(16, 8);
			BigInteger firstIndexBig = byteArrayToBigInteger(bytes);
			this.firstIndex = firstIndexBig.longValue();
			// 总记录数
			bytes = readBytes(8, 8);
			BigInteger indexCountBig = byteArrayToBigInteger(bytes);
			this.indexCount = indexCountBig.longValue();
		} catch (Exception e) {
			logger.error("IPV6地址信息文件没有找到，IPV6显示功能将无法使用");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @return 单一实例
	 */
	public static IPV6Utils getInstance() {
		return instance;
	}

	private byte[] readBytes(long offset, int num) {
		byte[] ret = new byte[num];
		for (int i = 0; i < num; i++) {
			ret[i] = this.v6Data[(int) (offset + i)];
		}
		return ret;
	}

	/**
	 * 对little-endian字节序进行了转换 byte[]转换为long
	 * 
	 * @param b
	 * @return ret
	 */
	private BigInteger byteArrayToBigInteger(byte[] b) {
		BigInteger ret = new BigInteger("0");
		// 循环读取每个字节通过移位运算完成long的8个字节拼装
		for (int i = 0; i < b.length; i++) {
			// value |=((long)0xff &lt;&lt; shift) &amp; ((long)b[i] &lt;&lt; shift);
			int shift = i << 3;
			BigInteger shiftY = new BigInteger("ff", 16);
			BigInteger data = new BigInteger(b[i] + "");
			ret = ret.or(shiftY.shiftLeft(shift).and(data.shiftLeft(shift)));
		}
		return ret;
	}

	/**
	 * 二分查找
	 * 
	 * @param ip
	 * @param l
	 * @param r
	 * @return
	 */
	public long find(BigInteger ip, long l, long r) {
		if (r - l <= 1)
			return l;
		long m = (l + r) >>> 1;
		long o = this.firstIndex + m * (8 + this.offsetLen);
		byte[] bytes = readBytes(o, 8);
		BigInteger new_ip = byteArrayToBigInteger(bytes);
		if (ip.compareTo(new_ip) == -1) {
			return find(ip, l, m);
		} else {
			return find(ip, m, r);
		}
	}

	public static long ipv4ToNum(String ipStr) {
		long result = 0;
		String[] split = ipStr.split("\\.");
		if (split.length != 4) {
			// System.out.println("error ip address");
		}
		for (int i = 0; i < split.length; i++) {
			int s = Integer.valueOf(split[i]);
			if (s > 255) {
				// System.out.println("error ip address");
			}
			result = (result << 8) | s;
		}
		return result;
	}

	public BigInteger ipv6ToNum(String ipStr) {
		// String ipStr = "2400:3200::1";
		// 最多被冒号分隔为8段
		int ipCount = 8;
		List<String> parts = new ArrayList<>(Arrays.asList(ipStr.split(":")));
		// 最少也至少为三段，如：`::1`
		if (parts.size() < 3) {
			// System.out.println("error ip address");
		}
		String last = parts.get(parts.size() - 1);
		if (last.contains(".")) {
			long l = ipv4ToNum(last);
			parts.remove(parts.size() - 1);
			parts.add(new BigInteger(((l >> 16) & 0xFFFF) + "").toString(16));
			parts.add(new BigInteger((l & 0xFFFF) + "").toString(16));
		}
		int emptyIndex = -1;
		for (int i = 0; i < parts.size(); i++) {
			if (StringUtils.isEmpty(parts.get(i))) {
				emptyIndex = i;
			}
		}
		int parts_hi, parts_lo, parts_skipped;
		if (emptyIndex > -1) {
			parts_hi = emptyIndex;
			parts_lo = parts.size() - parts_hi - 1;
			if (StringUtils.isEmpty(parts.get(0))) {
				parts_hi -= 1;
				if (parts_hi > 0) {
					// System.out.println("error ip address");
				}
			}
			if (StringUtils.isEmpty(parts.get(parts.size() - 1))) {
				parts_lo -= 1;
				if (parts_lo > 0) {
					// System.out.println("error ip address");
				}
			}
			parts_skipped = ipCount - parts_hi - parts_lo;
			if (parts_skipped < 1) {
				// System.out.println("error ip address");
			}
		} else {
			// 完全地址
			if (parts.size() != ipCount) {
				// System.out.println("error ip address");
			}
			parts_hi = parts.size();
			parts_lo = 0;
			parts_skipped = 0;
		}
		BigInteger ipNum = new BigInteger("0");
		if (parts_hi > 0) {
			for (int i = 0; i < parts_hi; i++) {
				ipNum = ipNum.shiftLeft(16);
				String part = parts.get(i);
				if (part.length() > 4) {
					// System.out.println("error ip address");
				}
				BigInteger bigInteger = new BigInteger(part, 16);
				int i1 = bigInteger.intValue();
				if (i1 > 0xFFFF) {
					// System.out.println("error ip address");
				}
				ipNum = ipNum.or(bigInteger);
			}
		}
		ipNum = ipNum.shiftLeft(16 * parts_skipped);
		for (int i = -parts_lo; i < 0; i++) {
			// ipNum &lt;&lt;= 16;
			ipNum = ipNum.shiftLeft(16);
			String part = parts.get(parts.size() + i);
			if (part.length() > 4) {
				// System.out.println("error ip address");
			}
			BigInteger bigInteger = new BigInteger(part, 16);
			int i1 = bigInteger.intValue();
			if (i1 > 0xFFFF) {
				// System.out.println("error ip address");
			}
			// ipNum |= i1;
			ipNum = ipNum.or(bigInteger);
		}

		return ipNum;
	}

	public long getIpOff(long findIp) {
		return this.firstIndex + findIp * (8 + this.offsetLen);
	}

	public long getIpRecOff(long ip_off) {
		byte[] bytes = readBytes(ip_off + 8, this.offsetLen);
		BigInteger ip_rec_off_big = byteArrayToBigInteger(bytes);
		return ip_rec_off_big.longValue();
	}

	public String getAddr(long offset) {
		byte[] bytes = readBytes(offset, 1);
		int num = bytes[0];
		if (num == 1) {
			// 重定向模式1
			// [IP][0x01][国家和地区信息的绝对偏移地址]
			// 使用接下来的3字节作为偏移量调用字节取得信息
			bytes = readBytes(offset + 1, this.offsetLen);
			BigInteger l = byteArrayToBigInteger(bytes);
			return getAddr(l.longValue());
		} else {
			// 重定向模式2 + 正常模式
			// [IP][0x02][信息的绝对偏移][...]
			return  getAreaAddr(offset);
//			if (num == 2) {
//				offset += 1 + this.offsetLen;
//			} else {
//				offset = findEnd(offset) + 1;
//			}
//			// String aArea = getAreaAddr(offset);
//			this.loc.country = cArea;
//			// this.loc.area = aArea;
//			return this.loc;
		}
	}

	private String getAreaAddr(long offset) {
		// 通过给出偏移值，取得区域信息字符串
		byte[] bytes = readBytes(offset, 1);
		int num = bytes[0];
		if (num == 1 || num == 2) {
			bytes = readBytes(offset + 1, this.offsetLen);
			BigInteger p = byteArrayToBigInteger(bytes);
			return getAreaAddr(p.longValue());
		} else {
			return getString(offset);
		}
	}

	private String getString(long offset) {
		long o2 = findEnd(offset);
		// 有可能只有国家信息没有地区信息，
		byte[] bytes = readBytes(offset, Long.valueOf(o2 - offset).intValue());
		try {
			return new String(bytes, "utf8");
		} catch (UnsupportedEncodingException e) {
			return "未知数据";
		}
	}

	private long findEnd(long offset) {
		int i = Long.valueOf(offset).intValue();
		for (; i < this.v6Data.length; i++) {
			byte[] bytes = readBytes(i, 1);
			if ("\0".equals(new String(bytes))) {
				break;
			}
		}
		return i;
	}

	public String getCountry(String ipv6) {
		if (this.v6Data == null)
			return "错误的IP数据库文件";
		BigInteger ipNum = ipv6ToNum(ipv6);
		BigInteger ip = ipNum.shiftRight(64).and(new BigInteger("FFFFFFFFFFFFFFFF", 16));
		// 查找ip的索引偏移
		long findIp = find(ip, 0, getIndexCount());
		// 得到索引记录
		long ip_off = getIpOff(findIp);
		long ip_rec_off = getIpRecOff(ip_off);
		String loc = this.getAddr(ip_rec_off);
		if (loc == null) {
			return "未知国家";
		}
		return loc ;
	}

	public static boolean isValidIpv6Addr(String ipAddr) {

		String regex = "(^((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4}){1}|:))"
				+ "|(([0-9A-Fa-f]{1,4}:){6}((:[0-9A-Fa-f]{1,4}){1}|" + "((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
				+ "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|" + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){5}((:[0-9A-Fa-f]{1,4}){1,2}|" + ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
				+ "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|" + "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
				+ "(([0-9A-Fa-f]{1,4}:){4}((:[0-9A-Fa-f]{1,4}){1,3}" + "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|"
				+ "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|"
				+ "([0-9]){1,2})){3})|:))|(([0-9A-Fa-f]{1,4}:){3}((:[0-9A-Fa-f]{1,4}){1,4}|"
				+ ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|" + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
				+ "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|" + "(([0-9A-Fa-f]{1,4}:){2}((:[0-9A-Fa-f]{1,4}){1,5}|"
				+ ":((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|" + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
				+ "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))" + "|(([0-9A-Fa-f]{1,4}:){1}((:[0-9A-Fa-f]{1,4}){1,6}"
				+ "|:((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|" + "([0-9]){1,2})([.](25[0-5]|2[0-4][0-9]|"
				+ "[0-1][0-9][0-9]|([0-9]){1,2})){3})|:))|"
				+ "(:((:[0-9A-Fa-f]{1,4}){1,7}|(:[fF]{4}){0,1}:((22[0-3]|2[0-1][0-9]|" + "[0-1][0-9][0-9]|([0-9]){1,2})"
				+ "([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})){3})|:)))$)";

		if (StringUtils.isEmpty(ipAddr)) {
			return false;
		}
		ipAddr = Normalizer.normalize(ipAddr, Form.NFKC);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ipAddr);
		return matcher.matches();
	}

	public static void main(String[] args) {
		// IPV6Utils ipv6Service = IPV6Utils.getInstance();
		// String addr = ipv6Service.getCountry("2409:880a:d63:3b97:48:b034:100:0");
		String ipv61 = "2001:db8:a583:64:c68c:d6df:600c:ee9a";
		String ipv62 = "2001:db8:a583::9e42:be55:53a7";
		String ipv63 = "2001:db8:a583:::9e42:be55:53a7";
		String ipv64 = "1:2:3:4:5::";
		String ipv65 = "CDCD:910A:2222:5498:8475:1111:3900:2020";
		String ipv66 = "1030::C9B4:FF12:48AA:1A2B";
		String ipv67 = "2000:0:0:0:0:0:0:1";
		String ipv68 = "::0:0:0:0:0:0:1";
		String ipv69 = "2000:0:0:0:0::";

		// System.out.println(isValidIpv6Addr("2409:880a:d63:3b97:48:b034:100:0"));192.168.254.106
		System.out.println(isValidIpv6Addr("192.168.254.106"));
		System.out.println(isValidIpv6Addr(ipv61));
		System.out.println(isValidIpv6Addr(ipv62));
		System.out.println(isValidIpv6Addr(ipv63));
		System.out.println(isValidIpv6Addr(ipv64));
		System.out.println(isValidIpv6Addr(ipv65));
		System.out.println(isValidIpv6Addr(ipv66));
		System.out.println(isValidIpv6Addr(ipv67));
		System.out.println(isValidIpv6Addr(ipv68));
		System.out.println(isValidIpv6Addr(ipv69));
	}

}