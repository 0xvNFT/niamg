package com.play.web.utils.draw;

import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.SpringUtils;
import com.play.core.MoneyRecordType;
import com.play.core.UserTypeEnum;
import com.play.core.WithdrawRuleEnum;
import com.play.dao.SysUserDao;
import com.play.model.*;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//提款审核规则审核工具类
public class DrawCheckRuleUtils {

    /**
     * 检查是否提款刷子
     * @param account
     * @return 是否是刷子
     */
    public static StationDrawRuleStrategy isFloodOrder(SysUser account,MnyDrawRecord drawRecord) {

        LoggerFactory.getLogger("isflood order === " + JSONObject.toJSONString(drawRecord));
        List<StationDrawRuleStrategy> ruleStrategys = SpringUtils.getBean(StationDrawRuleStrategyService.class).find(account.getDegreeId(),
                account.getGroupId(), account.getStationId(), Constants.STATUS_ENABLE);
        LoggerFactory.getLogger("a").error("rules size === " + ruleStrategys.size());
        try{
            if (ruleStrategys == null || ruleStrategys.isEmpty()) {
                return null;
            }
            MnyDrawRecordService drawRecordService = SpringUtils.getBean(MnyDrawRecordService.class);
            MnyDepositRecordService depositRecordService = SpringUtils.getBean(MnyDepositRecordService.class);
            SysUserDailyMoneyService sysUserDailyMoneyService = SpringUtils.getBean(SysUserDailyMoneyService.class);
            SysUserDao sysUserDao = SpringUtils.getBean(SysUserDao.class);
            SysUserDepositService sysUserDepositService = SpringUtils.getBean(SysUserDepositService.class);
            SysUserMoneyHistoryService sysUserMoneyHistoryService = SpringUtils.getBean(SysUserMoneyHistoryService.class);
            DrawClickFarmingService farmingService = SpringUtils.getBean(DrawClickFarmingService.class);
            for (int i = 0; i < ruleStrategys.size(); i++) {
                StationDrawRuleStrategy rule = ruleStrategys.get(i);
                Date startDate = null;
                Date endDate = null;
                if (rule.getDays() != null && rule.getDays() > 0) {
                    endDate = DateUtil.dayEndTime(new Date(), 0);
                    startDate = DateUtil.dayFirstTime(new Date(), -rule.getDays());
                }
                DrawClickFarming drawClickFarming = farmingService.setDrawClickFarming(drawRecord.getOrderId(), rule.getType(), getRuleName(rule.getType()), startDate, endDate, rule.getValue());


                if (rule.getType() == WithdrawRuleEnum.depositWithdrawAmountDiff.getType()) {
                    //一段时间内提现充值金额差限制
                    //充值总额
                    BigDecimal totalDeposit = depositRecordService.getDepositTotalMoney(account.getStationId(), MnyDepositRecord.STATUS_SUCCESS,
                            null, null, startDate, endDate);
                    //提款总额
                    BigDecimal totalDraw = drawRecordService.getDrawTotalMoney(account.getStationId(), MnyDrawRecord.STATUS_SUCCESS,
                            null, null, startDate, endDate);

                    LoggerFactory.getLogger("a").error("total deposit,draw == " + totalDeposit + "," + totalDraw);
                    if (totalDraw == null) {
                        totalDraw = BigDecimal.ZERO;
                    }
                    BigDecimal diff = BigDecimalUtil.absOr0(BigDecimalUtil.subtract(totalDraw, totalDeposit));
                    LoggerFactory.getLogger("a").error("total deposit,draw 2 == " + diff);
                    if (BigDecimalUtil.subtract(diff,rule.getValue()).compareTo(BigDecimal.ZERO) > 0) {

                        farmingService.save(drawClickFarming, totalDeposit, totalDraw, diff);

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.depositWithdrawAmountDiff.getRuleName());
                        return rule;
                    }
                } else if (rule.getType() == WithdrawRuleEnum.withdrawMoneyLimit.getType()) {
                    //提现金额最大值限制
                    if (drawRecord.getDrawMoney().compareTo(rule.getValue()) > 0) {

                        farmingService.save(drawClickFarming, drawRecord.getDrawMoney());

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.withdrawMoneyLimit.getRuleName());
                        return rule;
                    }
                } else if (rule.getType() == WithdrawRuleEnum.combineUserCount.getType()) {
                    //关联用户数限制
                    //获取该用户关联的ip,操作系统
                    //查出来的记录中的用户与当前提款的用户不相同才对
                    long count = drawRecordService.countOfRecord(account.getStationId(), null, null,
                            null, startDate, endDate, IpUtils.getIp(), LogUtils.getOs(), account.getId());
                    LoggerFactory.getLogger("a").error("ip and os list size ===" + count);
                    if (BigDecimal.valueOf(count).compareTo(rule.getValue()) > 0) {

                        farmingService.save(drawClickFarming, IpUtils.getIp(), LogUtils.getOs(), Integer.valueOf(String.valueOf(count)));

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.combineUserCount.getRuleName());
                        return rule;
                    }
                } else if (rule.getType() == WithdrawRuleEnum.sameIpUserCount.getType()) {
                    //同IP用户数限制
                    //获取该用户关联的ip
                    long count = drawRecordService.countOfRecord(account.getStationId(), null, null,
                            null, startDate, endDate, IpUtils.getIp(), null,account.getId());
                    if (BigDecimal.valueOf(count).compareTo(rule.getValue()) > 0) {

                        farmingService.save(drawClickFarming, IpUtils.getIp(), null, Integer.valueOf(String.valueOf(count)));

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.sameIpUserCount.getRuleName());
                        return rule;
                    }
                } else if (rule.getType() == WithdrawRuleEnum.depositTimesLimit.getType()) {
                    //一段时间内充值次数小于设定值
                    //获取充值次数
                    Integer count = depositRecordService.countOfDeposit(account.getStationId(), MnyDepositRecord.STATUS_SUCCESS, null,
                            startDate, endDate, account.getId());
                    if (BigDecimal.valueOf(count).compareTo(rule.getValue()) < 0) {

                        farmingService.save(drawClickFarming, null, null, Integer.valueOf(String.valueOf(count)));

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.depositTimesLimit.getRuleName());
                        return rule;
                    }
                } else if (rule.getType() == WithdrawRuleEnum.thirdGameProfitLimit.getType()) {
                    //三方游戏总盈利大于设定值
                    //获取三方游戏盈利总额
                    SysUserDailyMoney d = sysUserDailyMoneyService.getDailyBet(account.getId(), account.getStationId(), startDate, endDate, null,
                            null, null, null, null, null, null);
                    LoggerFactory.getLogger("a").error("ttttt recommend user SysUserDailyMoney = " + JSONObject.toJSONString(d));
                    if (d != null) {
                        BigDecimal totalWin = BigDecimalUtil.addAll(d.getSportWinAmount(), d.getLiveWinAmount(), d.getEgameWinAmount(),
                                d.getChessWinAmount(), d.getLotWinAmount(), d.getEsportWinAmount(), d.getFishingWinAmount());
                        LoggerFactory.getLogger("a").error("ttttt recommend user SysUserDailyMoney totalWin = " + totalWin);
                        if (totalWin != null && totalWin.compareTo(rule.getValue()) > 0) {

                            farmingService.save(drawClickFarming, null, null, null, totalWin);

                            LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.thirdGameProfitLimit.getRuleName());
                            return rule;
                        }
                    }
                } else if (rule.getType() == WithdrawRuleEnum.recomUsersLimitAndDepositRateLimit.getType()) {
                    //一段时间内邀请用户数大于设定值而且用户的充值率大于设定值2
                    //获取当前会员推荐的会员列表
                    List<SysUser> sysUsers = sysUserDao.findRecommendUsers(account.getStationId(), account.getId(),
                            startDate, endDate, null, account.getType() == UserTypeEnum.PROXY.getType() ? false : true);
                    LoggerFactory.getLogger("a").error("ttttt recommend users size === " + sysUsers.size());
                    float validDepositPerson = 0;
                    for (SysUser user : sysUsers) {
                        int firstDepositTime = sysUserDepositService.getLastCountOfFirstDeposit(account.getStationId(), startDate, user.getId());
                        LoggerFactory.getLogger("a").error("first deposit time of user == " + firstDepositTime + ",of username = " + account.getUsername());
                        validDepositPerson += (firstDepositTime > 0 ? 1 : 0);
                    }
                    LoggerFactory.getLogger("a").error("ttttt deposit validDepositPerson === " + validDepositPerson);
                    float depositeRate = validDepositPerson / sysUsers.size();
                    LoggerFactory.getLogger("a").error("ttttt deposit rate2 === " + depositeRate);
                    if (BigDecimal.valueOf(sysUsers.size()).compareTo(rule.getValue()) > 0) {
                        LoggerFactory.getLogger("a").error("ttttt deposit 333 === ");
                        if (rule.getRate() != null && BigDecimal.valueOf(depositeRate).compareTo(rule.getRate()) > 0) {

                            farmingService.save(drawClickFarming, sysUsers.size(), (int)validDepositPerson, new BigDecimal(depositeRate), rule.getRate());

                            LoggerFactory.getLogger("a").error("ttttt deposit 444 === " + rule.getRate());
                            LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.recomUsersLimitAndDepositRateLimit.getRuleName());
                            return rule;
                        }
                    }
                } else if (rule.getType() == WithdrawRuleEnum.recommendUsersDepositWithdrawDiffSubstractProxyMoney.getType()) {
                    //邀请用户的充提差减代理佣金大于设定值
                    //获取当前会员推荐的会员或下级代理列表
//                    boolean isMember = account.getType() == UserTypeEnum.MEMBER.getType();
//                    Integer days = DateUtil.getDayMargin(startDate, endDate);
//                    BigDecimal bigDecimal = sysUserBonusService.countInvitePersons(account.getId(), account.getStationId(), endDate, isMember, days, false);
                    List<SysUser> sysUsers = sysUserDao.findRecommendUsers(account.getStationId(), account.getId(),
                            startDate, endDate, null, account.getType() == UserTypeEnum.PROXY.getType() ? false : true);
                    LoggerFactory.getLogger("a").error("ttttt recommend users size === " + sysUsers.size());
                    StringBuilder userIds = new StringBuilder();
                    for (SysUser user : sysUsers) {
                        userIds.append(user.getId()).append(",");
                    }
                    if (userIds.length() > 0) {
                        userIds = userIds.deleteCharAt(userIds.length() - 1);
                    }
                    //推荐会员的充值总额
                    BigDecimal totalDeposit = depositRecordService.getDepositTotalMoney(account.getStationId(), MnyDepositRecord.STATUS_SUCCESS,
                            null, null, startDate, endDate, userIds.toString());
                    if (totalDeposit == null) {
                        totalDeposit = BigDecimal.ZERO;
                    }
                    LoggerFactory.getLogger("a").error("ttttt recommend user deposit draw totalDeposit = " + totalDeposit.longValue());
//                  //提款总额
                    BigDecimal totalDraw = drawRecordService.getDrawTotalMoney(account.getStationId(), MnyDrawRecord.STATUS_SUCCESS,
                            null, null, startDate, endDate, userIds.toString());
                    if (totalDraw == null) {
                        totalDraw = BigDecimal.ZERO;
                    }
                    LoggerFactory.getLogger("a").error("ttttt recommend user deposit draw totalDraw = " + totalDraw.longValue());
                    BigDecimal depositWithdrawDiff = BigDecimalUtil.absOr0(BigDecimalUtil.subtract(totalDeposit, totalDraw));
                    LoggerFactory.getLogger("a").error("ttttt recommend user deposit draw diff = " + depositWithdrawDiff.longValue());
                    //下级的打码返点+下级存款返佣+下级注册返佣总额
                    BigDecimal proxyBackMoney = sysUserMoneyHistoryService.findMoneyByTypes(account.getStationId(), startDate,
                            endDate, account.getId(), MoneyRecordType.PROXY_REBATE_ADD.getType()
                                    +","+MoneyRecordType.INVITE_REG_GIFT_BACK
                                    +","+MoneyRecordType.INVITE_DEPOSIT_GIFT_BACK);
                    if (proxyBackMoney == null) {
                        proxyBackMoney = BigDecimal.ZERO;
                    }
                    LoggerFactory.getLogger("a").error("ttttt recommend user deposit proxyBackMoney = " + proxyBackMoney.doubleValue());
                    LoggerFactory.getLogger("a").error("ttttt rule value = " + rule.getValue());

                    BigDecimal proxyDiff = BigDecimalUtil.subtract(depositWithdrawDiff,proxyBackMoney);

                    if (proxyDiff.compareTo(rule.getValue()) > 0) {

                        farmingService.save(drawClickFarming, totalDeposit, totalDraw, depositWithdrawDiff, sysUsers.size(), proxyBackMoney, proxyDiff);

                        LoggerFactory.getLogger("a").error("命中刷子规则，规则名===" + WithdrawRuleEnum.recommendUsersDepositWithdrawDiffSubstractProxyMoney.getRuleName());
                        return rule;
                    }
                }
            }
        }catch (Exception e){
            LoggerFactory.getLogger("a").error("rule error ==== ", e);
        }

        return null;
    }

    public static String getRuleName(Integer type) {
        WithdrawRuleEnum[] withdrawRuleEnums = WithdrawRuleEnum.values();
        String name = "";
        for(int i = 0, n = withdrawRuleEnums.length ; i < n ; i++) {
            if(type.compareTo(withdrawRuleEnums[i].getType()) == 0) {
                name = I18nTool.getMessage("WithdrawRuleEnum." + withdrawRuleEnums[i].name(), withdrawRuleEnums[i].getRuleName());
            }
        }
        return name;

    }
}
