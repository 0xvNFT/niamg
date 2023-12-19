package com.play.service;

import java.math.BigDecimal;
import java.util.Date;

import com.play.model.SysUserDeposit;

/**
 * 会员存款总计
 *
 * @author admin
 *
 */
public interface SysUserDepositService {

	void init(Long id, Long stationId);

	/**
	 * 获取用户存款记录
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	SysUserDeposit findOne(Long id, Long stationId);

	/**
	 * 统计有效会员人数
	 *
	 * @param stationId
	 * @param isDeposit
	 * @return
	 */
	int countDepositNum(Long stationId, boolean isDeposit);

	int getLastCountOfFirstDeposit(Long stationId, Date begin, Long userId);

	/**
	 * 
	 * @param userId
	 * @param stationId
	 * @return
	 */
	BigDecimal getTotalMoney(Long userId, Long stationId);

}
