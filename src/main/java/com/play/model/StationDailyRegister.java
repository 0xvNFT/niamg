package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点每日注册信息表
 *
 * @author admin
 *
 */
@Table(name = "station_daily_register")
public class StationDailyRegister {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 日期
	 */
	@Column(name = "stat_date")
	private Date statDate;
	/**
	 * 当天注册人数
	 */
	@Column(name = "register_num")
	private Integer registerNum;
	/**
	 * 当天注册会员人数
	 */
	@Column(name = "member_num")
	private Integer memberNum;
	/**
	 * 当天注册代理人数
	 */
	@Column(name = "proxy_num")
	private Integer proxyNum;
	/**
	 * 当天注册并且首充人数
	 */
	@Column(name = "first_deposit")
	private Integer firstDeposit;
	/**
	 * 当天注册并且二充人数
	 */
	@Column(name = "sec_deposit")
	private Integer secDeposit;
	@Column(name = "third_deposit")
	private Integer thirdDeposit;
	/**
	 * 当天通过代理推广注册人数
	 */
	@Column(name = "promo_member")
	private Integer promoMember;
	/**
	 * 当天通过代理推广注册代理人数
	 */
	@Column(name = "promo_proxy")
	private Integer promoProxy;
	/**
	 * 当天注册充值次数
	 */
	@Column(name = "deposit")
	private Integer deposit;
	/**
	 * 当天注册充值金额
	 */
	@Column(name = "deposit_money")
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

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
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
