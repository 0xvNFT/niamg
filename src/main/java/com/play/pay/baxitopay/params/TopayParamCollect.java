package com.play.pay.baxitopay.params;

import com.alibaba.fastjson.JSONObject;

public class TopayParamCollect implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String merchant_no;
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String out_trade_no;
	/**
	 * 描述，不超过200字符
	 * <p>
	 * 必填
	 */
	private String description;
	/**
	 * 商品标题
	 * <p>
	 * 必填
	 */
	private String title;
	/**
	 * 支付金额，保留两位小数，最小0.01
	 * <p>
	 * 必填
	 */
	private String pay_amount;
	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String notify_url;
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
}
