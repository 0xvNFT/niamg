package com.play.pay.baxicashpay.params;

import com.alibaba.fastjson.JSONObject;
public class CashpayParamCollect implements CashpayParamBase {
	/**
	 * 平台唯一标识，即商户号
	 * <p>
	 * 必填
	 */
	private String amount;
	/**
	 * 币种编码
	 * <p>
	 * 必填
	 */
	private String merchantOrderId;
	/**
	 * 支付类型编码
	 * <p>
	 * 必填
	 */
	private String notifyUrl;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	/**
	 * 随机数
	 * <p>
	 * 必填
	 */
	public void setSign(String sign) {
	}
	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}
