package com.play.pay.goopago.result.query;

import com.play.pay.goopago.result.BaseResult;

@SuppressWarnings("serial")
public class QueryOrderResult extends BaseResult {

	private String orderInfo;
	private Boolean single;

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public Boolean getSingle() {
		return single;
	}

	public void setSingle(Boolean single) {
		this.single = single;
	}
}