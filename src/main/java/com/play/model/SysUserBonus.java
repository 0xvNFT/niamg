package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.InsertValue;
import com.play.orm.jdbc.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员奖金表结构
 */
@Table(name = "sys_user_bonus")
public class SysUserBonus {

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

    /**
     * 上级代理
     */
    @Column(name = "proxy_id")
    private Long proxyId;
    /**
     * 上级代理账号
     */
    @Column(name = "proxy_name")
    private String proxyName;

    /**
     * 多级代理层级id，使用逗号分开
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 推荐者id
     */
    @Column(name = "recommend_id")
    private Long recommendId;

    /**
     * 帐变记录id
     */
    @Column(name = "daily_money_id")
    private Long dailyMoneyId;
    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 对应的记录id
     */
    @Column(name = "record_id")
    private Long recordId;
    /**
     * 用户账号
     */
    @Column(name = "username")
    private String username;
    /**
     * 用户类型
     */
    @Column(name = "user_type")
    private Integer userType;


    /**
     * 奖金类型
     */
    @Column(name = "bonus_type")
    private Integer bonusType;

    @Column(name = "create_datetime")
    private Date createDatetime;
    /**
     * 奖金金额
     */
    @InsertValue(value = "0")
    @Column(name = "money")
    private BigDecimal money;

    private BigDecimal thirdBetAmount;//三方总打码金额

    private BigDecimal depositMoney;//存款金额，只针对充值赠送类型

    private String bonusTypeText;//奖金类型文字

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

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Long getDailyMoneyId() {
        return dailyMoneyId;
    }

    public void setDailyMoneyId(Long dailyMoneyId) {
        this.dailyMoneyId = dailyMoneyId;
    }

    public Integer getBonusType() {
        return bonusType;
    }

    public void setBonusType(Integer bonusType) {
        this.bonusType = bonusType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    public BigDecimal getThirdBetAmount() {
        return thirdBetAmount;
    }

    public void setThirdBetAmount(BigDecimal thirdBetAmount) {
        this.thirdBetAmount = thirdBetAmount;
    }

    public String getBonusTypeText() {
        return bonusTypeText;
    }

    public void setBonusTypeText(String bonusTypeText) {
        this.bonusTypeText = bonusTypeText;
    }
}
