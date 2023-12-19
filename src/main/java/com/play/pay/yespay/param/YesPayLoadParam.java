package com.play.pay.yespay.param;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 代付的其他信息
 */
public class YesPayLoadParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 收款人姓名
     */
    private String cardname;

    /**
     * 收款卡号
     */
    private String cardno;

    /**
     * 银行编号
     */
    private String bankid;

    /**
     * 银行名称
     */
    private String bankname;

    /**
     * 银行所在省（非必传）
     */
    private String province;

    /**
     * 银行所在市（非必传）
     */
    private String city;

    /**
     * 支行名称（非必传）
     */
    private String branchname;

    /**
     * IFSC CODE（用于印度）
     */
    private String ifsc;

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
}
