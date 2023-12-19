package com.play.pay.speedlypay.param;

public class SpeedlyPayParam extends SpeedlyBaseParam {

    /**
     * 回调通知地址。支付成功后，向商户发送成功的回调通知
     */
    private String notification_url;

    public String getNotification_url() {
        return notification_url;
    }

    public void setNotification_url(String notification_url) {
        this.notification_url = notification_url;
    }

}
