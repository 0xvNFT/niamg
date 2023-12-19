package com.play.pay.baxiowenpay.params;

import com.alibaba.fastjson.JSONObject;

import java.net.InetAddress;
import java.util.Random;

public class OwenpayParamCollect implements OwenpayParamBase {
	/**
	 * 平台唯一标识，即商户号
	 * <p>
	 * 必填
	 */
	private String appID;
	/**
	 * 币种编码
	 * <p>
	 * 必填
	 */
	private String currencyCode;
	/**
	 * 支付类型编码
	 * <p>
	 * 必填
	 */
	private String tradeCode;
	/**
	 * 随机数
	 * <p>
	 * 必填
	 */
	private String randomNo;
	/**
	 * 订单号s
	 * <p>
	 * 必填
	 */
	private String outTradeNo;
	/**
	 * 订单金额
	 * <p>
	 * 必填
	 */
	private String totalAmount;
	/**
	 * 商品标题
	 * <p>
	 * 必填
	 */
	private String productTitle;


	/**
	 * 交易异步回调地址
	 * <p>
	 * 必填
	 */
	private String notifyUrl;
	/**
	 * 交易请求IP
	 * <p>
	 * 必填
	 */
	private String tradeIP;
	/**
	 * 付款人姓名
	 * <p>
	 * 必填
	 */
	private String payName;
	/**
	 * 邮箱
	 * <p>
	 * 必填
	 */
	private String payEmail;	/**
	 * 付款人手机号
	 * <p>
	 * 必填
	 */
	private String payPhone;	/**
	 * 加密字符串
	 * <p>
	 * 必填
	 */
	private String sign;	/**
	 * 银行账户
	 * <p>
	 * 必填
	 */
	private String payBankCard;

    //银行编码/付款方式
	private String payBankCode;

	public OwenpayParamCollect(){
		this.payBankCode = "PIX";
		this.payBankCard = "123456";
		this.payPhone = "68660387";
		this.payEmail = "xiaoming@email.com";
		this.payName = "linc";
		this.productTitle = "test";
		this.currencyCode = "BRL";//币种编码new Random()
		Random r =new Random();
		this.randomNo = r.nextInt(9999999) + "" + r.nextInt(9999999);
		this.tradeCode = "BRL002";
		try {
			this.tradeIP = InetAddress.getLocalHost().getHostAddress();
		}catch (Exception e){
			e.printStackTrace();
		}

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

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
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

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeIP() {
		return tradeIP;
	}

	public void setTradeIP(String tradeIP) {
		this.tradeIP = tradeIP;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayEmail() {
		return payEmail;
	}

	public void setPayEmail(String payEmail) {
		this.payEmail = payEmail;
	}

	public String getPayPhone() {
		return payPhone;
	}

	public void setPayPhone(String payPhone) {
		this.payPhone = payPhone;
	}

	public String getPayBankCard() {
		return payBankCard;
	}

	public void setPayBankCard(String payBankCard) {
		this.payBankCard = payBankCard;
	}

	public String getPayBankCode() {
		return payBankCode;
	}

	public void setPayBankCode(String payBankCode) {
		this.payBankCode = payBankCode;
	}
}
