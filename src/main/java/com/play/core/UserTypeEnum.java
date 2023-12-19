package com.play.core;

import java.util.ArrayList;
import java.util.List;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.var.SystemUtil;

public enum UserTypeEnum {
	MANAGER_SUPER(1, UserGroupEnum.head_quarters, "超级总控"), // 总控

	MANAGER(10, UserGroupEnum.head_quarters, "普通总控"), // 总控

	PARTNER_SUPER(20, UserGroupEnum.partner, "合作商后台超级管理员"), // 合作商后台，总部用的

	ADMIN_MASTER_SUPER(40, UserGroupEnum.admin, "站点后台超级管理员"), // 站点后台总部的超级管理员

	ADMIN_MASTER(50, UserGroupEnum.admin, "站点后台总部管理员"), // 站点后台总部的普通管理员

	PARTNER_DEFAULT(60, UserGroupEnum.partner, "合作商后台超级管理员"), // 合作商后台超级管理员，合作商使用

	PARTNER(70, UserGroupEnum.partner, "合作商后台管理员"), // 合作商后台普通管理员

	ADMIN_PARTNER_SUPER(80, UserGroupEnum.admin, "站点后台合作商超级管理员"), // 站点后台合作商超级管理员

	ADMIN_PARTNER(90, UserGroupEnum.admin, "站点后台合作商管理员"), // 站点后台合作商管理员

	ADMIN(100, UserGroupEnum.admin, "站点管理员"), // 站点后台普通管理员

	AGENT(110, UserGroupEnum.agent, "站点下代理商"),

	PROXY(120, UserGroupEnum.front, "代理"),

	MEMBER(130, UserGroupEnum.front, "正式玩家"),

	GUEST(150, UserGroupEnum.front, "试玩玩家"),

	GUEST_BACK(160, UserGroupEnum.front, "后台试玩玩家"),

	SYS_JOB(240, UserGroupEnum.other, "定时器"),

	RECEIVE(245, UserGroupEnum.other, "数据接收器"),

	DEFAULT_TYPE(255, UserGroupEnum.other, "其他");

	private int type;
	private UserGroupEnum group;
	private String desc;

	private UserTypeEnum(int type, UserGroupEnum group, String desc) {
		this.type = type;
		this.group = group;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return I18nTool.getMessage("UserTypeEnum." + this.name(), desc);
	}

	public UserGroupEnum getGroup() {
		return group;
	}

	public static List<UserTypeEnum> getLotUserTypeList() {
		List<UserTypeEnum> logTypes = new ArrayList<>();
		int type = SystemUtil.getUserTypeVaule();
		for (UserTypeEnum lt : UserTypeEnum.values()) {
			if (lt.getType() < type) {
				continue;
			}
			logTypes.add(lt);
		}
		return logTypes;
	}

	public static UserTypeEnum getUserType(Integer userType) {
		if (userType == null) {
			return null;
		}
		for (UserTypeEnum lt : UserTypeEnum.values()) {
			if (lt.getType() == userType) {
				return lt;
			}
		}
		return null;
	}

	public boolean isManager() {// 总控
		return this.getGroup() == UserGroupEnum.head_quarters;
	}

	public boolean isAdmin() {// 租户后台
		return this.getGroup() == UserGroupEnum.admin;
	}

	public static List<UserTypeEnum> getAdminTypes() {
		List<UserTypeEnum> logTypes = new ArrayList<>();
		int type = SystemUtil.getUserTypeVaule();
		for (UserTypeEnum lt : UserTypeEnum.values()) {
			if (lt.getType() < type) {
				continue;
			}
			if (lt.getGroup() != UserGroupEnum.admin) {
				continue;
			}
			logTypes.add(lt);
		}
		return logTypes;
	}

	public boolean isAgent() {// 代理商后台
		return this.getGroup() == UserGroupEnum.agent;
	}

	public boolean isFront() {// 前台
		return this.getGroup() == UserGroupEnum.front;
	}

	public static void main(String[] args) {
		for (UserTypeEnum e : UserTypeEnum.values()) {
			System.out.println("UserTypeEnum." + e.name() + "=" + e.desc);
		}
	}
}
