package com.play.service;

import java.util.Date;

import com.play.model.Station;
import com.play.model.StationDailyDeposit;
import com.play.orm.jdbc.page.Page;

/**
 * 站点每日充值报表
 *
 * @author admin
 *
 */
public interface StationDailyDepositService {

	Page<StationDailyDeposit> page(Long stationId, Date begin, Date end, String payName, String sortName,
			String sortOrder,Integer depositType);

	/**
	 * 存款报表导出
	 *
	 * @param begin
	 * @param end
	 * @param payName
	 */
	void depositReportExport(Long stationId, Date begin, Date end, String payName,Integer depositType);

	/**
	 * 生成存款报表
	 *
	 * @param statDate
	 * @param sysStation
	 */
	void generateDepositReport(Date statDate, Station station);

}