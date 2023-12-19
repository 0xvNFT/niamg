package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点返点设置信息
 *
 * @author admin
 *
 */
@Table(name = "station_rebate")
public class StationRebate {
	public final static int USER_TYPE_PROXY = 1;// 代理返点
	public final static int USER_TYPE_MEMBER = 2;// 会员推荐返点

	public final static int TYPE_SAME = 1;// 所有层级返点一样
	public final static int TYPE_DECLINE = 2;// 逐级递减

	public final static int REBATE_MODE_TIMELY = 1;// 实时自动返点
	public final static int REBATE_MODE_AUTO = 2;// 第二天自动返点
	public final static int REBATE_MODE_HANDLE = 3;// 第二天手动返点

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 1=代理返点，2=会员推荐返点
	 */
	@Column(name = "user_type")
	private Integer userType;
	/**
	 * 代理返点类型，1=所有层级返点一样，2=逐级递减
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 返点方式，1=自动返点，2=第二天手动返点
	 */
	@Column(name = "rebate_mode")
	private Integer rebateMode;

	@Column(name = "live")
	private BigDecimal live;

	@Column(name = "egame")
	private BigDecimal egame;

	@Column(name = "chess")
	private BigDecimal chess;

	@Column(name = "esport")
	private BigDecimal esport;

	@Column(name = "sport")
	private BigDecimal sport;

	@Column(name = "fishing")
	private BigDecimal fishing;

	@Column(name = "lottery")
	private BigDecimal lottery;
	/**
	 * 返点层级差
	 */
	@Column(name = "level_diff")
	private BigDecimal levelDiff;

	/**
	 * 返点层级最大差值
	 */
	@Column(name = "max_diff")
	private BigDecimal maxDiff;
	/**
	 * 打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRebateMode() {
		return rebateMode;
	}

	public void setRebateMode(Integer rebateMode) {
		this.rebateMode = rebateMode;
	}

	public BigDecimal getLive() {
		return live;
	}

	public void setLive(BigDecimal live) {
		this.live = live;
	}

	public BigDecimal getEgame() {
		return egame;
	}

	public void setEgame(BigDecimal egame) {
		this.egame = egame;
	}

	public BigDecimal getChess() {
		return chess;
	}

	public void setChess(BigDecimal chess) {
		this.chess = chess;
	}

	public BigDecimal getEsport() {
		return esport;
	}

	public void setEsport(BigDecimal esport) {
		this.esport = esport;
	}

	public BigDecimal getSport() {
		return sport;
	}

	public void setSport(BigDecimal sport) {
		this.sport = sport;
	}

	public BigDecimal getFishing() {
		return fishing;
	}

	public void setFishing(BigDecimal fishing) {
		this.fishing = fishing;
	}

	public BigDecimal getLottery() {
		return lottery;
	}

	public void setLottery(BigDecimal lottery) {
		this.lottery = lottery;
	}

	public BigDecimal getLevelDiff() {
		return levelDiff;
	}

	public void setLevelDiff(BigDecimal levelDiff) {
		this.levelDiff = levelDiff;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getMaxDiff() {
		return maxDiff;
	}

	public void setMaxDiff(BigDecimal maxDiff) {
		this.maxDiff = maxDiff;
	}

}
