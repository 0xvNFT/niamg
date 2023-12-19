package com.play.spring.config.i18n;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 国际化配置
 * 
 * @author macair
 *
 */
@Configuration
public class I18nConfig {
	/**
	 * 配置SessionLocaleResolver用于将Locale对象存储于Session中供后续使用
	 * 
	 * CookieLocaleResolver 将语言版本存储到cookie中
	 * 
	 * AcceptHeaderLocaleResolver 请求头部的Accept-Language
	 * 
	 * @return
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}

	/**
	 * https://blog.csdn.net/DislodgeCocoon/article/details/80520235
	 * ReloadableResourceBundleMessageSource提供了定时刷新功能，允许在不重启系统的情况下，更新资源的信息
	 * 
	 * @return
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource(@Value("${sys.i18n.cacheSeconds}") String cacheSeconds) {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setBasenames("classpath:/messages/base","classpath:/messages/admin");
		ms.setUseCodeAsDefaultMessage(true);
		ms.setDefaultEncoding("UTF-8");
		if (cacheSeconds != null) {
			ms.setCacheSeconds(NumberUtils.toInt(cacheSeconds, -1));
		} else {
			ms.setCacheSeconds(-1);
		}
		return ms;
	}
}
