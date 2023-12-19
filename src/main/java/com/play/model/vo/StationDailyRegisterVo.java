package com.play.model.vo;

import java.math.BigDecimal;

public class StationDailyRegisterVo {
	private Long id;

	/**
	 * 站点ID
	 */
	private Long stationId;

	/**
	 * 日期
	 */
	private String statDate;

	/**
	 * 当天注册总人数
	 */
	private Integer registerNum;

	/**
	 * 当天注册会员人数
	 */
	private Integer memberNum;

	/**
	 * 当天注册代理人数
	 */
	private Integer proxyNum;

	/**
	 * 当天注册会员首充人数
	 */
	private Integer firstDeposit;

	/**
	 * 当天二次充值人数
	 */
	private Integer secDeposit;
	/**
	 * 当天三次充值人数
	 */
	private Integer thirdDeposit;

	/**
	 * 当日通过代理推广注册会员人数
	 */
	private Integer promoMember;

	/**
	 * 当日通过代理推广注册代理人数
	 */
	private Integer promoProxy;

	/**
	 * 当天注册充值次数
	 */
	private Integer deposit;

	/**
	 * 当天注册充值金额
	 */
	private BigDecimal depositMoney;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public Integer getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(Integer registerNum) {
		this.registerNum = registerNum;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Integer getProxyNum() {
		return proxyNum;
	}

	public void setProxyNum(Integer proxyNum) {
		this.proxyNum = proxyNum;
	}

	public Integer getFirstDeposit() {
		return firstDeposit;
	}

	public void setFirstDeposit(Integer firstDeposit) {
		this.firstDeposit = firstDeposit;
	}

	public Integer getSecDeposit() {
		return secDeposit;
	}

	public void setSecDeposit(Integer secDeposit) {
		this.secDeposit = secDeposit;
	}

	public Integer getThirdDeposit() {
		return thirdDeposit;
	}

	public void setThirdDeposit(Integer thirdDeposit) {
		this.thirdDeposit = thirdDeposit;
	}

	public Integer getPromoMember() {
		return promoMember;
	}

	public void setPromoMember(Integer promoMember) {
		this.promoMember = promoMember;
	}

	public Integer getPromoProxy() {
		return promoProxy;
	}

	public void setPromoProxy(Integer promoProxy) {
		this.promoProxy = promoProxy;
	}

	public Integer getDeposit() {
		return deposit;
	}

	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}

	public BigDecimal getDepositMoney() {
		return depositMoney;
	}

	public void setDepositMoney(BigDecimal depositMoney) {
		this.depositMoney = depositMoney;
	}

}
