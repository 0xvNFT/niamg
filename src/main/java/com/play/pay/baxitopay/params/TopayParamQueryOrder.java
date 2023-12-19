package com.play.pay.baxitopay.params;

import com.alibaba.fastjson.JSONObject;

public class TopayParamQueryOrder implements ITopayParamBase {
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
