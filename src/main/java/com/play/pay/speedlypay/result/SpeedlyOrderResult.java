package com.play.pay.speedlypay.result;

public class SpeedlyOrderResult extends SpeedlyBaseResult {

    /**
     * 平台代付订单唯一号
     *
     */
    private String payment_id;

    /**
     * 平台代付订单号（唯一ID）
     *
     */
    private String settlement_id;

    /**
     * 附加数据
     *
     */
    private String extend;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getSettlement_id() {
        return settlement_id;
    }

    public void setSettlement_id(String settlement_id) {
        this.settlement_id = settlement_id;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
