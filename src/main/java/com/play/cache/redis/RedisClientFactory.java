package com.play.cache.redis;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.play.web.exception.BaseException;

/**
 * 根据不用的db，使用不同的redis-server 在RedisConfiguration 配置
 * 配置项如：redis.cluster.confs=127.0.0.1:6379:1,2,3,4,5,6,7,8,9;127.0.0.1:3680:10,11,12,13,14,15
 * redis.cluster.confs=ip:port:db,db,db;ip:port:db,db
 * 
 * @author macair
 *
 */
public class RedisClientFactory {
	private Logger logger = LoggerFactory.getLogger(RedisClientFactory.class);
	private static ConcurrentHashMap<Integer, StringRedisTemplate> redisMap = new ConcurrentHashMap<>();

	public StringRedisTemplate getRedis(Integer db) {
		try {
			if (db == null) {
				db = 0;
			}
			return redisMap.get(db);
		} catch (Exception e) {

			logger.error("获取redis链接发送异常", e);
			throw new BaseException(e);
		}
	}

	public void putRedis(Integer db, StringRedisTemplate redis) {
		redisMap.put(db, redis);
	}
}
