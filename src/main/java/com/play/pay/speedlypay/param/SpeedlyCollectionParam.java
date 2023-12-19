package com.play.pay.speedlypay.param;

import java.math.BigDecimal;

public class SpeedlyCollectionParam extends SpeedlyBaseParam {

    /**
     * 支付方式
     * 必填
     */
    private String payment_method_id;

    /**
     * 支付流程
     * 必填
     */
    private String payment_method_flow;

    /**
     * 回调通知地址。支付成功后，向商户发送成功的回调通知
     */
    private String notification_url;

    /**
     * 支付成功页面跳转链接
     */
    private String success_redirect_url	;

    /**
     * 附加数据，回调时原数据返回
     */
    private String extend;

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

    public String getNotification_url() {
        return notification_url;
    }

    public void setNotification_url(String notification_url) {
        this.notification_url = notification_url;
    }

    public String getSuccess_redirect_url() {
        return success_redirect_url;
    }

    public void setSuccess_redirect_url(String success_redirect_url) {
        this.success_redirect_url = success_redirect_url;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
