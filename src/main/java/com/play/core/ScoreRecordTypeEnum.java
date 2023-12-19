package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.spring.config.i18n.I18nTool;

/**
 * 积分来源类型
 *
 * @author admin
 */
public enum ScoreRecordTypeEnum {

	/**
	 * 活动积分扣除
	 */
	ACTIVE_SUB(1, false, "活动积分扣除"),

	/**
	 * 会员签到
	 */
	SIGN_IN(2, true, "会员签到"),
	/**
	 * 现金兑换积分
	 */
	EXCHANGE_MNY_TO_SCORE(3, true, "现金兑换积分"),
	/**
	 * 积分兑换现金
	 */
	EXCHANGE_SCORE_TO_MNY(4, false, "积分兑换现金"),
	/**
	 * 存款赠送
	 */
	DEPOSIT_GIFT_ACTIVITY(5, true, "存款赠送"),
	/**
	 * 人工入款
	 */
	ADD_ARTIFICIAL(6, true, "人工添加"),
	/**
	 * 人工出款
	 */
	SUB_ARTIFICIAL(7, false, "人工扣除"),
	/**
	 * 活动积分赠送
	 */
	ACTIVE_ADD(8, true, "活动积分赠送"),

	PROMOTION_ACTIVE(12, true, "优惠活动"),

	REGISTER_GIFT(13, true, "注册赠送"),

    /**
     * 首次提款赠送
     */
    FIRST_WITHDRAW_GIFT_ACTIVITY(14, true, "首次提款赠送");

	private int type;
	private boolean add;// true : 加积分操作 false：扣积分操作
	private String name;

	public int getType() {
		return type;
	}

	private ScoreRecordTypeEnum(Integer type, boolean add, String name) {
		this.type = type;
		this.add = add;
		this.name = name;
	}

	public boolean isAdd() {
		return add;
	}

	public String getName() {
		return I18nTool.getMessage("ScoreRecordTypeEnum." + this.name(), this.name);
	}

	public static ScoreRecordTypeEnum getScoreRecordType(Integer val) {
		if (val == null) {
			return null;
		}
		for (ScoreRecordTypeEnum t : ScoreRecordTypeEnum.values()) {
			if (t.getType() == val) {
				return t;
			}
		}
		return null;
	}

	public static JSONArray getTypeArray() {
		JSONArray array = new JSONArray();
		for (ScoreRecordTypeEnum anEnum : ScoreRecordTypeEnum.values()) {
			JSONObject object = new JSONObject();
			object.put("type", anEnum.type);
			object.put("name", anEnum.getName());
			array.add(object);
		}
		return array;
	}

	public static void main(String[] args) {
		for (ScoreRecordTypeEnum t : ScoreRecordTypeEnum.values()) {
			System.out.println("ScoreRecordTypeEnum." + t.name() + "=" + t.name);
		}
	}
}
