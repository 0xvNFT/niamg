package com.play.pay.goopago.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zzn
 */
public class SecretUtils {

    private final static Logger log = LoggerFactory.getLogger(SecretUtils.class);


    public static boolean verify(Object data, String key) {
        return verify(JSONObject.parseObject(JSONObject.toJSONString(data)), key);
    }

    public static boolean verify(Map<String, Object> data, String key) {
        String sign = data.get("sign").toString();
        data.remove("sign");
        String signValue = sign(data, key);
        return Objects.equals(sign, signValue);
    }

    public static String sign(Object data, String key) {
        return sign(JSONObject.parseObject(JSONObject.toJSONString(data)), key);
    }

    public static String sign(Map<String, Object> data, String key) {
        data.remove("sign");
        String signedValue = getSignedValue(data);
        signedValue += "key=" + key;
        log.info("signedValue:{}", signedValue);
        return md5(signedValue, "UTF-8").toUpperCase();
    }

    private static String getSignedValue(Map<String, Object> reqMap) {
        Map<String, String> copy = new TreeMap<>();

        reqMap.forEach((k, v) -> {
            if (v != null && !"".equals(v)) {
                copy.put(k, v.toString());
            }
        });

        StringBuilder sb = new StringBuilder();

        copy.forEach((k, v) -> {
            if (v != null) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        return sb.toString();
    }

    public static String concatUrlParams(String url, Map<String, Object> map, Map<String, Object> extendMap) {
        return url + "?" + encodeOnce(map) + "&" + encodeOnce(extendMap);
    }


    public static String encodeOnce(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();

        map.forEach((k, v) -> {
            String value = (v == null) ? "" : v.toString();
            try {
                sb.append(k).append("=")
                        .append(URLEncoder.encode(value, "UTF-8")).append("&");
            } catch (UnsupportedEncodingException ignored) {
            }
        });
        return sb.toString().substring(0, sb.length() - 1);
    }


    public static String md5(String value, String charset) {
        MessageDigest md;
        try {
            byte[] data = value.getBytes(charset);
            md = MessageDigest.getInstance("MD5");
            byte[] digestData = md.digest(data);
            return toHex(digestData);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toHex(byte[] input) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }

        return output.toString();
    }


}
