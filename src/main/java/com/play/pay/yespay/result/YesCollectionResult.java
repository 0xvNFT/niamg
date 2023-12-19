package com.play.pay.yespay.result;

import java.math.BigDecimal;

/**
 * YesPay代收 结果实体
 */
public class YesCollectionResult extends YesBaseResult {
    /**
     * 订单访问票据或标识
     */
    private String ticket;

    /**
     * 商户已经直接在前台跳转到该地址
     */
    private String pageurl;

    /**
     * 商户订单类型 zalo、momo
     */
    private String type;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 尾部金额
     */
    private BigDecimal bamount;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getPageurl() {
        return pageurl;
    }

    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBamount() {
        return bamount;
    }

    public void setBamount(BigDecimal bamount) {
        this.bamount = bamount;
    }
}
