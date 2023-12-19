package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_deposit_bank")
public class StationDepositBank {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 银行名称
	 */
	@Column(name = "bank_name")
	private String bankName;
	/**
	 * 银行卡号
	 */
	@Column(name = "bank_card")
	private String bankCard;
	/**
	 * 开户人姓名
	 */
	@Column(name = "real_name")
	private String realName;
	/**
	 * 开户银行地址
	 */
	@Column(name = "bank_address")
	private String bankAddress;
	/**
	 * 最小金额
	 */
	@Column(name = "min")
	private BigDecimal min;
	/**
	 * 最大金额
	 */
	@Column(name = "max")
	private BigDecimal max;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 存款汇率
	 */
	@Column(name = "deposit_rate")
	private BigDecimal depositRate;
	/**
	 * 取款汇率
	 */
	@Column(name = "withdraw_rate")
	private BigDecimal withdrawRate;
	/**
	 * 图标样式
	 */
	@Column(name = "icon")
	private String icon;
	/**
	 * 支付平台代码
	 */
	@Column(name = "pay_platform_code")
	private String payPlatformCode;
	/**
	 * 有效会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 有效会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	/**
	 * 排序
	 */
	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 创建人
	 */
	@Column(name = "create_user")
	private Long createUser;
	/**
	 * 开启人
	 */
	@Column(name = "open_user")
	private Long openUser;
	/**
	 * 二维码路径
	 */
	@Column(name = "qr_code_img")
	private String qrCodeImg;
	/**
	 * 支付宝转卡状态
	 */
	@Column(name = "ali_qrcode_status")
	private Integer aliQrcodeStatus;
	/**
	 * 支付宝转卡链接(自动生成)
	 */
	@Column(name = "ali_qrcode_link")
	private String aliQrcodeLink;
	/**
	 * 支付宝转卡隐藏部分卡号
	 */
	@Column(name = "card_index")
	private String cardIndex;
	/**
	 * 前台备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 后台备注
	 */
	@Column(name = "bg_remark")
	private String bgRemark;

	String usdtBankCard;

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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public BigDecimal getMin() {
		return min;
	}

	public void setMin(BigDecimal min) {
		this.min = min;
	}

	public BigDecimal getMax() {
		return max;
	}

	public void setMax(BigDecimal max) {
		this.max = max;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getDepositRate() {
		return depositRate;
	}

	public void setDepositRate(BigDecimal depositRate) {
		this.depositRate = depositRate;
	}

	public BigDecimal getWithdrawRate() {
		return withdrawRate;
	}

	public void setWithdrawRate(BigDecimal withdrawRate) {
		this.withdrawRate = withdrawRate;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPayPlatformCode() {
		return payPlatformCode;
	}

	public void setPayPlatformCode(String payPlatformCode) {
		this.payPlatformCode = payPlatformCode;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Long getOpenUser() {
		return openUser;
	}

	public void setOpenUser(Long openUser) {
		this.openUser = openUser;
	}

	public String getQrCodeImg() {
		return qrCodeImg;
	}

	public void setQrCodeImg(String qrCodeImg) {
		this.qrCodeImg = qrCodeImg;
	}

	public Integer getAliQrcodeStatus() {
		return aliQrcodeStatus;
	}

	public void setAliQrcodeStatus(Integer aliQrcodeStatus) {
		this.aliQrcodeStatus = aliQrcodeStatus;
	}

	public String getAliQrcodeLink() {
		return aliQrcodeLink;
	}

	public void setAliQrcodeLink(String aliQrcodeLink) {
		this.aliQrcodeLink = aliQrcodeLink;
	}

	public String getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(String cardIndex) {
		this.cardIndex = cardIndex;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBgRemark() {
		return bgRemark;
	}

	public void setBgRemark(String bgRemark) {
		this.bgRemark = bgRemark;
	}

	public String getUsdtBankCard() {
		return usdtBankCard;
	}

	public void setUsdtBankCard(String usdtBankCard) {
		this.usdtBankCard = usdtBankCard;
	}
}
