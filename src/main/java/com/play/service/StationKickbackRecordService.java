package com.play.service;

import java.math.BigDecimal;
import java.util.Date;

import com.play.model.StationKickbackRecord;
import com.play.orm.jdbc.page.Page;

/**
 * 会员反水记录表，按日投注和游戏类型来反水
 *
 * @author admin
 *
 */
public interface StationKickbackRecordService {

	Page<StationKickbackRecord> page(Long stationId, String username, Date start, Date end, Integer betType,
			Integer status, String realName);

	int cancel(Integer betType, String username, String betDate, Long stationId);

	void manualRollback(Integer betType, String username, String betDate, Long stationId, BigDecimal money);

	void doBackwaterMoneyByBatch(String[] moneys, Long stationId, Date start, Date end);

	/**
	 * 站点自动反水
	 * 
	 * @param begin
	 * @param end
	 * @param stationId
	 */
	void autoBackwater(Date begin, Date end, Long stationId);

	/**
	 * 导出
	 * 
	 * @param username
	 * @param start
	 * @param end
	 * @param betType
	 * @param status
	 * @param stationId
	 * @param realName
	 */
	void export(String username, Date start, Date end, Integer betType, Integer status, Long stationId,
			String realName);

}