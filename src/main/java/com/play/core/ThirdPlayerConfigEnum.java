package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.spring.config.i18n.I18nTool;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ThirdPlayerConfigEnum {

	ag_oddtype("AG盘口", "select", "oddtype", PlatformType.AG);

	private String label;
	/**
	 * 类型 select,text
	 */
	private String type;
	private String paramName;// 实际参数名
	private PlatformType platform;// 第三方类型,1=ag，2=

	ThirdPlayerConfigEnum(String label, String type, String paramName, PlatformType platform) {
		this.label = label;
		this.type = type;
		this.paramName = paramName;
		this.platform = platform;
	}

	public static JSONArray getConfigList() {
		JSONArray array = new JSONArray();
		Arrays.stream(ThirdPlayerConfigEnum.values()).forEach(x -> {
			JSONObject o = new JSONObject();
			o.put("name", x.name());
			o.put("label", x.getLabel());
			o.put("type", x.type);
			array.add(o);
		});
		return array;
	}

	public String getLabel() {
		return I18nTool.getMessage("ThirdPlayerConfigEnum." + this.name(), this.label);
	}

	public String getType() {
		return type;
	}

	public String getParamName() {
		return paramName;
	}

	public PlatformType getPlatform() {
		return platform;
	}

	public static String getParamName(String name, PlatformType platform) {
		if (StringUtils.isNotEmpty(name)) {
			for (ThirdPlayerConfigEnum p : ThirdPlayerConfigEnum.values()) {
				if (p.getPlatform() == platform && StringUtils.equals(name, p.name())) {
					return p.getParamName();
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		for (ThirdPlayerConfigEnum t : ThirdPlayerConfigEnum.values()) {
			System.out.println("ThirdPlayerConfigEnum." + t.name() + "=" + t.label);
		}
	}
}
