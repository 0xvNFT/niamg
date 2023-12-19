package com.play.pay.speedlypay.param;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpeedlyBaseParam implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商户号
     * 必填
     */
    private String merchant_no;

    /**
     * 国家代码，如巴西为BR
     * 必填
     */
    private String country;

    /**
     * 货币代码，如巴西为BRL
     * 必填
     */
    private String currency;

    /**
     * 商户订单编号
     * 必填
     */
    private String order_id;

    /**
     * 固定2位小数点的浮点数金额，如：10.00
     */
    private BigDecimal amount;

    /**
     * 时间戳 毫秒级UTC时间戳（13位）
     */
    private Long timestamp;

    /**
     * MD5签名，32位大写字母
     * 必填
     */
    private String signature;

    public JSONObject toMap() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
