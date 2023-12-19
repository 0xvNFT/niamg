package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员反水记录表，按日投注和游戏类型来反水
 *
 * @author admin
 *
 */
@Table(name = "station_kickback_record")
public class StationKickbackRecord {
	public static final int STATUS_NOT_ROLL = 1;// 未反水
	public static final int STATUS_ROLL = 2;// 已经反水
	public static final int STATUS_BACK = 3;// 反水已经回滚

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 投注日期
	 */
	@Column(name = "bet_date")
	private Date betDate;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 投注类型(1真人,2电子游艺,3体育,4沙巴体育)
	 */
	@Column(name = "bet_type")
	private Integer betType;
	/**
	 * 投注金额
	 */
	@Column(name = "bet_money")
	private BigDecimal betMoney;
	/**
	 * 有效打码
	 */
	@Column(name = "real_bet_num")
	private BigDecimal realBetNum;
	/**
	 * 会员中奖金额
	 */
	@Column(name = "win_money")
	private BigDecimal winMoney;
	/**
	 * 反水金额
	 */
	@Column(name = "kickback_money")
	private BigDecimal kickbackMoney;
	/**
	 * 返水比率(1-100)
	 */
	@Column(name = "kickback_rate")
	private BigDecimal kickbackRate;
	/**
	 * 反水描述
	 */
	@Column(name = "kickback_desc")
	private String kickbackDesc;
	/**
	 * 反水需要的打码量
	 */
	@Column(name = "draw_need")
	private BigDecimal drawNeed;
	/**
	 * 会员返水状态 （1还未返水 2返水已到账 3返水已经回滚）
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 代理id
	 */
	@Column(name = "proxy_id")
	private Long proxyId;
	/**
	 * 代理账号
	 */
	@Column(name = "proxy_name")
	private String proxyName;

	@Column(name = "operator")
	private String operator;

	@Column(name = "operator_id")
	private Long operatorId;
	/**
	 * 会员等级id
	 */
	@Column(name = "degree_id")
	private Long degreeId;
	/**
	 * 会员等级名称
	 */
	@Column(name = "degree_name")
	private String degreeName;

	/**
	 * 会员组别ID
	 */
	@Column(name = "group_id")
	private Long groupId;

	private String realName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getBetDate() {
		return betDate;
	}

	public void setBetDate(Date betDate) {
		this.betDate = betDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getBetType() {
		return betType;
	}

	public void setBetType(Integer betType) {
		this.betType = betType;
	}

	public BigDecimal getBetMoney() {
		return betMoney;
	}

	public void setBetMoney(BigDecimal betMoney) {
		this.betMoney = betMoney;
	}

	public BigDecimal getRealBetNum() {
		return realBetNum;
	}

	public void setRealBetNum(BigDecimal realBetNum) {
		this.realBetNum = realBetNum;
	}

	public BigDecimal getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}

	public BigDecimal getKickbackMoney() {
		return kickbackMoney;
	}

	public void setKickbackMoney(BigDecimal kickbackMoney) {
		this.kickbackMoney = kickbackMoney;
	}

	public BigDecimal getKickbackRate() {
		return kickbackRate;
	}

	public void setKickbackRate(BigDecimal kickbackRate) {
		this.kickbackRate = kickbackRate;
	}

	public String getKickbackDesc() {
		return kickbackDesc;
	}

	public void setKickbackDesc(String kickbackDesc) {
		this.kickbackDesc = kickbackDesc;
	}

	public BigDecimal getDrawNeed() {
		return drawNeed;
	}

	public void setDrawNeed(BigDecimal drawNeed) {
		this.drawNeed = drawNeed;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProxyId() {
		return proxyId;
	}

	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
