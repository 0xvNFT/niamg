package com.play.common.utils.security;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	private static final String PWD_MD5_KEY_MANAGER = "JFEPOfdwweFE0{{!U)@*&9ksajdhfiubOFEW223g8343";
	private static final String PWD_MD5_KEY_PARTNER_USER = "F!@(8(@GYp)!(Hb3klfEFjpFNih1h92iwq8";
	private static final String PWD_MD5_KEY_ADMIN = "DFEFLewqfehiy9y172uy1y98iuoKJH(*&!@()-";
	private static final String PWD_MD5_KEY_AGENT = "JKF1O(*IOEHUF(0-102h9u413hijPP!)(@e";
	private static final String PWD_MD5_KEY_MEMBER = "OP_))Y!*(@&T(HL:Pohig1uy2980rhi";
	private static final String LOGIN_SESSION_KEY_ADMIN = "few!@#efwfew{!@I)U$tr6FEF8F)JUU*(&G23l";
	private static final String PWD_MD5_KEY_ADMIN_RECEIPT = "K12(*&82Hfewq998128-F8i2higf0f&*ewqfwouy";
	private static final String PWD_MD5_KEY_DRAW = "HIO!@*(Y78g321h12h76g89d12b";

	public final static String pwdMd5Manager(String username, String password, String salt) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_MANAGER).append(password);
		sb.append(salt);
		return md5(sb.toString());
	}

	public final static String pwdMd5PartnerUser(String username, String password, String salt) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_PARTNER_USER).append(password);
		sb.append(salt);
		return md5(sb.toString());
	}

	public final static String pwdMd5Admin(String username, String password, String salt) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_ADMIN).append(password);
		sb.append(salt);
		return md5(sb.toString());
	}

	public static void main(String[] args) {
		String uname = "jisadmin1";
		String pwd = "11111111";
		String salt = "S3QdQ21rVo";
		String encPwd = MD5Util.pwdMd5Admin(uname, pwd, salt);
		System.out.println("pwddd = " + encPwd);


	}

	public final static String pwdMd5Agent(String username, String password, String salt) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_AGENT).append(password);
		sb.append(salt);
		return md5(sb.toString());
	}

	public final static String pwdMd5Member(String username, String password, String salt) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_MEMBER).append(password);
		sb.append(salt);
		return md5(sb.toString());
	}

	public final static String md5(String s) {
		return DigestUtils.md5Hex(s);
	}

	public final static String sessionIdMd5(String id) {
		StringBuilder sb = new StringBuilder(id);
		sb.append(LOGIN_SESSION_KEY_ADMIN);
		return DigestUtils.sha1Hex(sb.toString());
	}

	public static String receiptPwdMd5Admin(String username, String password) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_ADMIN_RECEIPT).append(password);
		return md5(sb.toString());
	}

	public final static String pwdMd5Draw(String username, String password) {
		StringBuilder sb = new StringBuilder(username.toLowerCase());
		sb.append(PWD_MD5_KEY_DRAW).append(password);
		return md5(sb.toString());
	}
}
