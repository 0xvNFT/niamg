package com.play.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum SysConfigEnum {

	sys_server_name("集群名称", "text", "系统配置", 90),

	sys_tron_pro_api_key("API_KEY", "text", "Tron链配置", 91),
	
	third_center_token("真人中心授权口令", "text", "第三方系统配置", 200),
	
	third_center_url("真人中心服务器地址", "text", "第三方系统配置", 190),
	
	;

	private String cname;// 配置的名称
	private String eleType;// 前端输入的类型（text, select)
	private String title;// 描述标题
	private String initValue;// 初始值
	private String groupName;// 分组名称
	private int sortNo;// 序号

	public static int sport_max_kickback = 1;

	private SysConfigEnum(String cname, String eleType, String groupName, int sortNo) {
		this(cname, eleType, cname, null, groupName, sortNo);
	}

	private SysConfigEnum(String cname, String eleType, String initValue, String groupName, int sortNo) {
		this(cname, eleType, cname, initValue, groupName, sortNo);
	}

	private SysConfigEnum(String cname, String eleType, String title, String initValue, String groupName, int sortNo) {
		this.cname = cname;
		this.eleType = eleType;
		this.title = title;
		this.initValue = initValue;
		this.groupName = groupName;
		this.sortNo = sortNo;
	}

	public String getCname() {
		return cname;
	}

	public String getEleType() {
		return eleType;
	}

	public String getTitle() {
		return title;
	}

	public String getInitValue() {
		return initValue;
	}

	public String getGroupName() {
		return groupName;
	}

	public int getSortNo() {
		return sortNo;
	}

	public static List<String> getGroupNameList() {
		List<String> list = new ArrayList<>();
		for (SysConfigEnum e : values()) {
			if (!list.contains(e.getGroupName())) {
				list.add(e.getGroupName());
			}
		}
		return list;
	}

	public static JSONObject groupMap(Map<String, String> data) {
		Map<String, List<SysConfigEnum>> map = new HashMap<>();
		List<SysConfigEnum> list = null;
		for (SysConfigEnum e : values()) {
			list = map.get(e.getGroupName());
			if (list == null) {
				list = new ArrayList<>();
				map.put(e.getGroupName(), list);
			}
			list.add(e);
		}
		for (List<SysConfigEnum> l : map.values()) {
			l.sort(new Comparator<SysConfigEnum>() {
				@Override
				public int compare(SysConfigEnum o1, SysConfigEnum o2) {
					return o2.getSortNo() - o1.getSortNo();
				}
			});
		}
		return toJSON(map, data);
	}

	private static JSONObject toJSON(Map<String, List<SysConfigEnum>> map, Map<String, String> data) {
		int id = 0;
		JSONObject json = new JSONObject();
		JSONArray arr = null;
		JSONObject ele = null;
		for (String key : map.keySet()) {
			for (SysConfigEnum sc : map.get(key)) {
				ele = new JSONObject();
				ele.put("key", sc.name());
				ele.put("name", sc.getCname());
				ele.put("title", sc.getTitle());
				ele.put("eleType", sc.getEleType());
				if (data.containsKey(sc.name())) {
					ele.put("initValue", data.get(sc.name()));
				} else {
					ele.put("initValue", sc.getInitValue());
				}
				if (json.containsKey(key)) {
					arr = json.getJSONObject(key).getJSONArray("sons");
					arr.add(ele);
				} else {
					arr = new JSONArray();
					arr.add(ele);
					ele = new JSONObject();
					ele.put("id", id++);
					ele.put("sons", arr);
					json.put(key, ele);
				}
			}
		}
		return json;
	}
}
