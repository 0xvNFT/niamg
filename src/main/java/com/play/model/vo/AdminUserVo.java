package com.play.model.vo;

import java.math.BigDecimal;
import java.util.Date;

public class AdminUserVo {
	private Long id;

	private String username;

	/**
	 * 分组id
	 */
	private Long groupId;
	private String groupName;
	/**
	 * 1=禁用，2=启用
	 */
	private Integer status;
	/**
	 * 创建者id
	 */
	private Long creatorId;
	/**
	 * 添加时间
	 */
	private Date createDatetime;
	/**
	 * 类型
	 */
	private Integer type;

	private String createIp;

	private String lastLoginIp;
	private String lastLoginIpStr;

	private Date lastLoginTime;

	private Long stationId;

	private String remark;

	private BigDecimal addMoneyLimit;
	private BigDecimal depositLimit;
	private BigDecimal withdrawLimit;

	private Integer original;

	/**
	 * 是否在线
	 */
	private boolean isOnline;

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	public BigDecimal getAddMoneyLimit() {
		return addMoneyLimit;
	}

	public void setAddMoneyLimit(BigDecimal addMoneyLimit) {
		this.addMoneyLimit = addMoneyLimit;
	}

	public BigDecimal getDepositLimit() {
		return depositLimit;
	}

	public void setDepositLimit(BigDecimal depositLimit) {
		this.depositLimit = depositLimit;
	}

	public BigDecimal getWithdrawLimit() {
		return withdrawLimit;
	}

	public void setWithdrawLimit(BigDecimal withdrawLimit) {
		this.withdrawLimit = withdrawLimit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIpStr() {
		return lastLoginIpStr;
	}

	public void setLastLoginIpStr(String lastLoginIpStr) {
		this.lastLoginIpStr = lastLoginIpStr;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

}
