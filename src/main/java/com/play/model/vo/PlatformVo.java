package com.play.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.core.PlatformType;
import com.play.spring.config.i18n.I18nTool;

public class PlatformVo {
	private String value;
	private String name;
	@JSONField(serialize=false)
	private PlatformType type;

	public PlatformVo(String value, String name, PlatformType type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return I18nTool.getMessage("PlatformVo." + value, name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PlatformType getType() {
		return type;
	}

	public void setType(PlatformType type) {
		this.type = type;
	}

}
