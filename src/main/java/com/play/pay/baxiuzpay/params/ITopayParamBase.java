package com.play.pay.baxiuzpay.params;

import com.alibaba.fastjson.JSONObject;

public interface ITopayParamBase {
	JSONObject toMap();

	String toJsonString();

	void setSign(String sign);
}
