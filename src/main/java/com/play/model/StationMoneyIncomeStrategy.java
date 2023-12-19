package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 余额生金策略 
 *
 * @author admin
 *
 */
@Table(name = "station_money_income_strategy")
public class StationMoneyIncomeStrategy {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 限制会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 最低余额
	 */
	@Column(name = "balance_limit")
	private BigDecimal balanceLimit;
	/**
	 * 最大余额
	 */
	@Column(name = "balance_max_limit")
	private BigDecimal balanceMaxLimit;
	/**
	 * 余额所需打码倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 少于打码时是否计算收益
	 */
	@Column(name = "less_have_income")
	private Integer lessHaveIncome;
	/**
	 * 少于打码时最小利率
	 */
	@Column(name = "less_min_scale")
	private BigDecimal lessMinScale;
	/**
	 * 少于打码时最大利率
	 */
	@Column(name = "less_max_scale")
	private BigDecimal lessMaxScale;
	/**
	 * 大于打码时最大利率
	 */
	@Column(name = "more_min_scale")
	private BigDecimal moreMinScale;
	/**
	 * 大于打码时最大利率
	 */
	@Column(name = "more_max_scale")
	private BigDecimal moreMaxScale;
	/**
	 * 收益打码量
	 */
	@Column(name = "income_bet_rate")
	private BigDecimal incomeBetRate;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "status")
	private Integer status;
	/**
	 * 发放收益后是否发送站内信
	 */
	@Column(name = "send_msg")
	private Integer sendMsg;
	/**
	 * 开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;

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

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public BigDecimal getBalanceLimit() {
		return balanceLimit;
	}

	public void setBalanceLimit(BigDecimal balanceLimit) {
		this.balanceLimit = balanceLimit;
	}

	public BigDecimal getBalanceMaxLimit() {
		return balanceMaxLimit;
	}

	public void setBalanceMaxLimit(BigDecimal balanceMaxLimit) {
		this.balanceMaxLimit = balanceMaxLimit;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public Integer getLessHaveIncome() {
		return lessHaveIncome;
	}

	public void setLessHaveIncome(Integer lessHaveIncome) {
		this.lessHaveIncome = lessHaveIncome;
	}

	public BigDecimal getLessMinScale() {
		return lessMinScale;
	}

	public void setLessMinScale(BigDecimal lessMinScale) {
		this.lessMinScale = lessMinScale;
	}

	public BigDecimal getLessMaxScale() {
		return lessMaxScale;
	}

	public void setLessMaxScale(BigDecimal lessMaxScale) {
		this.lessMaxScale = lessMaxScale;
	}

	public BigDecimal getMoreMinScale() {
		return moreMinScale;
	}

	public void setMoreMinScale(BigDecimal moreMinScale) {
		this.moreMinScale = moreMinScale;
	}

	public BigDecimal getMoreMaxScale() {
		return moreMaxScale;
	}

	public void setMoreMaxScale(BigDecimal moreMaxScale) {
		this.moreMaxScale = moreMaxScale;
	}

	public BigDecimal getIncomeBetRate() {
		return incomeBetRate;
	}

	public void setIncomeBetRate(BigDecimal incomeBetRate) {
		this.incomeBetRate = incomeBetRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSendMsg() {
		return sendMsg;
	}

	public void setSendMsg(Integer sendMsg) {
		this.sendMsg = sendMsg;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
