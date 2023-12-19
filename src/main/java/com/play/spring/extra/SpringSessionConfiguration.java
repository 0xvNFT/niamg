package com.play.spring.extra;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class SpringSessionConfiguration {

//	/**
//	 *  * Redis内session过期事件监听  
//	 */
//	@EventListener
//	public void onSessionExpired(SessionExpiredEvent expiredEvent) {
//		String sessionId = expiredEvent.getSessionId();
//		System.out.println("onSessionExpired sessId" + sessionId);
//	}
//
//	/**
//	 *  * Redis内session删除事件监听  
//	 */
//	@EventListener
//	public void onSessionDeleted(SessionDeletedEvent deletedEvent) {
//		String sessionId = deletedEvent.getSessionId();
//		System.out.println("onSessionDeleted sessId" + sessionId);
//	}
//
//	/**
//	 *  * Redis内session保存事件监听  
//	 */
//	@EventListener
//	public void onSessionCreated(SessionCreatedEvent createdEvent) {
//		String sessionId = createdEvent.getSessionId();
//		System.out.println("onSessionCreated sessId" + sessionId);
//	}

	/**
	 * 用于spring session，防止每次创建一个线程
	 * 
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor springSessionRedisTaskExecutor() {
		ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
		springSessionRedisTaskExecutor.setCorePoolSize(8);
		springSessionRedisTaskExecutor.setMaxPoolSize(16);
		springSessionRedisTaskExecutor.setKeepAliveSeconds(10);
		springSessionRedisTaskExecutor.setQueueCapacity(1000);
		springSessionRedisTaskExecutor.setThreadNamePrefix("redis-thread-task-");
		return springSessionRedisTaskExecutor;
	}

	@Bean
	public ThreadPoolTaskExecutor springSessionRedisSubscriptionExecutor() {
		ThreadPoolTaskExecutor springSessionRedisTaskExecutor = new ThreadPoolTaskExecutor();
		springSessionRedisTaskExecutor.setCorePoolSize(8);
		springSessionRedisTaskExecutor.setMaxPoolSize(16);
		springSessionRedisTaskExecutor.setKeepAliveSeconds(10);
		springSessionRedisTaskExecutor.setQueueCapacity(1000);
		springSessionRedisTaskExecutor.setThreadNamePrefix("redis-thread-subscription-");
		return springSessionRedisTaskExecutor;
	}

}
