package com.play.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;

/**
 * 打码量类型
 */
public enum BetNumTypeEnum {
	live(1, "真人"),

	egame(2, "电子"),

	sport(3, "体育"),

	chess(4, "棋牌"),

	esport(5, "电竞"),

	fishing(6, "捕鱼"),

	lottery(7, "彩票"),

	drawneedAdd(10, "人工增加"),

	drawneedSub(11, "人工扣除"),

	registGift(12, "注册赠送"),

	redPacket(13, "红包"),

	bonusDeficit(14, "每日加奖、每周亏损"),

	backWater(15, "反水"),

	deposit(16, "存款"),

	active(17, "活动中奖"),

	upgradeDegree(18, "晋升等级赠送"),

	incomeMoney(19, "余额生金收益"),

	proxyRebate(20, "代理返点收益"),

	scoreToMoney(21, "积分兑换现金"),

	rechargeableCard(22, "充值卡"),

	coupons(23, "优惠券"),

	promotionActive(24, "优惠活动"),

	signIn(25, "签到奖励"),

	inviteRebate(26, "邀请人返佣"),

	;

	private int type;
	private String title;

	public int getType() {
		return type;
	}

	public String getTitle() {
		return I18nTool.getMessage("BetNumTypeEnum." + this.name(), this.title);
	}

	private BetNumTypeEnum(int type, String title) {
		this.type = type;
		this.title = title;
	}

	public static List<BetNumTypeEnum> getBetGameType() {
		List<BetNumTypeEnum> list = new ArrayList<>();
		list.add(live);
		list.add(egame);
		list.add(sport);
		list.add(chess);
		list.add(esport);
		list.add(fishing);
		list.add(lottery);
		return list;
	}

	public static BetNumTypeEnum getType(Integer type) {
		if (type == null) {
			return null;
		}
		for (BetNumTypeEnum t : BetNumTypeEnum.values()) {
			if (Objects.equals(t.type, type)) {
				return t;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		for (BetNumTypeEnum t : BetNumTypeEnum.values()) {
			System.out.println("BetNumTypeEnum." + t.name() + "=" + t.title);
		}
	}
}
