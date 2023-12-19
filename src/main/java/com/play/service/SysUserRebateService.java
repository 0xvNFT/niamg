package com.play.service;

import java.math.BigDecimal;

import com.play.model.SysUserRebate;

/**
 * 代理返点设置
 *
 * @author admin
 *
 */
public interface SysUserRebateService {

	void init(Long id, Long stationId, BigDecimal liveRebate, BigDecimal egameRebate, BigDecimal chessRebate,
			BigDecimal sportRebate, BigDecimal esportRebate, BigDecimal fishingRebate, BigDecimal lotteryRebate);

	SysUserRebate findOne(Long id, Long stationId);

	void update(SysUserRebate rebate);

	/**
	 * 统计代理下级返点大于给定返点值的个数
	 * 
	 * @param proxyId
	 * @param stationId
	 * @param rebate
	 * @param type
	 * @return
	 */
	int countUnusualNum(Long proxyId, Long stationId, BigDecimal rebate, String type);

}