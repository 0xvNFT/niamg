package com.play.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 站点管理员信息 
 *
 * @author admin
 *
 */
@Table(name = "admin_user")
public class AdminUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商id
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	/**
	 * 二级密码
	 */
	@Column(name = "receipt_password")
	private String receiptPassword;
	
	@Column(name = "salt")
	private String salt;
	/**
	 * 分组id
	 */
	@Column(name = "group_id")
	private Long groupId;
	/**
	 * 1=禁用，2=启用
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建者id
	 */
	@Column(name = "creator_id")
	private Long creatorId;
	/**
	 * 添加时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 类型，看UserTypeEnum 
	 */
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "create_ip")
	private String createIp;
	/**
	 * 最后登陆ip
	 */
	@Column(name = "last_login_ip")
	private String lastLoginIp;
	/**
	 * 最后登陆时间
	 */
	@Column(name = "last_login_time")
	private Date lastLoginTime;
	
	@Column(name = "remark")
	private String remark;
	/**
	 * 手动加款金额上限
	 */
	@Column(name = "add_money_limit")
	private BigDecimal addMoneyLimit;
	/**
	 * 处理单笔入款上限
	 */
	@Column(name = "deposit_limit")
	private BigDecimal depositLimit;
	/**
	 * 处理单笔出款上限
	 */
	@Column(name = "withdraw_limit")
	private BigDecimal withdrawLimit;
	/**
	 * 默认账号，2=默认，1=后期添加
	 */
	@Column(name = "original")
	private Integer original;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReceiptPassword() {
		return receiptPassword;
	}

	public void setReceiptPassword(String receiptPassword) {
		this.receiptPassword = receiptPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

}
