package com.play.pay.baxitrustpay.para;

import java.util.Map;

public interface ITrustpayParamBase {
	Map<String, String> toMap();

	String toJsonString();

	void setSign(String sign);
}
