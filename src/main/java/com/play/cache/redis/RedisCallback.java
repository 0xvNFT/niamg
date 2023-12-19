package com.play.cache.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

public interface RedisCallback<T> {

	public T execute(StringRedisTemplate temp);
}
