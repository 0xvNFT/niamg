package com.play.core;

import com.play.spring.config.i18n.I18nTool;
import org.slf4j.LoggerFactory;

/**
 * 提款判断刷子规则
 */
public enum WithdrawRuleEnum {

    depositWithdrawAmountDiff(1, "一段时间内提现充值金额差限制"),
    withdrawMoneyLimit(2, "提现金额最大值限制"),
    combineUserCount(3, "关联用户数限制"),
    sameIpUserCount(4, "同IP用户数限制"),
    depositTimesLimit(5, "一段时间内充值次数小于设定值"),
    thirdGameProfitLimit(6, "三方游戏总盈利大于设定值"),
    recommendUsersDepositWithdrawDiffSubstractProxyMoney(7, "邀请用户的充提差减代理佣金大于设定值"),
    recomUsersLimitAndDepositRateLimit(8, "一段时间内邀请用户数大于设定值而且用户的充值率大于设定值2");

    public Integer type;// 规则类型
    public String ruleName;// 规则名称


    private WithdrawRuleEnum(Integer type, String ruleName) {
        this.ruleName = ruleName;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRuleName() {
        String name = I18nTool.getMessage("WithdrawRuleEnum." + this.name(), this.ruleName);
        return name;
    }

    public void setRuleName(String gameName) {
        this.ruleName = gameName;
    }

}
