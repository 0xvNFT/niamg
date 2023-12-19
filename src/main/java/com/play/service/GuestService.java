package com.play.service;

import java.util.Date;

public interface GuestService {
	/**
	 * 获取试玩账号用户名
	 *
	 * @param stationId
	 * @return
	 */
	String getTestGuestUsername(Long stationId);

	/**
	 * 清理试玩账号数据
	 * 
	 * @param guestIdList
	 */
	void clearGuestData(Date end);
}
