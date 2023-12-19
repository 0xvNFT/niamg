package com.play.pay.goopago.param;

/**
 * @author zzn
 */
public class AgentPayParam extends BaseParam {

	private String notifyUrl;

	// 代付金额
	private Long amount;

	private String currency;

	// 代付类型
	private Integer accountType;
	// 账户名称
	private String accountName;
	// 账户
	private String accountNo;
	// 银行名称
	private String bankName;
	// 银行编码
	private String bankCode;
	// 账户数字
	private Integer accountDigit;
	// 银行分行
	private String bankBranch;
	// 证件类型
	private Integer idType;
	// 证件号
	private String idNumber;
	// 备注
	private String remark;
	// 扩展参数 通知时原样返回
	private String extra;
	// 邮箱
	private String email;

	private String phone;

	private String cciNumber;

	// 过期时间
	private Long expireTime;

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getAccountDigit() {
		return accountDigit;
	}

	public void setAccountDigit(Integer accountDigit) {
		this.accountDigit = accountDigit;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public Integer getIdType() {
		return idType;
	}

	public void setIdType(Integer idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCciNumber() {
		return cciNumber;
	}

	public void setCciNumber(String cciNumber) {
		this.cciNumber = cciNumber;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
}
