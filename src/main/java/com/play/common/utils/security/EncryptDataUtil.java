package com.play.common.utils.security;

import org.apache.commons.lang3.StringUtils;

import com.play.common.SystemConfig;

public class EncryptDataUtil {
	private static final String encrypt_data_prefix = "jm_";

	/**
	 * 解密数据
	 * 
	 * @param content
	 * @return
	 */
	public static String decryptData(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (StringUtils.startsWith(content, encrypt_data_prefix)) {
			if (StringUtils.isEmpty(SystemConfig.ENCRYPT_KEY_DATA)) {
				return content;
			}
			return AES.decrypt(content.substring(encrypt_data_prefix.length()), SystemConfig.ENCRYPT_KEY_DATA);
		}
		return content;
	}

	/**
	 * 加密数据
	 * 
	 * @param content
	 * @return
	 */
	public static String encryptData(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (StringUtils.startsWith(content, encrypt_data_prefix)) {
			return content;
		}
		if (StringUtils.isEmpty(SystemConfig.ENCRYPT_KEY_DATA)) {
			return content;
		}
		return encrypt_data_prefix + AES.encrypt(content, SystemConfig.ENCRYPT_KEY_DATA);
	}

	/**
	 * 支付/代付用的 解密数据
	 *
	 * @param content
	 * @return
	 */
	public static String decryptDataForPay(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (StringUtils.startsWith(content, encrypt_data_prefix)) {
			if (StringUtils.isEmpty(SystemConfig.ENCRYPT_KEY_PAY)) {
				return content;
			}
			return AES.decrypt(content.substring(encrypt_data_prefix.length()), SystemConfig.ENCRYPT_KEY_PAY);
		}
		return content;
	}

	public static String decryptDataForApi(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (StringUtils.startsWith(content, encrypt_data_prefix)) {
			if (StringUtils.isEmpty(SystemConfig.ENCRYPT_KEY_PAY)) {
				return content;
			}
			return AES.decrypt(content.substring(encrypt_data_prefix.length()), SystemConfig.ENCRYPT_KEY_PAY);
		}
		return content;
	}

	/**
	 * 支付/代付用的 加密数据
	 *
	 * @param content
	 * @return
	 */
	public static String encryptDataForPay(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		if (StringUtils.startsWith(content, encrypt_data_prefix)) {
			return content;
		}
		if (StringUtils.isEmpty(SystemConfig.ENCRYPT_KEY_PAY)) {
			return content;
		}
		return encrypt_data_prefix + AES.encrypt(content, SystemConfig.ENCRYPT_KEY_PAY);
	}

	public static String getAutoLoginKey(String username, Long stationId) {
		StringBuilder sb = new StringBuilder("DQWfeqf)@!(Y&*T*GJhoguy(*");
		sb.append(":").append(username);
		sb.append(":").append(stationId);
		sb.append(":").append("KJI8(QW)pfefonew127490-3e[-00UIfJOP)");
		return MD5Util.md5(sb.toString());
	}
}
