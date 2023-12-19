package com.play.core;

import com.play.spring.config.i18n.I18nTool;

public enum UserGroupEnum {
	head_quarters(1, "总控"),

	partner(2, "合作商管理后台"),

	admin(3, "站点后台"),

	agent(4, "站点下代理商后台"),

	front(5, "前台"),

	other(0, "其他");

	private int group;// 1=总控，2=合作商管理后台，3=站点后台，4=站点下代理商后台，5=前台，0=其他
	private String desc;

	private UserGroupEnum(int group, String desc) {
		this.group = group;
		this.desc = desc;
	}

	public int getGroup() {
		return group;
	}

	public String getDesc() {
		return I18nTool.getMessage("UserGroupEnum." + this.name(), this.desc);
	}

	public static void main(String[] args) {
		for (UserGroupEnum t : UserGroupEnum.values()) {
			System.out.println("UserGroupEnum." + t.name() + "=" + t.desc);
		}
	}
}
