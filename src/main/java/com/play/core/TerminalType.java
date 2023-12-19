package com.play.core;

import com.play.spring.config.i18n.I18nTool;

public enum TerminalType {
	PC(1, "电脑"), WAP(2, "手机浏览器"), APP(3, "原生app"), ANDROID(31, "安卓"), IOS(32, "苹果"), CHAT(4, "聊天室");

	private int type;
	private String desc;

	private TerminalType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return I18nTool.getMessage("TerminalType." + this.name(), this.desc);
	}
	
	public static void main(String[] args) {
		for (TerminalType t : TerminalType.values()) {
			System.out.println("TerminalType." + t.name()+"="+t.desc);
		}
	}
}
