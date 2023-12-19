package com.play.pay.baxitrustpay.para;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class TrustpayParamQueryBalance implements ITrustpayParamBase {
	/**
	 * 商户号，可以在管理平台找到
	 * <p>
	 * 必填
	 */
	private String merchantId;
	/**
	 * 默认填2，巴西
	 * <p>
	 * 必填
	 */
	private String country = "2";
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;

	public Map<String, String> toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this), new TypeReference<Map<String, String>>() {});
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
