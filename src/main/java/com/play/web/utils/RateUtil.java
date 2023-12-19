package com.play.web.utils;

import com.play.common.utils.BigDecimalUtil;
import com.play.model.bo.MnyMoneyBo;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18ErrorCode;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RateUtil {
    public static BigDecimal exchangeStationRate(BigDecimal money, String currency) {
        if (StringUtils.isEmpty(currency)) {
            return money;
        }
        switch (currency) {
            case "VND":
//                BigDecimal afterMoney = BigDecimalUtil.multiply(money, BigDecimalUtil.THOUSAND);
                if (money.compareTo(BigDecimalUtil.THOUSAND) < 0) {
                    throw new BaseException(BaseI18ErrorCode.moneyGreaterThan1, new Object[]{"1000"});
                }
        }
        return money;
    }

    //取出不需要转换的10位数以下金额,支持小数和整数
    //列: 用户系统金额7290.27会拿到90.27,余下的金额7200会转换到三方
    public static void setUnusedMoney(BigDecimal money, MnyMoneyBo vo, String currency) {
        switch (currency) {
            case "VND":
                int roundedNumber = (int) Math.round(money.doubleValue());
                double decimalPart = money.subtract(new BigDecimal(roundedNumber)).doubleValue();
                BigDecimal unusedMoney = new BigDecimal(Double.toString(decimalPart)).setScale(6, RoundingMode.DOWN).add(new BigDecimal(roundedNumber % 100));
                vo.setUnusedMoney(unusedMoney);
                vo.setMoney(money.subtract(unusedMoney));
        }
    }

}
