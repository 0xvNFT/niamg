package com.play.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserRebateDao;
import com.play.model.SysUserRebate;
import com.play.service.SysUserRebateService;

/**
 * 代理返点设置
 *
 * @author admin
 *
 */
@Service
public class SysUserRebateServiceImpl implements SysUserRebateService {

	@Autowired
	private SysUserRebateDao sysUserRebateDao;

	@Override
	public void init(Long id, Long stationId, BigDecimal liveRebate, BigDecimal egameRebate, BigDecimal chessRebate,
			BigDecimal sportRebate, BigDecimal esportRebate, BigDecimal fishingRebate, BigDecimal lotteryRebate) {
		SysUserRebate r = new SysUserRebate();
		r.setLive(liveRebate);
		r.setEgame(egameRebate);
		r.setEsport(esportRebate);
		r.setSport(sportRebate);
		r.setFishing(fishingRebate);
		r.setLottery(lotteryRebate);
		r.setChess(chessRebate);
		r.setUserId(id);
		r.setStationId(stationId);
		sysUserRebateDao.save(r);
	}

	@Override
	public SysUserRebate findOne(Long id, Long stationId) {
		return sysUserRebateDao.findOne(id, stationId);
	}

	@Override
	public void update(SysUserRebate rebate) {
		sysUserRebateDao.update(rebate);
	}

	@Override
	public int countUnusualNum(Long proxyId, Long stationId, BigDecimal rebate, String type) {
		return sysUserRebateDao.countUnusualNum(proxyId, stationId, rebate, type);
	}
}
