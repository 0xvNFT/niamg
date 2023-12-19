package com.play.core;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

public enum ArticleTypeEnum {

	about_us(1, "关于我们"),

	get_money_help(2, "取款帮助"),

	in_money_help(3, "存款帮助"),

	alliance_program(4, "联盟方案"),

	alliance_union(5, "联盟协议"),

	contact_us(6, "联系我们"),

	common_problem(7, "常见问题"),

	liu_he_data(8, "六合论坛"),

	new_pop(9, "弹窗公告"),

	new_roll(13, "滚动公告"),

	vip_new_roll(23, "VIP等级滚动公告"),

	new_news(16, "最新资讯"),

	sign_role(17, "签到规则"),

	novice_tutorial(18, "新手教程"),

	mob_pop(19, "手机弹窗"),

	clause(20, "责任条款"),
	
	RE_role(21, "红包规则"),

	;

	private Integer code;
	private String name;

	ArticleTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return I18nTool.getMessage("ArticleTypeEnum." + this.name(), this.name);
	}

	/**
	 * 获取类型列表
	 *
	 * @param type 1：公告 2：最新资讯 3：系统资料
	 * @return
	 */
	public static JSONArray getList(Integer type) {
		JSONArray array = new JSONArray();
		switch (type) {
		case 1:
			array.add(assemJson(new_roll));
			array.add(assemJson(new_pop));
			array.add(assemJson(mob_pop));
			array.add(assemJson(vip_new_roll));
			return array;
		case 2:
			array.add(assemJson(new_news));
			return array;
		case 3:
			Long stationId = SystemUtil.getStationId();
			array.add(assemJson(about_us));
			array.add(assemJson(get_money_help));
			array.add(assemJson(in_money_help));
			array.add(assemJson(alliance_program));
			array.add(assemJson(alliance_union));
			array.add(assemJson(contact_us));
			array.add(assemJson(common_problem));
			array.add(assemJson(liu_he_data));
			array.add(assemJson(novice_tutorial));
			if (StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in)) {
				array.add(assemJson(sign_role));
			}
			array.add(assemJson(clause));
			if (StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag)) {
				array.add(assemJson(RE_role));
			}
			return array;
		default:
			return null;
		}
	}

	public static List<Integer> getCodeList(Integer type) {
		List<Integer> list = new ArrayList<>();
		switch (type) {
		case 1:
			list.add(new_pop.code);
			list.add(new_roll.code);
			list.add(mob_pop.code);
			list.add(vip_new_roll.code);
			return list;
		case 2:
			list.add(new_news.code);
			return list;
		case 3:
			list.add(about_us.code);
			list.add(get_money_help.code);
			list.add(in_money_help.code);
			list.add(alliance_program.code);
			list.add(alliance_union.code);
			list.add(contact_us.code);
			list.add(common_problem.code);
			list.add(liu_he_data.code);
			list.add(sign_role.code);
			list.add(novice_tutorial.code);
			list.add(clause.code);
			list.add(RE_role.code);
			return list;
		case 4:
			list.add(new_roll.code);
			list.add(mob_pop.code);
			return list;
		case 5:
			list.add(new_pop.code);
			list.add(new_roll.code);
			list.add(mob_pop.code);
			// list.add(vip_new_roll.code);
			return list;
		case 6:
			// pc前台公告
			list.add(new_pop.code);
			list.add(new_roll.code);
			return list;
		case 7:
		// pc前台公告
			list.add(new_roll.code);
			return list;
		case 8:
			// 弹窗公告
			list.add(new_pop.code);
			return list;
		default:
			return null;
		}
	}

	private static JSONObject assemJson(ArticleTypeEnum t) {
		JSONObject object = new JSONObject();
		object.put("code", t.getCode());
		object.put("name", t.getName());
		return object;
	}

	public static void main(String[] args) {
		for (ArticleTypeEnum t : ArticleTypeEnum.values()) {
			System.out.println("ArticleTypeEnum." + t.name()+"="+t.name);
		}
	}
}
