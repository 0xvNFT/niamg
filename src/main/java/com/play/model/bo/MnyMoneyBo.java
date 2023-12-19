package com.play.model.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.play.core.MoneyRecordType;
import com.play.model.SysUser;

public class MnyMoneyBo {

	public final static int HANDLERTYPE_ARTIFICIAL = 1;
	public final static int HANDLERTYPE_NOTIFY = 2;

	public final static int bet_type_live = 1;// 真人
	public final static int bet_type_egame = 2;// 电子
	public final static int bet_type_chess = 3;// 棋牌
	public final static int bet_type_fishing = 4;// 捕鱼
	public final static int bet_type_esport = 5;// 电竞
	public final static int bet_type_sport = 6;// 体育
	public final static int bet_type_lot = 7;// 彩票
	public final static int bet_type_turnlate = 10;// 大转盘

	/**
	 * 金额
	 */
	private BigDecimal money;
	/**
	 * 手续费
	 */
	private BigDecimal feeMoney;
	private BigDecimal unusedMoney;
	/**
	 * 真实打码量
	 */
	private BigDecimal realBetNum;
	/**
	 * 帐变类型
	 */
	private MoneyRecordType moneyRecordType;

	private SysUser user;

	private String remark;// 前台会员可见的备注
	private String orderId;
	private Date bizDatetime;
	private String bgRemark;// 后台可见的备注
	private Long times;
	private Integer handleType;
	private Long stationId;
	private Integer betType;

	public BigDecimal getUnusedMoney() {
		return unusedMoney;
	}

	public BigDecimal getFeeMoney() {
		return feeMoney;
	}

	public void setFeeMoney(BigDecimal feeMoney) {
		this.feeMoney = feeMoney;
	}

	public void setUnusedMoney(BigDecimal unusedMoney) {
		this.unusedMoney = unusedMoney;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getRealBetNum() {
		return realBetNum;
	}

	public void setRealBetNum(BigDecimal realBetNum) {
		this.realBetNum = realBetNum;
	}

	public MoneyRecordType getMoneyRecordType() {
		return moneyRecordType;
	}

	public void setMoneyRecordType(MoneyRecordType moneyRecordType) {
		this.moneyRecordType = moneyRecordType;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getBizDatetime() {
		return bizDatetime;
	}

	public void setBizDatetime(Date bizDatetime) {
		this.bizDatetime = bizDatetime;
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	public String getBgRemark() {
		return bgRemark;
	}

	public void setBgRemark(String bgRemark) {
		this.bgRemark = bgRemark;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Integer getHandleType() {
		return handleType;
	}

	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}

	public Integer getBetType() {
		return betType;
	}

	public void setBetType(Integer betType) {
		this.betType = betType;
	}

	public static String getBetTypeName(Integer betType) {
		switch (betType) {
		case MnyMoneyBo.bet_type_live:
			return "真人";
		case MnyMoneyBo.bet_type_egame:
			return "电子";
		case MnyMoneyBo.bet_type_sport:
			return "体育";
		case MnyMoneyBo.bet_type_lot:
			return "彩票";
		case MnyMoneyBo.bet_type_chess:
			return "棋牌";
		case MnyMoneyBo.bet_type_esport:
			return "电竞";
		case MnyMoneyBo.bet_type_fishing:
			return "捕鱼";
		default:
			return "";
		}
	}
}
