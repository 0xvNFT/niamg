package com.play.pay.speedlypay.param;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class SpeedlyPayerOrPayeeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 付款方名字 或 收款方名字
     * 必填
     */
    private String name;

    /**
     * 付款方账号 或 收款方账号
     * 必填
     */
    private String account;

    /**
     * 账户类型
     * 巴西 PIX (CPF PHONE EMAIL CHAVE)
     */
    private String account_type;

    /**
     * 付款方身份ID 或 收款方个人公民身份ID
     *
     */
    private String document;

    /**
     * 电话号码
     *
     */
    private String phone;

    /**
     * 邮箱
     *
     */
    private String email;

    public JSONObject toMap() {
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
