package com.play.pay.yespay.result;

import java.math.BigDecimal;

/**
 * YesPay查询订单结果实体
 */
public class YesOrderResult extends YesBaseResult {

    /**
     * 访问票据
     */
    private String ticket;

    /**
     * 是否支付  0没有支付  1已经支付
     */
    private Integer ispay;

    /**
     * 支付代码
     */
    private String paycode;

    /**
     * 支付金额
     */
    private BigDecimal payamount;

    /**
     * 支付时间
     */
    private String paytime;

    /**
     * 支付用户
     */
    private String payuser;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String note;

    /**
     * 支付类型
     */
    private String type;

    /**
     * 支付备注
     */
    private Integer serialno;

    /**
     * 商户已经直接在前台跳转到该地址
     */
    private String pageurl;

    /**
     * 是否取消  0没有取消  1已经取消
     */
    private Integer iscancel;

    /**
     * 订单类型  1支付充值订单  2代付提现订单
     * 三方支付那边目前没有用到该字段
     */
    private Integer ordertype;

    /**
     * 订单取消原因
     */
    private String mark;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getIspay() {
        return ispay;
    }

    public void setIspay(Integer ispay) {
        this.ispay = ispay;
    }

    public String getPaycode() {
        return paycode;
    }

    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

    public BigDecimal getPayamount() {
        return payamount;
    }

    public void setPayamount(BigDecimal payamount) {
        this.payamount = payamount;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPayuser() {
        return payuser;
    }

    public void setPayuser(String payuser) {
        this.payuser = payuser;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSerialno() {
        return serialno;
    }

    public void setSerialno(Integer serialno) {
        this.serialno = serialno;
    }

    public String getPageurl() {
        return pageurl;
    }

    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    public Integer getIscancel() {
        return iscancel;
    }

    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    public Integer getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(Integer ordertype) {
        this.ordertype = ordertype;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
