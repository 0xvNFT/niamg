package com.play.pay.baxisunpay.params;

import com.alibaba.fastjson.JSONObject;

public class SunPayParamQueryOrder implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String orderNo;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */



	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public void setSign(String sign) {
	}
}
