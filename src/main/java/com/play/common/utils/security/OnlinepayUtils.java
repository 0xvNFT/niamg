package com.play.common.utils.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.play.web.utils.ServletUtils;

public class OnlinepayUtils {

	/**
	 * 获取支付接口的同步返回地址 可以保持登录状态
	 *
	 * @return
	 */
	public static String getReturnUrl() {
		return ServletUtils.getDomain();
	}

	/**
	 * 返回地址
	 *
	 * @deprecated
	 */
	public static String RETURN_URL = "@{merchant_domain}";

	/**
	 * 获取支付接口的同步返回地址
	 *
	 * @return
	 * @deprecated
	 */
	public static String getReturnUrl(String merchantDomain) {
		if (merchantDomain.startsWith("http://") || merchantDomain.startsWith("https://")) {
			String returnUrl = RETURN_URL.replace("@{merchant_domain}", merchantDomain);
			return returnUrl;
		} else {
			String returnUrl = "http://" + RETURN_URL.replace("@{merchant_domain}", merchantDomain);
			return returnUrl;
		}
	}

	/**
	 * 获取异步通知地址
	 *
	 * @param notifyUrl
	 * @param merchantDomain
	 * @return
	 */
	public static String getNotifyUrl(String notifyUrl, String merchantDomain) {
		if (merchantDomain.startsWith("http://") || merchantDomain.startsWith("https://")) {
			return notifyUrl.replace("@{merchant_domain}", merchantDomain);
		} else {
			return "http://" + notifyUrl.replace("@{merchant_domain}", merchantDomain);
		}
	}

	/**
	 * 功能：融宝支付MD5加密处理核心文件，不需要修改 版本：3.1 修改日期：2010-11-01 说明：
	 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
	 * 该代码仅供学习和研究融宝支付接口使用，只是提供一个
	 */
	public static class Md5Encrypt {
		/**
		 * Used building output as Hex
		 */
		private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };

		/**
		 * 对字符串进行MD5加密
		 *
		 * @param text 明文
		 * @return 密文
		 */
		public static String md5(String text, String charset) {
			MessageDigest msgDigest = null;

			try {
				msgDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("System doesn't support MD5 algorithm.");
			}

			try {
				msgDigest.update(text.getBytes(charset)); // 注意改接口是按照指定编码形式签名

			} catch (UnsupportedEncodingException e) {

				throw new IllegalStateException("System doesn't support your  EncodingException.");

			}

			byte[] bytes = msgDigest.digest();

			String md5Str = new String(encodeHex(bytes));

			return md5Str;
		}

		/**
		 * 十六进制转换
		 */
		private static char[] encodeHex(byte[] data) {

			int l = data.length;

			char[] out = new char[l << 1];

			// two characters form the hex value.
			for (int i = 0, j = 0; i < l; i++) {
				out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
				out[j++] = DIGITS[0x0F & data[i]];
			}

			return out;
		}

	}

	public static class Base64 {
		public static String getEncode(String str) {
			byte[] byteStr = str.getBytes();
			String strEncode = encode(byteStr);
			return strEncode;
		}

		public static String getDecode(String encodeStr) throws UnsupportedEncodingException {
			if ((encodeStr == null) || (encodeStr.equals(""))) {
				return "";
			}
			byte[] deCode = decode(encodeStr);
			String str = new String(deCode);
			return str;
		}

		public static String encode(byte[] data) {
			StringBuffer sb = new StringBuffer();
			int len = data.length;
			int i = 0;
			while (i < len) {
				int b1 = data[(i++)] & 0xFF;
				if (i == len) {
					sb.append(base64EncodeChars[(b1 >>> 2)]);
					sb.append(base64EncodeChars[((b1 & 0x3) << 4)]);
					sb.append("==");
					break;
				}
				int b2 = data[(i++)] & 0xFF;
				if (i == len) {
					sb.append(base64EncodeChars[(b1 >>> 2)]);
					sb.append(base64EncodeChars[((b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4)]);
					sb.append(base64EncodeChars[((b2 & 0xF) << 2)]);
					sb.append("=");
					break;
				}
				int b3 = data[(i++)] & 0xFF;
				sb.append(base64EncodeChars[(b1 >>> 2)]);
				sb.append(base64EncodeChars[((b1 & 0x3) << 4 | (b2 & 0xF0) >>> 4)]);
				sb.append(base64EncodeChars[((b2 & 0xF) << 2 | (b3 & 0xC0) >>> 6)]);
				sb.append(base64EncodeChars[(b3 & 0x3F)]);
			}
			return sb.toString();
		}

		public static byte[] decode(String str) throws UnsupportedEncodingException {
			StringBuffer sb = new StringBuffer();
			byte[] data = str.getBytes("US-ASCII");
			int len = data.length;
			int i = 0;
			while (i < len) {
				int b1;
				do {
					b1 = base64DecodeChars[data[(i++)]];
				} while ((i < len) && (b1 == -1));
				if (b1 == -1) {
					break;
				}
				int b2;
				do {
					b2 = base64DecodeChars[data[(i++)]];
				} while ((i < len) && (b2 == -1));
				if (b2 == -1) {
					break;
				}
				sb.append((char) (b1 << 2 | (b2 & 0x30) >>> 4));
				int b3;
				do {
					b3 = data[(i++)];
					if (b3 == 61) {
						return sb.toString().getBytes("ISO-8859-1");
					}
					b3 = base64DecodeChars[b3];
				} while ((i < len) && (b3 == -1));
				if (b3 == -1) {
					break;
				}
				sb.append((char) ((b2 & 0xF) << 4 | (b3 & 0x3C) >>> 2));
				int b4;
				do {
					b4 = data[(i++)];
					if (b4 == 61) {
						return sb.toString().getBytes("ISO-8859-1");
					}
					b4 = base64DecodeChars[b4];
				} while ((i < len) && (b4 == -1));
				if (b4 == -1) {
					break;
				}
				sb.append((char) ((b3 & 0x3) << 6 | b4));
			}
			return sb.toString().getBytes("ISO-8859-1");
		}

		private static char[] base64EncodeChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
				'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9', '+', '/' };
		private static byte[] base64DecodeChars = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
				5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1,
				26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
				-1, -1, -1, -1, -1 };
	}

	static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public final static String MD5(String s, String encoding) {
		try {
			byte[] btInput = s.getBytes(encoding);
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String signByMD5(String sourceData, String key, String charset) throws Exception {
		String data = sourceData + key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes(charset));

		return Bytes2HexString(sign).toLowerCase();
	}

	/**
	 * 将byte数组转成十六进制的字符串
	 *
	 * @param b byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}

	public static class RSA {

		/**
		 * RSA 签名算法
		 */
		public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

		/**
		 * RSA 加密算法
		 */
		public static final String KEY_ALGORITHM = "RSA";

		/**
		 * RSA最大加密明文大小
		 */
		private static final int MAX_ENCRYPT_BLOCK = 117;

		/**
		 * RSA最大解密密文大小
		 */
		private static final int MAX_DECRYPT_BLOCK = 128;

		/**
		 * RSA签名
		 *
		 * @param content       待签名数据
		 * @param privateKey    商户私钥
		 * @param input_charset 编码格式
		 * @return 签名值
		 */
		public static String sign(String content, String privateKey, String input_charset) {
			return sign(content, privateKey, input_charset, SIGN_ALGORITHMS);
		}

		/**
		 * RSA签名
		 *
		 * @param content       待签名数据
		 * @param privateKey    商户私钥
		 * @param input_charset 编码格式
		 * @return 签名值
		 */
		public static String sign(String content, String privateKey, String input_charset, String signAlgorithms) {
			try {
				PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
				KeyFactory keyf = KeyFactory.getInstance("RSA");
				PrivateKey priKey = keyf.generatePrivate(priPKCS8);
				Signature signature = Signature.getInstance(signAlgorithms);
				signature.initSign(priKey);
				signature.update(content.getBytes(input_charset));
				byte[] signed = signature.sign();
				return Base64.encode(signed);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * RSA验签名检查
		 *
		 * @param content        待签名数据
		 * @param sign           签名值
		 * @param ali_public_key 付啦公钥
		 * @param input_charset  编码格式
		 * @return 布尔值
		 */
		public static boolean verify(String content, String sign, String ali_public_key, String input_charset) {
			return verify(content, sign, ali_public_key, input_charset, SIGN_ALGORITHMS);
		}

		public static boolean verify(String content, String sign, String ali_public_key, String input_charset,
				String signAlgorithms) {
			try {
				KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
				byte[] encodedKey = Base64.decode(ali_public_key);
				PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
				Signature signature = Signature.getInstance(signAlgorithms);
				signature.initVerify(pubKey);
				signature.update(content.getBytes(input_charset));
				return signature.verify(Base64.decode(sign));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		/**
		 * <p>
		 * 公钥加密
		 * </p>
		 *
		 * @param rawText   源数据
		 * @param publicKey 公钥(BASE64编码)
		 * @return
		 * @throws Exception
		 */
		public static String encrypt(String rawText, String publicKey, String charset) throws Exception {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] data = rawText.getBytes(charset);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
			return Base64.encode(encryptedData);
		}

		/**
		 * 解密
		 *
		 * @param cipherText 密文
		 * @param privateKey 商户私钥(PKCS8格式)
		 * @param charset    编码格式
		 * @return 解密后的字符串
		 */
		public static String decrypt(String cipherText, String privateKey, String charset) throws Exception {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey prikey = keyFactory.generatePrivate(priPKCS8);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, prikey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(cipherText));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[MAX_DECRYPT_BLOCK];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			ins.close();
			return new String(writer.toByteArray(), charset);
		}

	}

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/*
	 * 加密 1.构造密钥生成器 2.根据ecnodeRules规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
	 */
	public static String AESEncode(String encodeRules, String content) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = content.getBytes("utf-8");
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System
			// Library，重新编译后就一切正常了。
			String AES_encode = new String(Base64.encode(byte_AES));
			// 11.将字符串返回
			return AES_encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	/*
	 * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
	 */
	public static String AESDncode(String encodeRules, String content) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byte_content = Base64.decode(content);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, "utf-8");
			return AES_decode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	/**
	 * 传入文本内容，返回 SHA-256 串
	 *
	 * @param strText
	 * @return
	 */
	public static String SHA256(final String strText) {
		return SHA(strText, "SHA-256");
	}

	/**
	 * 传入文本内容，返回 SHA-512 串
	 *
	 * @param strText
	 * @return
	 */
	public static String SHA512(final String strText) {
		return SHA(strText, "SHA-512");
	}

	/**
	 * 字符串 SHA 加密
	 *
	 * @return
	 */
	private static String SHA(final String strText, final String strType) {
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				// SHA 加密开始
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest.getInstance(strType);
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}

	public static class SHAUtils {

		public static final String SIGN_ALGORITHMS = "SHA-1";

		/**
		 * SHA1 安全加密算法
		 */
		public static String sign(String content, String inputCharset) {
			// 获取信息摘要 - 参数字典排序后字符串
			try {
				// 指定sha1算法
				MessageDigest digest = MessageDigest.getInstance(SIGN_ALGORITHMS);
				digest.update(content.getBytes(inputCharset));
				// 获取字节数组
				byte messageDigest[] = digest.digest();
				// Create Hex String
				StringBuffer hexString = new StringBuffer();
				// 字节数组转换为 十六进制 数
				for (int i = 0; i < messageDigest.length; i++) {
					String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
					if (shaHex.length() < 2) {
						hexString.append(0);
					}
					hexString.append(shaHex);
				}
				return hexString.toString().toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private static String encodingCharset = "UTF-8";

	/**
	 * @param aValue
	 * @param aKey
	 * @return
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null) {
			return null;
		}
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16) {
				output.append("0");
			}
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	public static boolean JudgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "ios", "iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java",
				"opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
				"dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola",
				"foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad",
				"webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips",
				"sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			String agent = request.getHeader("User-Agent");
			for (String mobileAgent : mobileAgents) {
				if (agent.toLowerCase().indexOf(mobileAgent) >= 0 && agent.toLowerCase().indexOf("windows nt") <= 0
						&& agent.toLowerCase().indexOf("macintosh") <= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}

//    public static boolean judgeMoblieSystem(HttpServletRequest request, Integer param) {
//        //不传request说明不要求过滤
//        if (request == null) {
//            return true;
//        }
//        String userAgent = request.getHeader("user-agent").toLowerCase();
//        if (userAgent.indexOf("android") != -1 && param == StationDepositOnline.SHOW_TYPE_ANDROID) {
//            //安卓
//            return true;
//        } else if ((userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1 || userAgent.indexOf("ipod") != -1 || userAgent.indexOf("ios") != -1) && param == AdminDepositOnline.SHOW_TYPE_IOS) {
//            //苹果
//            return true;
//        } else if (param == StationDepositOnline.SHOW_TYPE_ALL) {
//            return true;
//        } else {
//            return false;
//        }
//    }

	public static void scan2JsonMethod(JSONObject object, String returnType, String payName, String payType,
			String payAmount, String orderTime, String flag) {
		object.put("returnType", returnType);
		object.put("payName", payName);
		object.put("payType", payType);
		object.put("orderTime", orderTime);
		object.put("payAmount", payAmount);
		object.put("flag", flag);
	}
}
