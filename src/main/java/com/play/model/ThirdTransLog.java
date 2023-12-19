package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
import com.play.orm.jdbc.annotation.Transient;

@Table(name = "third_trans_log")
public class ThirdTransLog {

	public static final int TRANS_STATUS_FAIL = 1;
	public static final int TRANS_STATUS_SUCCESS = 2;
	public static final int TRANS_STATUS_UNKNOW = 3;

	public static final int TYPE_OUT_THIRD = 1; // 从第三方转入本系统
	public static final int TYPE_INTO_THIRD = 2; // 从本系统转入第三方

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 本地交易ID
	 */
	@Column(name = "order_id")
	private String orderId;
	/**
	 * 1,表示AG 2,表示BBin 3，表示MG
	 */
	@Column(name = "platform")
	private Integer platform;
	/**
	 * 1.从第三方转出;2.转入第三方
	 */
	@Column(name = "type")
	private Integer type;

	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 1，表示转账成功 2，表示转账失败 3，未知状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "before_money")
	private BigDecimal beforeMoney;

	@Column(name = "after_money")
	private BigDecimal afterMoney;
	/**
	 * 修改时间
	 */
	@Column(name = "update_datetime")
	private Date updateDatetime;

	@Transient
	private String msg;

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public BigDecimal getBeforeMoney() {
		return beforeMoney;
	}

	public void setBeforeMoney(BigDecimal beforeMoney) {
		this.beforeMoney = beforeMoney;
	}

	public BigDecimal getAfterMoney() {
		return afterMoney;
	}

	public void setAfterMoney(BigDecimal afterMoney) {
		this.afterMoney = afterMoney;
	}

	public Date getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
