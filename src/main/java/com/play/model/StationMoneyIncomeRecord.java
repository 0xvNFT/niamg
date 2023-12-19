package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 余额生金记录 
 *
 * @author admin
 *
 */
@Table(name = "station_money_income_record")
public class StationMoneyIncomeRecord {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "stat_date")
	private Date statDate;
	/**
	 * 余额
	 */
	@Column(name = "balance")
	private BigDecimal balance;
	/**
	 * 收益
	 */
	@Column(name = "income")
	private BigDecimal income;
	/**
	 * 利率
	 */
	@Column(name = "scale")
	private BigDecimal scale;
	/**
	 * 当日打码
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 1:赠送成功 2：回滚成功 3：回滚失败
	 */
	@Column(name = "status")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public BigDecimal getScale() {
		return scale;
	}

	public void setScale(BigDecimal scale) {
		this.scale = scale;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
