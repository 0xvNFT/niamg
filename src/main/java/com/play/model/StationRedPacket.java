package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_red_packet")
public class StationRedPacket {

	public static final int TODAY_DEPOSIT_NORMAL = 1;// 不限制
	public static final int TODAY_DEPOSIT_YES = 2;// 需要当天有充值
	public static final int TODAY_DEPOSIT_ACTIVE = 3;// 红包活动期间
	public static final int TODAY_DEPOSIT_SINGLE = 4;// 单笔充值
	public static final int TODAY_DEPOSIT_YESTODAY = 5;// 昨日充值
	public static final int TODAY_DEPOSIT_FRIST = 6;// 每日首充
	public static final int TODAY_DEPOSIT_HISTORY = 7; // 历史有充值(非当天)
	public static final int HISTORY_DEPOSIT_SINGLE = 8; // 活动期间内单笔充值(非当天)

	public static final int SELECT_MUTIL_DEPOSIT = 2;// 每个区间都能领取
	public static final int SELECT_MUTIL_DEPOSIT_ONLY_ONE = 1;// 只能在一个区间领取

	public static final int MANUAL_DEPOSIT_NORMAL = 1;// 手工充值无效
	public static final int MANUAL_DEPOSIT_YES = 2;// 手工充值有效

	public static final int REDNUM_TYPE_ONE = 1;// 单个会员领取红包次数统计方案1
	public static final int REDNUM_TYPE_TWO = 2;// 单个会员领取红包次数统计方案2

	public static final int RED_BAG_TYPE_1 = 1;// 按策略发红包
	public static final int RED_BAG_TYPE_2 = 2;// 按会员等级发红包
	public static final int RED_BAG_TYPE_3 = 3;// 按充值量发红包

	public static final int RED_BAG_TYPE_4 = 4;// 按邀请下级充值人数发红包(裂变红包)

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 总人数
	 */
	@Column(name = "total_number")
	private Integer totalNumber;
	/**
	 * 总金额
	 */
	@Column(name = "total_money")
	private BigDecimal totalMoney;
	/**
	 * 最小红包金额
	 */
	@Column(name = "min_money")
	private BigDecimal minMoney;
	/**
	 * 剩余金额
	 */
	@Column(name = "remain_money")
	private BigDecimal remainMoney;
	/**
	 * 剩余次数
	 */
	@Column(name = "remain_number")
	private Integer remainNumber;

	@Column(name = "begin_datetime")
	private Date beginDatetime;

	@Column(name = "end_datetime")
	private Date endDatetime;
	/**
	 * 红包标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 启用状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 同一IP可以抢多少次
	 */
	@Column(name = "ip_number")
	private Integer ipNumber;
	/**
	 * 打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 抢一次红包所需充值金额
	 */
	@Column(name = "money_base")
	private BigDecimal moneyBase;
	/**
	 * 当天有充值的会员才能抢红包,2=需要当天有充值，1=不限制
	 */
	@Column(name = "today_deposit")
	private Integer todayDeposit;
	/**
	 * 手工充值是否纳入充值金额,2=有效，1=无效
	 */
	@Column(name = "manual_deposit")
	private Integer manualDeposit;
	/**
	 * 单个会员可领取最大金额
	 */
	@Column(name = "max_money")
	private BigDecimal maxMoney;
	/**
	 * 红包个数等级限制,2=有效，1=无效
	 */
	@Column(name = "packet_strict")
	private Integer packetStrict;
	/**
	 * 单个会员领取红包次数统计方案,2=方案二，1=方案一
	 */
	@Column(name = "rednum_type")
	private Integer rednumType;
	/**
	 * 红包间隔金额设置
	 */
	@Column(name = "money_custom")
	private String moneyCustom;
	/**
	 * 1或者为空按方案配置;2按层级配置;3 按充值量配置
	 */
	@Column(name = "red_bag_type")
	private Integer redBagType;
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
	 * 1只能在一个区间领取2每个区间都能领取
	 */
	@Column(name = "select_mutil_deposit")
	private Integer selectMutilDeposit;
	/**
	 * 历史累计充值开始时间
	 */
	@Column(name = "his_begin")
	private Date hisBegin;
	/**
	 * 历史累计充值结束时间
	 */
	@Column(name = "his_end")
	private Date hisEnd;

	/**
	 * 下级充值人数
	 */
	@Column(name = "invite_depositer_num")
	private Integer inviteDepositerNum;

	private String degreeNames;

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

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getRemainMoney() {
		return remainMoney;
	}

	public void setRemainMoney(BigDecimal remainMoney) {
		this.remainMoney = remainMoney;
	}

	public Integer getRemainNumber() {
		return remainNumber;
	}

	public void setRemainNumber(Integer remainNumber) {
		this.remainNumber = remainNumber;
	}

	public Date getBeginDatetime() {
		return beginDatetime;
	}

	public void setBeginDatetime(Date beginDatetime) {
		this.beginDatetime = beginDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIpNumber() {
		return ipNumber;
	}

	public void setIpNumber(Integer ipNumber) {
		this.ipNumber = ipNumber;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getMoneyBase() {
		return moneyBase;
	}

	public void setMoneyBase(BigDecimal moneyBase) {
		this.moneyBase = moneyBase;
	}

	public Integer getTodayDeposit() {
		return todayDeposit;
	}

	public void setTodayDeposit(Integer todayDeposit) {
		this.todayDeposit = todayDeposit;
	}

	public Integer getManualDeposit() {
		return manualDeposit;
	}

	public void setManualDeposit(Integer manualDeposit) {
		this.manualDeposit = manualDeposit;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public Integer getPacketStrict() {
		return packetStrict;
	}

	public void setPacketStrict(Integer packetStrict) {
		this.packetStrict = packetStrict;
	}

	public Integer getRednumType() {
		return rednumType;
	}

	public void setRednumType(Integer rednumType) {
		this.rednumType = rednumType;
	}

	public String getMoneyCustom() {
		return moneyCustom;
	}

	public void setMoneyCustom(String moneyCustom) {
		this.moneyCustom = moneyCustom;
	}

	public Integer getRedBagType() {
		return redBagType;
	}

	public void setRedBagType(Integer redBagType) {
		this.redBagType = redBagType;
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

	public Integer getSelectMutilDeposit() {
		return selectMutilDeposit;
	}

	public void setSelectMutilDeposit(Integer selectMutilDeposit) {
		this.selectMutilDeposit = selectMutilDeposit;
	}

	public Date getHisBegin() {
		return hisBegin;
	}

	public void setHisBegin(Date hisBegin) {
		this.hisBegin = hisBegin;
	}

	public Date getHisEnd() {
		return hisEnd;
	}

	public void setHisEnd(Date hisEnd) {
		this.hisEnd = hisEnd;
	}

	public String getDegreeNames() {
		return degreeNames;
	}

	public void setDegreeNames(String degreeNames) {
		this.degreeNames = degreeNames;
	}

	public Integer getInviteDepositerNum() {
		return inviteDepositerNum;
	}

	public void setInviteDepositerNum(Integer inviteDepositerNum) {
		this.inviteDepositerNum = inviteDepositerNum;
	}
}
