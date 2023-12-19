package com.play.core;

import com.play.spring.config.i18n.I18nTool;

public enum LogTypeEnum {

	MODIFY_STATUS(10, "修改状态"), MODIFY_PWD(15, "修改密码"), ADD_USER(20, "添加用户"),

	VIEW_LIST(25, "查看列表"), VIEW_DETAIL(30, "查看数据详情"), VIEW_REPORT(35, "查看报表"),

	DEL_DATA(40, "删除数据"), ADD_DATA(45, "添加数据"), MODIFY_DATA(50, "修改数据"),

	EXPORT(60, "导出数据"),

	APP(65, "APP数据"),

	// PULL_DATA(60, "拉取数据"),

	DEFAULT_TYPE(100, "其他");

	private Integer type;
	private String desc;

	private LogTypeEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return I18nTool.getMessage("LogTypeEnum." + this.name(), desc);
	}

	public static void main(String[] args) {
		for (LogTypeEnum t : LogTypeEnum.values()) {
			System.out.println("LogTypeEnum." + t.name() + "=" + t.desc);
		}
	}

}
