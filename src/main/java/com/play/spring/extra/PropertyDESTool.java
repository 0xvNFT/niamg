package com.play.spring.extra;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


public class PropertyDESTool {
	private static Key key;
	private static String KEY_STR = "mykey";

	static {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(KEY_STR.getBytes());
			generator.init(secureRandom);
			key = generator.generateKey();
			generator = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getEncryptString(String str) {
		try {
			byte[] strBytes = str.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(1, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return Base64.getEncoder().encodeToString(encryptStrBytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static String getDecryptString(String str) {
		try {
			byte[] strBytes = Base64.getDecoder().decode(str);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, key);
			byte[] encryptStrBytes = cipher.doFinal(strBytes);
			return new String(encryptStrBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
