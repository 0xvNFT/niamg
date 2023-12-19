package com.play.service.impl;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationDrawRuleStrategyDao;
import com.play.model.StationDrawFeeStrategy;
import com.play.model.StationDrawRuleStrategy;
import com.play.model.SysUser;
import com.play.model.SysUserBetNum;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDrawRuleStrategyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationDrawRuleStrategyServiceImpl implements StationDrawRuleStrategyService {

    @Autowired
    StationDrawRuleStrategyDao stationDrawRuleStrategyDao;
    @Autowired
    private SysUserDegreeService userDegreeService;
    @Autowired
    private SysUserGroupService userGroupService;

    @Override
    public void save(StationDrawRuleStrategy strategy) {
        if (strategy.getType() == null) {
            throw new ParamException(BaseI18nCode.stationConfigTypeNotNull);
        }
        if (strategy.getValue() == null) {
            throw new ParamException(BaseI18nCode.parameterEmpty);
        }
        strategy.setStatus(Constants.STATUS_ENABLE);
        stationDrawRuleStrategyDao.save(strategy);
        LogUtils.addLog("新增提款刷子规则策略");
    }

    @Override
    public void modify(StationDrawRuleStrategy strategy) {
        if (strategy.getType() == null) {
            throw new ParamException(BaseI18nCode.stationConfigTypeNotNull);
        }
        if (strategy.getValue() == null) {
            throw new ParamException(BaseI18nCode.parameterEmpty);
        }
        StationDrawRuleStrategy old = stationDrawRuleStrategyDao.findOne(strategy.getId(), strategy.getStationId());
        if (old == null) {
            throw new ParamException(BaseI18nCode.drawFeeStrategyNotExist);
        }
        old.setType(strategy.getType());
        old.setValue(strategy.getValue());
        old.setDays(strategy.getDays());
        old.setLimitCountry(strategy.getLimitCountry());
        old.setRate(strategy.getRate());
        old.setDegreeIds(strategy.getDegreeIds());
        old.setGroupIds(strategy.getGroupIds());
        old.setRemark(strategy.getRemark());
        old.setStatus(strategy.getStatus());
        stationDrawRuleStrategyDao.update(strategy);
        LogUtils.modifyLog("修改提款刷子规则策略");
    }

    @Override
    public void delete(Long id, Long stationId) {
        StationDrawRuleStrategy old = stationDrawRuleStrategyDao.findOne(id, stationId);
        if (old == null) {
            throw new ParamException(BaseI18nCode.drawFeeStrategyNotExist);
        }
        stationDrawRuleStrategyDao.deleteById(id);
        LogUtils.delLog("删除提款刷子规则策略");
    }

    @Override
    public Page<StationDrawRuleStrategy> getPage(Long stationId, Integer status) {
        Page<StationDrawRuleStrategy> page = stationDrawRuleStrategyDao.getPage(stationId, status);
        if (page.hasRows()) {
            for (StationDrawRuleStrategy s : page.getRows()) {
                s.setDegreeNames(userDegreeService.getDegreeNames(s.getDegreeIds(), stationId));
                s.setGroupNames(userGroupService.getGroupNames(s.getGroupIds(), stationId));
            }
        }
        return page;
    }

    @Override
    public List<StationDrawRuleStrategy> find(Long degreeId, Long groupId, Long stationId, Integer status) {
        return stationDrawRuleStrategyDao.getList(stationId, degreeId, groupId, status);
    }

    @Override
    public StationDrawRuleStrategy findOne(Long id, Long stationId) {
        return stationDrawRuleStrategyDao.findOne(id, stationId);
    }

    @Override
    public void changeStatus(Long id, Integer status, Long stationId) {
        StationDrawRuleStrategy mcs = stationDrawRuleStrategyDao.findOne(id, stationId);
        if (mcs == null) {
            throw new ParamException(BaseI18nCode.drawFeeStrategyNotExist);
        }
        String statusStr = I18nTool.getMessage(BaseI18nCode.enable);
//        String statusStr = "启用";
        if (status != Constants.STATUS_ENABLE) {
//            statusStr = "禁用";
            statusStr = I18nTool.getMessage(BaseI18nCode.disable);
            status = Constants.STATUS_DISABLE;
        }
        if (!mcs.getStatus().equals(status)) {
            stationDrawRuleStrategyDao.updStatus(id, status, stationId);
            LogUtils.modifyStatusLog("修提款手续费策略：" + id + "状态为：" + statusStr);
        }
    }

    @Override
    public List<StationDrawRuleStrategy> getSuitableFeeStrategys(SysUser user) {
        List<StationDrawRuleStrategy> dfs = stationDrawRuleStrategyDao.getList(user.getStationId(), user.getDegreeId(),
                user.getGroupId(),Constants.STATUS_ENABLE);
        return dfs;
    }
}
