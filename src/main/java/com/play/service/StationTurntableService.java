package com.play.service;

import java.util.Date;
import java.util.List;

import com.play.model.StationTurntable;
import com.play.model.StationTurntableAward;
import com.play.model.StationTurntableRecord;
import com.play.orm.jdbc.page.Page;

/**
 * 转盘活动表
 *
 * @author admin
 *
 */
public interface StationTurntableService {

	Page<StationTurntable> getPage(Long stationId, Date begin, Date end);

	void saveScore(StationTurntable t);

	void modifyScore(StationTurntable t);

	void saveMoney(StationTurntable t);

	void modifyMoney(StationTurntable t);

	StationTurntable findOne(Long id, Long stationId);

	void delete(Long id, Long stationId);

	void updStatus(Long id, Long stationId, Integer status);

	List<StationTurntableAward> getAwards(Long turntableId);

	void saveAward(List<StationTurntableAward> awards, Long id, Long stationId);

	StationTurntable getProgress(Long stationId, Integer status, Long degreeId, Long groupId);

	StationTurntableAward play(StationTurntable ma);

	StationTurntableAward playPay(StationTurntable ma);

	/**
	 * 处理1000条 非奖品未处理的转盘记录
	 */
	List<StationTurntableRecord> getUntreatedRecords();

	/**
	 * 对金额和积分进行派奖计算
	 */
	boolean balanceAndRecord(StationTurntableRecord record);

}