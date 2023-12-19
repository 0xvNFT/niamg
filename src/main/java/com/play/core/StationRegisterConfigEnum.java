package com.play.core;

public enum StationRegisterConfigEnum {
	username("登录账号", 130L, "admin.account.login"),

	pwd("登录密码", 2, 120L, 2, "admin.login.password"),

	rpwd("确认登录密码", 2, 110L, 2,"admin.login.confirm.password"),

	realName("真实姓名", 100L,"admin.realName"),

	phone("手机", 90L,"admin.cell.phone"),

	qq("QQ", 70L,"admin.qq"),

	email("邮箱", 80L,"admin.mail"),

	receiptPwd("提款密码", 2, 60L, 3,"admin.withdrawal.password"),

	facebook("脸书", 50L,"admin.facebook.user"),

	wechat("微信", 40L,"admin.wechat"),

	line("Line", 60L,"admin.line"),

	promoCode("注册推广码", 1, 30L, 3,"admin.sign.up.code"), // 代理的推广码

	captcha("验证码", 1, 20L, 3,"admin.login.vcode"),

	;

	private String cname;// 名称
	private int eleType = 1;// 字段类型，1=text,2=password
	private Long sortNo;
	private String regex;// 正则
	private int uniqueness = 1;// 唯一性,1=不唯一，2=唯一，3=不可以设置
	private String code; //國際化code碼

	private StationRegisterConfigEnum(String cname, Long sortNo, String code) {
		this.cname = cname;
		this.sortNo = sortNo;
		this.code = code;
	}

	private StationRegisterConfigEnum(String cname, int eleType, Long sortNo, int uniqueness, String code) {
		this.cname = cname;
		this.eleType = eleType;
		this.sortNo = sortNo;
		this.uniqueness = uniqueness;
		this.code = code;
	}

	public String getCname() {
		return cname;
	}

	public int getEleType() {
		return eleType;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public int getUniqueness() {
		return uniqueness;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getCode() {
		return code;
	}
}
