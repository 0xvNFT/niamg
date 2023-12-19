package com.play.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class SysUserOnlineVo {
	private Long id;
	/**
	 * 会员账号
	 */
	private String username;
	private String proxyName;
	// 推荐者账号
	private String recommendUsername;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 余额
	 */
	private BigDecimal money;
	/**
	 * 1=电脑端，2=手机web端，3=原生app端
	 */
	private Integer terminal;
	private String remark;

	/**
	 * 是否在线警告
	 */
	private Integer onlineWarn;

	/**
	 * 最后登录时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginDatetime;
	/**
	 * 在线状态：1=下线，2=在线
	 */
	private Integer onlineStatus;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	/**
	 * 注册时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createDatetime;

	/**
	 * IP地址
	 */
	private String lastLoginIpAddress;

	private String domain;

	private boolean isBetting;
	private Integer status;

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

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getTerminal() {
		return terminal;
	}

	public void setTerminal(Integer terminal) {
		this.terminal = terminal;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOnlineWarn() {
		return onlineWarn;
	}

	public void setOnlineWarn(Integer onlineWarn) {
		this.onlineWarn = onlineWarn;
	}

	public Date getLastLoginDatetime() {
		return lastLoginDatetime;
	}

	public void setLastLoginDatetime(Date lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getLastLoginIpAddress() {
		return lastLoginIpAddress;
	}

	public void setLastLoginIpAddress(String lastLoginIpAddress) {
		this.lastLoginIpAddress = lastLoginIpAddress;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isBetting() {
		return isBetting;
	}

	public void setBetting(boolean isBetting) {
		this.isBetting = isBetting;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRecommendUsername() {
		return recommendUsername;
	}

	public void setRecommendUsername(String recommendUsername) {
		this.recommendUsername = recommendUsername;
	}

}
