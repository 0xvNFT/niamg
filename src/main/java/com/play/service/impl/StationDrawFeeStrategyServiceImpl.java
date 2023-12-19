package com.play.service.impl;

import java.util.List;

import com.play.spring.config.i18n.I18nTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationDrawFeeStrategyDao;
import com.play.model.StationDrawFeeStrategy;
import com.play.model.SysUser;
import com.play.model.SysUserBetNum;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDrawFeeStrategyService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 */
@Service
public class StationDrawFeeStrategyServiceImpl implements StationDrawFeeStrategyService {

	@Autowired
	private StationDrawFeeStrategyDao stationDrawFeeStrategyDao;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	@Override
	public void save(StationDrawFeeStrategy strategy) {
		if (strategy.getFeeType() == null) {
			throw new ParamException(BaseI18nCode.selectFeeType);
		}
		if (strategy.getFeeValue() == null) {
			throw new ParamException(BaseI18nCode.inputFeeMOney);
		}
		if (strategy.getDrawNum() != null && strategy.getDrawNum() < 0) {
			throw new ParamException(BaseI18nCode.inputRightFreeNum);
		}
		strategy.setStatus(Constants.STATUS_ENABLE);
		stationDrawFeeStrategyDao.save(strategy);
		LogUtils.addLog("新增提款手续费策略");
	}

	@Override
	public void modify(StationDrawFeeStrategy strategy) {
		if (strategy.getFeeType() == null) {
			throw new ParamException(BaseI18nCode.selectFeeType);
		}
		if (strategy.getFeeValue() == null) {
			throw new ParamException(BaseI18nCode.inputFeeMOney);
		}
		if (strategy.getDrawNum() != null && strategy.getDrawNum() < 0) {
			throw new ParamException(BaseI18nCode.inputRightFreeNum);
		}
		StationDrawFeeStrategy old = stationDrawFeeStrategyDao.findOne(strategy.getId(), strategy.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.drawFeeStrategyNotExist);
		}
		old.setFeeType(strategy.getFeeType());
		old.setFeeValue(strategy.getFeeValue());
		old.setDrawNum(strategy.getDrawNum());
		old.setLowerLimit(strategy.getLowerLimit());
		old.setUpperLimit(strategy.getUpperLimit());
		old.setRemark(strategy.getRemark());
		old.setStatus(strategy.getStatus());
		stationDrawFeeStrategyDao.update(strategy);
		LogUtils.modifyLog("修改提款手续费策略");
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationDrawFeeStrategy old = stationDrawFeeStrategyDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.drawFeeStrategyNotExist);
		}
		stationDrawFeeStrategyDao.deleteById(id);
		LogUtils.delLog("删除提款手续费策略");
	}

	@Override
	public Page<StationDrawFeeStrategy> getPage(Long stationId, Integer status) {
		Page<StationDrawFeeStrategy> page = stationDrawFeeStrategyDao.getPage(stationId, status);
		if (page.hasRows()) {
			for (StationDrawFeeStrategy s : page.getRows()) {
				s.setDegreeNames(userDegreeService.getDegreeNames(s.getDegreeIds(), stationId));
				s.setGroupNames(userGroupService.getGroupNames(s.getGroupIds(), stationId));
			}
		}
		return page;
	}

    @Override
    public StationDrawFeeStrategy findOne(Long id, Long stationId, String degreeIds, String groupIds) {
        return stationDrawFeeStrategyDao.findOne(id, stationId, degreeIds, groupIds);
    }

    @Override
    public StationDrawFeeStrategy findOne(Long id, Long stationId) {
        return stationDrawFeeStrategyDao.findOne(id, stationId);
    }

	@Override
	public void changeStatus(Long id, Integer status, Long stationId) {
		StationDrawFeeStrategy mcs = stationDrawFeeStrategyDao.findOne(id, stationId);
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
			stationDrawFeeStrategyDao.updStatus(id, status, stationId);
			LogUtils.modifyStatusLog("修提款手续费策略：" + id + "状态为：" + statusStr);
		}
	}

	@Override
	public StationDrawFeeStrategy getSuitableFeeStrategy(SysUser user) {
		if (!StationConfigUtil.isOn(user.getStationId(), StationConfigEnum.bet_num_not_enough_can_draw)) {
			// 没有开启 打码量不够读取手续费策略收取手续费 开关
			return null;
		}
		// 开启打码量不够读取手续费策略收取手续费后过滤策略
		List<StationDrawFeeStrategy> dfs = stationDrawFeeStrategyDao.getList(user.getStationId(), user.getDegreeId(),
				user.getGroupId());// 手续费高的排前面
		StationDrawFeeStrategy strategy = null;
		if (dfs != null && !dfs.isEmpty()) {
			strategy = dfs.get(0);
		}
		if (strategy != null) {
			SysUserBetNum betNum = userBetNumService.findOne(user.getId(), user.getStationId());
			if (betNum.getBetNum().compareTo(betNum.getDrawNeed()) >= 0) {
				// 当前打码量大于提款所需打码量，就不需要使用策略
				return null;
			}
		}
		return strategy;
	}
}
