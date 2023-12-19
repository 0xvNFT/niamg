package com.play.pay.baxisunpay.util;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class SunpayRSAEncrypt {
    public static String HexHMac(long timestamp,String nonce,String body,String secretKey)throws Exception {
        String mianBody = timestamp+nonce+body;
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        hmacSha256.init(new SecretKeySpec(secretKey.getBytes("utf-8"), 0, secretKey.getBytes("utf-8").length, "HmacSHA256"));
        byte[] hmacSha256Bytes = hmacSha256.doFinal(mianBody.getBytes("utf-8"));
        return Hex.encodeHexString(hmacSha256Bytes).toUpperCase();
    }
}
