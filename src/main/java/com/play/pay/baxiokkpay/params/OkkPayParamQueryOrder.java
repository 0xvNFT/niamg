package com.play.pay.baxiokkpay.params;

import com.alibaba.fastjson.JSONObject;

public class OkkPayParamQueryOrder implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String appId;
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String apiOrderNo;

	/**
	 * （必填）utf-8(默认填写)
	 * <p>
	 * 必填
	 */
	private String charset;

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



	public String getSign() {
		return sign;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApiOrderNo() {
		return apiOrderNo;
	}

	public void setApiOrderNo(String apiOrderNo) {
		this.apiOrderNo = apiOrderNo;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
