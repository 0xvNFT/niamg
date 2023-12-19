package com.play.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.math.BigDecimal;

/**
 * 用户提款规则
 * @author admin
 *
 */
@Table(name = "station_draw_rule_strategy")
public class StationDrawRuleStrategy {

    @Column(name = "id", primarykey = true)
    private Long id;
    @JSONField(serialize = false)
    @Column(name = "station_id")
    private Long stationId;
    /**
     * 规则类型 1--一段时间内提现充值金额差 2--提现金额最大值限制 3--关联用户数 4--同IP用户数 5--一段时间内充值次数小于设定值
     * 6--三方游戏总盈利大于设定值 7--邀请用户的充提差减代理佣金大于设定值
     */
    @Column(name = "type")
    private Integer type;
    /**
     * 设定值 统一使用BigDecimal
     */
    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "rate")
    private BigDecimal rate;

    /**
     * 规则适用的天数(多少天内)
     */
    @Column(name = "days")
    private Integer days;

    @Column(name = "remark")
    private String remark;
    /**
     * 会员等级id 多个以,分割
     */
    @Column(name = "degree_ids")
    private String degreeIds;

    /**
     * 会员组别id 多个以,分割
     */
    @Column(name = "group_ids")
    private String groupIds;
    /**
     * 启用状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 限制国家的代码
     */
    @Column(name = "limit_country")
    private String limitCountry;

    private String degreeNames;

    private String groupNames;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDegreeIds() {
        return degreeIds;
    }

    public void setDegreeIds(String degreeIds) {
        this.degreeIds = degreeIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getDegreeNames() {
        return degreeNames;
    }

    public void setDegreeNames(String degreeNames) {
        this.degreeNames = degreeNames;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getLimitCountry() {
        return limitCountry;
    }

    public void setLimitCountry(String limitCountry) {
        this.limitCountry = limitCountry;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

