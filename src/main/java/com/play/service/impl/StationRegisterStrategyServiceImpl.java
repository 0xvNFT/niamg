package com.play.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.common.Constants;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.dao.StationRegisterStrategyDao;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositStrategy;
import com.play.model.StationRegisterStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationRegisterStrategyService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户注册赠送策略
 *
 * @author admin
 */
@Service
public class StationRegisterStrategyServiceImpl implements StationRegisterStrategyService {
    private static final Logger logger = LoggerFactory.getLogger(StationRegisterStrategyServiceImpl.class);
    @Autowired
    private StationRegisterStrategyDao stationRegisterStrategyDao;

    @Override
    public Page<StationRegisterStrategy> getPage(Integer depositType, Integer giftType, Integer valueType, Date begin,
                                                 Date end, Long stationId) {
        return stationRegisterStrategyDao.getPage(giftType, valueType, begin, end, stationId);
    }

    @Override
    public StationRegisterStrategy getOne(Long id, Long stationId) {
        return stationRegisterStrategyDao.getOne(id, stationId);
    }

    @Override
    public void delete(Long id, Long stationId) {
        stationRegisterStrategyDao.delete(id, stationId);
    }

    @Override
    public void addSave(StationRegisterStrategy com) {
        if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
            throw new ParamException(BaseI18nCode.selectActivityTime);
        }
        if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
            throw new ParamException(BaseI18nCode.endMustBeforeStart);
        }
        com.setCreateDatetime(new Date());
        com.setStatus(Constants.STATUS_ENABLE);
        validStrategy(com);
        stationRegisterStrategyDao.save(com);
        LogUtils.addLog("添加存款赠送策略");
    }



    private void validStrategy(StationRegisterStrategy com) {
        List<StationRegisterStrategy> list = stationRegisterStrategyDao.find(com.getStationId(), Constants.STATUS_ENABLE, com.getValueType(), null, null);
        if (list == null || list.isEmpty()) {
            return;
        }
        long start = com.getStartDatetime().getTime();
        long end = com.getEndDatetime().getTime();
        Set<Long> degreeIds, groupIds, configIds;
        Set<Long> curDegreeIds = getSet(com.getDegreeIds());
        // 同一时间内，同一个会员等级/组别范围内，同种赠送类型 不能存在2条赠送策略
        for (StationRegisterStrategy mcs : list) {
            if (com.getId() != null && com.getId().equals(mcs.getId())) {
                continue;
            }
            if (mcs.getEndDatetime().getTime() < start || mcs.getStartDatetime().getTime() > end) {
                continue;
            }
            degreeIds = getSet(mcs.getDegreeIds());
            for (Long did : curDegreeIds) {
                if (degreeIds.contains(did)) {// 等级有包含
                    throw new ParamException(BaseI18nCode.activityConflict, mcs.getDesc());
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
        StationRegisterStrategy mcs = stationRegisterStrategyDao.getOne(id, stationId);
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
            stationRegisterStrategyDao.updStatus(id, status);
            LogUtils.modifyLog("修改存款赠送策略：" + id + "状态为：" + statusStr);
        }
    }

    @Override
    public void update(StationRegisterStrategy com) {
        if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
            throw new ParamException(BaseI18nCode.selectActivityTime);
        }
        if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
            throw new ParamException(BaseI18nCode.endMustBeforeStart);
        }
        StationRegisterStrategy old = stationRegisterStrategyDao.getOne(com.getId(), com.getStationId());
        if (old == null) {
            throw new ParamException(BaseI18nCode.selectDepositStrategy);
        }
        old.setGiftType(com.getGiftType());
        old.setBackGiftType(com.getBackGiftType());
        old.setValueType(com.getValueType());
        old.setGiftValue(com.getGiftValue());
        old.setBonusBackValue(com.getBonusBackValue()==null?BigDecimal.ZERO:com.getBonusBackValue());
        old.setBonusBackBetRate(com.getBonusBackBetRate()==null?BigDecimal.ZERO:com.getBonusBackBetRate());
        old.setUpperLimit(com.getUpperLimit());
        old.setBetRate(com.getBetRate());
        old.setStartDatetime(com.getStartDatetime());
        old.setEndDatetime(com.getEndDatetime());
        old.setRemark(com.getRemark());
        old.setDegreeIds(com.getDegreeIds());
        old.setGroupIds(com.getGroupIds());
        if (old.getStatus() == Constants.STATUS_ENABLE) {
            validStrategy(old);
        }
        stationRegisterStrategyDao.update(old);
        LogUtils.modifyLog("修改存款赠送策略" + old.getId());
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
    public List<StationRegisterStrategy> filter(SysUser account, Date registerDate) {
        List<StationRegisterStrategy> list = stationRegisterStrategyDao.find(account.getStationId(),
                Constants.STATUS_ENABLE, null, registerDate, null);
        logger.error("registerStrategy filter, username:{}, stationId:{}, registerDate:{}", account.getUsername(), account.getStationId(), DateUtil.toDatetimeStr(registerDate));

        list = filterByMemberDegreeAndGroup(account, list);
        list = filterSameValueType(list);

        logger.error("registerStrategy filter, list:{}", JSON.toJSON(list));
        return list;
    }

    /**
     * 赠送同种类型的只取一个,积分，彩金每种只能一个
     *
     * @param list
     * @return
     */
    private List<StationRegisterStrategy> filterSameValueType(List<StationRegisterStrategy> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        logger.error("registerStrategy filter filterSameValueType, size:{}", list.size());

        Map<Integer, StationRegisterStrategy> map = new HashMap<>();
        for (StationRegisterStrategy s : list) {
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
     * @param depositCount
     * @param list
     * @return
     */
    private List<StationRegisterStrategy> filterByDepositCount(Integer depositCount, Integer dayDepositCount, List<StationRegisterStrategy> list) {
        return list;
    }

    /**
     * 根据会员等级和组别过滤策略
     */
    private List<StationRegisterStrategy> filterByMemberDegreeAndGroup(SysUser user, List<StationRegisterStrategy> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        logger.error("registerStrategy filter filterByMemberDegreeAndGroup, size:{}", list.size());
        if (user == null || user.getDegreeId() == null || user.getDegreeId() <= 0 || user.getGroupId() == null
                || user.getGroupId() <= 0) {
            return null;
        }
        List<StationRegisterStrategy> rlist = new ArrayList<>();
        Set<Long> set = null;
        for (StationRegisterStrategy m : list) {
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

    private List<StationRegisterStrategy> filterByDepositConfig(List<StationRegisterStrategy> strategies, Long payId) {
        if (strategies == null || strategies.isEmpty()) {
            return strategies;
        }
        return strategies;
//        return strategies.stream().filter(x -> StringUtils.isEmpty(x.getDepositConfigIds())
//                || (x.getDepositConfigIds()).contains("," + payId + ",")).collect(Collectors.toList());
    }
}

