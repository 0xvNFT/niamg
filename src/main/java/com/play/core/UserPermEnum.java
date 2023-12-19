package com.play.core;

import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;

public enum UserPermEnum {
	deposit(1, "存款", true),

	withdraw(2, "取款", true),

	thirdConvert(3, "真人额度转换", false),

	liveBet(4, "真人投注", false),

	egameBet(5, "电子投注", false),

	chessBet(6, "棋牌投注", false),

	sportBet(7, "体育投注", false),

	fishingBet(8, "捕鱼投注", false),

	esportBet(9, "电竞投注", false),
	
	lotteryBet(10, "彩票投注", true),

	rebate(20, "会员反水", true),

	proxyRebate(21, "代理(推荐)返点", false),

	signIn(22, "签到", true),

	turntable(23, "大转盘", true),

	redEnvelope(24, "抢红包", true),

	exchangeScore(25, "积分兑换", true),

	depositGift(26, "充值优惠", true),

	moneyIncome(27, "余额生金", true),

	upgradeGift(28, "等级晋升奖金", true),

	activeApply(29, "活动申请", true),

	
	;

	private int type;
	private String desc;
	private boolean guestStatus = true;// 试玩账号状态

	private UserPermEnum(int type, String desc, boolean guestStatus) {
		this.type = type;
		this.desc = desc;
		this.guestStatus = guestStatus;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return I18nTool.getMessage("UserPermEnum." + this.name(), desc);
	}

	public boolean isGuestStatus() {
		return guestStatus;
	}

	public static void main(String[] args) {
		for (UserPermEnum e : UserPermEnum.values()) {
			System.out.println("UserPermEnum."+e.name()+"="+e.desc);
//			System.out.println("private boolean " + e.name() + ";");
		}
	}

	public static UserPermEnum getPerm(Integer type) {
		if (type == null) {
			return null;
		}
		for (UserPermEnum e : UserPermEnum.values()) {
			if (Objects.equals(e.getType(), type)) {
				return e;
			}
		}
		return null;
	}

}
