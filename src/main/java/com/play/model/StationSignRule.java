package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员签到赠送积分、金额规则表
 *
 * @author admin
 *
 */
@Table(name = "station_sign_rule")
public class StationSignRule {
	public static final int TODAY_DEPOSIT_NO = 1; // 无限制
	public static final int TODAY_DEPOSIT_YES = 2;// 当天需要充值
	public static final int TODAY_DEPOSIT_NOT_LIMIT = 3;// 不限天数,充值量达标即可

	public static final int TODAY_BET_NO = 1; // 无限制
	public static final int TODAY_BET_YES = 2;// 当天需要打码
	public static final int TODAY_BET_NOT_LIMIT = 3;// 不限天数,打码量达标即可
	public static final int TODAY_BET_YES_BET_MULTIPLE = 4; // 需要当天打码且打码是所需充值倍数

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 连续签到天数
	 */
	@Column(name = "days")
	private Integer days;

	@Column(name = "score")
	private Long score;
	/**
	 * 当天有充值的会员才能签到,2=需要当天有充值，1=不限制
	 */
	@Column(name = "today_deposit")
	private Integer todayDeposit;
	/**
	 * 限制会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 限制会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	/**
	 * 签到天数到达后是否重置签到 1否2是
	 */
	@Column(name = "is_reset")
	private Integer isReset;
	/**
	 * 需充值金额
	 */
	@Column(name = "deposit_money")
	private BigDecimal depositMoney;
	/**
	 * 所需打码
	 */
	@Column(name = "bet_need")
	private BigDecimal betNeed;
	/**
	 * 赠送金额
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 当天有打码才能签到，2=当天需要打码，1不限制
	 */
	@Column(name = "today_bet")
	private Integer todayBet;
	/**
	 * 彩金所需打码倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

	/**
	 * 签到每日赠送彩金配置 [{"score":"12","cash":"12","betRate":"1","day":1},{"score":"31","cash":"22","betRate":"2","day":2}]
	 */
	@Column(name = "day_gift_config")
	private String dayGiftConfig;

	private String groupNames;
	private String degreeNames;

	/**
	 * 签到天数
	 */
	private Integer signCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Integer getTodayDeposit() {
		return todayDeposit;
	}

	public void setTodayDeposit(Integer todayDeposit) {
		this.todayDeposit = todayDeposit;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Integer getIsReset() {
		return isReset;
	}

	public void setIsReset(Integer isReset) {
		this.isReset = isReset;
	}

	public BigDecimal getDepositMoney() {
		return depositMoney;
	}

	public void setDepositMoney(BigDecimal depositMoney) {
		this.depositMoney = depositMoney;
	}

	public BigDecimal getBetNeed() {
		return betNeed;
	}

	public void setBetNeed(BigDecimal betNeed) {
		this.betNeed = betNeed;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getTodayBet() {
		return todayBet;
	}

	public void setTodayBet(Integer todayBet) {
		this.todayBet = todayBet;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getDegreeNames() {
		return degreeNames;
	}

	public void setDegreeNames(String degreeNames) {
		this.degreeNames = degreeNames;
	}

	public Integer getSignCount() {
		return signCount;
	}

	public void setSignCount(Integer signCount) {
		this.signCount = signCount;
	}

	public String getDayGiftConfig() {
		return dayGiftConfig;
	}

	public void setDayGiftConfig(String dayGiftConfig) {
		this.dayGiftConfig = dayGiftConfig;
	}
}
