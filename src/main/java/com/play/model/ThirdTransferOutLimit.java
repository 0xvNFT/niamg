package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.math.BigDecimal;

@Table(name = "third_transfer_out_limit")
public class ThirdTransferOutLimit {

    @Column(name = "id", primarykey = true)
    private Long id;

    @Column(name = "platform")
    private Integer platform;
    /**
     * 站点id
     */
    @Column(name = "station_id")
    private Long stationId;
    /**
     * 最小金额
     */
    @Column(name = "min_money")
    private BigDecimal minMoney;
    /**
     * 最大金额
     */
    @Column(name = "max_money")
    private BigDecimal maxMoney;
    /**
     * 限制会员账号
     */
    @Column(name = "limit_accounts")
    private String limitAccounts;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
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

    public String getLimitAccounts() {
        return limitAccounts;
    }

    public void setLimitAccounts(String limitAccounts) {
        this.limitAccounts = limitAccounts;
    }
}
