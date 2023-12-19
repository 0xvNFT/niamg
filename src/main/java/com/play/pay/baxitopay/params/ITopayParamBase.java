package com.play.pay.baxitopay.params;

import com.alibaba.fastjson.JSONObject;

public interface ITopayParamBase {
	JSONObject toMap();

	String toJsonString();

	void setSign(String sign);
}
