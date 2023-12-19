package com.play.cache.redis;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * description: 使用spring-data-redis 连接池为lettuce
 * 包括lettuce连接池的配置以及Redistemplate和StringRedistemplate
 *
 * @author wkGui
 */
@Configuration
@EnableRedisHttpSession
public class RedisConfiguration {
	//private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
	private static int dbIndex = 0;

	@Value("${redis.host}")
	private String hostname;
	@Value("${redis.port}")
	private int port;
	@Value("${redis.password}")
	private String password;
	@Value("${redis.maxTotal}")
	private int maxTotal;
	@Value("${redis.maxIdle}")
	private int maxIdle;
	@Value("${redis.minIdle}")
	private int minIdle;
	@Value("${redis.blockWhenExhausted}")
	private boolean isBlockWhenExhausted;
	@Value("${redis.maxWait}")
	private int maxWait;
	@Value("${redis.testOnBorrow}")
	private boolean isTestOnBorrow;
	@Value("${redis.testOnReturn}")
	private boolean isTestOnReturn;
	@Value("${redis.testWhileIdle}")
	private boolean isTestWhileIdle;
	@Value("${redis.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;
	@Value("${redis.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${redis.cluster.confs}")
	private String clusterConfs;

	/**
	 * 配置连接池参数
	 *
	 * @return GenericObjectPool
	 */
	@Bean
	public GenericObjectPoolConfig getRedisConfig() {
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(maxIdle);
		genericObjectPoolConfig.setMaxTotal(maxTotal);
		genericObjectPoolConfig.setMinIdle(minIdle);
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		genericObjectPoolConfig.setBlockWhenExhausted(isBlockWhenExhausted);
		genericObjectPoolConfig.setMaxWaitMillis(maxWait);
		// 在borrow一个实例时，是否提前进行alidate操作；如果为true，则得到的实例均是可用的
		genericObjectPoolConfig.setTestOnBorrow(isTestOnBorrow);
		// 调用returnObject方法时，是否进行有效检查
		genericObjectPoolConfig.setTestOnReturn(isTestOnReturn);
		// 在空闲时检查有效性, 默认false
		genericObjectPoolConfig.setTestWhileIdle(isTestWhileIdle);
		// 表示idle object evitor两次扫描之间要sleep的毫秒数；
		genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		// 表示一个对象至少停留在idle状态的最短时间，
		// 然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		genericObjectPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

		return genericObjectPoolConfig;
	}

	@Bean
	public LettuceClientConfiguration clientConfig(GenericObjectPoolConfig poolConfig) {
		return LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(maxWait))
				.poolConfig(poolConfig).build();
	}

	/**
	 * lettuce 连接工厂配置
	 *
	 * @return LettuceConnectionFactory implement RedisConnectionFactory
	 */
	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory(LettuceClientConfiguration clientConfig) {
		// 单机版配置
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(dbIndex);
		redisStandaloneConfiguration.setHostName(hostname);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

		return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfig);
	}

	@Bean
	public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory factory) {
		return new StringRedisTemplate(factory);
	}

	/**
	 * spring session 不用在重新配置redis
	 * 
	 * @param factory
	 * @param stringRedisSerializer
	 * @return
	 */
	@Bean
	public ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}

	@Bean
	public RedisClientFactory redisClientFactory(StringRedisTemplate redisTemplate,
			LettuceClientConfiguration clientConfig) {
		RedisClientFactory rcf = new RedisClientFactory();
		rcf.putRedis(dbIndex, redisTemplate);
		if (StringUtils.isNotEmpty(clusterConfs)) {
			String[] ccs = StringUtils.split(clusterConfs.trim(), ";");
			Set<Integer> dbSet = new HashSet<>();
			for (String cc : ccs) {
				if (StringUtils.isEmpty(cc)) {
					continue;
				}
				String[] cs = StringUtils.split(cc.trim(), ":");
				if (cs.length != 3) {
				//	logger.error("redis 配置错误," + cc);
					System.exit(0);
				}
				int port = NumberUtils.toInt(cs[1]);
				if (port == 0) {
				//	logger.error("redis 配置错误,端口错误" + cc);
					System.exit(0);
				}
				String[] dbs = StringUtils.split(cs[2], ",");
				for (String db : dbs) {
					int i = NumberUtils.toInt(db, -1);
					if (i == -1) {
						continue;
					}
					if (dbSet.contains(i)) {
					//	logger.error("redis db 已经分配过，不需要重新分配，db=" + i);
						System.exit(0);
					}
					dbSet.add(i);
					initRedisClientAndAddToMap(cs[0], port, i, rcf, clientConfig);
				}
			}
		} else {
			for (int i = 0; i < 16; i++) {
				if (i != dbIndex) {
					initRedisClientAndAddToMap(hostname, port, i, rcf, clientConfig);
				}
			}
		}
		return rcf;
	}

	private void initRedisClientAndAddToMap(String hostname, int port, int db, RedisClientFactory rcf,
			LettuceClientConfiguration clientConfig) {
		RedisStandaloneConfiguration c = new RedisStandaloneConfiguration();
		c.setDatabase(db);
		c.setHostName(hostname);
		c.setPort(port);
		c.setPassword(RedisPassword.of(password));
		LettuceConnectionFactory f = new LettuceConnectionFactory(c, clientConfig);
		f.afterPropertiesSet();
		StringRedisTemplate t=new StringRedisTemplate(f);
		t.afterPropertiesSet();
		rcf.putRedis(db, t);
	}
}
