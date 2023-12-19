package com.play.pay.baxiowenpay.params;

import com.alibaba.fastjson.JSONObject;

public class OwenpayParamQueryOrder implements OwenpayParamBase {
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
	private String outTradeNo;
	/**
	 * 签名
	 * <p>
	 * 必填
	 */

	private String remitDate;
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

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getRemitDate() {
		return remitDate;
	}

	public void setRemitDate(String remitDate) {
		this.remitDate = remitDate;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
