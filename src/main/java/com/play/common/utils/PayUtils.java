package com.play.common.utils;

import java.util.regex.Pattern;

import com.play.core.BrazilCashAccountTypeEnum;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import com.play.core.BrazilPixAccountTypeEnum;

/**
 * PIX 类型，
 * <p>
 * CPF 用户身份证，长度11位数字
 * <p>
 * CNPJ 实体登记号，长度14位数字
 * <p>
 * EMAIL 用户邮箱，合法邮箱格式
 * <p>
 * PHONE 用户手机号，长度11位数字
 * <p>
 * RANDOMKEY PIX key，长度36位uuid格式  这个不太明确啊
 * <p>
 * EVP PIX key，长度36位uuid格式
 * <p>
 * 必填
 */
public class PayUtils {
	/* 正则表达式：验证邮箱 */
	public static final Pattern EMAIL = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
	/* 巴西个人CPF（个人税号）共11位数字，格式【XXXXXXXXXXX】 验证位：最后两位 验证方式：加权Mod 11 */
	public static final Pattern BRAZIL_CPF_TYPE1 = Pattern.compile("^[0-9]{11}$");
	/* 巴西个人CPF（个人税号）共11位数字，格式【XXX.XXX.XXX.XX】 验证位：最后两位 验证方式：加权Mod 11 */
	public static final Pattern BRAZIL_CPF_TYPE2 = Pattern.compile("^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\.[0-9]{2}$");
	/* 巴西CNPJ（国家法人登记的联邦税务识别编号）共14位数字，格式【XX.XXX.XXX/XXXX.XX】 */
	public static final Pattern BRAZIL_CNPJ = Pattern.compile("^[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}/[0-9]{4}.[0-9]{2}$");
	/* 巴西电话号码 共【10、11】位数字，格式【+55XXXXXXXXX】 */
//	public static final Pattern BRAZIL_PHONE_NUMBER = Pattern.compile("^\\+55[0-9]{10,11}$");

	public static final Pattern BRAZIL_PHONE_NUMBER = Pattern.compile("^(\\+?55)\\d{9}");
	public static final Pattern BRAZIL_PHONE_NUMBER2 = Pattern.compile("^(\\?55)\\d{10}");
	public static final Pattern BRAZIL_PHONE_NUMBER3 = Pattern.compile("^(\\+?55)\\d{10}");
	public static final Pattern BRAZIL_PHONE_NUMBER4 = Pattern.compile("^(\\+?55)\\d{11}");

	/* 巴西EVP，格式【01eb9090-73e5-4187-b43d-0d80a149e1dc】（不能确定） */
	public static final Pattern BRAZIL_EVP = Pattern.compile("^[0-9a-z]{8}\\-[0-9a-z]{4}\\-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");

	// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
	// 验证CPF是否有效 https://www.geradorcpf.com/validar-cpf.htm
	public static BrazilPixAccountTypeEnum getBrazilPixAccountType(String account) {
		if (BRAZIL_PHONE_NUMBER.matcher(account).matches()||BRAZIL_PHONE_NUMBER2.matcher(account).matches()
				||BRAZIL_PHONE_NUMBER3.matcher(account).matches()||BRAZIL_PHONE_NUMBER4.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.PHONE;
		}
		if (BRAZIL_CPF_TYPE1.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.CPF;
		}
		if (BRAZIL_CPF_TYPE2.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.CPF;
		}
		if (BRAZIL_CNPJ.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.CNPJ;
		}
		if (EMAIL.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.EMAIL;
		}
		if (BRAZIL_EVP.matcher(account).matches()) {
			return BrazilPixAccountTypeEnum.EVP;
		}
		return null;
	}


	public static BrazilCashAccountTypeEnum getBrazilCashAccountType(String account) {
		if (BRAZIL_CPF_TYPE1.matcher(account).matches()) {
			return BrazilCashAccountTypeEnum.CPF;
		}
		if (BRAZIL_CPF_TYPE2.matcher(account).matches()) {
			return BrazilCashAccountTypeEnum.CNPJ;
		}
		if (EMAIL.matcher(account).matches()) {
			return BrazilCashAccountTypeEnum.EMAIL;
		}
		if (BRAZIL_PHONE_NUMBER.matcher(account).matches()) {
			return BrazilCashAccountTypeEnum.PHONE;
		}
		return null;
	}


	public static boolean isValidBrazilPixAccount(String account) {
		return getBrazilPixAccountType(account) == null ? false : true;
	}

	public static void main(String[] args) {
		boolean validBrazilPixAccount = isValidBrazilPixAccount("+55842809291");
		System.out.println("pay pix match result = " + validBrazilPixAccount);
	}

	/** 暂时没用上 */
	private static boolean verifyBrazilCPF(String account) {
		account = account.replace(".", "");
		char[] charArray = account.toCharArray();
		Integer sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += Integer.valueOf(charArray[i]) - '0';
		}
		if (StringUtils.compare(account.substring(10, 11), String.valueOf(sum % 11)) == 0)
			return true;
		return false;
	}
}