package com.play.model.dto;

import java.math.BigDecimal;

/**
 * @author Leon
 * @create 2023-07-03 21:09
 */
public class TronUSDTDto {

    /**
     * 虚拟币数量
     */
    private BigDecimal num;

    /**
     * 虚拟币汇率
     */
    private BigDecimal rate;

    /**
     * 实际金额
     */
    private BigDecimal exchangeMoney;

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getExchangeMoney() {
        return exchangeMoney;
    }

    public void setExchangeMoney(BigDecimal exchangeMoney) {
        this.exchangeMoney = exchangeMoney;
    }
}
