package com.play.pay.baxitopay.params;

import com.alibaba.fastjson.JSONObject;

public class TopayParamQueryBalance implements ITopayParamBase {
    /**
     * 商户号
     * <p>
     * 必填
     */
    private String merchant_no;
    /**
     * 时间戳
     * <p>
     * 必填
     */
    private String timestamp;
    /**
     * 签名
     * <p>
     * 必填
     */
    private String sign;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
