package com.play.pay.speedlypay.result;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpeedlyBalanceResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 可用余额，固定2位小数点的浮点数金额，如：10.00
     */
    private BigDecimal available_balance;

    /**
     * 冻结金额，固定2位小数点的浮点数金额，如：10.00
     */
    private BigDecimal freeze_balance;

    /**
     * 商户总余额(可⽤余额+冻结⾦额)，固定2位小数点的浮点数金额，如：10.00
     */
    private BigDecimal total_balance;

    /**
     * MD5签名，32位大写字母
     * 必填
     */
    private String signature;

    public BigDecimal getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(BigDecimal available_balance) {
        this.available_balance = available_balance;
    }

    public BigDecimal getFreeze_balance() {
        return freeze_balance;
    }

    public void setFreeze_balance(BigDecimal freeze_balance) {
        this.freeze_balance = freeze_balance;
    }

    public BigDecimal getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(BigDecimal total_balance) {
        this.total_balance = total_balance;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
