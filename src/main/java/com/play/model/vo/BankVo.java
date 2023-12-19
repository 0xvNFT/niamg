package com.play.model.vo;

public class BankVo {

	/**
	 * 银行代码
	 */
	private String bankCode;

	/**
	 * 银行名称
	 */
	private String bankName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 最后一次绑卡真实姓名
	 */
	private String lastRealName;

	/**
	 * 银行卡号
	 */
	private String cardNo;

	/**
	 * 最后一次卡号
	 */
	private String lastCardNo;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 站点ID
	 */
	private Long stationId;

	/**
	 * 开户行地址
	 */
	private String bankAddress;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 提款密码
	 */
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLastRealName() {
		return lastRealName;
	}

	public void setLastRealName(String lastRealName) {
		this.lastRealName = lastRealName;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getLastCardNo() {
		return lastCardNo;
	}

	public void setLastCardNo(String lastCardNo) {
		this.lastCardNo = lastCardNo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
