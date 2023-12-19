package com.play.pay.yespay.config;

public class YesPayConfig {
	private Long mchId;
	private String key;
	private String payUrl;
	private String queryUrl;

	public YesPayConfig(Long mchId, String key, String payUrl, String queryUrl) {
		this.mchId = mchId;
		this.key = key;
		this.payUrl = payUrl;
		this.queryUrl = queryUrl;
	}

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getQueryUrl() {
		return queryUrl;
	}

	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
}
