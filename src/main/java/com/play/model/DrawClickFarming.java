package com.play.model;


import com.play.core.WithdrawRuleEnum;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
import com.play.spring.config.i18n.I18nTool;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提款记录刷单详情表
 */
@Table(name = "draw_click_farming")
public class DrawClickFarming {

    //提款订单表ID
    @Column(name = "order_id", primarykey = true, generator = Column.PK_BY_HAND)
    private String orderId;

    //刷单类型
    @Column(name = "brush_type")
    private Integer brushType;

    //刷单类型名称
    @Column(name = "brush_name")
    private String brushName;

    //规则设定值
    @Column(name = "standard")
    private BigDecimal standard;

    //开始统计时间
    @Column(name = "start_time")
    private Date startTime;

    //结束统计时间
    @Column(name = "end_time")
    private Date endTime;

    //时间范围内总充值
    @Column(name = "total_deposit")
    private BigDecimal totalDeposit;

    //时间范围内总提款
    @Column(name = "total_draw")
    private BigDecimal totalDraw;

    //时间范围内总差值
    @Column(name = "diff")
    private BigDecimal diff;

    //单次提款
    @Column(name = "single_draw")
    private BigDecimal singleDraw;

    //关联IP
    @Column(name = "draw_ip")
    private String drawIp;

    //关联操作系统
    @Column(name = "draw_os")
    private String drawOs;

    //关联数量
    @Column(name = "relation_count")
    private Integer relationCount;

    //充值次数
    @Column(name = "deposit_times")
    private Integer depositTimes;

    //三方盈利总额;代理返点、邀请注册返佣、邀请充值返佣总额
    @Column(name = "totalMoney")
    private BigDecimal totalMoney;

    //邀请会员的数量
    @Column(name = "invite_user_count")
    private Integer inviteUserCount;

    //邀请会员的充值人数
    @Column(name = "deposit_count")
    private Integer depositCount;

    //邀请会员的充值人数/邀请会员的数量
    @Column(name = "deposit_rate")
    private BigDecimal depositRate;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getBrushType() {
        return brushType;
    }

    public void setBrushType(Integer brushType) {
        this.brushType = brushType;
    }

    public String getBrushName() {
        return brushName;
    }

    public void setBrushName(String brushName) {
        this.brushName = brushName;
    }

    public BigDecimal getStandard() {
        return standard;
    }

    public void setStandard(BigDecimal standard) {
        this.standard = standard;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public BigDecimal getTotalDraw() {
        return totalDraw;
    }

    public void setTotalDraw(BigDecimal totalDraw) {
        this.totalDraw = totalDraw;
    }

    public BigDecimal getDiff() {
        return diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }

    public BigDecimal getSingleDraw() {
        return singleDraw;
    }

    public void setSingleDraw(BigDecimal singleDraw) {
        this.singleDraw = singleDraw;
    }

    public String getDrawIp() {
        return drawIp;
    }

    public void setDrawIp(String drawIp) {
        this.drawIp = drawIp;
    }

    public String getDrawOs() {
        return drawOs;
    }

    public void setDrawOs(String drawOs) {
        this.drawOs = drawOs;
    }

    public Integer getRelationCount() {
        return relationCount;
    }

    public void setRelationCount(Integer relationCount) {
        this.relationCount = relationCount;
    }

    public Integer getDepositTimes() {
        return depositTimes;
    }

    public void setDepositTimes(Integer depositTimes) {
        this.depositTimes = depositTimes;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getInviteUserCount() {
        return inviteUserCount;
    }

    public void setInviteUserCount(Integer inviteUserCount) {
        this.inviteUserCount = inviteUserCount;
    }

    public Integer getDepositCount() {
        return depositCount;
    }

    public void setDepositCount(Integer depositCount) {
        this.depositCount = depositCount;
    }

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public String getRuleName(Integer type) {
        WithdrawRuleEnum[] withdrawRuleEnums = WithdrawRuleEnum.values();
        String name = "";
        for(int i = 0, n = withdrawRuleEnums.length ; i < n ; i++) {
            if(type.compareTo(withdrawRuleEnums[i].getType()) == 0) {
                name = I18nTool.getMessage("WithdrawRuleEnum." + withdrawRuleEnums[i].name(), withdrawRuleEnums[i].getRuleName());
            }
        }
        return name;
    }
}
