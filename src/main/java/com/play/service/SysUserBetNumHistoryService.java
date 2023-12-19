package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.play.core.BetNumTypeEnum;
import com.play.model.SysUserBetNumHistory;
import com.play.orm.jdbc.page.Page;

/**
 * 会员打码量变动记录
 *
 * @author admin
 *
 */
public interface SysUserBetNumHistoryService {
	/**
	 * 租户后台打码量列表
	 * 
	 * @param stationId
	 * @param begin
	 * @param end
	 * @param username
	 * @param type
	 * @param proxyName
	 * @param agentUser
	 * @return
	 */
	Page<SysUserBetNumHistory> adminPage(Long stationId, Date begin, Date end, String username, Integer type,
			String proxyName, String agentUser);

	/**
	 * 用户中心打码量列表
	 *
	 * @param begin
	 * @param end
	 * @param type
	 * @return
	 */
	Page<SysUserBetNumHistory> frontPage(Long stationId, Long userId, Date begin, Date end, Integer type);

	BigDecimal getBetNum(Date begin, Date end, Long userId, Long stationId, Integer type);

	BigDecimal getBetNumForUser(Date begin, Date end, Long userId, Long stationId, List<BetNumTypeEnum> betTypes);

}