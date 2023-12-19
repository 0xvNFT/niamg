package com.play.pay.yespay.param;

/**
 * YesPay代收 参数实体
 */
public class YesCollectionParam extends YesBaseParam {
    /**
     * 商户订单类型 zalo、momo
     */
    private String type;

    /**
     * 订单信息通知地址
     */
    private String notifyurl;

    /**
     * 前端跳转地址
     */
    private String returnurl;

    /**
     * 商户备注
     */
    private String note;

    /**
     * 订单其他数据
     */
    private String payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl;
    }

    public String getReturnurl() {
        return returnurl;
    }

    public void setReturnurl(String returnurl) {
        this.returnurl = returnurl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
