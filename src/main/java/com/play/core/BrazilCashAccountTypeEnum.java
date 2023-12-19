package com.play.core;

public enum BrazilCashAccountTypeEnum {
    CPF(1),
    PHONE(2),
    EMAIL(3),
    CNPJ(4);
    private Integer type;
    BrazilCashAccountTypeEnum(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
}
