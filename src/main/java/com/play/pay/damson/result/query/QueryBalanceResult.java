package com.play.pay.damson.result.query;

import com.play.pay.damson.result.BaseResult;

public class QueryBalanceResult extends BaseResult {
	private Long balance;
	private Long freezeBalance;

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getFreezeBalance() {
		return freezeBalance;
	}

	public void setFreezeBalance(Long freezeBalance) {
		this.freezeBalance = freezeBalance;
	}
}
