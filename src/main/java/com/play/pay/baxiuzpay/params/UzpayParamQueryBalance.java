package com.play.pay.baxiuzpay.params;

import com.alibaba.fastjson.JSONObject;

public class UzpayParamQueryBalance implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String sign_type;
	/**
	 * 时间戳
	 * <p>
	 * 必填
	 */
	private String mch_id; //商户代码

	private String mch_transferId; //商家转账单号
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;

	public UzpayParamQueryBalance() {
		this.sign_type = "MD5";
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

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getMch_transferId() {
		return mch_transferId;
	}

	public void setMch_transferId(String mch_transferId) {
		this.mch_transferId = mch_transferId;
	}
}
