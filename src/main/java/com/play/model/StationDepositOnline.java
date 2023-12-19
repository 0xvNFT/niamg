package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_deposit_online")
public class StationDepositOnline {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 商户编号
	 */
	@Column(name = "merchant_code")
	private String merchantCode;
	/**
	 * 商户密钥
	 */
	@Column(name = "merchant_key")
	private String merchantKey;
	/**
	 * 接口域名
	 */
	@Column(name = "url")
	private String url;
	/**
	 * 账号
	 */
	@Column(name = "account")
	private String account;
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
	 * 是否默认
	 */
	@Column(name = "def")
	private Long def;
	/**
	 * 状态 
	 */
	@Column(name = "status")
	private Long status;
	/**
	 * 版本
	 */
	@Column(name = "version")
	private Integer version;
	/**
	 * 图标样式
	 */
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "pay_platform_code")
	private String payPlatformCode;
	/**
	 * 支付方式： 1-收银台，2-银行直连，3-单微信，4-单支付宝，5-QQ钱包，6-京东扫码
	 */
	@Column(name = "pay_type")
	private String payType;
	
	@Column(name = "pay_getway")
	private String payGetway;
	/**
	 * 限制会员等级使用，等级ID使用逗号隔开
	 */
	@Column(name = "degree_ids")
	private String degreeIds;

	/**
	 * 有效会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	
	@Column(name = "appid")
	private String appid;
	/**
	 * 排序
	 */
	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 显示类型: all - 所有终端都显示、 pc - pc端显示、 mobile - 手机端显示、 app - app端显示
	 */
	@Column(name = "show_type")
	private String showType;
	/**
	 * 随机加最大额度，单位分
	 */
	@Column(name = "random_add")
	private Integer randomAdd;
	/**
	 * 随机减最大额度，单位分
	 */
	@Column(name = "random_sub")
	private Integer randomSub;
	/**
	 * 是否固定金额
	 */
	@Column(name = "is_fixed_amount")
	private Integer isFixedAmount;
	/**
	 * 固定金额 ，分割
	 */
	@Column(name = "fixed_amount")
	private String fixedAmount;
	/**
	 * 收银台列表
	 */
	@Column(name = "bank_list")
	private String bankList;
	/**
	 * 前台显示别名
	 */
	@Column(name = "pay_alias")
	private String payAlias;
	/**
	 * 备选参数
	 */
	@Column(name = "alter_native")
	private String alterNative;
	/**
	 * 白名单Ip
	 */
	@Column(name = "white_list_ip")
	private String whiteListIp;
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
	
	@Column(name = "deposit_daily_start_time")
	private Date depositDailyStartTime;
	
	@Column(name = "deposit_daily_end_time")
	private Date depositDailyEndTime;
	
	@Column(name = "query_gateway")
	private String queryGateway;
	/**
	 * PC备注
	 */
	@Column(name = "pc_remark")
	private String pcRemark;
	/**
	 * WAP备注
	 */
	@Column(name = "wap_remark")
	private String wapRemark;
	/**
	 * APP备注
	 */
	@Column(name = "app_remark")
	private String appRemark;
	/**
	 * 后台备注
	 */
	@Column(name = "bg_remark")
	private String bgRemark;
	/**
	 * 系统
	 */
	@Column(name = "system_type")
	private Integer systemType;

	/**
	 * 支付名称
	 */
	private String payName;

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

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public Long getDef() {
		return def;
	}

	public void setDef(Long def) {
		this.def = def;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayGetway() {
		return payGetway;
	}

	public void setPayGetway(String payGetway) {
		this.payGetway = payGetway;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Integer getRandomAdd() {
		return randomAdd;
	}

	public void setRandomAdd(Integer randomAdd) {
		this.randomAdd = randomAdd;
	}

	public Integer getRandomSub() {
		return randomSub;
	}

	public void setRandomSub(Integer randomSub) {
		this.randomSub = randomSub;
	}

	public Integer getIsFixedAmount() {
		return isFixedAmount;
	}

	public void setIsFixedAmount(Integer isFixedAmount) {
		this.isFixedAmount = isFixedAmount;
	}

	public String getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(String fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public String getBankList() {
		return bankList;
	}

	public void setBankList(String bankList) {
		this.bankList = bankList;
	}

	public String getPayAlias() {
		return payAlias;
	}

	public void setPayAlias(String payAlias) {
		this.payAlias = payAlias;
	}

	public String getAlterNative() {
		return alterNative;
	}

	public void setAlterNative(String alterNative) {
		this.alterNative = alterNative;
	}

	public String getWhiteListIp() {
		return whiteListIp;
	}

	public void setWhiteListIp(String whiteListIp) {
		this.whiteListIp = whiteListIp;
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

	public Date getDepositDailyStartTime() {
		return depositDailyStartTime;
	}

	public void setDepositDailyStartTime(Date depositDailyStartTime) {
		this.depositDailyStartTime = depositDailyStartTime;
	}

	public Date getDepositDailyEndTime() {
		return depositDailyEndTime;
	}

	public void setDepositDailyEndTime(Date depositDailyEndTime) {
		this.depositDailyEndTime = depositDailyEndTime;
	}

	public String getQueryGateway() {
		return queryGateway;
	}

	public void setQueryGateway(String queryGateway) {
		this.queryGateway = queryGateway;
	}

	public String getPcRemark() {
		return pcRemark;
	}

	public void setPcRemark(String pcRemark) {
		this.pcRemark = pcRemark;
	}

	public String getWapRemark() {
		return wapRemark;
	}

	public void setWapRemark(String wapRemark) {
		this.wapRemark = wapRemark;
	}

	public String getAppRemark() {
		return appRemark;
	}

	public void setAppRemark(String appRemark) {
		this.appRemark = appRemark;
	}

	public String getBgRemark() {
		return bgRemark;
	}

	public void setBgRemark(String bgRemark) {
		this.bgRemark = bgRemark;
	}

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}
}
