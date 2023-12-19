package com.play.core;

import java.util.Locale;
import java.util.Objects;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.web.controller.front.ThirdIndexController;
import com.play.web.i18n.BaseI18nCode;


/**
 * 从https://blog.csdn.net/listeningsea/article/details/6613715 查看
 * 
 * @author macair
 * 
 * </p>
 *  添加新得语言时，记得修改以下内容
 *  1、 {@link SystemUtil}
 * 	2、 {@link ThirdIndexController}
 *  3、 {@link BaseI18nCode}
 *  
 *  </p>
 *  同时注意添加新的翻译内容
 *  1、 /src/main/resources/messages/admin_es.properties
 *  2、 /src/main/resources/messages/base_es.properties
 *  3、 /src/main/webapp/common/lang/es.js
 * 
 */
public enum LanguageEnum {
	es("西班牙(西班牙语)", "español", new Locale("es", "MX")),
	
	th("泰国(泰文)", "ไทย", new Locale("th", "TH")),

	vi("越南(越南文)", "Tiếng Việt", new Locale("vi", "VN")),

	ms("马来(马来文)", "Melayu", new Locale("ms", "MY")),

	id("印尼(印尼文)", "Indonesia", new Locale("id", "ID")),

	/**
	 * 这俩个会容易出现问题 用的是枚举 或者后面locale的language的值
	 */
	in("印度(印地文)", "हिन्दी", new Locale("hi", "IN")),

	br("巴西(葡萄牙文)", "Português", new Locale("pt", "BR")),

	ja("日文", "日本語", new Locale("ja", "JP")),

	en("英文", "english", Locale.ENGLISH),

	cn("简体中文", "简体中文", Locale.CHINA),

	// tw("繁体中文", Locale.TAIWAN),

	;

	private String desc;
	private String lang;
	private Locale locale;

	private LanguageEnum(String desc, String lang, Locale locale) {
		this.desc = desc;
		this.lang = lang;
		this.locale = locale;
	}

	public String getDesc() {
		return desc;
	}

	public String getLang() {
		return lang;
	}

	public Locale getLocale() {
		return locale;
	}

	public static LanguageEnum getLanguageEnum(String lang) {
		for (LanguageEnum l : LanguageEnum.values()) {
			String locale = l.getLocale().getLanguage();
			if (Objects.equals(lang, l.name()) || Objects.equals(lang, locale)) {
				return l;
			}
		}
		return LanguageEnum.en;
	}

	public static LanguageEnum getLanguageEnum2(String lang) {
		for (LanguageEnum l : LanguageEnum.values()) {
			if (Objects.equals(lang, l.name())) {
				return l;
			}
		}
		return LanguageEnum.en;
	}

	public static JSONArray getLangs() {
		JSONArray arr = new JSONArray();
		JSONObject obj = null;
		for (LanguageEnum l : LanguageEnum.values()) {
			obj = new JSONObject();
			obj.put("name", l.name());
			obj.put("lang", l.getLang());
			arr.add(obj);
		}
		return arr;
	}
}
