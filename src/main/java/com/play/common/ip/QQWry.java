package com.play.common.ip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.play.common.ip.country.CNIpTool;

/**
 * Thread safe. A QQWry instance can share amount threads.
 *
 * <pre>
 * Usage: {
 * 	&#64;code
 * 	QQWry qqwry = new QQWry(); // load qqwry.dat from classpath
 *
 * 	QQWry qqwry = new QQWry(Paths.get("path/to/qqwry.dat")); // load qqwry.dat from java.nio.file.Path
 *
 * 	byte[] data = Files.readAllBytes(Paths.get("path/to/qqwry.dat"));
 * 	QQWry qqwry = new QQWry(data); // create QQWry with provided data
 * 
 * 	String dbVer = qqwry.getDatabaseVersion();
 * 	System.out.printf("qqwry.dat version=%s", dbVer);
 * 	// qqwry.dat version=2020.9.10
 *
 * 	String myIP = "127.0.0.1";
 * 	IPZone ipzone = qqwry.findIP(myIP);
 * 	System.out.printf("%s, %s", ipzone.getMainInfo(), ipzone.getSubInfo());
 * 	// IANA, 保留地址用于本地回送
 * }
 * </pre>
 *
 * @author Jarod Liu &lt;liuyuanzhi@gmail.com&gt;
 */
public class QQWry {
	private static class QIndex {
		public long minIP;
		public long maxIP;
		public int recordOffset;

		public QIndex(long minIP, long maxIP, int recordOffset) {
			this.minIP = minIP;
			this.maxIP = maxIP;
			this.recordOffset = recordOffset;
		}
	}

	private static class QString {
		public String string;
		/** length including the \0 end byte */
		public int length;

		public QString(String string, int length) {
			this.string = string;
			this.length = length;
		}
	}

	private static int INDEX_RECORD_LENGTH = 7;
	private static byte REDIRECT_MODE_1 = 0x01;
	private static byte REDIRECT_MODE_2 = 0x02;
	private static byte STRING_END = '\0';

	private byte[] data;
	private long indexHead;
	private long indexTail;
	// private String databaseVersion;

	/**
	 * Create QQWry by loading qqwry.dat from classpath.
	 *
	 * @throws IOException if encounter error while reading qqwry.dat
	 */
	public QQWry() throws IOException {
		InputStream in = null;
		try {
			in = QQWry.class.getClassLoader().getResourceAsStream("/../../QQWry.dat");
			ByteArrayOutputStream out = new ByteArrayOutputStream(10 * 1024 * 1024); // 10MB
			byte[] buffer = new byte[4096];
			while (true) {
				int r = in.read(buffer);
				if (r == -1) {
					break;
				}
				out.write(buffer, 0, r);
			}
			data = out.toByteArray();
			indexHead = readLong32(0);
			indexTail = readLong32(4);
			// databaseVersion = parseDatabaseVersion();
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}

	/**
	 * Create QQWry with provided qqwry.dat data.
	 *
	 * @param data fully read data from a qqwry.dat file.
	 */
	// public QQWry(byte[] data) {
	// this.data = data;
	// indexHead = readLong32(0);
	// indexTail = readLong32(4);
	// // databaseVersion = parseDatabaseVersion();
	// }

	/**
	 * Create QQWry from a path to qqwry.dat file.
	 *
	 * @param file path to qqwry.dat
	 * @throws IOException if encounter error while reading from the given file.
	 */
	// public QQWry(Path file) throws IOException {
	// this(Files.readAllBytes(file));
	// }

	public IPZone findIP(String ip) {
		if (!CNIpTool.isValidIpV4Address(ip)) {
			return new IPZone(ip);
		}
		long ipNum = toNumericIP(ip);
		QIndex idx = searchIndex(ipNum);
		if (idx == null) {
			return new IPZone(ip);
		}
		return readIP(ip, idx);
	}

	private long getMiddleOffset(long begin, long end) {
		long records = (end - begin) / INDEX_RECORD_LENGTH;
		records >>= 1;
		if (records == 0) {
			records = 1;
		}
		return begin + (records * INDEX_RECORD_LENGTH);
	}

	private QIndex readIndex(int offset) {
		long min = readLong32(offset);
		int record = readInt24(offset + 4);
		long max = readLong32(record);
		return new QIndex(min, max, record);
	}

	private int readInt24(int offset) {
		int v = data[offset] & 0xFF;
		v |= ((data[offset + 1] << 8) & 0xFF00);
		v |= ((data[offset + 2] << 16) & 0xFF0000);
		return v;
	}

	private IPZone readIP(String ip, QIndex idx) {
		int pos = idx.recordOffset + 4; // skip ip
		byte mode = data[pos];
		IPZone z = new IPZone(ip);
		if (mode == REDIRECT_MODE_1) {
			int offset = readInt24(pos + 1);
			if (data[offset] == REDIRECT_MODE_2) {
				readMode2(z, offset);
			} else {
				QString mainInfo = readString(offset);
				String subInfo = readSubInfo(offset + mainInfo.length);
				z.setMainInfo(mainInfo.string);
				z.setSubInfo(subInfo);
			}
		} else if (mode == REDIRECT_MODE_2) {
			readMode2(z, pos);
		} else {
			QString mainInfo = readString(pos);
			String subInfo = readSubInfo(pos + mainInfo.length);
			z.setMainInfo(mainInfo.string);
			z.setSubInfo(subInfo);
		}
		return z;
	}

	private long readLong32(int offset) {
		long v = data[offset] & 0xFFL;
		v |= (data[offset + 1] << 8L) & 0xFF00L;
		v |= ((data[offset + 2] << 16L) & 0xFF0000L);
		v |= ((data[offset + 3] << 24L) & 0xFF000000L);
		return v;
	}

	private void readMode2(IPZone z, int offset) {
		int mainInfoOffset = readInt24(offset + 1);
		String main = readString(mainInfoOffset).string;
		String sub = readSubInfo(offset + 4);
		z.setMainInfo(main);
		z.setSubInfo(sub);
	}

	private QString readString(int offset) {
		int i = 0;
		byte[] buf = new byte[128];
		for (;; i++) {
			byte b = data[offset + i];
			if (STRING_END == b) {
				break;
			}
			buf[i] = b;
		}
		try {
			return new QString(new String(buf, 0, i, "GB18030"), i + 1);
		} catch (UnsupportedEncodingException e) {
			return new QString("", 0);
		}
	}

	private String readSubInfo(int offset) {
		byte b = data[offset];
		if ((b == REDIRECT_MODE_1) || (b == REDIRECT_MODE_2)) {
			int areaOffset = readInt24(offset + 1);
			if (areaOffset == 0) {
				return "";
			} else {
				return readString(areaOffset).string;
			}
		} else {
			return readString(offset).string;
		}
	}

	private QIndex searchIndex(long ip) {
		long head = indexHead;
		long tail = indexTail;
		while (tail > head) {
			long cur = getMiddleOffset(head, tail);
			QIndex idx = readIndex((int) cur);
			if ((ip >= idx.minIP) && (ip <= idx.maxIP)) {
				return idx;
			}
			if ((cur == head) || (cur == tail)) {
				return idx;
			}
			if (ip < idx.minIP) {
				tail = cur;
			} else if (ip > idx.maxIP) {
				head = cur;
			} else {
				return idx;
			}
		}
		return null;
	}

	private long toNumericIP(String s) {
		String[] parts = s.split("\\.");
		if (parts.length != 4) {
			throw new IllegalArgumentException("ip=" + s);
		}
		long n = Long.parseLong(parts[0]) << 24L;
		n += Long.parseLong(parts[1]) << 16L;
		n += Long.parseLong(parts[2]) << 8L;
		n += Long.parseLong(parts[3]);
		return n;
	}

	/**
	 * qqwry database version in pattern x.x.x
	 */
	// public String getDatabaseVersion() {
	// return databaseVersion;
	// }

	// String parseDatabaseVersion() {
	// Pattern dbVerPattern = Pattern.compile("(\\d+)年(\\d+)月(\\d+)日.*");
	// IPZone ipz = findIP("255.255.255.255");
	// Matcher m = dbVerPattern.matcher(ipz.getSubInfo());
	// if (!m.matches() || m.groupCount() != 3) {
	// return "0.0.0";
	// }
	// return String.format("%s.%s.%s", m.group(1), m.group(2), m.group(3));
	// }
}
