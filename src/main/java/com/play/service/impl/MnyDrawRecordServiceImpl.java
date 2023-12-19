package com.play.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.ReplacePayWrapper;
import com.play.common.ReplacePayWrapperFactory;
import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.OrderIdMaker;
import com.play.common.utils.security.MD5Util;
import com.play.core.BankInfo;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.*;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.model.dto.ExchangeUSDTDto;
import com.play.model.vo.DailyMoneyVo;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.draw.DrawCheckRuleUtils;
import com.play.web.utils.export.ExportToCvsUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 用户提款记录
 *
 * @author admin
 */
@Service
public class MnyDrawRecordServiceImpl implements MnyDrawRecordService {
    /**
     * log
     */
    static Logger log = LoggerFactory.getLogger(MnyDrawRecordServiceImpl.class);
    @Autowired
    private MnyDrawRecordDao mnyDrawRecordDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserInfoDao sysUserInfoDao;
    @Autowired
    private SysUserBetNumDao sysUserBetNumDao;
    @Autowired
    private SysUserBankDao sysUserBankDao;
    @Autowired
    private SysUserMoneyService sysUserMoneyService;
    @Autowired
    private SysUserPermDao sysUserPermDao;
    @Autowired
    private SysUserDegreeService sysUserDegreeService;
    @Autowired
    private SysUserWithdrawDao sysUserWithdrawDao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private StationMessageService messageService;
    @Autowired
    private StationDrawFeeStrategyService stationDrawFeeStrategyService;
    @Autowired
    private SysUserGroupService sysUserGroupService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;
    @Autowired
    private StationDepositBankService stationDepositBankService;
    @Autowired
    StationDrawRuleStrategyService stationDrawRuleStrategyService;

    @Override
    public Integer getCountOfUntreated(Long stationId) {
        return mnyDrawRecordDao.getCountOfUntreated(stationId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withDraw(SysUser sysUser, Long stationId, String rePwd, BigDecimal money, Long bankId) {
        try {
            // method implementation
            if (GuestTool.isGuest(sysUser)) {
                throw new BaseException(BaseI18nCode.gusetPleaseRegister);
            }

            // 从DB重新查询用户信息，避免缓存影响
            SysUser account = sysUserService.findOneById(sysUser.getId(), sysUser.getStationId());
            if (account.getMoneyStatus() != Constants.STATUS_ENABLE) {
                throw new ParamException(BaseI18nCode.userMoneyStatusDisabled);
            }
            if (money == null || money.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParamException(BaseI18nCode.withdrawMoneyFormatError);
            }
            money = money.setScale(0, BigDecimal.ROUND_DOWN);
            if (bankId == null) {
                throw new ParamException(BaseI18nCode.selectBankCard);
            }
            if (StringUtils.isEmpty(rePwd)) {
                throw new ParamException(BaseI18nCode.inputWithdrawPassword);
            }
            if (!GuestTool.isGuest(account)) {
                UserPermVo perm = sysUserPermDao.getPerm(account.getId(), account.getStationId());
                if (!perm.isWithdraw()) {
                    throw new ParamException(BaseI18nCode.noWithdrawAuthority);
                }
            }
            if (StationConfigUtil.isOff(account.getStationId(), StationConfigEnum.draw_multiple)) {
                MnyDrawRecord oldRecord = mnyDrawRecordDao.getNewestRecord(account.getStationId(), account.getId(),
                        new Date(), null);
                if (oldRecord != null && oldRecord.getStatus() == MnyDrawRecord.STATUS_UNDO) {
                    throw new ParamException(BaseI18nCode.withdrawOrderUntreated);
                }
            }
            SysUserBank sysUserBank = sysUserBankDao.findOne(bankId, stationId);
            if (sysUserBank == null || !account.getId().equals(sysUserBank.getUserId())) {
                throw new ParamException(BaseI18nCode.bankCardInfoError);
            }
            if (sysUserBank.getStatus() == Constants.STATUS_DISABLE) {
                throw new ParamException(BaseI18nCode.bankCardBan);
            }
            // TODO
            drawValidate(account, stationId, account.getUsername(), money, rePwd);
            groupValidate(account, stationId, money);
            withDrawMoney(account, money, sysUserBank);
            LogUtils.addLog("用户“" + account.getUsername() + "”提现：" + money);
        } catch (Exception e) {
            log.error("Exception in withDraw method", e);
            throw e;
        } finally {
            log.info("End of withDraw method");
        }
    }

    /**
     * 保存账变记录，提款记录
     *
     * @param account
     * @param money
     * @param sysUserBank
     */

    private void withDrawMoney(SysUser account, BigDecimal money, SysUserBank sysUserBank) {
        String orderId = OrderIdMaker.getWithdrawOrderId();
        // 添加账变记录
        MnyMoneyBo mvo = new MnyMoneyBo();
        BigDecimal feeMoney = validDrawFeeStrategy(account, money);
        mvo.setUser(account);
        //应该加上手续费
        mvo.setMoney(money);
        mvo.setMoneyRecordType(MoneyRecordType.WITHDRAW_ONLINE);

        List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationUserDraw.getCode(),String.valueOf(money));
        String remarkString = I18nTool.convertCodeToArrStr(remarkList);
        mvo.setRemark(remarkString);
        //mvo.setRemark(I18nTool.convertCodeToArrStr(BaseI18nCode.stationUserDraw.getCode(),money.toString()));
     //   mvo.setRemark(I18nTool.getMessage(BaseI18nCode.stationUserDraw) + money);
        mvo.setOrderId(orderId);
        sysUserMoneyService.updMnyAndRecord(mvo);

        // 保存提款记录
        MnyDrawRecord drawRecord = new MnyDrawRecord();
        drawRecord.setFeeMoney(feeMoney);
        SysUserGroup group = sysUserGroupService.findOne(account.getGroupId(), account.getStationId());
//        if (group.getDailyDrawNum() != null && group.getDailyDrawNum() > 0) {
//            group.setDailyDrawNum(group.getDailyDrawNum() - 1);
//        }
        sysUserGroupService.update(group);
        drawRecord.setUsername(account.getUsername());
        drawRecord.setRealName(sysUserBank.getRealName());
        drawRecord.setUserId(account.getId());
        drawRecord.setBankName(sysUserBank.getBankName());
        drawRecord.setCardNo(sysUserBank.getCardNo());
        drawRecord.setDrawMoney(money);
        drawRecord.setTrueMoney(money.subtract(drawRecord.getFeeMoney()));
        drawRecord.setStatus(GuestTool.isGuest(account) ? MnyDrawRecord.STATUS_SUCCESS : MnyDrawRecord.STATUS_UNDO);
        drawRecord.setUserType(account.getType());
        drawRecord.setType(MnyDrawRecord.NORMAL);
        drawRecord.setStationId(account.getStationId());
        drawRecord.setCreateUserId(account.getId());
        drawRecord.setCreateDatetime(new Date());
        drawRecord.setOrderId(orderId);
        drawRecord.setLockFlag(MnyDrawRecord.LOCK_FLAG_UNLOCK);
        drawRecord.setDegreeId(account.getDegreeId());
        drawRecord.setParentIds(account.getParentIds());
        drawRecord.setRecommendId(account.getRecommendId());
        drawRecord.setRemark("-");
        drawRecord.setRecordIp(IpUtils.getIp());
        drawRecord.setRecordOs(LogUtils.getOs());
        drawRecord.setAgentName(account.getAgentName());
        if (BankInfo.USDT.getBankName().equalsIgnoreCase(sysUserBank.getBankName())) {
            ExchangeUSDTDto dto = stationDepositBankService.getUsdtRate(sysUserBank.getStationId());
            drawRecord.setWithdrawVirtualCoinRate(dto.getWithdrawRate());
            BigDecimal withdrawNum = drawRecord.getDrawMoney().divide(drawRecord.getWithdrawVirtualCoinRate(), 2, BigDecimal.ROUND_HALF_DOWN);
            drawRecord.setWithdrawVirtualCoinNum(withdrawNum);
        }
        //先判断该记录是否触发到了提款刷子规则策略
        StationDrawRuleStrategy hitRule = DrawCheckRuleUtils.isFloodOrder(account, drawRecord);
        if (hitRule != null) {
            log.error("trigger draw rule ----" + JSONObject.toJSONString(hitRule));
            drawRecord.setSecondReview(2);
        }
        mnyDrawRecordDao.save(drawRecord);
    }

    @Override
    public BigDecimal getDrawTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin, Date end, String userIds) {
        return mnyDrawRecordDao.getTotalRecordMoney(stationId, status, lockFlag, limit, begin, end, userIds);
    }

    @Override
    public BigDecimal getDrawTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin, Date end) {
        return getDrawTotalMoney(stationId, status, lockFlag, limit, begin, end, null);
    }

    private BigDecimal validDrawFeeStrategy(SysUser account, BigDecimal money) {
        StationDrawFeeStrategy strategy = stationDrawFeeStrategyService.getSuitableFeeStrategy(account);
        BigDecimal fee = BigDecimal.ZERO;
        Integer curNum = getCount4Day(account.getId(), account.getStationId());
        if (strategy != null) {
            if (strategy.getDrawNum() == null || curNum >= strategy.getDrawNum()) {
                if (strategy.getFeeType() == StationDrawFeeStrategy.FEE_TYPE_FIXED) {
                    fee = strategy.getFeeValue();
                } else {
                    fee = strategy.getFeeValue().multiply(money).divide(new BigDecimal("100")).setScale(2);
                    if (strategy.getUpperLimit() != null && fee.compareTo(strategy.getUpperLimit()) > 0) {
                        fee = strategy.getUpperLimit();
                    } else if (strategy.getLowerLimit() != null && fee.compareTo(strategy.getLowerLimit()) < 0) {
                        fee = strategy.getLowerLimit();
                    }
                }
            }
        }
        return fee;
    }

    /**
     * 验证是否在修改或新增银行卡的冷却时间内,
     */
    private void validWithdrawModifyBankCardTime(Long accountId, Long stationId) {
        String withdrawModifyBankCardTimes = StationConfigUtil.get(stationId, StationConfigEnum.withdraw_bank_timeout);
        if (StringUtils.isNoneEmpty(withdrawModifyBankCardTimes)) {
            int minutes = NumberUtils.toInt(withdrawModifyBankCardTimes, 0);
            SysUserInfo info = sysUserInfoDao.findOne(accountId, stationId);
            if (minutes > 0 && info.getModifyBankcardTime() != null) {
                if (DateUtils.addMinutes(info.getModifyBankcardTime(), minutes).after(new Date())) {
                    throw new ParamException(BaseI18nCode.xMinuteCanntWithdraw, withdrawModifyBankCardTimes);
                }
            }
        }
    }

    /**
     * 验证2次提现时间间隔
     */
    private void validWithdrawInterval(Long accountId, Long stationId) {
        String withdrawIntervalTimes = StationConfigUtil.get(stationId, StationConfigEnum.withdraw_interval_times);
        if (StringUtils.isNoneEmpty(withdrawIntervalTimes)) {
            int seconds = NumberUtils.toInt(withdrawIntervalTimes, 0);
            if (seconds > 0) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, -seconds);
                long count = mnyDrawRecordDao.countWithdrawCount(accountId, c.getTime(), stationId);
                if (count > 0) {
                    throw new ParamException(BaseI18nCode.xSecondCanWithdraw, withdrawIntervalTimes);
                }
            }
        }
    }

    /**
     * 判断取款时间
     */
    public static void validateWithdrawalTime(String startTime, String endTime) {
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int startHour = 0;
        if (!StringUtils.isEmpty(startTime)) {
            String[] hm = startTime.split(":");
            if (hm.length == 2) {
                startHour = NumberUtils.toInt(hm[0]);
                start.set(Calendar.HOUR_OF_DAY, startHour);
                start.set(Calendar.MINUTE, NumberUtils.toInt(hm[1]));
            }
        }
        int endHour = 0;
        if (!StringUtils.isEmpty(endTime)) {
            String[] hm = endTime.split(":");
            if (hm.length == 2) {
                endHour = NumberUtils.toInt(hm[0]);
                end.set(Calendar.HOUR_OF_DAY, endHour);
                end.set(Calendar.MINUTE, NumberUtils.toInt(hm[1]));
            }
        }
        if (!((endHour < startHour && (now.compareTo(start) >= 0 || now.compareTo(end) <= 0))
                || (endHour > startHour && now.compareTo(start) >= 0 && now.compareTo(end) <= 0))) {
            if (endHour < startHour) {
                throw new ParamException(BaseI18nCode.overWithdrawTimeByDay, startTime, endTime);
            } else {
                throw new ParamException(BaseI18nCode.overWithdrawTimeByHour, startTime, endTime);
            }
        }
    }

    /**
     * 组别验证
     *
     * @param account
     * @param stationId
     * @param money
     */
    private void groupValidate(SysUser account, Long stationId, BigDecimal money) {
        SysUserGroup group = sysUserGroupService.findOne(account.getGroupId(), stationId);
        if (group == null) {
            return;
        }
        if (group.getMaxDrawMoney() != null) {
            if (group.getMaxDrawMoney().compareTo(money) < 0) {
                throw new ParamException(BaseI18nCode.withdrawCanntGt, String.valueOf(group.getMaxDrawMoney()));
            }
        } else {
            BigDecimal max = BigDecimalUtil
                    .toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_max_money));
            if (max != null && max.compareTo(money) < 0) {
                throw new ParamException(BaseI18nCode.withdrawCanntGt, String.valueOf(max));
            }
        }
        if (group.getMinDrawMoney() != null) {
            if (group.getMinDrawMoney().compareTo(money) > 0) {
                throw new ParamException(BaseI18nCode.withdrawCanntLt, String.valueOf(group.getMinDrawMoney()));
            }
        } else {
            BigDecimal min = BigDecimalUtil
                    .toBigDecimal(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_min_money));
            if (min != null && min.compareTo(money) > 0) {
                throw new ParamException(BaseI18nCode.withdrawCanntLt, String.valueOf(min));
            }
        }
        // 站点一天可提款次数
        int curDNum = getCount4Day(account.getId(), stationId);
        int drawNum = NumberUtils.toInt(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_time_one_day));
        if (group.getDailyDrawNum() != null && group.getDailyDrawNum() > 0) {
            drawNum = group.getDailyDrawNum();
        }
        if (drawNum > 0 && curDNum >= drawNum) {
            throw new ParamException(BaseI18nCode.withDrawTimesOver, String.valueOf(curDNum));
        }
    }

    /**
     * 取款信息验证
     */
    private void drawValidate(SysUser account, Long stationId, String username, BigDecimal money, String rePwd) {
        // 判断是否在修改或新增银行卡的冷却时间内
        validWithdrawModifyBankCardTime(account.getId(), stationId);
        // 判断提款时间
        String startTime = StationConfigUtil.get(stationId, StationConfigEnum.money_start_time);
        String endTime = StationConfigUtil.get(stationId, StationConfigEnum.money_end_time);
        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            validateWithdrawalTime(startTime, endTime);
        }
        // 验证两次提款间隔时间
        validWithdrawInterval(account.getId(), stationId);
        // 判断提款密码
        SysUserInfo sysUserInfo = sysUserInfoDao.findOne(account.getId(), stationId);
        if (sysUserInfo == null || StringUtils.isEmpty(sysUserInfo.getReceiptPwd())) {
            throw new ParamException(BaseI18nCode.setWithdrawPassword);
        }
        rePwd = MD5Util.pwdMd5Draw(username, rePwd);
        if (!rePwd.equals(sysUserInfo.getReceiptPwd())) {
            throw new ParamException(BaseI18nCode.withDrawPasswordError);
        }
        // 打码量验证
        if (!GuestTool.isGuest(account) && StationConfigUtil.isOn(stationId, StationConfigEnum.withdraw_validate_bet_num)) {
            SysUserBetNum sysUserBetNum = sysUserBetNumDao.findOne(account.getId(), stationId);
            if (sysUserBetNum.getBetNum().compareTo(sysUserBetNum.getDrawNeed()) < 0) {
                throw new ParamException(BaseI18nCode.betNumNotEnough);
            }
        }

        // 判断余额
        BigDecimal beforeMoney = sysUserMoneyService.getMoney(account.getId());
        if (beforeMoney == null || beforeMoney.compareTo(money.abs()) < 0) {
            log.error("drawValidate money is not enough, money:{}", money);
            throw new ParamException(BaseI18nCode.insufficientBalance);
        }
    }

    @Override
    public Page<MnyDrawRecord> page(Date begin, Date end, Integer status, Integer type, String username,
                                    String proxyName, String levelIds, String pay, BigDecimal minMoney, BigDecimal maxMoney, String opUsername,
                                    String orderId, Long payId, Integer checkWithDrawOrderStatus, String remark, String agentUser,
                                    String bankName, Integer withdrawNum, String referrer, Integer drawType, Integer secondReview) {
        Long stationId = SystemUtil.getStationId();
        if (StringUtils.isNotEmpty(opUsername)) {
            AdminUser admin = adminUserDao.findByUsername(opUsername, stationId);
            if (admin != null) {
                if (admin.getType() != UserTypeEnum.ADMIN.getType()) {
                    return new Page<MnyDrawRecord>();
                }
            } else {
                return new Page<MnyDrawRecord>();
            }
        }
        Long proxyId = null;
        if (StringUtils.isNotEmpty(proxyName)) {
            SysUser sysUser = sysUserDao.findOneByUsername(proxyName, stationId, UserTypeEnum.PROXY.getType());
            if (sysUser == null) {
                throw new ParamException(BaseI18nCode.proxyUnExist);
            }
            proxyId = sysUser.getId();
        }
        Long accountId = null;
        if (StringUtils.isNotEmpty(username)) {
            SysUser account = sysUserDao.findOneByUsername(username, stationId, null);
            if (account == null) {
                throw new ParamException(BaseI18nCode.memberUnExist);
            }
            accountId = account.getId();
        }
        Long recommendId = null;
        if (StringUtils.isNotEmpty(referrer)) {
            SysUser account = sysUserDao.findOneByUsername(referrer, stationId, null);
            if (account == null) {
                throw new ParamException(BaseI18nCode.memberUnExist);
            }
            recommendId = account.getId();
        }
        int lock = 0;
        if (status != null) {
            if (status == 1) {
                lock = MnyDrawRecord.LOCK_FLAG_UNLOCK;
            } else if (status == 10) {
                status = MnyDrawRecord.STATUS_UNDO;
                lock = MnyDrawRecord.LOCK_FLAG_LOCK;
            }
        }
        // 验证金额是否在范围内
        if (minMoney != null) {
            if (maxMoney != null && maxMoney.compareTo(minMoney) < 0) {
                throw new ParamException(BaseI18nCode.checkMoneyRange);
            }

        }
        if (maxMoney != null) {
            if (minMoney != null && maxMoney.compareTo(minMoney) < 0) {
                throw new ParamException(BaseI18nCode.checkMoneyRange);
            }
        }
        if (begin.after(end)) {
            throw new ParamException(BaseI18nCode.startTimeLtEndTime);
        }
        // if(StringUtils.isNotEmpty(username)) {
        int diff = DateUtil.getDayMargin(begin, end);
        if (diff > 31) {
            throw new ParamException(BaseI18nCode.timeRangeNotOver31);
        }
        // }else {
        // int diff = DateUtil.getDayMargin(begin, end);
        // if(diff > 3) {
        // throw new ParamException("查询时间范围不能超过3天,输入会员号可查询31天!");
        // }
        // }
        // 获取排序配置
        boolean depositSort = StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_record_sort_by_status);
        // 是否开启提款审核
        // Integer checkStatus = stationConfigService.isOn(stationId,
        // StationConfigEnum.withdraw_check_switch)
        // ? MnyDrawRecord.CHECK_SUCCESS
        // : null;
        Integer checkStatus = null;
        // 计算充值人数（去重）
        Integer drawTimes = mnyDrawRecordDao.countDrawTimes(stationId, begin, end, accountId, status, lock, type,
                proxyId, levelIds, depositSort, pay, minMoney, maxMoney, opUsername, checkStatus, orderId, payId,
                checkWithDrawOrderStatus, remark, agentUser, bankName, withdrawNum, recommendId, drawType);
        Page<MnyDrawRecord> page = mnyDrawRecordDao.page(stationId, begin, end, accountId, status, lock, type, proxyId,
                levelIds, depositSort, pay, minMoney, maxMoney, opUsername, checkStatus, orderId, payId,
                checkWithDrawOrderStatus, remark, agentUser, bankName, withdrawNum, recommendId, drawType, secondReview);

        if (page.hasRows()) {
            for (MnyDrawRecord mnyDrawRecord : page.getRows()) {
                BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(mnyDrawRecord.getDegreeName());
                if (baseI18nCode != null) {
                    mnyDrawRecord.setDegreeName(I18nTool.getMessage(baseI18nCode));
                }
            }
        }

        Map<String, Object> aggs = page.getAggsData();
        aggs.put("drawTimes", drawTimes);
        page.setAggsData(aggs);
        return page;
    }

    @Override
    public Page<MnyDrawRecord> checkPage(Date begin, Date end, Integer type, String username, String proxyName,
                                         String levelIds, BigDecimal minMoney, BigDecimal maxMoney) {
        return null;
    }

    @Override
    public void drawLock(Long id, Integer lockFlag) {
        AdminUser adminUser = LoginAdminUtil.currentUser();
        if (adminUser.getType() != UserTypeEnum.ADMIN.getType() && !SystemConfig.SYS_MODE_DEVELOP) {
            throw new ParamException(BaseI18nCode.adminAuthority);
        }
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord record = mnyDrawRecordDao.findOne(id, stationId);
        if (record == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        if (adminUser.getWithdrawLimit() != null && adminUser.getWithdrawLimit().compareTo(record.getDrawMoney()) < 0) {
            throw new ParamException(BaseI18nCode.canntHandleXOrder, String.valueOf(adminUser.getWithdrawLimit()));
        }
        if (lockFlag == null
                || (lockFlag == MnyDrawRecord.LOCK_FLAG_UNLOCK && !record.getOpUserId().equals(adminUser.getId()))) {
            throw new ParamException(BaseI18nCode.withdrawOrderLockByOther);
        }
        if (lockFlag != MnyDrawRecord.LOCK_FLAG_UNLOCK && lockFlag != MnyDrawRecord.LOCK_FLAG_LOCK) {
            throw new ParamException(BaseI18nCode.illegalOperation);
        }
        if (record.getStatus() != MnyDrawRecord.STATUS_UNDO) {
            throw new ParamException(BaseI18nCode.orderProcessed);
        }
        // 验证记录审核模式下是否审核过
        // if (stationConfigService.isOn(stationId,
        // StationConfigEnum.withdraw_check_switch)) {
        // if (record.getCheckStatus() != null && record.getCheckStatus() !=
        // MnyDrawRecord.STATUS_SUCCESS) {
        // throw new ParamException("该记录未被审核通过");
        // }
        // }
        String remark = MnyDrawRecord.LOCK_FLAG_LOCK == lockFlag ? BaseI18nCode.stationHandlerIng.getMessage()
                : BaseI18nCode.stationCancelHandler.getMessage();

        if (mnyDrawRecordDao.updateDrawLockFlag(id, lockFlag, adminUser.getId(), adminUser.getUsername(), new Date(),
                remark, stationId)) {
            if (lockFlag == MnyDrawRecord.LOCK_FLAG_LOCK) {
                LogUtils.addLog("锁定订单号为:" + record.getOrderId() + "的提款记录");
            } else {
                LogUtils.addLog("取消锁定订单号为:" + record.getOrderId() + "的提款记录");
            }
        } else {
            throw new ParamException(BaseI18nCode.ordeerNotExistOrLock);
        }
    }

    @Override
    public void drawCheckLock(Long id, Integer lockFlag) {

    }

    @Override
    public void confirmCheck(Long id, Integer confirmFlag, String msg) {

    }

    @Override
    public MnyDrawRecord getOne(Long id, Long stationId) {
        MnyDrawRecord record = mnyDrawRecordDao.findOne(id, stationId);
        if (record != null) {
            SysUser account = sysUserDao.findOne(record.getUserId(), stationId);
            SysUserDegree accountLevel = Optional
                    .ofNullable(sysUserDegreeService.findOne(account.getDegreeId(), stationId))
                    .orElseThrow(() -> new ParamException(BaseI18nCode.setMemberDegree));
            record.setDegreeName(accountLevel.getName());
        }
        return record;
    }

    @Override
    public MnyDrawRecord getLastDraw(Long accountId, Long stationId) {
//		mnyDrawRecordDao.getRecordList()
        return mnyDrawRecordDao.getLastDraw(accountId, stationId);
    }

    @Override
    public void drawHandle(Long id, Integer status, String remark) {
        AdminUser adminUser = LoginAdminUtil.currentUser();
        if (adminUser.getType() != UserTypeEnum.ADMIN.getType() && !SystemConfig.SYS_MODE_DEVELOP) {
            throw new ParamException(BaseI18nCode.adminAuthority);
        }
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord drawRecord = getOne(id, stationId);
        if (drawRecord == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        if (adminUser.getWithdrawLimit() != null
                && adminUser.getWithdrawLimit().compareTo(drawRecord.getDrawMoney()) < 0) {
            throw new BaseException(BaseI18nCode.canntHandleXOrder, new Object[]{adminUser.getWithdrawLimit()});
        }
        if (drawRecord.getLockFlag().equals(MnyDrawRecord.LOCK_FLAG_UNLOCK)) {
            throw new ParamException(BaseI18nCode.orderNotLock);
        }
        // 判断该记录是否被其他用户锁定
        if (!drawRecord.getOpUserId().equals(adminUser.getId())) {
            throw new ParamException(BaseI18nCode.withdrawOrderLockByOther);
        }
        // 判断记录是否处理未处理状态
        if (!drawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO)) {
            throw new ParamException(BaseI18nCode.orderHadBeenHandel);
        }
        drawRecord.setStatus(status);
        drawRecord.setRemark(remark);
        drawRecord.setOpDatetime(new Date());
        if (!mnyDrawRecordDao.updateStatus(id, stationId, status, remark, new Date(), null, null, MnyDrawRecord.NORMAL,
                null)) {
            throw new ParamException(BaseI18nCode.orderHadBeenHandel);
        }
        SysUser user = sysUserService.findOneById(drawRecord.getUserId(), stationId);
        // 处理失败
        if (status == MnyDrawRecord.STATUS_FAIL) {
            // 添加账变
            MnyMoneyBo mvo = new MnyMoneyBo();
            mvo.setUser(user);
            //出款金额应该加上手续费
            mvo.setMoney(drawRecord.getDrawMoney());
            mvo.setFeeMoney(drawRecord.getFeeMoney());
            mvo.setMoneyRecordType(MoneyRecordType.WITHDRAW_ONLINE_FAILED);
            /**
             * 这里客户明确表示不能接受系统的这种处理方式
             * <p>
             * 需要先改成英文
             * */
//            mvo.setRemark(I18nTool.getMessage(BaseI18nCode.stationDrawCash) + drawRecord.getDrawMoney().setScale(2)
//                    + I18nTool.getMessage(BaseI18nCode.stationFailReson) + remark);
            List <String> remarkList = I18nTool.convertCodeToList("Withdrawal",String.valueOf(drawRecord.getDrawMoney().setScale(2)),
                    "Failed and degenerasting, Cause:",remark);
            String remarkString = I18nTool.convertCodeToArrStr(remarkList);
            mvo.setRemark(remarkString);

//            mvo.setRemark(
//                    "Withdrawal"
//                            + drawRecord.getDrawMoney().setScale(2)
//                            + "Failed and degenerasting, Cause:" + remark);
            mvo.setOrderId(drawRecord.getOrderId());
            mvo.setBizDatetime(drawRecord.getCreateDatetime());
            sysUserMoneyService.updMnyAndRecord(mvo);
            LogUtils.addLog("未批准编号为:" + drawRecord.getOrderId() + "会员:" + drawRecord.getUsername() + "的取款记录," + remark);
            // 判断站内信是否开启
            if (StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_send_message)) {

                messageService.sysMessageSend(stationId, drawRecord.getUserId(), drawRecord.getUsername(),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationNoPassDrawRec.getCode()),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationSorryOrder.getCode(), drawRecord.getOrderId(),
                                BaseI18nCode.stationNoPassReson.getCode(), remark),
                        Constants.STATUS_ENABLE);
            }
        } else if (status == MnyDrawRecord.STATUS_SUCCESS) {
            // 添加提款记录
            //  应该加上手续费
            sysUserWithdrawDao.handlerWithdraw(drawRecord.getUserId(), stationId, drawRecord.getDrawMoney().add(drawRecord.getFeeMoney()),
                    drawRecord.getCreateDatetime());
            // 判断站内信是否开启
            // 判断站内信发送是否开启
            if (StationConfigUtil.isOn(drawRecord.getStationId(), StationConfigEnum.deposit_send_message)) {

                messageService.sysMessageSend(drawRecord.getStationId(), drawRecord.getUserId(), drawRecord.getUsername(),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationPassDrawRec.getCode()),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccOrder.getCode(), drawRecord.getOrderId(),
                                BaseI18nCode.stationPassCheck.getCode()),
                        Constants.STATUS_DISABLE);
            }
            LogUtils.addLog("批准编号为:" + drawRecord.getOrderId() + "会员:" + drawRecord.getUsername() + "的取款记录");
        }
    }

    @Override
    public void dealWithdrawHandleSuccessOrfail(Long id, Integer status, String remark) {
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord drawRecord = getOne(id, stationId);
        drawRecord.setStatus(status);
        drawRecord.setRemark(remark);
        drawRecord.setOpDatetime(new Date());
        log.error("dealWithdrawHandleSuccessOrfail, id={}, status={}, remark={}", id, status, remark);
        if (status == MnyDrawRecord.STATUS_UNDO) {
            // 处理失败后 提款类型重置为1
            log.error("status == MnyDrawRecord.STATUS_UNDO");
            if (!mnyDrawRecordDao.updateStatus(id, stationId, status, remark, new Date(), null, null,
                    MnyDrawRecord.NORMAL, MnyDrawRecord.LOCK_FLAG_UNLOCK)) {
                throw new ParamException(BaseI18nCode.orderHadBeenHandel);
            }
            LogUtils.addLog(
                    "未批准编号为:" + drawRecord.getOrderId() + "会员:" + drawRecord.getUsername() + "的提现取款记录," + remark);
        } else if (status == MnyDrawRecord.STATUS_SUCCESS) {
            log.error("status == MnyDrawRecord.STATUS_SUCCESS");
            if (!mnyDrawRecordDao.updateStatus(id, stationId, status, remark, new Date(), null, null,
                    MnyDrawRecord.REPLCACE, null)) {
                throw new ParamException(BaseI18nCode.orderHadBeenHandel);
            }
            // 添加代付提款记录
            sysUserWithdrawDao.handlerWithdraw(drawRecord.getUserId(), stationId, drawRecord.getDrawMoney(),
                    drawRecord.getCreateDatetime());
            // 判断站内信息发送是否开启
            if (StationConfigUtil.isOn(drawRecord.getStationId(), StationConfigEnum.deposit_send_message)) {

                messageService.sysMessageSend(drawRecord.getStationId(), drawRecord.getUserId(), drawRecord.getUsername(),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationPassDrawRec.getCode()),
                        I18nTool.convertCodeToArrStr(BaseI18nCode.stationSuccOrder.getCode(), drawRecord.getOrderId(),
                                BaseI18nCode.stationPassCheck.getCode()),
                        Constants.STATUS_DISABLE);
            }
            LogUtils.addLog("编号为:" + drawRecord.getOrderId() + "会员:" + drawRecord.getUsername() + "的代付取款记录");
        } else {
            log.error("status unknow...");
        }
    }

    @Override
    public void doReplaceWithdrawHandle(Long id, Integer status, String remark, Long payId) {
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord drawRecord = getOne(id, stationId);
        StationReplaceWithDraw replaceWithDraw = stationReplaceWithDrawService.getReplaceWithDrawByPayId(payId,
                stationId);
        if (replaceWithDraw == null) {
            throw new ParamException(BaseI18nCode.depositMethodNotExist);
        }
        if (drawRecord == null) {
            throw new ParamException(BaseI18nCode.thirdTransLogNotExist);
        }
        if (drawRecord.getLockFlag().equals(MnyDrawRecord.LOCK_FLAG_UNLOCK)) {
            throw new ParamException(BaseI18nCode.orderNotLock);
        }
        // 判断该记录是否被其他用户锁定
        if (!drawRecord.getOpUserId().equals(LoginAdminUtil.getUserId())) {
            throw new ParamException(BaseI18nCode.withdrawOrderLockByOther);
        }
        // 判断记录是否处理未处理状态
        if (!drawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO)) {
            throw new ParamException(BaseI18nCode.orderHadBeenHandel);
        }
        String re = drawRecord.getRemark().contains("代付失败") ? drawRecord.getRemark()
                : replaceWithDraw.getPayType() + remark;
        if (!mnyDrawRecordDao.updateStatusAppendRemark(id, stationId, status, re, new Date(), payId, replaceWithDraw.getPayType(),
                MnyDrawRecord.REPLCACE, null)) {
            throw new BaseException("此记录已经被处理过！");
        }
    }

    @Override
    public void doReplaceHandle(HttpServletRequest request, Long id, Long payid, String payname, String icon,
                                String cardno, String onlineBank, String verifyCode) throws Exception {
        AdminUser adminUser = LoginAdminUtil.currentUser();
        if (adminUser.getType() != UserTypeEnum.ADMIN.getType() && !SystemConfig.SYS_MODE_DEVELOP) {
            throw new ParamException(BaseI18nCode.adminAuthority);
        }
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord drawRecord = getOne(id, stationId);
        if (drawRecord == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        if (adminUser.getWithdrawLimit() != null
                && adminUser.getWithdrawLimit().compareTo(drawRecord.getDrawMoney()) < 0) {
            throw new BaseException(BaseI18nCode.canntHandleXOrder, new Object[]{adminUser.getWithdrawLimit()});
        }
        if (drawRecord.getLockFlag().equals(MnyDrawRecord.LOCK_FLAG_UNLOCK)) {
            throw new ParamException(BaseI18nCode.orderNotLock);
        }
        // 判断该记录是否被其他用户锁定
        if (!drawRecord.getOpUserId().equals(adminUser.getId())) {
            throw new ParamException(BaseI18nCode.withdrawOrderLockByOther);
        }
        // 判断记录是否处理未处理状态
        if (!drawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO)) {
            throw new ParamException(BaseI18nCode.orderHadBeenHandel);
        }
        if ("undefined".equals(icon)) {
            throw new ParamException(BaseI18nCode.depositMethodNotExist);
        }

        // 代付出款设置
        StationReplaceWithDraw replaceWithDraw = stationReplaceWithDrawService.getReplaceWithDrawByPayId(payid,
                stationId);
        // 会员银行卡信息
        SysUserBank sysUserBank = sysUserBankDao.getLastBankInfo(drawRecord.getUserId(), stationId,
                cardno.replace(" ", ""));
        // 有可能是 PIX 的手机号，需要"+" 至于为什么传下来没有"+" 还没有研究
       /* if (sysUserBank == null && onlineBank != null && onlineBank.compareTo("PIX") == 0) {
            sysUserBank = sysUserBankDao.getLastBankInfo(drawRecord.getUserId(), stationId,
                    "+" + cardno.replace(" ", ""));
        }*/
        if (sysUserBank == null) {
            throw new ParamException(BaseI18nCode.bankCardInfoError);
        }
        if (sysUserBank.getStatus() == 1) {
            throw new ParamException(BaseI18nCode.bankCardBan);
        }

        BigDecimal money = drawRecord.getTrueMoney().setScale(2, RoundingMode.DOWN);
        if (replaceWithDraw.getMin() != null && money.compareTo(replaceWithDraw.getMin()) == -1) {
            throw new ParamException(BaseI18nCode.withdrawCanntLt, replaceWithDraw.getMin().toString());
        }
        if (replaceWithDraw.getMax() != null && money.compareTo(replaceWithDraw.getMax()) == 1) {
            throw new ParamException(BaseI18nCode.withdrawCanntGt, replaceWithDraw.getMax().toString());
        }
        LogUtils.addLog("代付申请人：" + LoginAdminUtil.getUsername() + "：代付申请icon：" + icon + "：站点:" + stationId + "：代付订单号 ："
                + drawRecord.getOrderId() + "申请时间：" + DateUtil.getCurrentTime());
        try {
            // 构建wapper参数
            String mechCode = replaceWithDraw.getMerchantCode();
            String mechKey = replaceWithDraw.getMerchantKey();
            String orderId = drawRecord.getOrderId();
            String orderAmount = new BigDecimal(drawRecord.getTrueMoney().toString()).stripTrailingZeros()
                    .toPlainString();
            String mechDomain = replaceWithDraw.getUrl();
            String bankCode = onlineBank == "" ? drawRecord.getBankName() : onlineBank;
            String merchantAccount = replaceWithDraw.getAccount();
            String referer = mechDomain;
            String payType = replaceWithDraw.getPayType();
            String payGetway = replaceWithDraw.getPayGetway();
            String appid = replaceWithDraw.getAppid();
            String domain = ServletUtils.getDomain();
            String account = drawRecord.getUsername();
            String extra = replaceWithDraw.getExtra();
            String payName = sysUserBank.getRealName();
            String payId = sysUserBank.getUserId().toString();
            String bankAddress = sysUserBank.getBankAddress();
            String cardNo = sysUserBank.getCardNo();

            ReplacePayWrapper wrapper = ReplacePayWrapperFactory.getWrapper(icon);
            JSONObject result = wrapper.wrap(mechCode, mechKey, orderId, orderAmount, mechDomain, bankCode, merchantAccount,
                    IpUtils.getIp(), referer, payType, payGetway, appid, domain, account, extra, payName, payId,
                    bankAddress, cardNo);
            log.error("thirdOrderId = {}", result.getString("thirdOrderId"));
            if (result.getString("thirdOrderId") != null) {
                if (result.getString("payNo") != null && !result.getString("payNo").equals("")) {
                    mnyDrawRecordDao.appenRemarkThrid(drawRecord.getId(), stationId, result.getString("thirdOrderId"), result.getString("payNo"));
                } else {
                    mnyDrawRecordDao.appenRemark(drawRecord.getId(), stationId, result.getString("thirdOrderId"));
                }
            }

        } catch (ParamException b) {
            LogUtils.addLog("代付异常，错误信息：" + b.getMessage() + " 申请人：" + LoginAdminUtil.getUsername() + " 代付icon：" + icon
                    + "：站点:" + stationId + " 订单号 ：" + drawRecord.getOrderId() + " 时间：" + DateUtil.getCurrentTime());
            throw new ParamException(BaseI18nCode.operateErrorReson, b.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("未找到:" + icon + " 错误：" + e.getMessage());
        }
    }

    @Override
    public Page<MnyDrawRecord> userCenterPage(Date start, Date end, String orderId, Boolean include, String username,
                                              Integer status) {
        SysUser login = LoginMemberUtil.currentUser();
        Long userId = login.getId();
        Long stationId = login.getStationId();
        boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
        if (StationConfigUtil.isOff(stationId, StationConfigEnum.proxy_view_account_data)) {
            include = false;
        } else {
            if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
                SysUser user = sysUserDao.findOneByUsername(username, stationId, null);
                if (user == null) {
                    throw new ParamException(BaseI18nCode.searchUserNotExist);
                }
                if (isMember) {// 会员则判断是否是推荐好友来的
                    if (!user.getRecommendId().equals(userId)) {
                        throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
                    }
                    include = false;// 会员推广只能查看直属的会员，不能再看下一级
                } else {// 代理推广来的
                    if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
                        throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
                    }
                }
                userId = user.getId();
            }
        }
        Page<MnyDrawRecord> recordPage = mnyDrawRecordDao.userCenterPage(stationId, userId, start, end, orderId,
                include, isMember, status);
        recordPage.getRows().forEach(x -> {
            x.setCardNo(x.getCardNo().length() > 4
                    ? x.getCardNo().substring(x.getCardNo().length() - 4, x.getCardNo().length())
                    : x.getCardNo());
            if (x.getDrawMoney() != null) {
                x.setDrawMoney(x.getDrawMoney().setScale(2, RoundingMode.DOWN));
            }
            x.setRemark(StringUtils.isNotEmpty(x.getRemark()) ? x.getRemark() : "-");
            if ((x.getStatus() != null && x.getStatus() == 1)
                    || (x.getRemark() != null && x.getRemark().contains(BaseI18nCode.stationOtherPay.getMessage()))) {
                x.setRemark("-");
            }
        });
        return recordPage;
    }

    @Override
    public Integer getCountOfUntreated(Long stationId, Boolean isCheck) {
        return mnyDrawRecordDao.getCountOfUntreated(stationId);
    }

    @Override
    public Integer getCount4Day(Long accountId, Long stationId) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date start = c.getTime();
        return mnyDrawRecordDao.getCount4Day(accountId, stationId, start, DateUtils.addDays(start, 1));
    }

    @Override
    public List<MnyDrawRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
                                             Date end) {
        return getRecordList(stationId, status, lockFlag, limit, begin, end, null, null);
    }

    @Override
    public List<MnyDrawRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin, Date end, String ip, String os) {
        return mnyDrawRecordDao.getRecordList(stationId, status, lockFlag, limit, begin, end, ip, os);
    }

    @Override
    public long countOfRecord(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin, Date end, String ip, String os, Long excludeUserId) {
        return mnyDrawRecordDao.countOfRecord(stationId, status, lockFlag, limit, begin, end, ip, os, excludeUserId);
    }

    @Override
    public boolean handelTimeoutDrawRecord(MnyDrawRecord record) {
        boolean success = mnyDrawRecordDao.handleTimeoutRecord(record.getStatus(), record.getRemark(),
                record.getStationId(), record.getId(), null) > 0;
        SysUser user = sysUserService.findOneById(record.getUserId(), record.getStationId());
        if (success) {
            if (record.getStatus() == MnyDrawRecord.STATUS_EXPIRE) {
                if (record.getDrawMoney() != null && record.getDrawMoney().compareTo(BigDecimal.ZERO) > 0) {
                    MnyMoneyBo mvo = new MnyMoneyBo();
                    mvo.setUser(user);
                    mvo.setMoney(record.getDrawMoney());
                    mvo.setMoneyRecordType(MoneyRecordType.WITHDRAW_ONLINE_FAILED);

                    List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationDrawFailNum.getCode(),record.getOrderId(),BaseI18nCode.stationDrawMoney.getCode(),
                            String.valueOf(record.getDrawMoney()),BaseI18nCode.stationUnit.getCode());
                    String remarkString = I18nTool.convertCodeToArrStr(remarkList);
                    mvo.setRemark(remarkString);

//                    mvo.setRemark(I18nTool.getMessage(BaseI18nCode.stationDrawFailNum, Locale.ENGLISH) + record.getOrderId()
//                            + I18nTool.getMessage(BaseI18nCode.stationDrawMoney, Locale.ENGLISH) + record.getDrawMoney()
//                            + I18nTool.getMessage(BaseI18nCode.stationUnit, Locale.ENGLISH));

                    mvo.setOrderId(record.getOrderId() + "");
                    mvo.setBizDatetime(record.getCreateDatetime());
                    sysUserMoneyService.updMnyAndRecord(mvo);
                    CacheUtil.delCache(CacheKey.STATISTIC, "admin:untreated:draw:" + record.getStationId());
                }
            }
        }
        return success;
    }

    @Override
    public void export(Date begin, Date end, Integer status, Integer type, String username, String proxyName,
                       String opUsername, String orderId, String levelIds, String pay, Long payId, String agentUser,
                       String bankName, Integer withdrawNum) {
        Long stationId = SystemUtil.getStationId();
        Long proxyId = null;
        if (StringUtils.isNotEmpty(proxyName)) {
            SysUser sysUser = sysUserDao.findOneByUsername(proxyName, stationId, UserTypeEnum.PROXY.getType());
            if (sysUser == null) {
                throw new ParamException(BaseI18nCode.proxyUnExist);
            }
            proxyId = sysUser.getId();
        }
        int lock = 0;
        if (status != null) {
            if (status == 1) {
                lock = MnyDrawRecord.LOCK_FLAG_UNLOCK;
            } else if (status == 10) {
                status = MnyDrawRecord.STATUS_UNDO;
                lock = MnyDrawRecord.LOCK_FLAG_LOCK;
            }
        }
        List<MnyDrawRecord> list = mnyDrawRecordDao.export(stationId, begin, end, username, status, lock, type, proxyId,
                opUsername, orderId, levelIds, pay, payId, agentUser, bankName, withdrawNum);
        String[] rowsName = new String[]{BaseI18nCode.stationSerialNumber.getMessage(),
                BaseI18nCode.stationMember.getMessage(), BaseI18nCode.stationAgent.getMessage(),
                BaseI18nCode.stationMemberNum.getMessage(), BaseI18nCode.stationMemberLevel.getMessage(),
                BaseI18nCode.stationRecName.getMessage(), BaseI18nCode.stationBankName.getMessage(),
                BaseI18nCode.stationBankNum.getMessage(), BaseI18nCode.stationDrawNum.getMessage(),
                BaseI18nCode.stationDateTime.getMessage(), BaseI18nCode.stationHandleTime.getMessage(),
                BaseI18nCode.stationStatus.getMessage(), BaseI18nCode.stationOperation.getMessage(),
                BaseI18nCode.stationRemark.getMessage()};
        if (StringUtils.isNotEmpty(opUsername)) {
            AdminUser admin = adminUserDao.findByUsername(opUsername, stationId);
            if (admin != null) {
                if (admin.getType() != UserTypeEnum.ADMIN.getType()) {
                    // PoiUtil.export("会员提款记录", rowsName, new ArrayList<>());
                    ExportToCvsUtil.export(BaseI18nCode.stationMemberDrawRec.getMessage(), rowsName, new ArrayList<>());
                    return;
                }
            }
        }
        Object[] objs;
        List<Object[]> dataList = new ArrayList<>();
        AdminUser adminUser = null;
        Map<Long, String> proxyNameMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            objs = new Object[rowsName.length];
            objs[0] = i + "";
            objs[1] = getStrNull(list.get(i).getUsername());
            objs[2] = getStrNull(getProxyName(proxyNameMap, list.get(i).getUserId(), stationId));
            objs[3] = getStrNull(list.get(i).getOrderId());
            objs[4] = getStrNull(list.get(i).getDegreeName());
            objs[5] = getStrNull(list.get(i).getRealName());
            objs[6] = getStrNull(list.get(i).getBankName());
            objs[7] = getStrNull(list.get(i).getCardNo()) + "'";
            objs[8] = getStrNull(list.get(i).getDrawMoney().setScale(2));
            objs[9] = getStrNull(list.get(i).getCreateDatetime());
            objs[10] = getStrNull(list.get(i).getOpDatetime());
            objs[11] = getDrawStatus(list.get(i).getStatus());
            if (list.get(i).getOpUserId() != null) {
                adminUser = adminUserDao.findOneById(list.get(i).getOpUserId(), stationId);
                objs[12] = adminUser == null ? "-" : adminUser.getUsername();
            } else {
                objs[12] = "-";
            }
            objs[13] = getStrNull(list.get(i).getRemark());
            dataList.add(objs);
        }
        ExportToCvsUtil.export(BaseI18nCode.stationMemberDrawRec.getMessage(), rowsName, dataList);
        LogUtils.addLog("导出会员提款记录");
    }

    private String getProxyName(Map<Long, String> proxyNameMap, Long accountId, Long stationId) {
        if (proxyNameMap.containsKey(accountId)) {
            return proxyNameMap.get(accountId);
        }
        SysUser acc = sysUserDao.findOneById(accountId, stationId);
        if (acc != null) {
            proxyNameMap.put(accountId, acc.getProxyName());
            return acc.getProxyName();
        }
        return "";
    }

    private String getStrNull(Object obj) {
        if (obj == null || obj.toString().isEmpty()) {
            return "-";
        }
        return obj.toString();
    }

    private String getDrawStatus(int type) {
        switch (type) {
            case MnyDrawRecord.STATUS_UNDO:
                return BaseI18nCode.stationNoHandler.getMessage();
            case MnyDrawRecord.STATUS_SUCCESS:
                return BaseI18nCode.stationDrawSucc.getMessage();
            case MnyDrawRecord.STATUS_FAIL:
                return BaseI18nCode.stationDrawFail.getMessage();
            case MnyDrawRecord.STATUS_EXPIRE:
                return BaseI18nCode.stationDrawPass.getMessage();
            default:
                return "-";
        }
    }

    @Override
    public void drawForApi(SysUser account, String ip, BigDecimal money, String orderId) {

    }

    @Override
    public MnyDrawRecord findOneByApiOrderId(Long stationId, String orderId) {
        return null;
    }

    @Override
    public MnyDrawRecord getHighestMoneyDrawRecord(Long stationId, Long accountId) {
        return mnyDrawRecordDao.getHighestMoneyDrawRecord(stationId, accountId);
    }

    @Override
    public void searchHandle(HttpServletRequest request, Long payId, Long stationId) throws Exception {
        MnyDrawRecord record = mnyDrawRecordDao.findOne(payId, stationId);
        if (record == null) {
            log.error("订单不存在:" + record);
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        StationReplaceWithDraw replaceWithDraw = stationReplaceWithDrawService
                .getReplaceWithDrawByPayId(record.getPayId(), stationId);
        log.error("查询station_replace_withdraw表记录:" + replaceWithDraw);
        try {
            ReplacePayWrapper wrapper = ReplacePayWrapperFactory.getWrapper(replaceWithDraw.getIcon());
            record.setTrueMoney(new BigDecimal(record.getTrueMoney().stripTrailingZeros().toPlainString()));
            log.error("ReplacePayWrapper:" + wrapper);
            wrapper.search(request, this, replaceWithDraw, record);
            LogUtils.addLog(record.getPayName() + "代付查询成功 ：操作员为 " + LoginAdminUtil.currentUser().getUsername()
                    + " :提款订单号为：" + record.getOrderId() + "：站点:" + SystemUtil.getStationId() + " 时间："
                    + DateUtil.getCurrentTime());
        } catch (Exception e) {
            LogUtils.addLog(record.getPayName() + "代付查询失败 ：操作员为 " + LoginAdminUtil.currentUser().getUsername()
                    + " :提款订单号为：" + record.getOrderId() + " ：站点:" + SystemUtil.getStationId() + " 时间："
                    + DateUtil.getCurrentTime());
            throw new ParamException(BaseI18nCode.operateErrorReson, e.getMessage());
        }
    }

    @Override
    public void remarkModify(Long id, String remark) {
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord mny = mnyDrawRecordDao.findOne(id, SystemUtil.getStationId());
        if (mny == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        mnyDrawRecordDao.changeRemark(id, stationId, remark);
        LogUtils.modifyLog("用户:" + mny.getUsername() + " 的备注从 " + mny.getRemark() + " 修改成 " + remark);
    }

    @Override
    public void bgRemarkModify(Long id, String remark) {
        Long stationId = SystemUtil.getStationId();
        MnyDrawRecord mny = mnyDrawRecordDao.findOne(id, SystemUtil.getStationId());
        if (mny == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        mnyDrawRecordDao.changeBgRemark(id, stationId, remark);
        LogUtils.modifyLog("用户:" + mny.getUsername() + " 的备注从 " + mny.getBgRemark() + " 修改成 " + remark);
    }

    @Override
    public DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end) {
        return mnyDrawRecordDao.getMoneyVo(userId, stationId, start, end);
    }

    @Override
    public void updateApiOrderId(String orderId, String newOrderId) {

    }

}
