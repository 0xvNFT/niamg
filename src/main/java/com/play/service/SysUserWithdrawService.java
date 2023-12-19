package com.play.service;

import com.play.model.SysUserWithdraw;

/**
 * 会员取款总计 
 *
 * @author admin
 *
 */
public interface SysUserWithdrawService {

	void init(Long id, Long stationId);

	/**
	 * 获取用户存款记录
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	SysUserWithdraw findOne(Long id, Long stationId);
}