package com.play.pay.damson.result.query;

import com.play.pay.damson.result.BaseResult;

public class QueryOrderResult extends BaseResult {
	private String orderInfo;
	private boolean single;

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}
}
