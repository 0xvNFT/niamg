package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 用户提款记录
 *
 * @author admin
 *
 */
@Table(name = "mny_draw_record")
public class MnyDrawRecord {
	public static final int LOCK_FLAG_LOCK = 2;// 锁定
	public static final int LOCK_FLAG_UNLOCK = 1;// 未锁定

	public static final int STATUS_UNDO = 1;// 未处理
	public static final int STATUS_SUCCESS = 2;// 支付成功
	public static final int STATUS_FAIL = 3; // 处理失败
	public static final int STATUS_EXPIRE = 4; // 已过期

	public static final int UN_CHECK = 1; // 未审核
	public static final int CHECK_SUCCESS = 2; // 审核通过
	public static final int CHECK_FAIL = 3; // 审核未通过

	public static final int NORMAL = 1; // 银行提款
	public static final int REPLCACE = 2; // 代付提款
	public static final int USDT = 3; // USDT提款
	public static final int ALIPAY = 4; // 支付宝提款

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
	 * 操作ip
	 */
	@Column(name = "record_ip")
	private String recordIp;

	/**
	 * 设备操作系统
	 */
	@Column(name = "record_os")
	private String recordOs;
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
	 * 出款金额
	 */
	@Column(name = "draw_money")
	private BigDecimal drawMoney;
	/**
	 * 手续费
	 */
	@Column(name = "fee_money")
	private BigDecimal feeMoney;

	/**
	 * 可疑订单 是否需要二次人工审核
	 */
	@Column(name = "second_review")
	private Integer secondReview;

	/**
	 * 实际出款金额
	 */
	@Column(name = "true_money")
	private BigDecimal trueMoney;
	/**
	 * 状态，1=未处理，2=处理成功，3=处理失败，4=已过期
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建者
	 */
	@Column(name = "create_user_id")
	private Long createUserId;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 锁定（1未锁，2锁定）
	 */
	@Column(name = "lock_flag")
	private Integer lockFlag;
	/**
	 * 账变记录ID
	 */
	@Column(name = "money_record_id")
	private Long moneyRecordId;
	/**
	 * 银行名称
	 */
	@Column(name = "bank_name")
	private String bankName;
	/**
	 * 用户姓名
	 */
	@Column(name = "real_name")
	private String realName;
	/**
	 * 银行卡号
	 */
	@Column(name = "card_no")
	private String cardNo;
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
	 * 提款类型
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 代付id
	 */
	@Column(name = "pay_id")
	private Long payId;

	/**
	 * 代付名
	 */
	@Column(name = "pay_name")
	private String payName;

	/**
	 * 取款虚拟币数量
	 */
	@Column(name = "withdraw_virtual_coin_num")
	private BigDecimal withdrawVirtualCoinNum;

	/**
	 * 取款虚拟币汇率
	 */
	@Column(name = "withdraw_virtual_coin_rate")
	private BigDecimal withdrawVirtualCoinRate;
	/**
	 * 平台订单好
	 */
	@Column(name = "pay_platform_no")
	private String payPlatformNo;
	/**
	 * 会员层级名称
	 * 
	 * @return
	 */
	private String degreeName;

	private String proxyName;

	private boolean isWarning;

	private boolean isFirst;

	private Integer drawTimes;

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

	public BigDecimal getDrawMoney() {
		return drawMoney;
	}

	public void setDrawMoney(BigDecimal drawMoney) {
		this.drawMoney = drawMoney;
	}

	public BigDecimal getFeeMoney() {
		return feeMoney;
	}

	public void setFeeMoney(BigDecimal feeMoney) {
		this.feeMoney = feeMoney;
	}

	public BigDecimal getTrueMoney() {
		return trueMoney;
	}

	public void setTrueMoney(BigDecimal trueMoney) {
		this.trueMoney = trueMoney;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
	}

	public Long getMoneyRecordId() {
		return moneyRecordId;
	}

	public void setMoneyRecordId(Long moneyRecordId) {
		this.moneyRecordId = moneyRecordId;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public boolean isWarning() {
		return isWarning;
	}

	public void setWarning(boolean warning) {
		isWarning = warning;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean first) {
		isFirst = first;
	}

	public Integer getDrawTimes() {
		return drawTimes;
	}

	public void setDrawTimes(Integer drawTimes) {
		this.drawTimes = drawTimes;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public BigDecimal getWithdrawVirtualCoinNum() {
		return withdrawVirtualCoinNum;
	}

	public void setWithdrawVirtualCoinNum(BigDecimal withdrawVirtualCoinNum) {
		this.withdrawVirtualCoinNum = withdrawVirtualCoinNum;
	}

	public BigDecimal getWithdrawVirtualCoinRate() {
		return withdrawVirtualCoinRate;
	}

	public void setWithdrawVirtualCoinRate(BigDecimal withdrawVirtualCoinRate) {
		this.withdrawVirtualCoinRate = withdrawVirtualCoinRate;
	}

	public Integer getSecondReview() {
		return secondReview;
	}

	public void setSecondReview(Integer secondReview) {
		this.secondReview = secondReview;
	}

	public String getRecordIp() {
		return recordIp;
	}

	public void setRecordIp(String recordIp) {
		this.recordIp = recordIp;
	}

	public String getRecordOs() {
		return recordOs;
	}

	public String getPayPlatformNo() {
		return payPlatformNo;
	}

	public void setPayPlatformNo(String payPlatformNo) {
		this.payPlatformNo = payPlatformNo;
	}

	public void setRecordOs(String recordOs) {
		this.recordOs = recordOs;
	}
}
