package com.play.spring.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

import com.play.spring.extra.PropertyDESTool;

public class EncryptYamlPropertiesFactoryBean extends YamlPropertiesFactoryBean {

	/**
	 * 需要解密的配置项前缀
	 */
	private static final String PREFIX_ENC = "enc:";

	@Override
	public Properties getObject() {
		Properties p = super.getObject();
		for (Object propertyName : p.keySet()) {
			decrypt(propertyName.toString(), p);
		}
		return p;
	}

	private void decrypt(String propertyName, Properties p) {
		String value = p.getProperty(propertyName);
		if (StringUtils.startsWith(value, PREFIX_ENC)) {
			value = PropertyDESTool.getDecryptString(value.substring(4));
			p.setProperty(propertyName, value);
		}
	}

	public String getPropertyDefault(String name, String defaultVaule) {
		String v = getObject().getProperty(name);
		if (StringUtils.isEmpty(v)) {
			return defaultVaule;
		}
		return v;
	}

	public List<String> getByKeyPref(String pref) {
		Properties p = super.getObject();
		String value;
		List<String> list = new ArrayList<>();
		String key;
		for (Object obj : p.keySet()) {
			key = obj.toString();
			if (key.startsWith(pref)) {
				value = p.getProperty(key);
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

	public String getProperty(String key) {
		return getObject().getProperty(key);
	}
}
