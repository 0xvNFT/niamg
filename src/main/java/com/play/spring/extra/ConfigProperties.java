package com.play.spring.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源配置参数处理
 * <p/>
 * 配置信息事先被DES加密处理，需要在此解密然后绑定到数据源 Created by Alvin on 2016/7/31.
 */
public class ConfigProperties extends Properties {
	private static final long serialVersionUID = 6877283452655996998L;
	private static Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

	/**
	 * 构造方法
	 * 
	 * @param propertyNames 需要解密的属性名称
	 */
	public ConfigProperties() {
		try {
			this.load(ConfigProperties.class.getResourceAsStream("/application.properties"));
			for (Object propertyName : keySet()) {
				decrypt(propertyName.toString());
			}
		} catch (Exception e) {
			logger.error("读取配置文件发生错误", e);
		}
	}

	/**
	 * 解密
	 */
	private void decrypt(String propertyName) {
		String value = this.getProperty(propertyName);
		if (StringUtils.startsWith(value, "des-")) {
			value = PropertyDESTool.getDecryptString(value.substring(4));
			this.setProperty(propertyName, value);
		}
	}

	public String getPropertyDefault(String name, String defaultVaule) {
		String v = getProperty(name);
		if (StringUtils.isEmpty(v)) {
			return defaultVaule;
		}
		return v;
	}

	public List<String> getByKeyPref(String pref) {
		String value;
		List<String> list = new ArrayList<>();
		String key;
		for (Object obj : keySet()) {
			key = obj.toString();
			if (key.startsWith(pref)) {
				value = getProperty(key);
				if (StringUtils.isNotEmpty(value)) {
					for (String s : value.split(",")) {
						if (StringUtils.isNotEmpty(s)) {
							list.add(s.trim());
						}
					}
				}
			}
		}
		return list;
	}
}
