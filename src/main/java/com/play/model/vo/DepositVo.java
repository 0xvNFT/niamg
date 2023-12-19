package com.play.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值记录查询类
 */
public class DepositVo {

    /**
     * 开始时间 str
     */
    private String startTime;

    /**
     * 结束时间 str
     */
    private String endTime;

    /**
     * 开始时间
     */
    private Date begin;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 充值类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 充值方式ID
     */
    private Long payId;

    /**
     * 处理方式
     */
    private Integer handlerType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 代理名称
     */
    private String proxyName;

    /**
     * 附言码
     */
    private String withCode;

    /**
     * 会员等级Id
     */
    private Long userDegreeId;

    /**
     * 会员等级Ids
     */
    private String degreeIds;

    /**
     *
     */
    private String onlineCode;

    /**
     * 最小金额
     */
    private BigDecimal minMoney;

    /**
     * 最大金额
     */
    private BigDecimal maxMoney;

    /**
     * 操作员名称
     */
    private String opUsername;

    /**
     * 订单锁定状态
     */
    private Integer lockStatus;

    /**
     * 代理Id
     */
    private Long proxyId;

    /**
     * 站点ID
     */
    private Long stationId;

    /**
     * 排序方式
     */
    private boolean depositSort;

    /**
     * 充值次数，1首充，2：二次充值
     */
    private Integer depositNum;

    /**
     * 在线支付收银台名称
     */
    private String payBankName;

    /**
     * 充值方式IDs
     */
    private String payIds;

    /**
     * 是否只查询未处理和处理中订单
     */
    private Integer checkOrderStatus;

    /**
     * 后台备注
     */
    private String bgRemark;

    String registerTime;
    String registerTimeEnd;
    Date registerTimeD;
    Date registerTimeEndD;
    
    /**
     * 会员Id
     */
    private Long userId;

    private String isDeposit;

    /**
     * 所属代理商
     */
    private String agentUser;

    /**
     * 推荐人
     */
    private String referrer;
    /**
     * 推荐人id
     */
    private Long recommendId;

    public String getAgentUser() {
        return agentUser;
    }

    public void setAgentUser(String agentUser) {
        this.agentUser = agentUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getRegisterTimeD() {
		return registerTimeD;
	}

	public void setRegisterTimeD(Date registerTimeD) {
		this.registerTimeD = registerTimeD;
	}

	public Date getRegisterTimeEndD() {
		return registerTimeEndD;
	}

	public void setRegisterTimeEndD(Date registerTimeEndD) {
		this.registerTimeEndD = registerTimeEndD;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

    public String getRegisterTimeEnd() {
        return registerTimeEnd;
    }

    public void setRegisterTimeEnd(String registerTimeEnd) {
        this.registerTimeEnd = registerTimeEnd;
    }

    public String getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(String degreeIds) {
        this.degreeIds = degreeIds;
    }

    public Integer getDepositNum() {
        return depositNum;
    }

    public void setDepositNum(Integer depositNum) {
        this.depositNum = depositNum;
    }

    public boolean isDepositSort() {
        return depositSort;
    }

    public void setDepositSort(boolean depositSort) {
        this.depositSort = depositSort;
    }

    public Integer getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Integer getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(Integer handlerType) {
        this.handlerType = handlerType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public String getWithCode() {
        return withCode;
    }

    public void setWithCode(String withCode) {
        this.withCode = withCode;
    }

    public Long getUserDegreeId() {
        return userDegreeId;
    }

    public void setUserDegreeId(Long userDegreeId) {
        this.userDegreeId = userDegreeId;
    }

    public String getOnlineCode() {
        return onlineCode;
    }

    public void setOnlineCode(String onlineCode) {
        this.onlineCode = onlineCode;
    }

    public BigDecimal getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }

    public BigDecimal getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getOpUsername() {
        return opUsername;
    }

    public void setOpUsername(String opUsername) {
        this.opUsername = opUsername;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public String getPayIds() {
        return payIds;
    }

    public void setPayIds(String payIds) {
        this.payIds = payIds;
    }

    public String getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(String isDeposit) {
        this.isDeposit = isDeposit;
    }

    public Integer getCheckOrderStatus() {
        return checkOrderStatus;
    }

    public void setCheckOrderStatus(Integer checkOrderStatus) {
        this.checkOrderStatus = checkOrderStatus;
    }

    public String getBgRemark() {
        return bgRemark;
    }

    public void setBgRemark(String bgRemark) {
        this.bgRemark = bgRemark;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }
}
