package com.play.pay.goopago.result.query;

import com.play.pay.goopago.result.BaseResult;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class QueryBalanceResult extends BaseResult {

	private Long balance;
	private Long unBalance;

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getUnBalance() {
		return unBalance;
	}

	public void setUnBalance(Long unBalance) {
		this.unBalance = unBalance;
	}
}
