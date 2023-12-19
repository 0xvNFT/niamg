package com.play.cache;

import com.alibaba.fastjson.JSON;
import com.play.cache.redis.RedisAPI;

public class CacheUtil {

	/**
	 * 增加缓存
	 * 
	// * @param type
	 * @param key
	 * @param val
	 */
	public static void addCache(CacheKey ckey, String key, String val) {
		addCache(ckey, key, val, ckey.getTimeout());
	}

	public static void addCache(CacheKey ckey, String key, String val, int timeout) {
		String cacheKey = toKey(ckey, key);
		RedisAPI.addCache(cacheKey, val, timeout, ckey.getDb());
	}

	/**
	 * 获取缓存
	 * 
	 //* @param type
	 * @param key
	 * @return
	 */
	public static String getCache(CacheKey ckey, String key) {
		String cacheKey = toKey(ckey, key);
		return RedisAPI.getCache(cacheKey, ckey.getDb());
	}

	/**
	 * 转换成缓存key
	 * 
	 //* @param type
	 * @param key
	 * @return
	 */
	public static String toKey(CacheKey ckey, String key) {
		return new StringBuilder(ckey.name()).append(":").append(key).toString();
	}

	/**
	 * 从缓存中取出字符串，转换成对象
	 * 
	 //* @param type
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getCache(CacheKey ckey, String key, Class<T> clazz) {
		String json = getCache(ckey, key);
		if (json == null) {
			return null;
		}
		if (clazz == String.class) {
			return (T) json;
		}
		return JSON.parseObject(json, clazz);
	}

	public static String getAndDel(CacheKey ckey, String key) {
		String cacheKey = toKey(ckey, key);
		return RedisAPI.getAndDel(cacheKey, ckey.getDb());
	}

	public static void addCache(CacheKey ckey, String key, Object model, int timeout) {
		if (model == null) {
			return;
		}
		String json = null;
		if (model.getClass() == String.class) {
			json = (String) model;
		} else {
			json = JSON.toJSONString(model);
		}
		addCache(ckey, key, json, timeout);
	}

	/**
	 * 将一个对象转换成json，存入缓存
	 * 
	 //* @param type
	 * @param key
	 * @param model
	 //* @param timeout 单位分钟 null值则为永久
	 */
	public static void addCache(CacheKey ckey, String key, Object model) {
		if (model == null) {
			return;
		}
		String json = null;
		if (model.getClass() == String.class) {
			json = (String) model;
		} else {
			json = JSON.toJSONString(model);
		}
		addCache(ckey, key, json, ckey.getTimeout());
	}

	public static void delCache(CacheKey ckey, String key) {
		String cacheKey = toKey(ckey, key);
		RedisAPI.delCache(ckey.getDb(), cacheKey);
	}

	public static void delCacheByPrefix(CacheKey ckey, String keyPrefix) {
		RedisAPI.delByPrefix(toKey(ckey, keyPrefix), ckey.getDb());
	}

	public static void delCacheByPrefix(CacheKey ckey) {
		RedisAPI.delByPrefix(ckey.name(), ckey.getDb());
	}

	public static void sadd(CacheKey type, String key, String... value) {
		RedisAPI.sadd(toKey(type, key), type.getDb(), type.getTimeout(), value);
	}

	public static void sadd(CacheKey type, String key, int timeout, String... value) {
		RedisAPI.sadd(toKey(type, key), type.getDb(), timeout, value);
	}

	public static int getSetSize(CacheKey type, String key) {
		return RedisAPI.getSetSize(toKey(type, key), type.getDb());
	}
}
