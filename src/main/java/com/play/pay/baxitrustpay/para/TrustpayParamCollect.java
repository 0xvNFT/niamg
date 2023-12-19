package com.play.pay.baxitrustpay.para;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class TrustpayParamCollect implements ITrustpayParamBase {
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
	 * 商户的订单号码， 需要唯一
	 * <p>
	 * 必填
	 */
	private String orderNo;
	/**
	 * 订单金额 单位雷亚，两位小数
	 * <p>
	 * 必填
	 */
	private String amount;
	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String notifyUrl;
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;

	public Map<String, String> toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this), new TypeReference<Map<String, String>>() {
		});
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}
