package com.play.web.user.online;

import java.util.Date;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.DateUtil;

public class OnlineUserNumTool {

	/**
	 * 添加在线人数
	 * 
	 * @param stationId
	 * @param userId
	 */
	public static void addOnlieNum(Long stationId, Long userId) {
		Date now = new Date();
		// 记录今日登陆会员数
		CacheUtil.sadd(CacheKey.ONLINE_USER_ID_SET, getDayKey(stationId, now), userId.toString());
		// 记录每分钟的登陆个数
		CacheUtil.sadd(CacheKey.ONLINE_USER_ID_SET, getMinuteKey(stationId, now), 300, userId.toString());
	}

	private static String getDayKey(Long stationId, Date date) {
		return new StringBuilder("sid:").append(stationId).append(":d:").append(DateUtil.toDateStr(date)).toString();
	}

	private static String getMinuteKey(Long stationId, Date date) {
		return new StringBuilder("sid:").append(stationId).append(":t:")
				.append(DateUtil.formatDate(date, "yyyy-MM-dd_HH:mm")).toString();
	}

	/**
	 * 获取一天在线人数
	 * 
	 * @param stationId
	 * @param date
	 * @return
	 */
	public static Integer getDayOnlineNum(Long stationId, Date date) {
		return CacheUtil.getSetSize(CacheKey.ONLINE_USER_ID_SET, getDayKey(stationId, date));
	}

	/**
	 * 获取每分钟在线人数
	 * 
	 * @param stationId
	 * @param date
	 * @return
	 */
	public static Integer getMinuteOnlineNum(Long stationId, Date date) {
		return CacheUtil.getSetSize(CacheKey.ONLINE_USER_ID_SET, getMinuteKey(stationId, date));
	}
}
