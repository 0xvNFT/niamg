package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员存款总计
 *
 * @author admin
 *
 */
@Table(name = "sys_user_deposit")
public class SysUserDeposit {
	/**
	 * 会员ID
	 */
	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;
	/**
	 * 存款次数
	 */
	@Column(name = "times")
	private Integer times;
	/**
	 * 存款总额
	 */
	@Column(name = "total_money")
	private BigDecimal totalMoney;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 首次充值时间
	 */
	@Column(name = "first_time")
	private Date firstTime;
	/**
	 * 首次充值金额
	 */
	@Column(name = "first_money")
	private BigDecimal firstMoney;
	/**
	 * 二次充值时间
	 */
	@Column(name = "sec_time")
	private Date secTime;
	/**
	 * 二次充值金额
	 */
	@Column(name = "sec_money")
	private BigDecimal secMoney;
	/**
	 * 三次充值时间
	 */
	@Column(name = "third_time")
	private Date thirdTime;
	/**
	 * 三次充值金额
	 */
	@Column(name = "third_money")
	private BigDecimal thirdMoney;
	/**
	 * 最大充值金额
	 */
	@Column(name = "max_money")
	private BigDecimal maxMoney;
	/**
	 * 最大充值时间
	 */
	@Column(name = "max_time")
	private Date maxTime;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}

	public BigDecimal getFirstMoney() {
		return firstMoney;
	}

	public void setFirstMoney(BigDecimal firstMoney) {
		this.firstMoney = firstMoney;
	}

	public Date getSecTime() {
		return secTime;
	}

	public void setSecTime(Date secTime) {
		this.secTime = secTime;
	}

	public BigDecimal getSecMoney() {
		return secMoney;
	}

	public void setSecMoney(BigDecimal secMoney) {
		this.secMoney = secMoney;
	}

	public Date getThirdTime() {
		return thirdTime;
	}

	public void setThirdTime(Date thirdTime) {
		this.thirdTime = thirdTime;
	}

	public BigDecimal getThirdMoney() {
		return thirdMoney;
	}

	public void setThirdMoney(BigDecimal thirdMoney) {
		this.thirdMoney = thirdMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

}
