package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.play.model.StationRedPacket;
import com.play.model.StationRedPacketRecord;
import com.play.orm.jdbc.page.Page;

/**
 * 
 *
 * @author admin
 *
 */
public interface StationRedPacketService {

	Page<StationRedPacketRecord> getRecordPage(String username, Long stationId, Date begin, Date end);

	StationRedPacketRecord getRecord(Long id, Long stationId);

	void handlerRecord(Long id, Integer status, String remark, Long stationId);

	boolean balanceAndRecord(StationRedPacketRecord record);

	Page<StationRedPacket> getPage(Long stationId);

	void saveRedPacket(StationRedPacket redPacket, Long[] degreeIds);

	void saveRedPacketFission(StationRedPacket redPacket, Long[] degreeIds);

	void saveRedPacketDegree(StationRedPacket redPacket, Long[] redBagDid, Integer[] redBagTmn, Long[] redBagMin,
			Long[] redBagMax);

	void saveRedPacketRecharge(StationRedPacket redPacket, Long[] degreeIds, Long[] redBagRechargeMin,
			Long[] redBagRechargeMax, Integer[] packetBumber, Integer[] ipNumber, Long[] redBagMin, Long[] redBagMax);

	void cloneRedBag(Long rid, Long stationId, String begin, String end, Integer addDay);

	List<StationRedPacket> getRedPacketList(Long stationId, Integer status, Date time);

	void delRedPacket(Long id, Long stationId);

	void updStatus(Long id, Integer status, Long stationId);

	void initRedPacketRedis(StationRedPacket rp);

	StationRedPacket findOne(Long id, Long stationId);

	StationRedPacketRecord getMoneyAndCount(Long id);

	/**
	 * 获取当前用户可以领取的红包
	 * 
	 * @return
	 */
	StationRedPacket getCurrentRedPacketNew();
	StationRedPacket getRedPacket(Long redPackeId);
	StationRedPacket getCurrentRedPacketNewForApp();

	/**
	 * 抢红包
	 * 
	 * @param redPacketId
	 * @return
	 */
	BigDecimal grabRedPacketPlan(Long redPacketId);

	List<StationRedPacketRecord> getListBysidAndRid(Long stationId, Long redPacketId, String username, Integer limit);

	/**
	 * 获取未处理的领取红包记录
	 * 
	 * @param pageSize
	 * @return
	 */
	List<StationRedPacketRecord> getUntreateds(int pageSize);

	/**
	 * 获取用户红包记录
	 * @param userId
	 * @param stationId
	 * @param packetId
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<StationRedPacketRecord> getUserRedPacketRecordList(Long userId, Long stationId, Long packetId, Integer status, Date startDate, Date endDate);
}