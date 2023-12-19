package com.play.pay.baxiowenpay.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class OwenRSAEncrypt {
    /**
     * data 加密串
       charsetname 编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     */
    public static String encrypt(String data, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(data);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString.toUpperCase();
    }
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String Algorithm = "DESede";
    /**
     *加密模式及填充方式
     */
    public static String Encrypt3DES(String value,String key)

    {
        try {
            String str = byte2Base64(encryptMode(key.getBytes(), value.getBytes("UTF-8")));
            return str;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    public static String byte2Base64(byte[] b)
    {
        return Base64.getEncoder().encodeToString(b);
    }

    // keybyte为加密密钥，长度为24字节
    // src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 数字字符串去除小数点后末尾多余的0
     * 如果字符串为空返回0，非数字则返回原字符串
     * @param str
     * @return
     */
    public  static String numberRemoveZero(String str){
        String str2 = "";
        if(str==null||"".equals(str)){
            str="0";
        }else{
            try{
                BigDecimal b = new BigDecimal(str);
                //java1.7BigDecimal的stripTrailingZeros处理0.00没生效
                if(b.compareTo(BigDecimal.ZERO)==0){
                    str2 = "0";
                }else{
                    str2 = b.stripTrailingZeros().toPlainString();
                }
            }catch(Exception e){
                e.printStackTrace();
                str2 =str;
            }
        }
        return str2;
    }


}
