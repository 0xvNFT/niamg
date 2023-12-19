package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 用户充值记录
 *
 * @author admin
 *
 */
@Table(name = "mny_deposit_record")
public class MnyDepositRecord {

	public static final int STATUS_UNDO = 1;// 未处理
	public static final int STATUS_SUCCESS = 2;// 支付成功
	public static final int STATUS_FAIL = 3; // 处理失败
	public static final int STATUS_EXPIRE = 4; // 已过期

	public static final int LOCK_FLAG_LOCK = 2;// 锁定
	public static final int LOCK_FLAG_UNLOCK = 1;// 未锁定

	public static final int HANDLE_TYPE_HAND = 1;// 手动处理
	public static final int HANDLE_TYPE_SYS = 2;// 系统处理

	public static final String PAY_CODE_ONLINE = "online";// 在线支付
	public static final String PAY_CODE_BANK = "bank";// 银行付款

	public static final int TYPE_ONLINE = 1;// 在线入款
	public static final int TYPE_FAST = 2;// 快速入款
	public static final int TYPE_BANK = 3;// 银行转账
	public static final int TYPE_HAND = 4;// 手动加款

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商ID
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 代理商名称
	 */
	@Column(name = "agent_name")
	private String agentName;
	/**
	 * 代理层级关系
	 */
	@Column(name = "parent_ids")
	private String parentIds;
	/**
	 * 推荐者id
	 */
	@Column(name = "recommend_id")
	private Long recommendId;
	/**
	 * 会员ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 会员层级ID
	 */
	@Column(name = "degree_id")
	private Long degreeId;
	/**
	 * 用户类型(UserTypeEnum的值)
	 */
	@Column(name = "user_type")
	private Integer userType;
	/**
	 * 订单号
	 */
	@Column(name = "order_id")
	private String orderId;
	/**
	 * 存入金额
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 赠送彩金，优惠彩金
	 */
	@Column(name = "gift_money")
	private BigDecimal giftMoney;
	/**
	 * 创建人
	 */
	@Column(name = "create_user_id")
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 状态，1=未处理，2=处理成功，3=处理失败，4=已取消，5=已过期
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 支付类型，1=在线支付，2=快速入款，3=银行转账，4=手动加款
	 */
	@Column(name = "deposit_type")
	private Integer depositType;
	/**
	 * 汇款人姓名
	 */
	@Column(name = "depositor")
	private String depositor;
	/**
	 * 锁定（1未锁，2锁定）
	 */
	@Column(name = "lock_flag")
	private Integer lockFlag;
	/**
	 * 支付方式ID
	 */
	@Column(name = "pay_id")
	private Long payId;
	/**
	 * 支付方式名字
	 */
	@Column(name = "pay_name")
	private String payName;
	/**
	 * 银行转账方式 1:网银转账，2 ATM入款 3：银行柜台 4：手机转账 5：支付宝
	 */
	@Column(name = "bank_way")
	private Integer bankWay;
	/**
	 * 银行所属分行
	 */
	@Column(name = "bank_address")
	private String bankAddress;
	/**
	 * 在线支付收银台支付名称
	 */
	@Column(name = "pay_bank_name")
	private String payBankName;

	@Column(name = "pay_platform_code")
	private String payPlatformCode;
	/**
	 * 处理方式（1、手动处理,2、系统处理）
	 */
	@Column(name = "handler_type")
	private Integer handlerType;
	/**
	 * 操作人账号
	 */
	@Column(name = "op_username")
	private String opUsername;
	/**
	 * 操作人ID
	 */
	@Column(name = "op_user_id")
	private Long opUserId;
	/**
	 * 操作时间
	 */
	@Column(name = "op_datetime")
	private Date opDatetime;
	/**
	 * 用户前台入款时备注
	 */
	@Column(name = "user_remark")
	private String userRemark;
	/**
	 * 前台可以看到的备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 只有后台可见的备注
	 */
	@Column(name = "bg_remark")
	private String bgRemark;

	/**
	 * 存款虚拟币数量
	 */
	@Column(name = "deposit_virtual_coin_num")
	private BigDecimal depositVirtualCoinNum;

	/**
	 * 存款虚拟币汇率
	 */
	@Column(name = "deposit_virtual_coin_rate")
	private BigDecimal depositVirtualCoinRate;

	/**
	 * 会员等级
	 */
	private String degreeName;

	private Integer paytimes;

	// 会员姓名
	private String realName;

	private BigDecimal drawNeed;

	private String proxyName;

	private boolean isFirst;

	private boolean isWarning;

	/**
	 * 入款方式开户人姓名
	 */
	private String creatorName;

	/**
	 * tronScan链接地址，用于拼接订单号，跳转到波场查看订单转账信息
	 */
	private String tronScanUrl;

	/**
	 * 是否为USDT存款
	 */
	private boolean isUsdtDeposit;

	/**
	 * 站点语言
	 */
	private String stationLanguage;

	public String getStationLanguage() {
		return stationLanguage;
	}

	public void setStationLanguage(String stationLanguage) {
		this.stationLanguage = stationLanguage;
	}

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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
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

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(BigDecimal giftMoney) {
		this.giftMoney = giftMoney;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDepositType() {
		return depositType;
	}

	public void setDepositType(Integer depositType) {
		this.depositType = depositType;
	}

	public String getDepositor() {
		return depositor;
	}

	public void setDepositor(String depositor) {
		this.depositor = depositor;
	}

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public Integer getBankWay() {
		return bankWay;
	}

	public void setBankWay(Integer bankWay) {
		this.bankWay = bankWay;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getPayBankName() {
		return payBankName;
	}

	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public String getPayPlatformCode() {
		return payPlatformCode;
	}

	public void setPayPlatformCode(String payPlatformCode) {
		this.payPlatformCode = payPlatformCode;
	}

	public Integer getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(Integer handlerType) {
		this.handlerType = handlerType;
	}

	public String getOpUsername() {
		return opUsername;
	}

	public void setOpUsername(String opUsername) {
		this.opUsername = opUsername;
	}

	public Long getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(Long opUserId) {
		this.opUserId = opUserId;
	}

	public Date getOpDatetime() {
		return opDatetime;
	}

	public void setOpDatetime(Date opDatetime) {
		this.opDatetime = opDatetime;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
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

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public Integer getPaytimes() {
		return paytimes;
	}

	public void setPaytimes(Integer paytimes) {
		this.paytimes = paytimes;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getDrawNeed() {
		return drawNeed;
	}

	public void setDrawNeed(BigDecimal drawNeed) {
		this.drawNeed = drawNeed;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean first) {
		isFirst = first;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean warning) {
		isWarning = warning;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public BigDecimal getDepositVirtualCoinNum() {
		return depositVirtualCoinNum;
	}

	public void setDepositVirtualCoinNum(BigDecimal depositVirtualCoinNum) {
		this.depositVirtualCoinNum = depositVirtualCoinNum;
	}

	public BigDecimal getDepositVirtualCoinRate() {
		return depositVirtualCoinRate;
	}

	public void setDepositVirtualCoinRate(BigDecimal depositVirtualCoinRate) {
		this.depositVirtualCoinRate = depositVirtualCoinRate;
	}

	public String getTronScanUrl() {
		return tronScanUrl;
	}

	public void setTronScanUrl(String tronScanUrl) {
		this.tronScanUrl = tronScanUrl;
	}

	public boolean isUsdtDeposit() {
		return isUsdtDeposit;
	}

	public void setUsdtDeposit(boolean usdtDeposit) {
		isUsdtDeposit = usdtDeposit;
	}
}
