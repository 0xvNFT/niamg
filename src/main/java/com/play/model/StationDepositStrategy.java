package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.common.utils.DateUtil;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 用户充值赠送策略 
 *
 * @author admin
 *
 */
@Table(name = "station_deposit_strategy")
public class StationDepositStrategy {
	// 支付类型，5=在线支付，6=快速入款，7=银行转账 ,1=手动加款
	final public static int TYPE_ARTIFICIAL = 1;
	final public static int TYPE_ONLINE = 5;
	final public static int TYPE_FAST = 6;
	final public static int TYPE_BANK = 7;
	// 赠送类型 1=固定数额 2=浮动比例
	final public static int GIFT_TYPE_FIXED = 1;
	final public static int GIFT_TYPE_PERCENT = 2;
	// 赠送类型 1=彩金 2=积分
	final public static int VALUE_TYPE_MONEY = 1;
	final public static int VALUE_TYPE_SCORE = 2;

	// 赠送频率
	final public static int deposit_count_0 = 0;// 每次充值
	final public static int deposit_count_1 = 1;// 首充
	final public static int deposit_count_2 = 2;// 第二次充值
	final public static int deposit_count_3 = 3;// 第三次充值
	final public static int deposit_count_222 = 222;// 前两次充值
	final public static int deposit_count_333 = 333;// 前三次充值
	final public static int deposit_count_444 = 444;// 每日首充
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 支付类型，5=在线支付，6=快速入款，7=银行转账，1=手动加款
	 */
	@Column(name = "deposit_type")
	private Integer depositType;
	/**
	 * 赠送方式 1=固定数额 2=浮动比例
	 */
	@Column(name = "gift_type")
	private Integer giftType;

	/**
	 * 返佣赠送方式 1=固定数额 2=浮动比例
	 */
	@Column(name = "back_gift_type")
	private Integer backGiftType;
	/**
	 * 赠送类型  1=彩金  2=积分
	 */
	@Column(name = "value_type")
	private Integer valueType;
	/**
	 * 0表示每次, 1表示首充, N表示第N次
	 */
	@Column(name = "deposit_count")
	private Integer depositCount;
	/**
	 * 赠送额度
	 */
	@Column(name = "gift_value")
	private BigDecimal giftValue;
	/**
	 * 打码量倍数。(充值金额+赠送)x流水倍数=出款需要达到的投注量
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 活动期间赠送上限
	 */
	@Column(name = "upper_limit")
	private BigDecimal upperLimit;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 开始时间
	 */
	@Column(name = "start_datetime")
	private Date startDatetime;
	/**
	 * 截止时间
	 */
	@Column(name = "end_datetime")
	private Date endDatetime;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 状态 1=禁用，2=启用
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 最小充值金额，=0代表不限制
	 */
	@Column(name = "min_money")
	private BigDecimal minMoney;
	/**
	 * 最大充值金额，=0代表不限制
	 */
	@Column(name = "max_money")
	private BigDecimal maxMoney;
	/**
	 * 单日赠送上限
	 */
	@Column(name = "dayupper_limit")
	private BigDecimal dayupperLimit;
	/**
	 * 站点入款方式Ids
	 */
	@Column(name = "deposit_config_ids")
	private String depositConfigIds;
	/**
	 * 有效会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 有效会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;

	/**
	 * 给推荐人/上级代理返佣的额度
	 */
	@Column(name = "bonus_back_value")
	private BigDecimal bonusBackValue;

	/**
	 * 上级返佣打码量倍数。(返佣额度)x流水倍数=出款需要达到的投注量
	 */
	@Column(name = "bonus_back_bet_rate")
	private BigDecimal bonusBackBetRate;

	/**
	 * 返佣层级差
	 */
	@Column(name = "back_bonus_level_diff")
	private BigDecimal backBonusLevelDiff;


	private String dateInterval;


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

	public Integer getDepositType() {
		return depositType;
	}

	public void setDepositType(Integer depositType) {
		this.depositType = depositType;
	}

	public Integer getGiftType() {
		return giftType;
	}

	public void setGiftType(Integer giftType) {
		this.giftType = giftType;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Integer getDepositCount() {
		return depositCount;
	}

	public void setDepositCount(Integer depositCount) {
		this.depositCount = depositCount;
	}

	public BigDecimal getGiftValue() {
		return giftValue;
	}

	public void setGiftValue(BigDecimal giftValue) {
		this.giftValue = giftValue;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public BigDecimal getDayupperLimit() {
		return dayupperLimit;
	}

	public void setDayupperLimit(BigDecimal dayupperLimit) {
		this.dayupperLimit = dayupperLimit;
	}

	public String getDepositConfigIds() {
		return depositConfigIds;
	}

	public void setDepositConfigIds(String depositConfigIds) {
		this.depositConfigIds = depositConfigIds;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public BigDecimal getBonusBackValue() {
		return bonusBackValue;
	}

	public void setBonusBackValue(BigDecimal bonusBackValue) {
		this.bonusBackValue = bonusBackValue;
	}

	public BigDecimal getBonusBackBetRate() {
		return bonusBackBetRate;
	}

	public void setBonusBackBetRate(BigDecimal bonusBackBetRate) {
		this.bonusBackBetRate = bonusBackBetRate;
	}

	public String getDesc() {
		StringBuilder sb = new StringBuilder(DateUtil.toDateStr(getStartDatetime()));
		sb.append("至").append(DateUtil.toDateStr(getEndDatetime()));
		switch (getDepositCount()) {
			case deposit_count_0:// 每次
				sb.append("每次充值");
				break;
			case deposit_count_1:// 首充
				sb.append("首充");
				break;
			case deposit_count_2:// 第二次充值
				sb.append("第二次充值");
				break;
			case deposit_count_3:// 第三次充值
				sb.append("第三次充值");
				break;
			case deposit_count_222:// 前两次充值
				sb.append("前两次充值");
				break;
			case deposit_count_333:// 前三次充值
				sb.append("前三次充值");
				break;
			case deposit_count_444://每日首充
				sb.append("每日首充");
				break;
			default:
				break;
		}
		sb.append("  大于等于").append(getMinMoney()).append("且  小于等于").append(getMaxMoney());
		sb.append(" 赠送");
		String vt = "积分";
		if (getValueType() == VALUE_TYPE_MONEY) {
			vt = "彩金";
		}
		if (getGiftType() == GIFT_TYPE_FIXED) {
			sb.append(vt).append(getGiftValue());
		} else {
			sb.append(getGiftValue()).append("%").append(vt);
		}
		return sb.toString();
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getDateInterval() {
		return dateInterval;
	}

	public void setDateInterval(String dateInterval) {
		this.dateInterval = dateInterval;
	}

	public Integer getBackGiftType() {
		return backGiftType;
	}

	public void setBackGiftType(Integer backGiftType) {
		this.backGiftType = backGiftType;
	}

	public BigDecimal getBackBonusLevelDiff() {
		return backBonusLevelDiff;
	}

	public void setBackBonusLevelDiff(BigDecimal backBonusLevelDiff) {
		this.backBonusLevelDiff = backBonusLevelDiff;
	}
}
