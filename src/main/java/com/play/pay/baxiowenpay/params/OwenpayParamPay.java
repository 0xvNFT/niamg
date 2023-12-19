package com.play.pay.baxiowenpay.params;

import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;

import java.util.Random;

public class OwenpayParamPay implements OwenpayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String appID;
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String currencyCode;
	/**
	 * 描述，不超过200字符
	 * <p>
	 * 必填
	 */
	private String randomNo;
	/**
	 * 支付金额，保留两位小数，最小0.01
	 * <p>
	 * 必填
	 */
	private String outTradeNo;
	/**
	 * 用户名，必须英文、葡萄牙文
	 * <p>
	 * 必填
	 */
	private String bankCode;


	private String bankAcctName;



	private String bankAcctNo;

	private String totalAmount;

	private String accPhone;

	private String notifyUrl;
	private String identityNo;

	private String identityType;
	private String sign;

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public OwenpayParamPay(String merchantAccount)  {
		this.currencyCode = "BRL";//币种编码
		Random r =new Random();
		this.randomNo = r.nextInt(9999999) + "" + r.nextInt(9999999);
		this.bankCode = "PIX";
		this.identityNo = "PIX";
		this.identityType = "PHONE";
		this.bankAcctName = OwenRSAEncrypt.Encrypt3DES("feihong",merchantAccount);
		this.accPhone = OwenRSAEncrypt.Encrypt3DES("+552112345678",merchantAccount);
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRandomNo() {
		return randomNo;
	}

	public void setRandomNo(String randomNo) {
		this.randomNo = randomNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAcctName() {
		return bankAcctName;
	}

	public void setBankAcctName(String bankAcctName) {
		this.bankAcctName = bankAcctName;
	}

	public String getBankAcctNo() {
		return bankAcctNo;
	}

	public void setBankAcctNo(String bankAcctNo) {
		this.bankAcctNo = bankAcctNo;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAccPhone() {
		return accPhone;
	}

	public void setAccPhone(String accPhone) {
		this.accPhone = accPhone;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getIdentityNo() {
		return identityNo;
	}

	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
