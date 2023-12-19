package com.play.pay.baxicashpay.util;


import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class CashRSAEncrypt {
    public static final String KEY = "Authorization";
    private static final String BASIC_START_WITH = "Basic ";


    public static String build(String appId, String signKey) {
        String v = appId + ":" + signKey;
        return BASIC_START_WITH + new String(Base64.getEncoder().encode(v.getBytes(StandardCharsets.UTF_8)));
    }


    public static String sign(String signKey, JSONObject payParams) {
        //1.参数名自然排序升序的顺序排序
        List<String> collect = payParams.keySet().stream().sorted().collect(Collectors.toList());

        //2.拼接成字符串stringA
        StringBuilder kvs = new StringBuilder();
        for (String key : collect) {
            String value = payParams.getString(key);
            if (value == null || "".equals(value) || "sign".equals(key)) {
                continue;
            }
            kvs.append(key).append("=").append(value).append("&");
        }
        String stringA = kvs.toString();
        //拼接 MD5加密的signKey
        String stringSign = stringA + "key=" + toMD5(signKey);;

        //3. MD5加密stringSign
        return toMD5(stringSign);
    }

    public static String toMD5(String plainText) {
        String md5cod = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(plainText.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < digest.length; i++) {
                md5cod += Integer.toHexString((0x000000FF & digest[i]) | 0xFFFFFF00).substring(6);
            }
        } catch (Exception e) {
            throw new RuntimeException("toMD5 error.");
        }
        return md5cod;
    }


}
