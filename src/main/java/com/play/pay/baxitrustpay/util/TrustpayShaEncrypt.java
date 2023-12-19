package com.play.pay.baxitrustpay.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class TrustpayShaEncrypt {

	public static String genSign(Map<String, String> params, String key) {
		try {
			String data = params.keySet().stream().sorted().filter(k -> StringUtils.isNotBlank(params.get(k))).map(k -> k + "=" + params.get(k)).collect(Collectors.joining("&"));
		//	System.out.println(data);
			// 还原秘钥
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			// 实例化MAC
			Mac mac = Mac.getInstance("HmacSHA1");
			// 初始化MAC
			mac.init(signingKey);
			// 获得消息概要
			byte[] rawHmac = mac.doFinal(data.getBytes());
			// BASE64编码
			return java.util.Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String genSignObject(Map<String, Object> paramsObject, String key) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.putAll(paramsObject);
			params.remove("sign");
			String data = params.keySet().stream().sorted().filter(k -> StringUtils.isNotBlank(params.get(k)+"")).map(k -> k + "=" + params.get(k)).collect(Collectors.joining("&"));
		//	System.out.println(data);
			// 还原秘钥
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			// 实例化MAC
			Mac mac = Mac.getInstance("HmacSHA1");
			// 初始化MAC
			mac.init(signingKey);
			// 获得消息概要
			byte[] rawHmac = mac.doFinal(data.getBytes());
			// BASE64编码
			return java.util.Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		String Key = "U0X214AWH77PLB5SO9IG6FBM2MENZ0YMP83DNCQ9RW8GK4T0FCCVS4GWDMMQWERT";
		String MerchantId = "98b57889799740d18a9875226b8d1d26";
		Map<String, String> params = new HashMap<>();
		params.put("amount", "1");
		params.put("orderNo", "1284205570012600");
		params.put("merchantId", MerchantId);
		params.put("phone", "916371231700");
		params.put("name", "srabankumar sabar");
		params.put("notifyUrl", "https://api.yourdomain.com/test");
		params.put("email", "dharmvirsingh.agmail.com@");
		System.out.println(genSign(params, Key));
	}
}
