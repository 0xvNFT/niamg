package com.play.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserWithdrawDao;
import com.play.model.SysUserWithdraw;
import com.play.service.SysUserWithdrawService;

/**
 * 会员取款总计
 *
 * @author admin
 *
 */
@Service
public class SysUserWithdrawServiceImpl implements SysUserWithdrawService {

	@Autowired
	private SysUserWithdrawDao sysUserWithdrawDao;

	@Override
	public void init(Long id, Long stationId) {
		SysUserWithdraw w = new SysUserWithdraw();
		w.setUserId(id);
		w.setStationId(stationId);
		w.setTimes(0);
		w.setTotalMoney(BigDecimal.ZERO);
		w.setMaxMoney(BigDecimal.ZERO);
		sysUserWithdrawDao.save(w);
	}

	@Override
	public SysUserWithdraw findOne(Long id, Long stationId) {
		return sysUserWithdrawDao.findOne(id, stationId);
	}
}
