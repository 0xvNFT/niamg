package com.play.common.utils.security;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class Sha256Util {
	private static final String ALGORITHM_SHA256 = "SHA-256";
	private static final String UTF8 = "UTF-8";

	public static String digest(String s) {
		if (StringUtils.isEmpty(s)) {
			return s;
		}
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA256);
			byte[] in = s.getBytes(UTF8);
			md.update(in);
			char[] array = Hex.encodeHex(md.digest());
			return new String(array);
		} catch (Exception e) {
			throw new RuntimeException("digest failed!", e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Sha256Util.digest("123456"));
	}
}
