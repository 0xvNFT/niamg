package com.play.pay.baxicashpay.params;

import com.alibaba.fastjson.JSONObject;

public interface CashpayParamBase {
	JSONObject toMap();

	String toJsonString();

	void setSign(String sign);
}
