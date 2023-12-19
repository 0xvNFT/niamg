package com.play.pay.baxiokkpay.params;

import com.alibaba.fastjson.JSONObject;

public class OkkpayParamCollect implements ITopayParamBase {
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

	private String sign;

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
