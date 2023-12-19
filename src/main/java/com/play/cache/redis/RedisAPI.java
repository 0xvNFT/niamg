package com.play.cache.redis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.Nullable;

import com.play.common.utils.SpringUtils;

public class RedisAPI {
	private static final Logger logger = LoggerFactory.getLogger(RedisAPI.class);

	private static RedisClientFactory factory = null;
	public static int DEFAULT_DB_INDEX = 0;

	/**
	 * 获取连接池
	 * 
	 * @return
	 */
	private static StringRedisTemplate getRedis(Integer db) {
		if (factory == null) {
			factory = SpringUtils.getBean(RedisClientFactory.class);
		}
		return factory.getRedis(db);
	}

	/**
	 * 是否存在KEY
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(String key) {
		return exists(key, DEFAULT_DB_INDEX);
	}

	/**
	 * 是否存在KEY
	 * 
	 * @param key
	 * @return
	 */
	public static boolean exists(String key, int db) {
		return getRedis(db).hasKey(key);
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String getCache(String key, int db) {
		return getRedis(db).opsForValue().get(key);
	}

	public static String getCache(String key) {
		return getRedis(DEFAULT_DB_INDEX).opsForValue().get(key);
	}

	public static String getAndDel(String key) {
		return getAndDel(key, DEFAULT_DB_INDEX);
	}

	public static String getAndDel(String key, int db) {
		StringRedisTemplate t = getRedis(db);
		String value = t.opsForValue().get(key);
		t.delete(key);
		return value;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key     键
	 * @param val     值
	 * @param timeout 超时时间单位（秒），0：永不超时
	 * @param db      数据
	 */
	public static void addCache(String key, String val, int timeout, int db) {
		StringRedisTemplate t = getRedis(db);
		if (timeout <= 0) {
			t.opsForValue().set(key, val);
		} else {
			t.opsForValue().set(key, val, timeout, TimeUnit.SECONDS);
		}
	}

	public static void addCache(String key, String val, Integer timeout) {
		addCache(key, val, timeout, DEFAULT_DB_INDEX);
	}

	public static <T> T operateCache(int db, com.play.cache.redis.RedisCallback<T> callback) {
		return callback.execute(getRedis(db));
	}

	public static Set<String> getKeys(String keyHeader, int db) {
		return getRedis(db).keys(keyHeader + "*");
	}

	private static final String DELETE_SCRIPT_IN_LUA = "local keys = redis.call('keys',KEYS[1]);if next(keys)==1 then for i=1,#keys,5000 do redis.call('del', unpack(keys, i, math.min(i+4999, #keys))) ;end; end;return 0;";
	private static DefaultRedisScript<Long> delScript = null;

	public static void delByPrefix(String pref, int db) {
		delCacheByExp(new StringBuilder(pref).append("*").toString(), db);
	}

	public static void delCacheByExp(String keyExp, int db) {
		try {
			if (delScript == null) {
				delScript = new DefaultRedisScript<>();
				delScript.setScriptText(DELETE_SCRIPT_IN_LUA);
				delScript.setResultType(Long.class);
			}
			getRedis(db).execute(delScript, Collections.singletonList(keyExp), "");
		} catch (Exception e) {
		//	logger.error("DELETE_SCRIPT_IN_LUA 错误", e);
		}
	}

	/**
	 * 删除缓存
	 *
	 * @param db
	 * @param keys
	 */
	public static void delCache(String... keys) {
		delCache(DEFAULT_DB_INDEX, keys);
	}

	/**
	 * 删除缓存
	 *
	 * @param db
	 * @param keys
	 */
	public static void delCache(int db, String... keys) {
		if (keys.length > 1) {
			getRedis(db).delete(Arrays.asList(keys));
		} else {
			getRedis(db).delete(keys[0]);
		}
	}

	/**
	 * 往LIST增加元素
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static void rpush(String key, String... value) {
		rpush(key, DEFAULT_DB_INDEX, value);
	}

	/**
	 * 往LIST增加元素
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static void rpush(String key, int db, String... value) {
		if (value.length > 1) {
			getRedis(db).opsForList().rightPushAll(key, value);
		} else {
			getRedis(db).opsForList().rightPush(key, value[0]);
		}
	}

	/**
	 * 通过KEY取出LIST
	 *
	 * @param key
	 * @param start
	 * @param stop
	 */
	public static List<String> lrange(String key, int start, int stop) {
		return lrange(key, start, stop, DEFAULT_DB_INDEX);
	}

	/**
	 * 通过KEY取出LIST
	 *
	 * @param key
	 * @param start
	 * @param stop
	 */
	public static List<String> lrange(String key, int start, int stop, int db) {
		return getRedis(db).opsForList().range(key, start, stop);
	}

	/**
	 * 移除LIST的VALUE
	 *
	 * @param key
	 * @param value
	 */
	public static void lrem(String key, String value) {
		lrem(key, value, DEFAULT_DB_INDEX);
	}

	/**
	 * 移除LIST的VALUE
	 *
	 * @param key
	 * @param value
	 */
	public static void lrem(String key, String value, int db) {
		lrem(key, -1, value, db);
	}

	/**
	 * 移除LIST的VALUE
	 *
	 * @param key
	 * @param count -1 表示从头到尾的第一个 其他 COUNT为几就移除几个
	 * @param value
	 * @param db
	 */
	public static void lrem(String key, int count, String value, int db) {
		getRedis(db).opsForList().remove(key, count, value);
	}

	/**
	 * 取LIST的长度
	 *
	 * @param key
	 */
	public static Long llen(String key) {
		return llen(key, DEFAULT_DB_INDEX);
	}

	/**
	 *
	 * @param key
	 */
	public static Long llen(String key, int db) {
		return getRedis(db).opsForList().size(key);
	}

	/**
	 * 使用管道，批量获取List 里面的值
	 *
	 * @param key
	 * @return
	 */
	public static List<Object> lpopByPipe(String key) {
		return lpopByPipe(key, DEFAULT_DB_INDEX, 20);
	}

	/**
	 * 使用管道，批量获取List 里面的值
	 *
	 * @param key
	 * @param db
	 * @param recordNum 记录条数
	 * @return
	 */
	public static List<Object> lpopByPipe(String key, int db, int recordNum) {
		return getRedis(db).executePipelined(new RedisCallback<String>() {
			@Nullable
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				for (int i = 0; i < recordNum; i++) {
					connection.lPop(key.getBytes());
				}
				return null;
			}
		});
	}

	public static String lpop(String key) {
		return lpop(key, DEFAULT_DB_INDEX);
	}

	public static String lpop(String key, int db) {
		return getRedis(db).opsForList().leftPop(key);
	}

	/**
	 * 往set增加元素
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static void sadd(String key, String... value) {
		sadd(key, DEFAULT_DB_INDEX, 0, value);
	}

	/**
	 * 往set增加元素
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public static void sadd(String key, int db, String... value) {
		sadd(key, db, 0, value);
	}

	/**
	 * 往set增加元素
	 *
	 * @param key
	 * @param value
	 * @param timeout 过期时间，单位秒,0 代表不过期
	 * @return
	 */
	public static void sadd(String key, int db, int timeout, String... value) {
		StringRedisTemplate t = getRedis(db);
		t.opsForSet().add(key, value);
		if (timeout > 0) {
			t.expire(key, timeout, TimeUnit.SECONDS);
		}
	}

	public static int getSetSize(String key) {
		return getSetSize(key, DEFAULT_DB_INDEX);
	}

	public static int getSetSize(String key, int db) {
		Long size = getRedis(db).opsForSet().size(key);
		if (size != null) {
			return size.intValue();
		}
		return 0;
	}

	public static Set<String> smembers(String key, int db) {
		return getRedis(db).opsForSet().members(key);
	}

	/**
	 * 取出MAP
	 *
	 * @param key
	 */
	public static Map<Object, Object> hgetAll(String key) {
		return hgetAll(key, DEFAULT_DB_INDEX);
	}

	/**
	 * 取出MAP
	 *
	 * @param key
	 */
	public static Map<Object, Object> hgetAll(String key, int db) {
		return getRedis(db).opsForHash().entries(key);
	}

	/**
	 * 删除MAP中的元素
	 *
	 * @param key
	 */
	public static void hdel(String key, Object... fields) {
		hdel(key, DEFAULT_DB_INDEX, fields);
	}

	/**
	 * 删除MAP中的元素
	 *
	 * @param key
	 */
	public static void hdel(String key, int db, Object... fields) {
		getRedis(db).opsForHash().delete(key, fields);
	}

	/**
	 * 累加
	 *
	 * @param key
	 * @param increment
	 * @param db
	 */
	public static Long incrby(String key, long increment) {
		return incrby(key, increment, DEFAULT_DB_INDEX, 0);
	}

	/**
	 * 累加
	 *
	 * @param key
	 * @param increment
	 * @param db
	 */
	public static Long incrby(String key, long increment, int timeout) {
		return incrby(key, increment, DEFAULT_DB_INDEX, timeout);
	}

	/**
	 * 累加
	 *
	 * @param key
	 * @param increment
	 * @param db
	 */
	public static Long incrby(String key, long increment, int db, int timeout) {
		StringRedisTemplate t = getRedis(db);
		Long after = t.opsForValue().increment(key, increment);
		if (after <= 1 && timeout > 0) {
			t.expire(key, timeout, TimeUnit.SECONDS);
		}
		return after;
	}

	public static void expire(String key, int timeout) {
		expire(key, timeout, DEFAULT_DB_INDEX);
	}

	public static void expire(String key, int timeout, int db) {
		getRedis(db).expire(key, timeout, TimeUnit.SECONDS);
	}

	public static void zrem(String key, String value) {
		zrem(key, value, DEFAULT_DB_INDEX);
	}

	public static void zrem(String key, String value, int db) {
		getRedis(db).opsForZSet().remove(key, value);
	}

	public static Long zcard(String key) {
		return zcard(key, DEFAULT_DB_INDEX);
	}

	public static Long zcard(String key, int db) {
		return getRedis(db).opsForZSet().zCard(key);
	}

	public static Set<String> zrange(String key, long start, long stop) {
		return zrange(key, start, stop, DEFAULT_DB_INDEX);
	}

	public static Set<String> zrange(String key, long start, long stop, int db) {
		return getRedis(db).opsForZSet().range(key, start, stop);
	}

	/**
	 *
	 * 尝试获取分布式锁
	 *
	 *
	 * @param expireTime 超期时间
	 *
	 * @return 成功时返回请求标识，否则返回空
	 *
	 */
	public static boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
		return getRedis(DEFAULT_DB_INDEX).opsForValue().setIfAbsent(lockKey, requestId, expireTime, TimeUnit.SECONDS);
	}

	private static final String releaseDistributedLockScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	private static DefaultRedisScript<Long> unLockScript = null;

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
		try {
			if (unLockScript == null) {
				unLockScript = new DefaultRedisScript<>();
				unLockScript.setScriptText(releaseDistributedLockScript);
				unLockScript.setResultType(Long.class);
			}
			Long it = getRedis(DEFAULT_DB_INDEX).execute(unLockScript, Collections.singletonList(lockKey), requestId);
			return it.equals(1L);
		} catch (Exception e) {
			 logger.error("DELETE_SCRIPT_IN_LUA 错误", e);
		}
		return false;
	}

	/**
	 * 累加
	 *
	 * @param key
	 * @param increment
	 * @param db
	 */
	public static Double incrByFloat(String key, double increment, int db, int timeout) {
		Double after = 0d;
		after = getRedis(db).opsForValue().increment(key, increment);
		if (timeout > 0) {
			getRedis(db).expire(key, timeout, TimeUnit.SECONDS);
		}
		return after;
	}
}
