package com.play.pay.baxiuzpay.util;
import org.apache.commons.lang3.StringUtils;
import java.security.MessageDigest;



public class SignAPI {
    /**
     * MD5鍔犲瘑
     * @param signSource
     * @return
     */
    private static String calculate(String signSource) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signSource.getBytes("utf-8"));
            byte[] b = md.digest();

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(String signSource, String key) {
        if (StringUtils.isNotBlank(key)) {
            signSource = signSource + "&key=" + key;
        }
        return calculate(signSource);
    }

    public static boolean validateSignByKey(String signSource, String key, String retsign) {
        if (StringUtils.isNotBlank(key)) {
            signSource = signSource + "&key=" + key;
        }
        String signkey = calculate(signSource);
        if (signkey.equals(retsign)) {
            return true;
        }
        return false;
    }
}
