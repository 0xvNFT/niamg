package com.play.pay.goopago.param;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zzn
 */
public class BaseParam {
	/**
	 * 商户ID
	 * <p>
	 * 必填
	 */
	private Long mchId;
	/**
	 * 应用ID
	 * <p>
	 * 必填
	 */
	private String appId;
	/**
	 * <p>
	 * 必填
	 */
	private String mchOrderNo;
	/**
	 * 回调通知地址
	 * <p>
	 * 必填
	 */
	private String nonceStr;

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

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchOrderNo() {
		return mchOrderNo;
	}

	public void setMchOrderNo(String mchOrderNo) {
		this.mchOrderNo = mchOrderNo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
