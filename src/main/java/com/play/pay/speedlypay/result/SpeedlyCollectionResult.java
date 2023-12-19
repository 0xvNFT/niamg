package com.play.pay.speedlypay.result;

import com.play.pay.yespay.result.YesBaseResult;

import java.math.BigDecimal;

public class SpeedlyCollectionResult extends SpeedlyBaseResult {

    /**
     * 支付单号
     */
    private String payment_id;

    /**
     * 支付方式
     */
    private String payment_method_id;

    /**
     * 支付流程
     */
    private String payment_method_flow;

    /**
     * 收银台支付链接
     */
    private String redirect_url;

    /**
     * 回调通知地址。支付成功后，向商户发送成功的回调通知
     */
    private String notification_url	;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getPayment_method_flow() {
        return payment_method_flow;
    }

    public void setPayment_method_flow(String payment_method_flow) {
        this.payment_method_flow = payment_method_flow;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getNotification_url() {
        return notification_url;
    }

    public void setNotification_url(String notification_url) {
        this.notification_url = notification_url;
    }
}
