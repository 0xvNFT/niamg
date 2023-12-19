package com.play.cache.redis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.play.common.ip.IpUtils;

public class DistributedLockUtil {
	private static Logger logger = LoggerFactory.getLogger(DistributedLockUtil.class);
	private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	/**
	 * 
	 * 尝试获取分布式锁
	 * 
	 * @param lockKey        锁
	 * 
	 * @param lockMillSecond 超期时间  秒
	 * 
	 * @return 是否获取成功
	 * 
	 */
	public static boolean tryGetDistributedLock(String lockKey, int lockSecond) {
		return RedisAPI.tryGetDistributedLock(lockKey, IpUtils.getName() + lockKey, lockSecond);
	}

	/**
	 * 
	 * 释放分布式锁
	 * 
	 * @param lockKey   锁
	 * 
	 * @param requestId 请求标识
	 * 
	 * @return 是否释放成功
	 * 
	 */

	public static boolean releaseDistributedLock(String lockKey, String requestId) {
		return RedisAPI.releaseDistributedLock(lockKey, requestId);
	}

	/**
	 * 
	 * 尝试获取分布式锁
	 * 
	 * @param lockKey       锁
	 * 
	 * @param lockSecond    超期时间 单位秒
	 * @param tryLockSecond 等待锁的时间 单位秒
	 * 
	 * @return 加锁的值
	 * 
	 */
	public static String tryGetDistributedLock(String lockKey, int lockSecond, int tryLockSecond) {
		try {
			String value = fetchLockValue();
			Long firstTryTime = System.currentTimeMillis();
			long sleep = tryLockSecond * 100;
			do {
				if (RedisAPI.tryGetDistributedLock(lockKey, value, lockSecond)) {
					return value;
				}
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					//logger.error("Redis lock failure,waiting try next key : " + lockKey, e);
				}
			} while ((System.currentTimeMillis() - tryLockSecond * 1000) < firstTryTime);
		} catch (Exception e) {
			logger.error("Redis lock failure,waiting try next key : " + lockKey, e);
		}
		return null;
	}

	private static String fetchLockValue() {
		return UUID.randomUUID().toString() + "_" + df.format(new Date());
	}

}
