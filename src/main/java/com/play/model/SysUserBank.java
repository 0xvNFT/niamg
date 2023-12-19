package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 用户银行卡信息表
 *
 * @author admin
 *
 */
@Table(name = "sys_user_bank")
public class SysUserBank {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 会员账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 卡号
	 */
	@Column(name = "card_no")
	private String cardNo;
	/**
	 * 真实姓名
	 */
	@Column(name = "real_name")
	private String realName;
	/**
	 * 银行代码
	 */
	@Column(name = "bank_code")
	private String bankCode;
	/**
	 * 银行地址
	 */
	@Column(name = "bank_address")
	private String bankAddress;
	/**
	 * 银行名称
	 */
	@Column(name = "bank_name")
	private String bankName;
	/**
	 * 备注
	 */
	@Column(name = "remarks")
	private String remarks;
	/**
	 * 状态：2正常，1禁用
	 */
	@Column(name = "status")
	private Integer status;

	@Column(name = "create_time")
	private Date createTime;

	private String degreeName;

	private boolean isUSDT;
	private String usdtRemark;
	private String usdtRate;
	private String depositRate;
	private String withdrawRate;

	public String getDepositRate() {
		return depositRate;
	}

	public void setDepositRate(String depositRate) {
		this.depositRate = depositRate;
	}

	public String getWithdrawRate() {
		return withdrawRate;
	}

	public void setWithdrawRate(String withdrawRate) {
		this.withdrawRate = withdrawRate;
	}

	public boolean isUSDT() {
		return isUSDT;
	}

	public void setUSDT(boolean USDT) {
		isUSDT = USDT;
	}

	public String getUsdtRemark() {
		return usdtRemark;
	}

	public void setUsdtRemark(String usdtRemark) {
		this.usdtRemark = usdtRemark;
	}

	public String getUsdtRate() {
		return usdtRate;
	}

	public void setUsdtRate(String usdtRate) {
		this.usdtRate = usdtRate;
	}

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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

}
