package com.play.model.vo;

import java.math.BigDecimal;

public class DailyMoneyVo {
	// 当日充值/提款次数
	private Integer times;
	// 当日充值/提款总金额
	private BigDecimal money;

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
