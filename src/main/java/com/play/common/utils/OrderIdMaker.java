package com.play.common.utils;

import java.util.Date;

import com.play.cache.redis.RedisAPI;

public class OrderIdMaker {

	public static final String ORDER_ID_DEPOSIT = "D";// 网站入款
	public static final String ORDER_ID_WITHDRAW = "W";// 网站出款
	public static final String ORDER_ID_EXCHANGE = "E";// 额度转换
	

	/**
	 * 转入转出第三方订单号
	 * 
	 * @return
	 */
	public static String getThirdOrderId() {
		return getOrderNo();
	}

	public static String getDepositOrderId() {
		return ORDER_ID_DEPOSIT + getOrderNo();
	}

	public static String getWithdrawOrderId() {
		return ORDER_ID_WITHDRAW + getOrderNo();
	}
	
	public static String getExchangeOrderId() {
		return ORDER_ID_EXCHANGE + getOrderNo();
	}

	private static String getOrderNo() {
		Date now = new Date();
		String date = DateUtil.formatDate(now, "yyyyMMdd");
		String time = DateUtil.formatDate(now, "yyMMddHHmmss");
		long inc = RedisAPI.incrby("sysOrderIdInc" + date, 1L, 86400);
		if (inc < 10) {
			return time + "0000" + inc;
		}
		if (inc < 100) {
			return time + "000" + inc;
		}
		if (inc < 1000) {
			return time + "00" + inc;
		}
		if (inc < 10000) {
			return time + "0" + inc;
		}
		return time + RandomStringUtils.randomInt(2) + inc;
	}
}
