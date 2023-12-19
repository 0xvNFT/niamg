package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "rechargeable_card")
public class RechargeableCard {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 卡号
	 */
	@Column(name = "card_no")
	private String cardNo;
	/**
	 * 密码
	 */
	@Column(name = "card_password")
	private String cardPassword;
	/**
	 * 充值卡面额
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 状态(1未使用2已使用)
	 */
	@Column(name = "status")
	private Long status;
	/**
	 * 充值日期
	 */
	@Column(name = "used_datetime")
	private Date usedDatetime;
	/**
	 * 充值卡使用人
	 */
	@Column(name = "used_username")
	private String usedUsername;
	/**
	 * 充值卡使用人id
	 */
	@Column(name = "used_userid")
	private Long usedUserid;
	
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
	 * 打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardPassword() {
		return cardPassword;
	}

	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getUsedDatetime() {
		return usedDatetime;
	}

	public void setUsedDatetime(Date usedDatetime) {
		this.usedDatetime = usedDatetime;
	}

	public String getUsedUsername() {
		return usedUsername;
	}

	public void setUsedUsername(String usedUsername) {
		this.usedUsername = usedUsername;
	}

	public Long getUsedUserid() {
		return usedUserid;
	}

	public void setUsedUserid(Long usedUserid) {
		this.usedUserid = usedUserid;
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

}
