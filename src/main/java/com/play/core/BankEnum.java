//package com.play.core;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//public enum BankEnum {
//	/* 银行入款 */
//	ZGGSYX(LanguageEnum.cn, "中国工商银行", 10, "1"), ZGNYYX(LanguageEnum.cn, "中国农业银行", 20, "2"),
//	ZGMSYX(LanguageEnum.cn, "中国民生银行", 30, "3"), ZGJSYX(LanguageEnum.cn, "中国建设银行", 40, "4"),
//	ZGZSYX(LanguageEnum.cn, "中国招商银行", 50, "5"), ZGYX(LanguageEnum.cn, "中国银行", 60, "6"),
//	ZGJTYX(LanguageEnum.cn, "中国交通银行", 70, "7"), ZGYZYX(LanguageEnum.cn, "中国邮政银行", 80, "8"),
//	ZGXYYX(LanguageEnum.cn, "中国兴业银行", 90, "9"), HXYX(LanguageEnum.cn, "华夏银行", 100, "10"),
//	PFYX(LanguageEnum.cn, "浦发银行", 110, "11"), GZYX(LanguageEnum.cn, "广州银行", 120, "12"),
//	BEADYYX(LanguageEnum.cn, "东亚银行", 130, "13"), GZNSYX(LanguageEnum.cn, "广州农商银行", 140, "14"),
//	SDNSYX(LanguageEnum.cn, "顺德农商银行", 150, "15"), BJYX(LanguageEnum.cn, "北京银行", 160, "16"),
//	PAYX(LanguageEnum.cn, "平安银行", 170, "17"), HZYX(LanguageEnum.cn, "杭州银行", 180, "18"),
//	WZYX(LanguageEnum.cn, "温州银行", 190, "19"), SHNSYX(LanguageEnum.cn, "上海农商银行", 200, "20"),
//	YDXYS(LanguageEnum.cn, "山东尧都农村商业银行银行", 210, "21"), ZGGDYX(LanguageEnum.cn, "中国光大银行", 220, "22"),
//	ZXYX(LanguageEnum.cn, "中信银行", 230, "23"), BHYX(LanguageEnum.cn, "渤海银行", 240, "24"),
//	ZSYX(LanguageEnum.cn, "浙商银行", 250, "25"), SXJSYX(LanguageEnum.cn, "晋商银行", 260, "26"),
//	HKYX(LanguageEnum.cn, "汉口银行", 270, "27"), ZJCZSYYX(LanguageEnum.cn, "浙江稠州商业银行", 280, "28"),
//	SHYX(LanguageEnum.cn, "上海银行", 290, "29"), GFYX(LanguageEnum.cn, "广发银行", 300, "30"),
//	DWYX(LanguageEnum.cn, "东莞银行", 310, "32"), NBYX(LanguageEnum.cn, "宁波银行", 320, "33"),
//	NJYX(LanguageEnum.cn, "南京银行", 330, "34"), BJNSYX(LanguageEnum.cn, "北京农商银行", 340, "36"),
//	ZQYX(LanguageEnum.cn, "重庆银行", 350, "37"), GXXYS(LanguageEnum.cn, "广西农村信用社", 360, "38"),
//	JSYX(LanguageEnum.cn, "江苏银行", 370, "40"), JLYX(LanguageEnum.cn, "吉林银行", 380, "41"),
//	CDYX(LanguageEnum.cn, "成都银行", 390, "42"), TJYH(LanguageEnum.cn, "天津银行", 400, "46"),
//	JCYX(LanguageEnum.cn, "晋城银行", 410, "47"), HDYH(LanguageEnum.cn, "邯郸银行", 420, "48"),
//	ZZYH(LanguageEnum.cn, "郑州银行", 430, "49"), JJYH(LanguageEnum.cn, "九江银行", 440, "50"),
//	SJYH(LanguageEnum.cn, "盛京银行", 450, "52"), AHXYS(LanguageEnum.cn, "安徽省农村信用社联合社", 460, "53"),
//	GSYH(LanguageEnum.cn, "甘肃银行", 470, "54"), GSXYS(LanguageEnum.cn, "甘肃农村信用社", 480, "55"),
//	DGNSYH(LanguageEnum.cn, "东莞农村商业银行", 490, "56"), GDNSYH(LanguageEnum.cn, "广东农商银行", 500, "57"),
//	GLYH(LanguageEnum.cn, "桂林银行", 510, "58"), XAYH(LanguageEnum.cn, "西安银行", 520, "59"),
//	GDNCYH(LanguageEnum.cn, "广东省农村信用社联合社", 530, "60"), GZNCYH(LanguageEnum.cn, "贵州省农村信用社联合社", 540, "61"),
//	JXYH(LanguageEnum.cn, "江西银行", 550, "62"), OTHER(LanguageEnum.cn, "其他银行", 560, "999");
//
//	/**
//	 * 语种
//	 */
//	private LanguageEnum le;
//	/**
//	 * 支付名称
//	 */
//	private String payName;
//	/**
//	 * 排序
//	 */
//	private Integer sortNo;
//
//	/**
//	 * todo 提现页面显示 银行图标代码
//	 */
//	private String iconCode;
//
//	BankEnum(LanguageEnum le, String payName, Integer sortNo, String iconCode) {
//		this.le = le;
//		this.payName = payName;
//		this.sortNo = sortNo;
//		this.iconCode = iconCode;
//	}
//
//	public LanguageEnum getLe() {
//		return le;
//	}
//
//	public String getPayName() {
//		return payName;
//	}
//
//	public Integer getSortNo() {
//		return sortNo;
//	}
//
//	public String getIconCode() {
//		return iconCode;
//	}
//
//	/**
//	 * 获取OTHER
//	 */
//	public static String getOther() {
//		return BankEnum.OTHER.name();
//	}
//
//	private static Map<String, List<BankEnum>> map = new HashMap<>();
//
//	public static List<BankEnum> getList(String language) {
//		if (StringUtils.isEmpty(language)) {
//			return null;
//		}
//		List<BankEnum> list = map.get(language);
//		if (list != null) {
//			return list;
//		}
//		list = new ArrayList<>();
//		for (BankEnum b : BankEnum.values()) {
//			if (b.getLe().name().equals(language)) {
//				list.add(b);
//			}
//		}
//		Collections.sort(list, new Comparator<BankEnum>() {
//			@Override
//			public int compare(BankEnum o1, BankEnum o2) {
//				return o1.getSortNo() - o2.getSortNo();
//			}
//		});
//		map.put(language, list);
//		return list;
//	}
//
//	public static JSONArray getArrayByType(String language) {
//		JSONArray array = new JSONArray();
//		List<BankEnum> list = getList(language);
//		for (BankEnum p : list) {
//			JSONObject object = new JSONObject();
//			object.put("code", p.name());
//			object.put("payName", p.getPayName());
//			object.put("sortNo", p.getSortNo());
//			array.add(object);
//		}
//		array.sort((x, y) -> JSONObject.parseObject(x.toString()).getLongValue("sortNo") < JSONObject
//				.parseObject(y.toString()).getLongValue("sortNo") ? -1 : 1);
//		return array;
//	}
//
//	/**
//	 * 根据iconCode 获取银行名称
//	 *
//	 * @param iconCode
//	 * @return
//	 */
//	public static String getPayNameByBankCode(String bankCode) {
//		if (StringUtils.isNotEmpty(bankCode)) {
//			for (BankEnum b : BankEnum.values()) {
//				if (StringUtils.equals(b.name(), bankCode) || StringUtils.equals(b.getIconCode(), bankCode)) {
//					return b.getPayName();
//				}
//			}
//		}
//		return "";
//	}
//
//}
