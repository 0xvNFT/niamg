package com.play.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.StationRebateDao;
import com.play.model.StationRebate;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationRebateService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 站点返点设置信息
 *
 * @author admin
 *
 */
@Service
public class StationRebateServiceImpl implements StationRebateService {

	@Autowired
	private StationRebateDao stationRebateDao;

	@Override
	public void init(Long stationId) {
		StationRebate r = new StationRebate();
		r.setStationId(stationId);
		r.setUserType(StationRebate.USER_TYPE_PROXY);
		r.setType(StationRebate.TYPE_SAME);
		r.setRebateMode(StationRebate.REBATE_MODE_AUTO);
		BigDecimal two = new BigDecimal("2");
		r.setLive(two);
		r.setEgame(two);
		r.setChess(two);
		r.setEsport(two);
		r.setSport(two);
		r.setFishing(two);
		r.setLottery(two);
		r.setLevelDiff(BigDecimal.ZERO);
		r.setMaxDiff(two);
		r.setBetRate(BigDecimal.ONE);
		stationRebateDao.save(r);

		r.setUserType(StationRebate.USER_TYPE_MEMBER);
		r.setId(null);
		stationRebateDao.save(r);
	}

	@Override
	public Page<StationRebate> getPage(Long stationId, Integer userType, Integer type) {
		return stationRebateDao.getPage(stationId, userType, type);
	}

	@Override
	public StationRebate findOneById(Long id) {
		return stationRebateDao.findOneById(id);
	}
	
	@Override
	public void modifyMember(StationRebate rebate) {
		StationRebate old = stationRebateDao.findOneById(rebate.getId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationWebSiteConfigNot);
		}	
		old.setRebateMode(rebate.getRebateMode());
		old.setLive(nullTo0(rebate.getLive()));
		old.setEgame(nullTo0(rebate.getEgame()));
		old.setChess(nullTo0(rebate.getChess()));
		old.setEsport(nullTo0(rebate.getEsport()));
		old.setSport(nullTo0(rebate.getSport()));
		old.setFishing(nullTo0(rebate.getFishing()));
		old.setLottery(nullTo0(rebate.getLottery()));
		old.setBetRate(nullTo0(rebate.getBetRate()));
		stationRebateDao.update(old);
		LogUtils.addLog("修改站点会员推荐返点配置 stationId=" + old.getStationId());
	}

	@Override
	public void modifyProxy(StationRebate rebate) {
		StationRebate old = stationRebateDao.findOneById(rebate.getId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationWebSiteConfigNot);
		}
		old.setType(rebate.getType());
		old.setRebateMode(rebate.getRebateMode());
		old.setLive(nullTo0(rebate.getLive()));
		old.setEgame(nullTo0(rebate.getEgame()));
		old.setChess(nullTo0(rebate.getChess()));
		old.setEsport(nullTo0(rebate.getEsport()));
		old.setSport(nullTo0(rebate.getSport()));
		old.setFishing(nullTo0(rebate.getFishing()));
		old.setLottery(nullTo0(rebate.getLottery()));
		old.setLevelDiff(nullTo0(rebate.getLevelDiff()));
		old.setMaxDiff(nullTo0(rebate.getMaxDiff()));
		old.setBetRate(nullTo0(rebate.getBetRate()));
		stationRebateDao.update(old);
		LogUtils.addLog("修改站点代理返点配置 stationId=" + old.getStationId());
	}

	private BigDecimal nullTo0(BigDecimal b) {
		if (b == null) {
			return BigDecimal.ZERO;
		}
		return b;
	}

	@Override
	public StationRebate findOne(Long stationId, Integer userType) {
		return stationRebateDao.findOne(stationId, userType);
	}
}
