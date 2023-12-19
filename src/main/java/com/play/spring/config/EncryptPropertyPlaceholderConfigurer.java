package com.play.spring.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import com.play.spring.extra.PropertyDESTool;

/**
 * 配置解密,只能自定义一个PropertySourcesPlaceholderConfigurer,否则会报异常
 *
 * 目前没用使用EncryptYamlPropertiesFactoryBean 机制
 **/
public class EncryptPropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer
		implements InitializingBean {

	/**
	 * 需要解密的配置项前缀
	 */
	private static final String PREFIX_ENC = "enc:";

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Properties mergeProperties() throws IOException {
		Properties mergedProperties = new Properties();
		for (Properties localProp : localProperties) {
			mergedProperties.putAll(localProp);
		}

		String value = null;
		for (Map.Entry entry : mergedProperties.entrySet()) {
			value = entry.getValue().toString();
			if (StringUtils.startsWith(value, PREFIX_ENC)) {
				mergedProperties.setProperty(entry.getKey().toString(),
						PropertyDESTool.getDecryptString(value.substring(4)));
			}
		}

		// 针对sharding-jdbc datasource自定义解密的特殊处理
		// 因为sharding-jdbc的datasource注入是从environment中获取propertySource,
		// 不能直接通过PropertySourcesPlaceholderConfigurer定义的resource获取
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
		sources.addFirst(new PropertiesPropertySource(LOCAL_PROPERTIES_PROPERTY_SOURCE_NAME, mergedProperties));

		return mergedProperties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		localOverride = true;
	}
}