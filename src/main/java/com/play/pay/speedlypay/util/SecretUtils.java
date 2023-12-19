package com.play.pay.speedlypay.util;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author zzn
 */
public class SecretUtils {

    private final static Logger log = LoggerFactory.getLogger(SecretUtils.class);


    public static boolean verify(Object data, String key) {
        return verify(JSONObject.parseObject(JSONObject.toJSONString(data)), key);
    }

    public static boolean verify(Map<String, Object> data, String key) {
        String sign = data.get("signature").toString();
        data.remove("signature");
        String signValue = sign(data, key);
        return Objects.equals(sign, signValue);
    }

    public static String sign(Object data, String key) {
        return sign(JSONObject.parseObject(JSONObject.toJSONString(data)), key);
    }

    public static String sign(Map<String, Object> data, String key) {
        // 移除无需签名的参数
        data.remove("payer");
        data.remove("payee");
        Map<String, Object> param = getNotEmptyParam(data);
        String signValue = signValue(param);
        signValue += "key=" + key;
        System.out.println("signValue === " + signValue);
        return md5(signValue, "UTF-8").toUpperCase();
    }

    public static Map<String, Object> getNotEmptyParam(Map<String, Object> reqMap) {
        Map<String, Object> copy = new TreeMap<>();
        reqMap.forEach((k, v) -> {
            if (v != null && !"".equals(v)) {
                copy.put(k, v.toString());
            }
        });
        return copy;
    }

    private static String signValue(Map<String, Object> reqMap) {
        StringBuilder sb = new StringBuilder();
        reqMap.forEach((k, v) -> {
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
