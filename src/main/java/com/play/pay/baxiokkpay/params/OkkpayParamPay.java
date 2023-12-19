package com.play.pay.baxiokkpay.params;

import com.alibaba.fastjson.JSONObject;

public class OkkpayParamPay implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String apiOrderNo;
	/**
	 * 商户appId
	 * <p>
	 * 必填
	 */
	private String appId;
	/**
	 * （必填）下单金额（单位：卢比）
	 */
	private String totalFee;
	/**
	 * 请在商户后台通道列表查看（默认使用：default，原生通道编码：YS，非原生通道编码：FYS）
	 * <p>
	 * 必填
	 */
	private String channelCode;

	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String notifyUrl;
	/**
	 * （必填）utf-8(默认填写)
	 * <p>
	 * 必填
	 */
	private String charset;
	/**
	 * （必填）银行账号
	 * <p>
	 * 必填
	 */
	private String bankcardNo;
	/**
	 * （必填）银行名称
	 * <p>
	 * 必填
	 */
	private String bankName;
	/**
	 * （必填）ifsc码，由4位大写字母+7位数字组成
	 * <p>
	 * 必填
	 */
	private String ifsc;
	/**
	 *（必填）持卡人姓名
	 * <p>
	 * 必填
	 */
	private String name;
	/**
	 *（必填）持卡人邮箱
	 * <p>
	 * 必填
	 */
	private String email;
	/**
	 *（必填）模式（IMPS/UPI）目前只有IMPS一种模式
	 * <p>
	 * 必填
	 */
	private String mode;
	/**
	 *（必填）持卡人手机号
	 * <p>
	 * 必填
	 */
	private String phone;
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;
	public OkkpayParamPay(){
		this.setBankName("Yesbank");//银行名称
		this.setIfsc("icic0556623");//没有传递这里先喜色
		this.setName("Jack Chen");//没有传递这里先写死
		this.setEmail("jackchen@gmail.com");//没有传递先写死
		this.setMode("IMPS");
		this.setPhone("9152565523655");//电话号这里先写死
		this.setCharset("utf-8");
		this.setChannelCode("UPI");
	}

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}


	public String getApiOrderNo() {
		return apiOrderNo;
	}

	public void setApiOrderNo(String apiOrderNo) {
		this.apiOrderNo = apiOrderNo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getBankcardNo() {
		return bankcardNo;
	}

	public void setBankcardNo(String bankcardNo) {
		this.bankcardNo = bankcardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
}
