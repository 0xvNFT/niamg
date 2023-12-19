package com.play.pay.baxicashpay.params;

import com.alibaba.fastjson.JSONObject;

public class CashpayParamQueryOrder implements CashpayParamBase {
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String merchantOrderId;

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
	public void setSign(String sign) {
	}
}
