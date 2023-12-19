package com.play.service;

import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.*;
import com.play.dao.*;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Callable;

public class MnyHanderlyThread implements Callable<Integer> {

    private static Logger logger = LoggerFactory.getLogger(MnyHanderlyThread.class);

    private MnyDepositRecord mnyDepositRecord;
    private SysUser sysUser;

    private String msg;

    private SysUserDepositDao sysUserDepositDao;
    private StationDailyRegisterDao stationDailyRegisterDao;
    private SysUserMoneyHistoryDao sysUserMoneyHistoryDao;
    private SysUserPermDao sysUserPermDao;
    private MnyDepositRecordDao mnyDepositRecordDao;
    private SysUserDegreeService degreeService;
    private StationDepositStrategyService stationDepositStrategyService;
    private SysUserMoneyService sysUserMoneyService;
    private SysUserScoreService sysUserScoreService;
    private SysUserBetNumService sysUserBetNumService;


    public MnyHanderlyThread(SysUserDepositDao sysUserDepositDao,StationDailyRegisterDao stationDailyRegisterDao,SysUserDegreeService degreeService,
                             MnyDepositRecord mnyDepositRecord, SysUser sysUser, String msg) {
        this.mnyDepositRecord = mnyDepositRecord;
        this.sysUser = sysUser;
        this.msg = msg;
        this.sysUserDepositDao = sysUserDepositDao;
        this.stationDailyRegisterDao = stationDailyRegisterDao;
        this.degreeService = degreeService;
    }

    @Override
    public Integer call() throws Exception {

        // 当日注册充值
        Integer depositNum = null;
        BigDecimal depositMoney = null;
        Integer first = null;
        Integer second = null;
        Integer third = null;
        // 是否是当日注册当日充值
        if (sysUser.getCreateDatetime() != null
                && DateUtil.toDateStr(sysUser.getCreateDatetime()).equals(DateUtil.getCurrentDate())) {
            depositNum = 1;
            depositMoney = mnyDepositRecord.getMoney();
        }
        // 统计所有存款总额和存款次数
        SysUserDeposit deposit = sysUserDepositDao.findOne(sysUser.getId(), sysUser.getStationId());
        if (deposit == null) {
            deposit = new SysUserDeposit();
            deposit.setUserId(sysUser.getId());
            deposit.setStationId(sysUser.getStationId());
            deposit.setTimes(1);
            deposit.setTotalMoney(mnyDepositRecord.getMoney());
            deposit.setFirstMoney(mnyDepositRecord.getMoney());
            deposit.setFirstTime(mnyDepositRecord.getCreateDatetime());
            deposit.setMaxMoney(mnyDepositRecord.getMoney());
            deposit.setMaxTime(mnyDepositRecord.getCreateDatetime());
            sysUserDepositDao.save(deposit);
            // 是否是首充充值
            first = 1;
        } else {
            deposit.setTotalMoney(deposit.getTotalMoney().add(mnyDepositRecord.getMoney()));
            deposit.setTimes(deposit.getTimes() + 1);
            if (deposit.getFirstTime() == null) {
                deposit.setFirstTime(mnyDepositRecord.getCreateDatetime());
                deposit.setFirstMoney(mnyDepositRecord.getMoney());
                first = 1;
            } else if (deposit.getSecTime() == null) {
                deposit.setSecTime(mnyDepositRecord.getCreateDatetime());
                deposit.setSecMoney(mnyDepositRecord.getMoney());
                second = 1;
            } else if (deposit.getThirdTime() == null) {
                deposit.setThirdTime(mnyDepositRecord.getCreateDatetime());
                deposit.setThirdMoney(mnyDepositRecord.getMoney());
                third = 1;
            }
            if (deposit.getMaxMoney().compareTo(mnyDepositRecord.getMoney()) < 0) {
                deposit.setMaxMoney(mnyDepositRecord.getMoney());
                deposit.setMaxTime(mnyDepositRecord.getCreateDatetime());
            }
            sysUserDepositDao.update(deposit);
        }
        // 更新每日注册
        stationDailyRegisterDao.depositNumAdd(sysUser.getStationId(), first, second, third, depositNum, depositMoney);
        // 判断会员是否转变层级
        SysUserDegree degree = degreeService.findOne(sysUser.getDegreeId(), sysUser.getStationId());
        if (degree != null && degree.getType() != null && degree.getType() != SysUserDegree.TYPE_BETNUM) {
            degreeService.changeUserDegree(sysUser, deposit.getTotalMoney(), msg);
        }
        // 存款策略奖金处理
        mnyDepositRecord = handleComStrategy(mnyDepositRecord, sysUser, deposit.getTimes());
        // 提款所需打码量处理
        handleDrawNeed(mnyDepositRecord);
        return 1;
    }

    private MnyDepositRecord handleComStrategy(MnyDepositRecord record, SysUser user, Integer allDepositCount) {
        // 验证权限
        BigDecimal curDrawNeed = BigDecimal.ZERO;
        SysUserPerm perm = sysUserPermDao.findOne(sysUser.getId(), sysUser.getStationId(),
                UserPermEnum.depositGift.getType());
        if (perm == null || !Objects.equals(perm.getStatus(), Constants.STATUS_ENABLE)) {
            // 禁用权限的话读取默认的出款比例
            BigDecimal scale = BigDecimalUtil.toBigDecimalDefaultZero(
                    StationConfigUtil.get(sysUser.getStationId(), StationConfigEnum.consume_rate));
            curDrawNeed = scale.multiply(mnyDepositRecord.getMoney());
            mnyDepositRecord.setDrawNeed(curDrawNeed);
            return mnyDepositRecord;
        }
        int dayDepositCount = mnyDepositRecordDao.countUserDepositNum(sysUser.getStationId(), sysUser.getId(),
                record.getCreateDatetime());// 今日存款次数
        List<StationDepositStrategy> strategyList = stationDepositStrategyService.filter(user, allDepositCount,
                dayDepositCount, record.getDepositType(), record.getMoney(), record.getCreateDatetime(),
                record.getPayId());

        logger.error("MnyHanderlyThread handleComStrategy, strategySize:{}", strategyList.size());

        if (strategyList != null && !strategyList.isEmpty()) {
            for (StationDepositStrategy ds : strategyList) {
                BigDecimal amount = BigDecimal.ZERO;
                // 固定数额
                if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
                    amount = ds.getGiftValue();
                } else {// 浮动比例
                    amount = record.getMoney().multiply(ds.getGiftValue()).divide(BigDecimalUtil.HUNDRED).setScale(4,
                            BigDecimal.ROUND_FLOOR);
                    BigDecimal limit = ds.getUpperLimit();
                    // 如果超过最大限制，取最大限制的值
                    if (limit != null && limit.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(limit) > 0) {
                        amount = limit;
                    }
                }
                if (ds.getDayupperLimit() != null && ds.getDayupperLimit().compareTo(BigDecimal.ZERO) > 0) {
                    // 计算今日上限
                    BigDecimal todayMoney = sysUserMoneyHistoryDao.sumDayPay(user.getId(), user.getStationId());// 统计今日已赠送
                    if (todayMoney == null) {
                        todayMoney = BigDecimal.ZERO;
                    }
                    if (amount.add(todayMoney).compareTo(ds.getDayupperLimit()) > 0) {
                        amount = ds.getDayupperLimit().subtract(todayMoney);
                    }
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        amount = null;
                        LogUtils.addLog(user.getUsername() + " 存款赠送已达单日赠送上限：" + ds.getDayupperLimit());
                    }
                }
                if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                    if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
                        MnyMoneyBo mvo = new MnyMoneyBo();
                        mvo.setUser(user);
                        mvo.setMoney(amount);
                        mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_GIFT_ACTIVITY);
                        mvo.setOrderId(record.getOrderId());
                        if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
                            List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
                                    "（", BaseI18nCode.fixedNum.getCode(), "：", String.valueOf(ds.getGiftValue()), "）");
                            mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
                        } else {
                            List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
                                    "（", BaseI18nCode.floatScale.getCode(), "：", String.valueOf(ds.getGiftValue()), "%）");
                            mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
                        }
                        mvo.setBizDatetime(record.getCreateDatetime());
                        record.setGiftMoney(amount);
                        sysUserMoneyService.updMnyAndRecord(mvo);
                        mnyDepositRecordDao.updateGiftMoney(record.getId(), amount, record.getStationId());
                        if (ds.getBetRate() != null) {
                            // 根据打码量倍数得到打码量
                            curDrawNeed = (amount.add(record.getMoney())).multiply(ds.getBetRate()).setScale(0,
                                    RoundingMode.UP);
                        }
                    } else {// 赠送积分
                        List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardPoint.getCode(), "：", String.valueOf(amount.longValue()));
                        sysUserScoreService.operateScore(ScoreRecordTypeEnum.DEPOSIT_GIFT_ACTIVITY, user, amount.longValue(), I18nTool.convertCodeToArrStr(remarkList));
                    }
                } else {
                    amount = BigDecimal.ZERO;
                    if (ds.getBetRate() != null) {
                        // 根据打码量倍数得到打码量
                        curDrawNeed = (amount.add(record.getMoney())).multiply(ds.getBetRate()).setScale(0, RoundingMode.UP);
                    }
                }
            }
        }
        record.setDrawNeed(curDrawNeed);
        return record;
    }

    /**
     * 打码量处理
     */
    private void handleDrawNeed(MnyDepositRecord record) {
        BigDecimal curDrawNeed = record.getDrawNeed();
        // 所需打码量计算规则， 先使用存款策略、为空时 使用人工加款手动输入的， 如果为空 在使用 站点配置的
        if (curDrawNeed.compareTo(BigDecimal.ZERO) < 1) {
            // 取得消费比例
            BigDecimal betNumMultiple = BigDecimalUtil
                    .toBigDecimal(StationConfigUtil.get(record.getStationId(), StationConfigEnum.consume_rate));
            // 记算此时会员如果提款，需要多少的打码量
            curDrawNeed = BigDecimalUtil.multiply(record.getMoney(), betNumMultiple);
        }
        // 打码量为0不执行数据库操作
        if (curDrawNeed.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        // 更新会员提款的判断所需要的数据
        if (record.getGiftMoney() == null) {
            record.setGiftMoney(BigDecimal.ZERO);
        }
        List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationMemberSaveMoney.getCode());
        sysUserBetNumService.updateDrawNeed(record.getUserId(), record.getStationId(), curDrawNeed,
                BetNumTypeEnum.deposit.getType(), I18nTool.convertCodeToArrStr(remarkList),
                record.getOrderId());
    }

}
