package com.play.pay.baxiowenpay.params;

import com.alibaba.fastjson.JSONObject;

public interface OwenpayParamBase {
	JSONObject toMap();

	String toJsonString();

	void setSign(String sign);
}
