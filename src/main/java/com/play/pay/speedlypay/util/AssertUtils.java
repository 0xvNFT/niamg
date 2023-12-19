package com.play.pay.speedlypay.util;

/**
 * @author zzn
 */
public class AssertUtils {

	public static void assertNotNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertTrue(Boolean b, String message) {
		if (b != null && b) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertFalse(Boolean b, String message) {
		assertTrue(!b, message);
	}
}
