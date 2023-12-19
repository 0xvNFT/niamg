package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代付出款设置
 */
@Table(name = "station_replace_withdraw")
public class StationReplaceWithDraw {
    /**
     * 禁用
     */
    public static int STATUS_DISABLED = 1;

    /**
     * 启用
     */
    public static int STATUS_ENABLE = 2;


    @Column(name = "id", primarykey = true)
    private Long id;

    /**
     * 商户号
     */
    @Column(name = "merchant_code")
    private String merchantCode;

    @Column(name = "merchant_key")
    private String merchantKey;

    @Column(name = "url")
    private String url;

    @Column(name = "min")
    private BigDecimal min;

    @Column(name = "max")
    private BigDecimal max;

    @Column(name = "account")
    private String account;

    @Column(name = "def")
    private Integer def;

    @Column(name = "status")
    private Integer status;

    @Column(name = "version")
    private Integer version;

    @Column(name = "station_id")
    private Long stationId;

    @Column(name = "pay_platform_id")
    private Integer payPlatformId;

    @Column(name = "icon")
    private String icon;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "pay_getway")
    private String payGetway;

    @Column(name = "level_group")
    private String levelGroup;

    @Column(name = "appid")
    private String appid;

    @Column(name = "sort_no")
    private Integer sortNo;

    @Column(name = "show_type")
    private String showType;

    @Column(name = "recharge_type")
    private String rechargeType;

    @Column(name = "fixed_amount")
    private String fixedAmount;

    @Column(name = "extra")
    private String extra;

    @Column(name = "pay_status")
    private String payStatus;

    @Column(name = "search_getway")
    private String searchGetway;

    @Column(name = "white_list_ip")
    private String whiteListIp;

    @Column(name = "update_key_time")
    private Date updateKeyTime;

    @Column(name = "remark")
    private String remark;

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
     * 支付公司名称
     */
    private String payCom;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Integer getPayPlatformId() {
        return payPlatformId;
    }

    public void setPayPlatformId(Integer payPlatformId) {
        this.payPlatformId = payPlatformId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getLevelGroup() {
        return levelGroup;
    }

    public void setLevelGroup(String levelGroup) {
        this.levelGroup = levelGroup;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayCom() {
        return payCom;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setPayCom(String payCom) {
        this.payCom = payCom;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getSearchGetway() {
        return searchGetway;
    }

    public void setSearchGetway(String searchGetway) {
        this.searchGetway = searchGetway;
    }

    public String getWhiteListIp() {
        return whiteListIp;
    }

    public void setWhiteListIp(String whiteListIp) {
        this.whiteListIp = whiteListIp;
    }

    public Date getUpdateKeyTime() {
        return updateKeyTime;
    }

    public void setUpdateKeyTime(Date updateKeyTime) {
        this.updateKeyTime = updateKeyTime;
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

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }
}
