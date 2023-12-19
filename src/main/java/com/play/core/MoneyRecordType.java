package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.spring.config.i18n.I18nTool;

import java.util.Arrays;

public enum MoneyRecordType {

	DEPOSIT_ARTIFICIAL(1, true, "系统加款"),

	WITHDRAW_ARTIFICIAL(2, false, "系统扣款"),

	WITHDRAW_ONLINE_FAILED(3, true, "在线取款失败", false),

	WITHDRAW_ONLINE(4, false, "在线取款"),

	DEPOSIT_ONLINE_THIRD(5, true, "在线支付"),

	DEPOSIT_ONLINE_FAST(6, true, "快速入款"),

	DEPOSIT_ONLINE_BANK(7, true, "银行入款"),

	DEPOSIT_RECHARGE_CARD(8, true, "充值卡入款"),

	DEPOSIT_COUPONS(9, true, "代金券"),

	MEMBER_ROLL_BACK_ADD(10, true, "反水加钱"),

	MEMBER_ROLL_BACK_SUB(11, false, "反水回滚"),

	PROXY_REBATE_ADD(12, true, "代理返点加钱"),

	PROXY_REBATE_SUB(13, false, "代理返点回滚"),

	REAL_GAME_ADD(14, true, "三方额度转入系统额度"),

	REAL_GAME_SUB(15, false, "系统额度转入三方额度"),

	REAL_GAME_FAILED(16, true, "三方转账失败"),

	DEPOSIT_GIFT_ACTIVITY(17, true, "存款赠送"),

	LEVEL_UPGRADE_GIFT(18, true, "升级赠送"),

	ACTIVE_AWARD(20, true, "转盘中奖"),

	// MID_AUTUMN_ACTIVE_AWARD(21, true, "中秋活动中奖"),

	// COUNT_MONEY_ACTIVE_AWARD(22, true, "滑一滑活动中奖"),

	RED_ACTIVE_AWARD(23, true, "红包中奖"),

	PROMOTION_ACTIVE_AWARD(24, true, "优惠活动"),

	// MEMBER_BONUS_AWARD(25, true, "每日加奖"),
	//
	// MEMBER_DEFICIT_AWARD(26, true, "每周亏损奖励"),

	MEMBER_SIGN_AWARD(27, true, "会员签到奖励"),

	EXCHANGE_MNY_TO_SCORE(30, false, "现金兑换积分"),

	EXCHANGE_SCORE_TO_MNY(31, true, "积分兑换现金"),

	// MONEY_INCOME(32, true, "余额生金收益"),

	// MONEY_INCOME_ROLLBACK(33, false, "余额生金收益失效回款", false),

	// DRAGON_BOAT_AWARD(34, true, "端午龙舟奖品兑换"),

	SUB_GIFT_AMOUNT(35, false, "彩金扣款"),
	INVITE_REG_GIFT_BACK(36, true, "邀请注册返佣"),
	INVITE_DEPOSIT_GIFT_BACK(37, true, "邀请充值返佣"),
	REGISTER_GIFT_BACK(38, true, "注册赠送"),
    FIRST_WITHDRAW(39, true, "首次提款赠送"),
	GUEST_TRY(40, true, "游客试玩金"),
	GIFT_OTHER(90, true, "其他"),

	;

	private int type;
	private boolean add;// true : 加款操作 false：扣款操作
	private boolean addTimes = true;// 是加次数，还是减次数，比如在线取款就是加次数，在线取款失败就得减次数
	private String name;

	public int getType() {
		return type;
	}

	private MoneyRecordType(int type, boolean add, String name) {
		this.type = type;
		this.add = add;
		this.name = name;
	}

	private MoneyRecordType(int type, boolean add, String name, boolean addTimes) {
		this.type = type;
		this.add = add;
		this.name = name;
		this.addTimes = addTimes;
	}

	public boolean isAdd() {
		return add;
	}

	public String getName() {
		return I18nTool.getMessage("MoneyRecordType." + this.name(), this.name);
	}

	public boolean isAddTimes() {
		return addTimes;
	}

	public static MoneyRecordType getMoneyRecordType(int val) {
		for (MoneyRecordType t : MoneyRecordType.values()) {
			if (t.getType() == val) {
				return t;
			}
		}
		return null;
	}

	public static JSONArray getTypes() {
		JSONArray array = new JSONArray();
		Arrays.stream(MoneyRecordType.values()).forEach(x -> {
			JSONObject object = new JSONObject();
			object.put("type", x.getType());
			object.put("name", x.getName());
			array.add(object);
		});
		return array;
	}

	public static void main(String[] args) {
		for (MoneyRecordType t : MoneyRecordType.values()) {
			System.out.println("MoneyRecordType." + t.name() + "=" + t.name);
		}
	}
}
