package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUser;
import com.play.model.SysUserDailyMoney;
import com.play.model.vo.RiskReportVo;
import com.play.orm.jdbc.page.Page;

/**
 * 
 *
 * @author admin
 *
 */
public interface SysUserDailyMoneyService {

	SysUserDailyMoney getDailyBet(Long userId, Long stationId, Date begin, Date end, Long proxyId, String agentName,
			String userRemark, String degreeIds, String groupIds, Integer level, Integer userType);

	SysUserDailyMoney personOverview(Long stationId, Long userId, Date start, Date end);

	Page<SysUserDailyMoney> betUserPage(SysUser user, Date beginTime, Date endTime, Integer pageSize,
			Integer pageNumber);

	JSONObject teamOverview(SysUser login, String username, Date begin, Date end);

	JSONObject recommendList(SysUser login, String dateStr, String username, Integer pageSize, Integer pageNumber);

	/**
	 * 个人报表
	 * 
	 * @param begin
	 * @param end
	 * @param username
	 * @param include
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	Page<SysUserDailyMoney> personReport(Date begin, Date end, String username, Boolean include, Integer pageSize,
			Integer pageNumber);



	/**
	 * 代理或者推荐好友团队报表
	 * 
	 * @param username
	 * @param begin
	 * @param end
	 * @param pageNumber
	 * @param pageSize
	 * @param sortName
	 * @param sortOrder
	 * @return
	 */
	JSONObject teamReport(String username, Date begin, Date end, Integer pageNumber, Integer pageSize, String sortName,
			String sortOrder);

	/**
	 * 全局报表
	 * 
	 * @param stationId
	 * @param begin
	 * @param end
	 * @param proxyName
	 * @param username
	 * @param agentUser
	 * @param userRemark
	 * @param degreeIds
	 * @param level
	 * @param userType
	 * @return
	 */
	String globalReport(Long stationId, Date begin, Date end, String proxyName, String username, String agentUser,
			String userRemark, String degreeIds, Integer level, Integer userType);
	String globalReport(Long stationId, Date begin, Date end, String proxyName, String username, String agentUser,
						String userRemark, String degreeIds, Integer level, Integer userType,boolean directSub);


	//首页数据展示
	Map<String,Object> indexGlobleReport(long stationId, Date begin, Date end);

	String userData(String username, Long stationId, Date begin, Date end);

	String adminTeamReport(Long stationId, Date begin, Date end, String username, String proxyName, Integer pageNumber,
			Integer pageSize, Integer level, String agentUser, String degreeIds, String userRemark);

	void adminTeamReportExport(Long stationId, Date begin, Date end, String username, String proxyName, Integer level,
			String agentUser, String degreeIds, String userRemark);

	Page<SysUserDailyMoney> moneyReport(Long stationId, Date begin, Date end, String username, String proxyName,
			Integer pageNumber, Integer pageSize, String agentName);

	void moneyReportExport(Long stationId, Date begin, Date end, String username, String proxyName, String agentName);

	String adminMonthReport(Long stationId);

	JSONObject adminProxyReport(Long stationId, Date begin, Date end, String proxyName);
	JSONObject adminProxyReport(Long stationId, Date begin, Date end, String proxyName,boolean directSub);

	Page<RiskReportVo> adminRiskReport(Long stationId, String gameType, Date begin, Date end, String username,
			String proxyName, String degreeIds, String agentName, String sortName, String sortOrder);

	Map<String, BigDecimal[]> dailyChartsMoneyRepot(Long stationId, Date begin, Date end);

	Map<String, BigDecimal> dailyChartsWinOrLossRepot(Long stationId, Date begin, Date end);

	Map<String, BigDecimal[]> dailyChartsFinanceRepot(Long stationId, Date begin, Date end);
}