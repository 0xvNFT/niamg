package com.play.pay.yespay.result;

import me.zhyd.oauth.log.Log;

import java.math.BigDecimal;

/**
 * YesPay查询余额结果实体
 */
public class YesBalanceResult extends YesBaseResult {

    /**
     * 当前余额
     */
    private BigDecimal balance;

    /**
     * 代付锁定金额
     */
    private BigDecimal lockedamount;

    /**
     * 冻结金额
     */
    private BigDecimal frozenamount;

    /**
     * 提现锁定金额
     */
    private BigDecimal onwayamount;

    /**
     * 时间戳
     */
    private Long timestamp;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getLockedamount() {
        return lockedamount;
    }

    public void setLockedamount(BigDecimal lockedamount) {
        this.lockedamount = lockedamount;
    }

    public BigDecimal getFrozenamount() {
        return frozenamount;
    }

    public void setFrozenamount(BigDecimal frozenamount) {
        this.frozenamount = frozenamount;
    }

    public BigDecimal getOnwayamount() {
        return onwayamount;
    }

    public void setOnwayamount(BigDecimal onwayamount) {
        this.onwayamount = onwayamount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
