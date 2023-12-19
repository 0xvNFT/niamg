package com.play.pay.baxiowenpay.params;

import com.alibaba.fastjson.JSONObject;

public class OwenFormValue implements OwenpayParamBase {

    private OwenpayParamCollect ApplyParams;

    public OwenpayParamCollect getApplyParams() {
        return ApplyParams;
    }

    public void setApplyParams(OwenpayParamCollect applyParams) {
        ApplyParams = applyParams;
    }

    public void setSign(String sign) {

    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
    public JSONObject toMap() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }
}
