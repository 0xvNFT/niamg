package com.play.service;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUserBetNum;

/**
 * 会员打码量信息
 *
 * @author admin
 *
 */
public interface SysUserBetNumService {
	void init(Long userId, Long stationId);

	void addBetNumber(Long userId, BigDecimal betNumber, Integer type, String remark, String orderId, Date bizDatetime,
			Long stationId);

	void updateDrawNeed(Long userId, Long stationId, BigDecimal drawNeed, Integer type, String remark, String orderId);
	void updateDrawNeed(Long userId, Long stationId, BigDecimal drawNeed,BigDecimal money, Integer type, String remark, String orderId,boolean clearDrawNeed);


	void clearbetNumOpe(Long id, Long stationId, String remark, int opeType);
	/**
	 * 手动加扣提款所需打码量
	 *
	 * @param userId
	 * @param stationId
	 * @param drawNeed
	 * @param type
	 * @param remark
	 * @param bizDatetime
	 * @return
	 */
	boolean addDrawNeed(Long userId, Long stationId, BigDecimal drawNeed, Integer type, String remark,
			Date bizDatetime);

	/**
	 * 获取用户打码量
	 *
	 * @param userId
	 * @param stationId
	 * @return
	 */
	SysUserBetNum findOne(Long userId, Long stationId);

	/**
	 * 根据会员账号获取打码量信息
	 *
	 * @param username
	 * @return
	 */
	JSONObject addOrGetBetInfo(Long stationId, String username);

	/**
	 * 加减打码量
	 *
	 * @param userId
	 * @param num
	 * @param remark
	 * @param type
	 */
	void adminUpdateBetNum(Long userId, Long stationId, BigDecimal num, String remark, Integer type);

}