package com.play.service.impl;

import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.MoneyRecordType;
import com.play.core.ScoreRecordTypeEnum;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.StationTaskStrategyDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserWithdrawDao;
import com.play.model.*;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationTaskStrategyService;
import com.play.service.SysUserBonusService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserScoreService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务管理策略
 *
 * @author admin
 */
@Service
public class StationTaskStrategyServiceImpl implements StationTaskStrategyService {
    @Autowired
    private StationTaskStrategyDao stationTaskStrategyDao;

    @Autowired
    private SysUserMoneyService sysUserMoneyService;

    @Autowired
    private MnyDepositRecordDao mnyDepositRecordDao;

    @Autowired
    private SysUserBonusService sysUserBonusService;

    @Autowired
    private SysUserScoreService sysUserScoreService;

    @Autowired
    private SysUserWithdrawDao sysUserWithdrawDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public Page<StationTaskStrategy> getPage(Integer taskType, Integer giftType, Integer valueType, Date begin,
                                             Date end, Long stationId) {
        return stationTaskStrategyDao.getPage(taskType, giftType, valueType, begin, end, stationId);
    }

    @Override
    public StationTaskStrategy getOne(Long id, Long stationId) {
        return stationTaskStrategyDao.getOne(id, stationId);
    }

    @Override
    public void delete(Long id, Long stationId) {
        stationTaskStrategyDao.delete(id, stationId);
    }

    @Override
    public void addSave(StationTaskStrategy com) {
        if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
            throw new ParamException(BaseI18nCode.selectActivityTime);
        }
        if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
            throw new ParamException(BaseI18nCode.endMustBeforeStart);
        }
        com.setCreateDatetime(new Date());
        com.setStatus(Constants.STATUS_ENABLE);
        if (com.getDepositCount() == null) {
            com.setDepositCount(0);
        }
        if (com.getMinMoney() == null) {
            com.setMinMoney(BigDecimal.ZERO);
        }
        if (com.getMaxMoney() == null) {
            com.setMaxMoney(new BigDecimal("10000000"));
        }
        if (com.getMinMoney().compareTo(com.getMaxMoney()) >= 0) {
            throw new ParamException(BaseI18nCode.depositMoneyMustLtMax);
        }
        validStrategy(com);
        com.setDepositConfigIds(StringUtils.isNotEmpty(com.getDepositConfigIds()) ? com.getDepositConfigIds() : "");
        stationTaskStrategyDao.save(com);
        LogUtils.addLog("添加任务管理策略");
    }


    private void validStrategy(StationTaskStrategy com) {
        List<StationTaskStrategy> list = stationTaskStrategyDao.findByDepositType(com.getTaskType(),
                com.getStationId(), Constants.STATUS_ENABLE, com.getValueType(), null, null);
        if (list == null || list.isEmpty()) {
            return;
        }
        long start = com.getStartDatetime().getTime();
        long end = com.getEndDatetime().getTime();
        BigDecimal min = com.getMinMoney();
        BigDecimal max = com.getMaxMoney();
        Set<Long> degreeIds, groupIds, configIds;
        Set<Long> curDegreeIds = getSet(com.getDegreeIds());
        Set<Long> curGroupIds = getSet(com.getGroupIds());
        Set<Long> curConfigIds = getSet(com.getDepositConfigIds());
        // 同一个支付方式，同一时间内，同一个充值金额范围内，同一个会员等级/组别范围内，同种赠送类型，同种赠送频率不能存在2条赠送策略
        for (StationTaskStrategy mcs : list) {
            if (com.getId() != null && com.getId().equals(mcs.getId())) {
                continue;
            }
            if (mcs.getEndDatetime().getTime() < start || mcs.getStartDatetime().getTime() > end) {
                continue;
            }
            if (mcs.getMaxMoney().compareTo(min) < 0 || mcs.getMinMoney().compareTo(max) > 0) {
                continue;
            }
            if (!mcs.getDepositCount().equals(com.getDepositCount())) {
                continue;
            }
            degreeIds = getSet(mcs.getDegreeIds());
            for (Long did : curDegreeIds) {
                if (degreeIds.contains(did)) {// 等级有包含
                    groupIds = getSet(mcs.getGroupIds());
                    for (Long gid : curGroupIds) {
                        if (groupIds.contains(gid)) {// 组别有包含
                            if (curConfigIds.isEmpty()) {
                                throw new ParamException(BaseI18nCode.activityConflict, mcs.getDesc());
                            }
                            configIds = getSet(mcs.getDepositConfigIds());
                            if (configIds.isEmpty()) {
                                throw new ParamException(BaseI18nCode.activityConflict, mcs.getDesc());
                            }
                            for (Long cid : curConfigIds) {
                                if (configIds.contains(cid)) {
                                    throw new ParamException(BaseI18nCode.depositMethodConflict, mcs.getDesc());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Set<Long> getSet(String ids) {
        Set<Long> set = new HashSet<Long>();
        if (ids == null) {
            return set;
        }
        for (String id : ids.split(",")) {
            if (StringUtils.isNotEmpty(id)) {
                set.add(NumberUtils.toLong(id));
            }
        }
        return set;
    }

    @Override
    public void updStatus(Integer status, Long id, Long stationId) {
        StationTaskStrategy mcs = stationTaskStrategyDao.getOne(id, stationId);
        if (mcs == null) {
            throw new ParamException(BaseI18nCode.depositStrategyNotExist);
        }
        String statusStr = I18nTool.getMessage(BaseI18nCode.disable);
        if (status == Constants.STATUS_ENABLE) {
            if (mcs.getEndDatetime().before(new Date())) {
                throw new ParamException(BaseI18nCode.endMustBeforeStart);
            }
            validStrategy(mcs);
            statusStr = I18nTool.getMessage(BaseI18nCode.enable);
        } else {
            status = Constants.STATUS_DISABLE;
        }
        if (!mcs.getStatus().equals(status)) {
            stationTaskStrategyDao.updStatus(id, status);
            LogUtils.modifyLog("修改任务管理策略：" + id + "状态为：" + statusStr);
        }
    }

    @Override
    public void update(StationTaskStrategy com) {
        if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
            throw new ParamException(BaseI18nCode.selectActivityTime);
        }
        if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
            throw new ParamException(BaseI18nCode.endMustBeforeStart);
        }
        if (com.getMinMoney() == null) {
            com.setMinMoney(BigDecimal.ZERO);
        }
        if (com.getMaxMoney() == null) {
            com.setMaxMoney(new BigDecimal("10000000"));
        }
        if (com.getMinMoney().compareTo(com.getMaxMoney()) >= 0) {
            throw new ParamException(BaseI18nCode.depositMoneyMustLtMax);
        }
        StationTaskStrategy old = stationTaskStrategyDao.getOne(com.getId(), com.getStationId());
        if (old == null) {
            throw new ParamException(BaseI18nCode.selectDepositStrategy);
        }
        old.setTaskType(com.getTaskType());
        old.setGiftType(com.getGiftType());
        old.setValueType(com.getValueType());

        old.setDepositCount(com.getDepositCount());
        if (old.getDepositCount() == null) {
            old.setDepositCount(0);
        }
        old.setGiftValue(com.getGiftValue());
        old.setUpperLimit(com.getUpperLimit());
        old.setMinMoney(com.getMinMoney());
        old.setMaxMoney(com.getMaxMoney());
        old.setBonusBackBetRate(com.getBonusBackBetRate());
        old.setBonusBackValue(com.getBonusBackValue());
        old.setBetRate(com.getBetRate());
        old.setStartDatetime(com.getStartDatetime());
        old.setEndDatetime(com.getEndDatetime());
        old.setRemark(com.getRemark());
        old.setDayupperLimit(com.getDayupperLimit() == null ? BigDecimal.ZERO : com.getDayupperLimit());
        old.setDepositConfigIds(StringUtils.isNotEmpty(com.getDepositConfigIds()) ? com.getDepositConfigIds() : "");
        old.setDegreeIds(com.getDegreeIds());
        old.setGroupIds(com.getGroupIds());
        if (old.getStatus() == Constants.STATUS_ENABLE) {
            validStrategy(old);
        }
        stationTaskStrategyDao.update(old);
        LogUtils.modifyLog("修改任务管理策略" + old.getId());
    }

    /**
     * 根据账变类型获取存款类型
     */
    private int getDepositType(int depositType) {
        int type = 0;
        if (depositType == MnyDepositRecord.TYPE_BANK) {
            type = StationDepositStrategy.TYPE_BANK;
        } else if (depositType == MnyDepositRecord.TYPE_HAND) {
            type = StationDepositStrategy.TYPE_ARTIFICIAL;
        }
        return type;
    }

    @Override
    public List<StationTaskStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
                                            Integer depositType, BigDecimal money, Date depositDate, Long payId) {
        int type = getDepositType(depositType);
        if (type == 0) {
            return null;
        }
        List<StationTaskStrategy> list = stationTaskStrategyDao.findByDepositType(type, account.getStationId(),
                Constants.STATUS_ENABLE, null, depositDate, money);
        list = filterByDepositCount(allDepositCount, dayDepositCount, list);
        list = filterByMemberDegreeAndGroup(account, list);
        list = filterByDepositConfig(list, payId);
        list = filterSameValueType(list);
        return list;
    }

    /**
     * 赠送同种类型的只取一个,积分，彩金每种只能一个
     *
     * @param list
     * @return
     */
    private List<StationTaskStrategy> filterSameValueType(List<StationTaskStrategy> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        Collections.sort(list, new Comparator<StationTaskStrategy>() {
            @Override
            public int compare(StationTaskStrategy o1, StationTaskStrategy o2) {// 赠送频率倒序
                return o2.getDepositCount().compareTo(o1.getDepositCount());
            }
        });
        Map<Integer, StationTaskStrategy> map = new HashMap<>();
        for (StationTaskStrategy s : list) {
            if (map.containsKey(s.getValueType())) {
                continue;
            }
            map.put(s.getValueType(), s);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 根据存款次数过滤
     *
     * @param depositCount    所有存款次数
     * @param dayDepositCount 今日存款次数
     * @param list
     * @return
     */
    private List<StationTaskStrategy> filterByDepositCount(Integer depositCount, Integer dayDepositCount,
                                                           List<StationTaskStrategy> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<StationTaskStrategy> rlist = new ArrayList<>();
        for (StationTaskStrategy m : list) {
            switch (m.getDepositCount()) {
                case StationTaskStrategy.addMosaic:// 打码倍数
                    if (depositCount == 1) {
                        rlist.add(m);
                    }
                    break;
                case StationTaskStrategy.first_withdraw:// 首次提款
                    if (depositCount == 2) {
                        rlist.add(m);
                    }
                    break;

                default:
                    break;
            }
        }
        return rlist;
    }

    /**
     * 根据会员等级和组别过滤策略
     */
    private List<StationTaskStrategy> filterByMemberDegreeAndGroup(SysUser user, List<StationTaskStrategy> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (user == null || user.getDegreeId() == null || user.getDegreeId() <= 0 || user.getGroupId() == null
                || user.getGroupId() <= 0) {
            return null;
        }
        List<StationTaskStrategy> rlist = new ArrayList<>();
        Set<Long> set = null;
        for (StationTaskStrategy m : list) {
            set = getSet(m.getDegreeIds());
            if (set.contains(user.getDegreeId())) {// 等级包含在里面
                set = getSet(m.getGroupIds());
                if (set.contains(user.getGroupId())) {// 组别包含在里面
                    rlist.add(m);
                }
            }
        }
        return rlist;
    }

    private List<StationTaskStrategy> filterByDepositConfig(List<StationTaskStrategy> strategies, Long payId) {
        if (strategies == null || strategies.isEmpty()) {
            return strategies;
        }
        return strategies.stream().filter(x -> StringUtils.isEmpty(x.getDepositConfigIds())
                || (x.getDepositConfigIds()).contains("," + payId + ",")).collect(Collectors.toList());
    }

    /**
     * 处理每日首次提款赠送定时任务
     *
     * @return
     */
    public void withdrawalGiftPolicyHandler(StationTaskStrategy ds, Date date) {

        BigDecimal amount = BigDecimal.ZERO;
        // 固定数额
        if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
            amount = ds.getGiftValue();
            amountPointGift(ds, date, amount);
        } else {
            // 查询 当前站点有哪些用户【首次提款】
            List<SysUserWithdraw> userList = sysUserWithdrawDao.queryFirstWithDraw(ds.getStationId(), DateUtil.toDateStr(date));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            for (SysUserWithdraw userWithdraw : userList) {
                SysUser user = sysUserDao.findOneById(userWithdraw.getUserId(), userWithdraw.getStationId());
                if (Objects.isNull(user)) {
                    continue;
                }
                // 会员等级是否包含
                String[] split = ds.getDegreeIds().split(",");
                List<String> stringList = Arrays.asList(split);
                boolean degreeFlag = stringList.contains(user.getDegreeId().toString());

                // 会员组别是否包含
                String[] splitGroup = ds.getGroupIds().split(",");
                List<String> splitGroups = Arrays.asList(splitGroup);
                boolean groupFlag = splitGroups.contains(user.getGroupId().toString());

                if (degreeFlag && groupFlag) {
                    // 浮动比例
                    amount = userWithdraw.getFirstMoney().multiply(ds.getGiftValue()).divide(BigDecimalUtil.HUNDRED).setScale(4,
                            BigDecimal.ROUND_FLOOR);
                    BigDecimal limit = ds.getUpperLimit();
                    // 如果超过最大限制，取最大限制的值
                    if (limit != null && limit.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(limit) > 0) {
                        amount = limit;
                    }
                    commonProcess(ds, amount, user);
                }

            }
        }


    }

    private void commonProcess(StationTaskStrategy ds, BigDecimal amount, SysUser user) {
        // 赠送彩金
        if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {
            MnyMoneyBo mvo = new MnyMoneyBo();
            mvo.setUser(user);
            mvo.setMoney(amount);
            mvo.setMoneyRecordType(MoneyRecordType.FIRST_WITHDRAW);
            List <String> remarkList = I18nTool.convertCodeToList();
            if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
                //mvo.setRemark(I18nTool.getMessage(BaseI18nCode.staionFirstSaveSend, Locale.ENGLISH) + ds.getGiftValue());
                remarkList.add(BaseI18nCode.staionFirstSaveSend.getCode());
                remarkList.add(String.valueOf(ds.getGiftValue()));
            } else {
                //mvo.setRemark(I18nTool.getMessage(BaseI18nCode.staionFirstSaveSend, Locale.ENGLISH) + ds.getGiftValue() + "%");
                remarkList.add(BaseI18nCode.staionFirstSaveSend.getCode());
                remarkList.add(String.valueOf(ds.getGiftValue()));
                remarkList.add("%");
            }

            String remarkString = I18nTool.convertCodeToArrStr(remarkList);
            mvo.setRemark(remarkString);

            SysUserMoneyHistory sysUserMoneyHistory = sysUserMoneyService.updMnyAndRecord(mvo);
//                    mnyDepositRecordDao.updateGiftMoney(record.getId(), amount, record.getStationId());
            //同时记录用户的存款赠送奖金记录
            saveUserBonus(user, amount, sysUserMoneyHistory.getId(), mvo.getMoneyRecordType());
        } else {// 赠送积分
            sysUserScoreService.operateScore(ScoreRecordTypeEnum.FIRST_WITHDRAW_GIFT_ACTIVITY, user,
                    amount.longValue(),
                    I18nTool.getMessage(BaseI18nCode.stationScoreFirstWithDraw, Locale.ENGLISH) + amount.longValue());
        }
    }

    private void amountPointGift(StationTaskStrategy ds, Date date, BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            // 查询 当前站点有哪些用户【首次提款】
            List<SysUserWithdraw> userList = sysUserWithdrawDao.queryFirstWithDraw(ds.getStationId(), DateUtil.toDateStr(date));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            for (SysUserWithdraw userWithdraw : userList) {
                SysUser user = sysUserDao.findOneById(userWithdraw.getUserId(), userWithdraw.getStationId());
                if (Objects.isNull(user)) {
                    continue;
                }
                // 会员等级是否包含
                String[] split = ds.getDegreeIds().split(",");
                List<String> stringList = Arrays.asList(split);
                boolean degreeFlag = stringList.contains(user.getDegreeId().toString());

                // 会员组别是否包含
                String[] splitGroup = ds.getGroupIds().split(",");
                List<String> splitGroups = Arrays.asList(splitGroup);
                boolean groupFlag = splitGroups.contains(user.getGroupId().toString());

                if (degreeFlag && groupFlag) {
                    // 赠送彩金
                    commonProcess(ds, amount, user);
                }

            }


        }
    }


    private void saveUserBonus(SysUser user, BigDecimal amount,
                               Long moneyHistoryId, MoneyRecordType moneyRecordType) {
        SysUserBonus sysUserBonus = new SysUserBonus();
        sysUserBonus.setMoney(amount);
        sysUserBonus.setUserId(user.getId());
//        sysUserBonus.setRecordId(recordId);
        sysUserBonus.setCreateDatetime(new Date());
        sysUserBonus.setUsername(user.getUsername());
        sysUserBonus.setRecommendId(user.getRecommendId());
        sysUserBonus.setProxyId(user.getProxyId());
        sysUserBonus.setBonusType(moneyRecordType.getType());
        sysUserBonus.setProxyName(user.getProxyName());
        sysUserBonus.setStationId(user.getStationId());
        sysUserBonus.setUserType(user.getType());
        sysUserBonus.setDailyMoneyId(moneyHistoryId);
        sysUserBonusService.saveBonus(sysUserBonus);
    }

    public List<StationTaskStrategy> currentUserTaskList(SysUser user) {
        List<StationTaskStrategy> taskStrategies = stationTaskStrategyDao.currentUserTaskList(user);
        List<StationTaskStrategy> newTaskStrategies=new ArrayList<>();
        for (StationTaskStrategy strategy : taskStrategies) {
            String degreeIds = strategy.getDegreeIds();
            List<String> splitDegree = Arrays.asList(degreeIds.split(","));
            String stationId = strategy.getGroupIds();
            List<String> splitGroup = Arrays.asList(stationId.split(","));
            if (splitDegree.contains(user.getDegreeId().toString()) && (splitGroup.contains(user.getGroupId().toString()))){
                newTaskStrategies.add(strategy);
            }
        }
        return newTaskStrategies;
    }
}
