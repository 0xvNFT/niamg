package com.play.core;

/**
 * 巴西PIX账号类型
 * 
 * PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
 */
public enum BrazilPixAccountTypeEnum {
	CPF(1),
	CNPJ(2),
	EMAIL(3),
	PHONE(4),
	EVP(5),
    ;

    private Integer type;

    BrazilPixAccountTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
