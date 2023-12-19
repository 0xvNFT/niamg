package com.play.spring.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ResourcePlaceholderConfig {

	@Bean
	public YamlPropertiesFactoryBean yamlConfigurer() {
		YamlPropertiesFactoryBean yaml = new EncryptYamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("application.yml"));
		return yaml;
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
		config.setProperties(yamlConfigurer().getObject());
		return config;
	}
	
}
