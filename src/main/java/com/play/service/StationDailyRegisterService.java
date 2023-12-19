package com.play.service;

import java.util.Date;
import java.util.Map;

import com.play.model.StationDailyRegister;
import com.play.model.vo.StationDailyRegisterVo;
import com.play.orm.jdbc.page.Page;

/**
 * 站点每日注册信息表
 *
 * @author admin
 *
 */
public interface StationDailyRegisterService {
	/**
	 * 注册时处理
	 *
	 * @param stationId
	 * @param accountType
	 * @param isPromo     是否是代理推广注册的用户
	 */
	void registerHandle(Long stationId, Long partnerId, Integer userType, boolean isPromo);

	/**
	 * 获取站点注册记录
	 *
	 * @param stationId
	 * @param begin
	 * @param end
	 * @return
	 */
	Page<StationDailyRegister> getPage(Long stationId, Date begin, Date end);

	Map dailyChartsRegisterRepot(Long stationId, Date begin, Date end);
	/**
	 * 获取站点当日数据
	 *
	 * @param stationId
	 * @param date
	 * @return
	 */
	StationDailyRegister getDailyData(Long stationId, Date date);

	/**
	 * 导出
	 *
	 * @param stationId
	 * @param begin
	 * @param end
	 */
	void export(Long stationId, Date begin, Date end);

	/**
	 * 获取通过代理名称获取站点注册记录
	 *
	 * @param stationId
	 * @param begin
	 * @param end
	 * @param agentName
	 * @return
	 */
	Page<StationDailyRegisterVo> getPageByproxyName(Long stationId, Date begin, Date end, String proxyName,
			String agentName);

}