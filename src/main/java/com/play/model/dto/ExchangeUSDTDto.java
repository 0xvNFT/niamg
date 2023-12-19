package com.play.model.dto;

import java.math.BigDecimal;

/**
 * USDT转换汇率
 *
 * @author Leon
 * @create 2023-07-13 15:04
 */
public class ExchangeUSDTDto {

    /**
     * 存款汇率
     */
    private BigDecimal depositRate;

    /**
     * 取款汇率
     */
    private BigDecimal withdrawRate;

    /**
     * 教程链接
     */
    private String teachUrl;

    /**
     * 备注
     */
    private String remark;

    public BigDecimal getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    public BigDecimal getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(BigDecimal withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public String getTeachUrl() {
        return teachUrl;
    }

    public void setTeachUrl(String teachUrl) {
        this.teachUrl = teachUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
