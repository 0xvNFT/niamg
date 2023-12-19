package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "coupons")
public class Coupons {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 代金券名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 发放显示类型(1普通发放,2投注回馈,3老用户回馈,4新用户注册回馈)
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 代金券面额
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 最小充值金额可使用
	 */
	@Column(name = "min_amount")
	private BigDecimal minAmount;
	/**
	 * 最大充值金额可使用
	 */
	@Column(name = "max_amount")
	private BigDecimal maxAmount;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 充值卡创建人
	 */
	@Column(name = "create_username")
	private String createUsername;
	/**
	 * 充值卡创建人id
	 */
	@Column(name = "create_userid")
	private Long createUserid;
	/**
	 * 所需打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 是否可用(1禁用2正常)
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 有效期开始时间
	 */
	@Column(name = "start_datetime")
	private Date startDatetime;
	/**
	 * 有效期结束时间
	 */
	@Column(name = "end_datetime")
	private Date endDatetime;
	/**
	 * 总发放数量
	 */
	@Column(name = "numbers")
	private Long numbers;
	/**
	 * 已使用数量
	 */
	@Column(name = "used")
	private Long used;
	/**
	 * 1指定日期范围有效,2有效天数(被领取开始算起),3长期有效
	 */
	@Column(name = "valid")
	private Integer valid;
	/**
	 * 有效天数
	 */
	@Column(name = "valid_days")
	private Long validDays;
	/**
	 * 已发放数量
	 */
	@Column(name = "send_numbers")
	private Long sendNumbers;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public Long getCreateUserid() {
		return createUserid;
	}

	public void setCreateUserid(Long createUserid) {
		this.createUserid = createUserid;
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

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public Long getNumbers() {
		return numbers;
	}

	public void setNumbers(Long numbers) {
		this.numbers = numbers;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Long getValidDays() {
		return validDays;
	}

	public void setValidDays(Long validDays) {
		this.validDays = validDays;
	}

	public Long getSendNumbers() {
		return sendNumbers;
	}

	public void setSendNumbers(Long sendNumbers) {
		this.sendNumbers = sendNumbers;
	}

}
