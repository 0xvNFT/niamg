package com.play.core;

import com.play.spring.config.i18n.I18nTool;

public enum DeviceTypeEnum {
	ALL(1, "all"),
	PC(2, "pc"),
	MOBILE(3, "app");

	private int type;
	
	private String desc;

	private DeviceTypeEnum(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return I18nTool.getMessage("DeviceType." + this.name(), this.desc);
	}

	public static void main(String[] args) {
		for (DeviceTypeEnum t : DeviceTypeEnum.values()) {
			System.out.println("DeviceType." + t.name()+"="+t.desc);
		}
	}
}
