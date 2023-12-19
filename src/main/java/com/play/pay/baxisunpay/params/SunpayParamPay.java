package com.play.pay.baxisunpay.params;

import com.alibaba.fastjson.JSONObject;

public class SunpayParamPay implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String out_order_no;
	/**
	 * 商户appId
	 * <p>
	 * 必填
	 */
	private String out_user_id;
	/**
	 * （必填）下单金额（单位：卢比）
	 */
	private String currency;
	/**
	 * 请在商户后台通道列表查看（默认使用：default，原生通道编码：YS，非原生通道编码：FYS）
	 * <p>
	 * 必填
	 */
	private String amount;

	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String address;
	/**
	 * （必填）utf-8(默认填写)
	 * <p>
	 * 必填
	 */
	private String webhook_url;
	/**
	 * （必填）银行账号
	 * <p>
	 * 必填
	 */

	public SunpayParamPay(){
		this.setCurrency("TRC20_USDT");
		this.setOut_user_id("123");
		this.setAddress("TR7hihY6otjNT6bsfKwXMHfgfUfVpqnhGj");
	}
	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
	}

	public String getOut_user_id() {
		return out_user_id;
	}

	public void setOut_user_id(String out_user_id) {
		this.out_user_id = out_user_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebhook_url() {
		return webhook_url;
	}

	public void setWebhook_url(String webhook_url) {
		this.webhook_url = webhook_url;
	}

	public void setSign(String sign) {

	}

}
