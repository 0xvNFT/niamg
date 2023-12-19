package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserDepositDao;
import com.play.model.SysUserDeposit;
import com.play.service.SysUserDepositService;

/**
 * 会员存款总计
 *
 * @author admin
 */
@Service
public class SysUserDepositServiceImpl implements SysUserDepositService {

	@Autowired
	private SysUserDepositDao sysUserDepositDao;

	@Override
	public void init(Long id, Long stationId) {
		SysUserDeposit d = new SysUserDeposit();
		d.setUserId(id);
		d.setStationId(stationId);
		d.setTimes(0);
		d.setTotalMoney(BigDecimal.ZERO);
		d.setMaxMoney(BigDecimal.ZERO);
		sysUserDepositDao.save(d);
	}

	@Override
	public SysUserDeposit findOne(Long id, Long stationId) {
		return sysUserDepositDao.findOne(id, stationId);
	}

	@Override
	public int countDepositNum(Long stationId, boolean isDeposit) {
		return sysUserDepositDao.countDepositNum(stationId, isDeposit);
	}

	@Override
	public BigDecimal getTotalMoney(Long userId, Long stationId) {
		SysUserDeposit d = sysUserDepositDao.findOne(userId, stationId);
		if (d != null) {
			return d.getTotalMoney();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public int getLastCountOfFirstDeposit(Long stationId, Date begin, Long userId) {
		return sysUserDepositDao.getLastCountOfDeposit(stationId, begin, userId);
	}
}
