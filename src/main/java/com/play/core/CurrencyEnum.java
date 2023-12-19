package com.play.core;

import org.apache.commons.lang3.StringUtils;

import com.play.spring.config.i18n.I18nTool;

public enum CurrencyEnum {
	MXN("墨西哥比索"),
	
	THB("泰铢"),

	MYR("马来西亚林吉特"),

	VND("越南盾"),

	IDR("印尼盾"),
	
	INR("印度卢比"),
	
	BRL("巴西雷亚尔"),

	USD("美元"),

	CNY("人民币"),

	JPY("日元"),

	// EUR("欧元"), HKD("港币"), GBP("英镑"),HKD("港幣"),TWD("台币"),

//	JPY("日元"), KRW("韩元"), CAD("加元"), AUD("澳元"), CHF("瑞郎"),

//	SGD("新加坡元"), NZD("新西兰"), PHP("菲律宾"),

	;

	private String desc;

	private CurrencyEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return I18nTool.getMessage("CurrencyEnum." + this.name(), this.desc);
	}

	public static CurrencyEnum getCurrency(String currency) {
		if (StringUtils.isEmpty(currency))
			return null;
		for (CurrencyEnum c : CurrencyEnum.values()) {
			if (c.name().equals(currency)) {
				return c;
			}
		}
		return null;
	}
	public static void main(String[] args) {
		for (CurrencyEnum t : CurrencyEnum.values()) {
			System.out.println("CurrencyEnum." + t.name()+"="+t.desc);
		}
	}
}
