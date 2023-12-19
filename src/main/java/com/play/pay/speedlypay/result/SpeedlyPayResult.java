package com.play.pay.speedlypay.result;

public class SpeedlyPayResult extends SpeedlyBaseResult {

    /**
     * 平台代付订单唯一号
     * 必填
     */
    private String settlement_id;

    public String getSettlement_id() {
        return settlement_id;
    }

    public void setSettlement_id(String settlement_id) {
        this.settlement_id = settlement_id;
    }
}
