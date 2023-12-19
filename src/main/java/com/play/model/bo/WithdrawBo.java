package com.play.model.bo;

public class WithdrawBo {
    Long bankId;
    String money;
    String repPwd;

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRepPwd() {
        return repPwd;
    }

    public void setRepPwd(String repPwd) {
        this.repPwd = repPwd;
    }
}
