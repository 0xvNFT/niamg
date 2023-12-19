package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.dao.StationKickbackStrategyDao;
import com.play.model.StationKickbackStrategy;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationKickbackStrategyService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * 会员按日反水策略
 *
 * @author admin
 *
 */
@Service
public class StationKickbackStrategyServiceImpl implements StationKickbackStrategyService {

	@Autowired
	private StationKickbackStrategyDao stationKickbackStrategyDao;

	@Override
	public Page<StationKickbackStrategy> adminPage(Long stationId, Integer type) {
		return stationKickbackStrategyDao.adminPage(stationId, type);
	}

	@Override
	public void addSave(StationKickbackStrategy strategy) {
		if (strategy.getType() == null || (strategy.getType() != MnyMoneyBo.bet_type_sport
				&& strategy.getType() != MnyMoneyBo.bet_type_egame && strategy.getType() != MnyMoneyBo.bet_type_live
				&& strategy.getType() != MnyMoneyBo.bet_type_lot && strategy.getType() != MnyMoneyBo.bet_type_chess
				&& strategy.getType() != MnyMoneyBo.bet_type_esport
				&& strategy.getType() != MnyMoneyBo.bet_type_fishing)) {
			throw new ParamException(BaseI18nCode.stationKickbackType);
		}

		if (strategy.getMinBet() == null || strategy.getMinBet().compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.inputStationKickbackHasValue);
		}
		BigDecimal kickBack = strategy.getKickback();
		if (kickBack == null || kickBack.compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.inputKickbackBetValue);
		}
		kickBack = kickBack.divide(BigDecimal.valueOf(100));
		if (strategy.getMaxBack() != null && strategy.getMaxBack().compareTo(BigDecimal.ZERO) > 0) {
			if (strategy.getMaxBack().compareTo(strategy.getMinBet().multiply(kickBack)) <= 0) {
				throw new ParamException(BaseI18nCode.stationKickbackMustHighValue);
			}
		}
		StationKickbackStrategy kickbackStrategy = stationKickbackStrategyDao.getByMinBetType(strategy.getMinBet(),
				strategy.getStationId(), strategy.getType());
		if (kickbackStrategy != null && !kickbackStrategy.getId().equals(strategy.getId())) {
			throw new ParamException(BaseI18nCode.stationKickbackHasMinValue);
		}
		strategy.setCreateDatetime(new Date());
		stationKickbackStrategyDao.save(strategy);
		LogUtils.addLog("新增会员反水策略");
	}

	@Override
	public void modifySave(StationKickbackStrategy strategy) {
		if (strategy.getType() == null || (strategy.getType() != MnyMoneyBo.bet_type_sport
				&& strategy.getType() != MnyMoneyBo.bet_type_egame && strategy.getType() != MnyMoneyBo.bet_type_live
				&& strategy.getType() != MnyMoneyBo.bet_type_lot && strategy.getType() != MnyMoneyBo.bet_type_chess
				&& strategy.getType() != MnyMoneyBo.bet_type_esport
				&& strategy.getType() != MnyMoneyBo.bet_type_fishing)) {
			throw new ParamException(BaseI18nCode.stationKickbackType);
		}

		if (strategy.getMinBet() == null) {
			throw new ParamException(BaseI18nCode.inputStationKickbackHasValue);
		}
		BigDecimal kickBack = strategy.getKickback();
		if (kickBack == null || kickBack.compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.inputKickbackBetValue);
		}
		kickBack = kickBack.divide(BigDecimal.valueOf(100));
		if (strategy.getMaxBack() != null && strategy.getMaxBack().compareTo(BigDecimal.ZERO) > 0) {
			if (strategy.getMaxBack().compareTo(strategy.getMinBet().multiply(kickBack)) <= 0) {
				throw new ParamException(BaseI18nCode.stationKickbackMustHighValue);
			}
		}
		StationKickbackStrategy old = stationKickbackStrategyDao.findOne(strategy.getId(), strategy.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationKickbackNotExist);
		}
		if (old.getMinBet().compareTo(strategy.getMinBet()) != 0) {
			StationKickbackStrategy kickbackStrategy = stationKickbackStrategyDao.getByMinBetType(strategy.getMinBet(),
					strategy.getStationId(), strategy.getType());
			if (kickbackStrategy != null && !kickbackStrategy.getId().equals(strategy.getId())) {
				throw new ParamException(BaseI18nCode.stationKickbackHasMinValue);
			}
		}
		old.setKickback(strategy.getKickback());
		old.setBetRate(strategy.getBetRate());
		old.setMaxBack(strategy.getMaxBack());
		old.setStatus(strategy.getStatus());
		old.setType(strategy.getType());
		old.setRemark(strategy.getRemark());
		old.setMinBet(strategy.getMinBet());
		old.setDegreeIds(strategy.getDegreeIds());
		old.setGroupIds(strategy.getGroupIds());
		stationKickbackStrategyDao.update(old);
		LogUtils.log("修改会员反水策略", LogTypeEnum.MODIFY_DATA);
	}

	@Override
	public StationKickbackStrategy findOne(Long stationId, Long id) {
		return stationKickbackStrategyDao.findOne(id, stationId);
	}

	@Override
	public void delete(Long id) {
		stationKickbackStrategyDao.deleteById(id);
	}

	@Override
	public void changeStatus(Long id, Integer status) {
		stationKickbackStrategyDao.changeStatus(id, SystemUtil.getStationId(), status);
	}
}
