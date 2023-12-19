package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点每日充值报表
 *
 * @author admin
 *
 */
@Table(name = "station_daily_deposit")
public class StationDailyDeposit {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "stat_date")
	private Date statDate;
	/**
	 * 提交充值次数
	 */
	@Column(name = "deposit_times")
	private Integer depositTimes;
	/**
	 * 充值成功次数
	 */
	@Column(name = "success_times")
	private Integer successTimes;
	/**
	 * 提交充值金额
	 */
	@Column(name = "deposit_amount")
	private BigDecimal depositAmount;
	/**
	 * 充值成功金额
	 */
	@Column(name = "success_amount")
	private BigDecimal successAmount;
	/**
	 * 最大充值金额
	 */
	@Column(name = "max_money")
	private BigDecimal maxMoney;
	/**
	 * 最小充值金额
	 */
	@Column(name = "min_money")
	private BigDecimal minMoney;
	/**
	 * 充值类型
	 */
	@Column(name = "deposit_type")
	private Integer depositType;
	/**
	 * 充值人数
	 */
	@Column(name = "deposit_user")
	private Integer depositUser;
	/**
	 * 充值平台
	 */
	@Column(name = "pay_platform_code")
	private String payPlatformCode;
	/**
	 * 支付名称
	 */
	@Column(name = "pay_name")
	private String payName;
	/**
	 * 充值失败人数
	 */
	@Column(name = "failed_times")
	private Integer failedTimes;

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

	public Integer getDepositTimes() {
		return depositTimes;
	}

	public void setDepositTimes(Integer depositTimes) {
		this.depositTimes = depositTimes;
	}

	public Integer getSuccessTimes() {
		return successTimes;
	}

	public void setSuccessTimes(Integer successTimes) {
		this.successTimes = successTimes;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public BigDecimal getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(BigDecimal successAmount) {
		this.successAmount = successAmount;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public Integer getDepositType() {
		return depositType;
	}

	public void setDepositType(Integer depositType) {
		this.depositType = depositType;
	}

	public Integer getDepositUser() {
		return depositUser;
	}

	public void setDepositUser(Integer depositUser) {
		this.depositUser = depositUser;
	}

	public String getPayPlatformCode() {
		return payPlatformCode;
	}

	public void setPayPlatformCode(String payPlatformCode) {
		this.payPlatformCode = payPlatformCode;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public Integer getFailedTimes() {
		return failedTimes;
	}

	public void setFailedTimes(Integer failedTimes) {
		this.failedTimes = failedTimes;
	}

}
