package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "proxy_daily_bet_stat")
public class ProxyDailyBetStat {
	public static final int status_unback = 1;// 未返点
	public static final int status_do = 2;// 已返

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 代理账号id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 代理账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 日期
	 */
	@Column(name = "stat_date")
	private Date statDate;
	/**
	 * 1=未返，2=已返
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 彩票下注金额
	 */
	@Column(name = "lot_bet")
	private BigDecimal lotBet;
	/**
	 * 彩票返点
	 */
	@Column(name = "lot_rollback")
	private BigDecimal lotRollback;
	/**
	 * 真人下注金额
	 */
	@Column(name = "live_bet")
	private BigDecimal liveBet;
	/**
	 * 真人返点
	 */
	@Column(name = "live_rollback")
	private BigDecimal liveRollback;
	/**
	 * 电子下注金额
	 */
	@Column(name = "egame_bet")
	private BigDecimal egameBet;
	/**
	 * 电子返点
	 */
	@Column(name = "egame_rollback")
	private BigDecimal egameRollback;
	/**
	 * 棋牌下注金额
	 */
	@Column(name = "chess_bet")
	private BigDecimal chessBet;
	/**
	 * 棋牌返点
	 */
	@Column(name = "chess_rollback")
	private BigDecimal chessRollback;
	/**
	 * 体育下注金额
	 */
	@Column(name = "sport_bet")
	private BigDecimal sportBet;
	/**
	 * 体育返点
	 */
	@Column(name = "sport_rollback")
	private BigDecimal sportRollback;
	/**
	 * 电竞下注金额
	 */
	@Column(name = "esport_bet")
	private BigDecimal esportBet;
	/**
	 * 电竞返点
	 */
	@Column(name = "esport_rollback")
	private BigDecimal esportRollback;
	/**
	 * 捕鱼下注金额
	 */
	@Column(name = "fishing_bet")
	private BigDecimal fishingBet;
	/**
	 * 捕鱼返点
	 */
	@Column(name = "fishing_rollback")
	private BigDecimal fishingRollback;
	/**
	 * 返点出款所需打码
	 */
	@Column(name = "draw_num")
	private BigDecimal drawNum;
	/**
	 * 操作者id
	 */
	@Column(name = "operator_id")
	private Long operatorId;
	/**
	 * 操作者账号
	 */
	@Column(name = "operator")
	private String operator;
	/**
	 * 操作时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getLotBet() {
		return lotBet;
	}

	public void setLotBet(BigDecimal lotBet) {
		this.lotBet = lotBet;
	}

	public BigDecimal getLotRollback() {
		return lotRollback;
	}

	public void setLotRollback(BigDecimal lotRollback) {
		this.lotRollback = lotRollback;
	}

	public BigDecimal getLiveBet() {
		return liveBet;
	}

	public void setLiveBet(BigDecimal liveBet) {
		this.liveBet = liveBet;
	}

	public BigDecimal getLiveRollback() {
		return liveRollback;
	}

	public void setLiveRollback(BigDecimal liveRollback) {
		this.liveRollback = liveRollback;
	}

	public BigDecimal getEgameBet() {
		return egameBet;
	}

	public void setEgameBet(BigDecimal egameBet) {
		this.egameBet = egameBet;
	}

	public BigDecimal getEgameRollback() {
		return egameRollback;
	}

	public void setEgameRollback(BigDecimal egameRollback) {
		this.egameRollback = egameRollback;
	}

	public BigDecimal getChessBet() {
		return chessBet;
	}

	public void setChessBet(BigDecimal chessBet) {
		this.chessBet = chessBet;
	}

	public BigDecimal getChessRollback() {
		return chessRollback;
	}

	public void setChessRollback(BigDecimal chessRollback) {
		this.chessRollback = chessRollback;
	}

	public BigDecimal getSportBet() {
		return sportBet;
	}

	public void setSportBet(BigDecimal sportBet) {
		this.sportBet = sportBet;
	}

	public BigDecimal getSportRollback() {
		return sportRollback;
	}

	public void setSportRollback(BigDecimal sportRollback) {
		this.sportRollback = sportRollback;
	}

	public BigDecimal getEsportBet() {
		return esportBet;
	}

	public void setEsportBet(BigDecimal esportBet) {
		this.esportBet = esportBet;
	}

	public BigDecimal getEsportRollback() {
		return esportRollback;
	}

	public void setEsportRollback(BigDecimal esportRollback) {
		this.esportRollback = esportRollback;
	}

	public BigDecimal getFishingBet() {
		return fishingBet;
	}

	public void setFishingBet(BigDecimal fishingBet) {
		this.fishingBet = fishingBet;
	}

	public BigDecimal getFishingRollback() {
		return fishingRollback;
	}

	public void setFishingRollback(BigDecimal fishingRollback) {
		this.fishingRollback = fishingRollback;
	}

	public BigDecimal getDrawNum() {
		return drawNum;
	}

	public void setDrawNum(BigDecimal drawNum) {
		this.drawNum = drawNum;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
