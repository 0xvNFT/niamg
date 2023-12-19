package com.play.common.utils;

import com.play.core.CurrencyEnum;

import java.math.BigDecimal;

public class CurrencyUtils {

    /**
     * 从第三方转入本系统汇率
     * @param currency
     * @return
     */
    public static BigDecimal getTransInRate(String currency) {
        CurrencyEnum ce = CurrencyEnum.getCurrency(currency);
        if (ce != null) {
            // 暂时代码中固定汇率，后期可改为后台配置方式
            if (ce == CurrencyEnum.VND) {
                return new BigDecimal(1000);
            }
        }
        return new BigDecimal(1);
    }

    /**
     * 从本系统转到地方汇率
     * @param currency
     * @return
     */
    public static BigDecimal getTransOutRate(String currency) {
        CurrencyEnum ce = CurrencyEnum.getCurrency(currency);
        if (ce != null) {
            // 暂时代码中固定汇率，后期可改为后台配置方式
            if (ce == CurrencyEnum.VND) {
                return new BigDecimal(0.001);
            }
        }
        return new BigDecimal(1);
    }
}
