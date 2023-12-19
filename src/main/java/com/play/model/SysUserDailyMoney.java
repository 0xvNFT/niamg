package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.InsertValue;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "sys_user_daily_money")
public class SysUserDailyMoney {

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商id
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 代理商账号
	 */
	@Column(name = "agent_name")
	private String agentName;
	/**
	 * 上级代理
	 */
	@Column(name = "proxy_id")
	private Long proxyId;
	/**
	 * 上级代理账号
	 */
	@Column(name = "proxy_name")
	private String proxyName;
	/**
	 * 多级代理层级id，使用逗号分开
	 */
	@Column(name = "parent_ids")
	private String parentIds;
	/**
	 * 推荐者id
	 */
	@Column(name = "recommend_id")
	private Long recommendId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 用户类型
	 */
	@Column(name = "user_type")
	private Integer userType;
	/**
	 * 日期
	 */
	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "stat_date")
	private Date statDate;
	/**
	 * 取款金额
	 */
	@InsertValue(value = "0")
	@Column(name = "withdraw_amount")
	private BigDecimal withdrawAmount;
	/**
	 * 取款次数
	 */
	@InsertValue(value = "0")
	@Column(name = "withdraw_times")
	private Long withdrawTimes;
	/**
	 * 手动扣款金额
	 */
	@InsertValue(value = "0")
	@Column(name = "withdraw_artificial")
	private BigDecimal withdrawArtificial;
	/**
	 * 存款金额
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_amount")
	private BigDecimal depositAmount;
	/**
	 * 存款次数
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_times")
	private Long depositTimes;
	/**
	 * 手动加款金额
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_artificial")
	private BigDecimal depositArtificial;
	/**
	 * 存款赠送金额
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_gift_amount")
	private BigDecimal depositGiftAmount;
	/**
	 * 存款赠送次数
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_gift_times")
	private Long depositGiftTimes;
	/**
	 * 手动处理存款数
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_handler_artificial")
	private BigDecimal depositHandlerArtificial;
	/**
	 * 手动处理存款次数
	 */
	@InsertValue(value = "0")
	@Column(name = "deposit_handler_artificial_times")
	private Long depositHandlerArtificialTimes;
	/**
	 * 真人投注
	 */
	@InsertValue(value = "0")
	@Column(name = "live_bet_amount")
	private BigDecimal liveBetAmount;
	/**
	 * 真人实际打码
	 */
	@InsertValue(value = "0")
	@Column(name = "live_bet_num")
	private BigDecimal liveBetNum;
	/**
	 * 真人中奖金额
	 */
	@InsertValue(value = "0")
	@Column(name = "live_win_amount")
	private BigDecimal liveWinAmount;
	/**
	 * 真人反水
	 */
	@InsertValue(value = "0")
	@Column(name = "live_rebate_amount")
	private BigDecimal liveRebateAmount;
	/**
	 * 真人投注次数
	 */
	@InsertValue(value = "0")
	@Column(name = "live_bet_times")
	private Long liveBetTimes;
	/**
	 * 真人中奖次数
	 */
	@InsertValue(value = "0")
	@Column(name = "live_win_times")
	private Long liveWinTimes;
	@InsertValue(value = "0")
	@Column(name = "egame_bet_amount")
	private BigDecimal egameBetAmount;
	@InsertValue(value = "0")
	@Column(name = "egame_bet_num")
	private BigDecimal egameBetNum;
	@InsertValue(value = "0")
	@Column(name = "egame_win_amount")
	private BigDecimal egameWinAmount;
	@InsertValue(value = "0")
	@Column(name = "egame_rebate_amount")
	private BigDecimal egameRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "egame_bet_times")
	private Long egameBetTimes;
	@InsertValue(value = "0")
	@Column(name = "egame_win_times")
	private Long egameWinTimes;
	@InsertValue(value = "0")
	@Column(name = "sport_bet_amount")
	private BigDecimal sportBetAmount;
	@InsertValue(value = "0")
	@Column(name = "sport_bet_num")
	private BigDecimal sportBetNum;
	@InsertValue(value = "0")
	@Column(name = "sport_win_amount")
	private BigDecimal sportWinAmount;
	@InsertValue(value = "0")
	@Column(name = "sport_rebate_amount")
	private BigDecimal sportRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "sport_bet_times")
	private Long sportBetTimes;
	@InsertValue(value = "0")
	@Column(name = "sport_win_times")
	private Long sportWinTimes;
	@InsertValue(value = "0")
	@Column(name = "chess_bet_amount")
	private BigDecimal chessBetAmount;
	@InsertValue(value = "0")
	@Column(name = "chess_bet_num")
	private BigDecimal chessBetNum;
	@InsertValue(value = "0")
	@Column(name = "chess_win_amount")
	private BigDecimal chessWinAmount;
	@InsertValue(value = "0")
	@Column(name = "chess_rebate_amount")
	private BigDecimal chessRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "chess_bet_times")
	private Long chessBetTimes;
	@InsertValue(value = "0")
	@Column(name = "chess_win_times")
	private Long chessWinTimes;
	@InsertValue(value = "0")
	@Column(name = "esport_bet_amount")
	private BigDecimal esportBetAmount;
	@InsertValue(value = "0")
	@Column(name = "esport_bet_num")
	private BigDecimal esportBetNum;
	@InsertValue(value = "0")
	@Column(name = "esport_win_amount")
	private BigDecimal esportWinAmount;
	@InsertValue(value = "0")
	@Column(name = "esport_rebate_amount")
	private BigDecimal esportRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "esport_bet_times")
	private Long esportBetTimes;
	@InsertValue(value = "0")
	@Column(name = "esport_win_times")
	private Long esportWinTimes;
	@InsertValue(value = "0")
	@Column(name = "fishing_bet_amount")
	private BigDecimal fishingBetAmount;
	@InsertValue(value = "0")
	@Column(name = "fishing_bet_num")
	private BigDecimal fishingBetNum;
	@InsertValue(value = "0")
	@Column(name = "fishing_win_amount")
	private BigDecimal fishingWinAmount;
	@InsertValue(value = "0")
	@Column(name = "fishing_rebate_amount")
	private BigDecimal fishingRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "fishing_bet_times")
	private Long fishingBetTimes;
	@InsertValue(value = "0")
	@Column(name = "fishing_win_times")
	private Long fishingWinTimes;
	@InsertValue(value = "0")
	@Column(name = "lot_bet_amount")
	private BigDecimal lotBetAmount;
	@InsertValue(value = "0")
	@Column(name = "lot_bet_num")
	private BigDecimal lotBetNum;
	@InsertValue(value = "0")
	@Column(name = "lot_win_amount")
	private BigDecimal lotWinAmount;
	@InsertValue(value = "0")
	@Column(name = "lot_rebate_amount")
	private BigDecimal lotRebateAmount;
	@InsertValue(value = "0")
	@Column(name = "lot_bet_times")
	private Long lotBetTimes;
	@InsertValue(value = "0")
	@Column(name = "lot_win_times")
	private Long lotWinTimes;
	/**
	 * 代理返点
	 */
	@InsertValue(value = "0")
	@Column(name = "proxy_rebate_amount")
	private BigDecimal proxyRebateAmount;
	/**
	 * 活动中奖金额
	 */
	@InsertValue(value = "0")
	@Column(name = "active_award_amount")
	private BigDecimal activeAwardAmount;
	/**
	 * 活动中奖次数
	 */
	@InsertValue(value = "0")
	@Column(name = "active_award_times")
	private Long activeAwardTimes;
	/**
	 * 积分兑换金额
	 */
	@InsertValue(value = "0")
	@Column(name = "score_to_money")
	private BigDecimal scoreToMoney;
	@InsertValue(value = "0")
	@Column(name = "score_to_money_times")
	private Long scoreToMoneyTimes;
	/**
	 * 金额兑换积分
	 */
	@InsertValue(value = "0")
	@Column(name = "money_to_score")
	private BigDecimal moneyToScore;
	@InsertValue(value = "0")
	@Column(name = "money_to_score_times")
	private Long moneyToScoreTimes;
	/**
	 * 真人电子jackpot
	 */
	@InsertValue(value = "0")
	@Column(name = "jackpot")
	private BigDecimal jackpot;
	/**
	 * 真人电子小费
	 */
	@InsertValue(value = "0")
	@Column(name = "third_tip")
	private BigDecimal thirdTip;
	/**
	 * 真人电子其他金额
	 */
	@InsertValue(value = "0")
	@Column(name = "third_other_money")
	private BigDecimal thirdOtherMoney;
	/**
	 * 其他
	 */
	@InsertValue(value = "0")
	@Column(name = "gift_other_amount")
	private BigDecimal giftOtherAmount;
	/**
	 * 充值卡金额
	 */
	@InsertValue(value = "0")
	@Column(name = "recharge_card_amount")
	private BigDecimal rechargeCardAmount;
	/**
	 * 充值卡次数
	 */
	@InsertValue(value = "0")
	@Column(name = "recharge_card_times")
	private Long rechargeCardTimes;
	/**
	 * 代金券金额
	 */
	@InsertValue(value = "0")
	@Column(name = "coupons_amount")
	private BigDecimal couponsAmount;
	/**
	 * 代金券次数
	 */
	@InsertValue(value = "0")
	@Column(name = "coupons_times")
	private Long couponsTimes;
	/**
	 * 彩金扣款
	 */
	@InsertValue(value = "0")
	@Column(name = "sub_gift_amount")
	private BigDecimal subGiftAmount;
	/**
	 * 红包中奖金额
	 */
	@InsertValue(value = "0")
	@Column(name = "red_active_award_amount")
	private BigDecimal redActiveAwardAmount;
	/**
	 * 红包中奖次数
	 */
	@InsertValue(value = "0")
	@Column(name = "red_active_award_times")
	private Long redActiveAwardTimes;

	private BigDecimal profitAndLoss;// 租户盈亏，会员为取反
	private BigDecimal money;// 余额

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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Long getProxyId() {
		return proxyId;
	}

	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	public BigDecimal getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(BigDecimal withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public Long getWithdrawTimes() {
		return withdrawTimes;
	}

	public void setWithdrawTimes(Long withdrawTimes) {
		this.withdrawTimes = withdrawTimes;
	}

	public BigDecimal getWithdrawArtificial() {
		return withdrawArtificial;
	}

	public void setWithdrawArtificial(BigDecimal withdrawArtificial) {
		this.withdrawArtificial = withdrawArtificial;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Long getDepositTimes() {
		return depositTimes;
	}

	public void setDepositTimes(Long depositTimes) {
		this.depositTimes = depositTimes;
	}

	public BigDecimal getDepositArtificial() {
		return depositArtificial;
	}

	public void setDepositArtificial(BigDecimal depositArtificial) {
		this.depositArtificial = depositArtificial;
	}

	public BigDecimal getDepositGiftAmount() {
		return depositGiftAmount;
	}

	public void setDepositGiftAmount(BigDecimal depositGiftAmount) {
		this.depositGiftAmount = depositGiftAmount;
	}

	public Long getDepositGiftTimes() {
		return depositGiftTimes;
	}

	public void setDepositGiftTimes(Long depositGiftTimes) {
		this.depositGiftTimes = depositGiftTimes;
	}

	public BigDecimal getDepositHandlerArtificial() {
		return depositHandlerArtificial;
	}

	public void setDepositHandlerArtificial(BigDecimal depositHandlerArtificial) {
		this.depositHandlerArtificial = depositHandlerArtificial;
	}

	public Long getDepositHandlerArtificialTimes() {
		return depositHandlerArtificialTimes;
	}

	public void setDepositHandlerArtificialTimes(Long depositHandlerArtificialTimes) {
		this.depositHandlerArtificialTimes = depositHandlerArtificialTimes;
	}

	public BigDecimal getLiveBetAmount() {
		return liveBetAmount;
	}

	public void setLiveBetAmount(BigDecimal liveBetAmount) {
		this.liveBetAmount = liveBetAmount;
	}

	public BigDecimal getLiveWinAmount() {
		return liveWinAmount;
	}

	public void setLiveWinAmount(BigDecimal liveWinAmount) {
		this.liveWinAmount = liveWinAmount;
	}

	public BigDecimal getLiveRebateAmount() {
		return liveRebateAmount;
	}

	public void setLiveRebateAmount(BigDecimal liveRebateAmount) {
		this.liveRebateAmount = liveRebateAmount;
	}

	public Long getLiveBetTimes() {
		return liveBetTimes;
	}

	public void setLiveBetTimes(Long liveBetTimes) {
		this.liveBetTimes = liveBetTimes;
	}

	public Long getLiveWinTimes() {
		return liveWinTimes;
	}

	public void setLiveWinTimes(Long liveWinTimes) {
		this.liveWinTimes = liveWinTimes;
	}

	public BigDecimal getEgameBetAmount() {
		return egameBetAmount;
	}

	public void setEgameBetAmount(BigDecimal egameBetAmount) {
		this.egameBetAmount = egameBetAmount;
	}

	public BigDecimal getEgameWinAmount() {
		return egameWinAmount;
	}

	public void setEgameWinAmount(BigDecimal egameWinAmount) {
		this.egameWinAmount = egameWinAmount;
	}

	public BigDecimal getEgameRebateAmount() {
		return egameRebateAmount;
	}

	public void setEgameRebateAmount(BigDecimal egameRebateAmount) {
		this.egameRebateAmount = egameRebateAmount;
	}

	public Long getEgameBetTimes() {
		return egameBetTimes;
	}

	public void setEgameBetTimes(Long egameBetTimes) {
		this.egameBetTimes = egameBetTimes;
	}

	public Long getEgameWinTimes() {
		return egameWinTimes;
	}

	public void setEgameWinTimes(Long egameWinTimes) {
		this.egameWinTimes = egameWinTimes;
	}

	public BigDecimal getSportBetAmount() {
		return sportBetAmount;
	}

	public void setSportBetAmount(BigDecimal sportBetAmount) {
		this.sportBetAmount = sportBetAmount;
	}

	public BigDecimal getSportWinAmount() {
		return sportWinAmount;
	}

	public void setSportWinAmount(BigDecimal sportWinAmount) {
		this.sportWinAmount = sportWinAmount;
	}

	public BigDecimal getSportRebateAmount() {
		return sportRebateAmount;
	}

	public void setSportRebateAmount(BigDecimal sportRebateAmount) {
		this.sportRebateAmount = sportRebateAmount;
	}

	public Long getSportBetTimes() {
		return sportBetTimes;
	}

	public void setSportBetTimes(Long sportBetTimes) {
		this.sportBetTimes = sportBetTimes;
	}

	public Long getSportWinTimes() {
		return sportWinTimes;
	}

	public void setSportWinTimes(Long sportWinTimes) {
		this.sportWinTimes = sportWinTimes;
	}

	public BigDecimal getChessBetAmount() {
		return chessBetAmount;
	}

	public void setChessBetAmount(BigDecimal chessBetAmount) {
		this.chessBetAmount = chessBetAmount;
	}

	public BigDecimal getChessWinAmount() {
		return chessWinAmount;
	}

	public void setChessWinAmount(BigDecimal chessWinAmount) {
		this.chessWinAmount = chessWinAmount;
	}

	public BigDecimal getChessRebateAmount() {
		return chessRebateAmount;
	}

	public void setChessRebateAmount(BigDecimal chessRebateAmount) {
		this.chessRebateAmount = chessRebateAmount;
	}

	public Long getChessBetTimes() {
		return chessBetTimes;
	}

	public void setChessBetTimes(Long chessBetTimes) {
		this.chessBetTimes = chessBetTimes;
	}

	public Long getChessWinTimes() {
		return chessWinTimes;
	}

	public void setChessWinTimes(Long chessWinTimes) {
		this.chessWinTimes = chessWinTimes;
	}

	public BigDecimal getEsportBetAmount() {
		return esportBetAmount;
	}

	public void setEsportBetAmount(BigDecimal esportBetAmount) {
		this.esportBetAmount = esportBetAmount;
	}

	public BigDecimal getEsportWinAmount() {
		return esportWinAmount;
	}

	public void setEsportWinAmount(BigDecimal esportWinAmount) {
		this.esportWinAmount = esportWinAmount;
	}

	public BigDecimal getEsportRebateAmount() {
		return esportRebateAmount;
	}

	public void setEsportRebateAmount(BigDecimal esportRebateAmount) {
		this.esportRebateAmount = esportRebateAmount;
	}

	public Long getEsportBetTimes() {
		return esportBetTimes;
	}

	public void setEsportBetTimes(Long esportBetTimes) {
		this.esportBetTimes = esportBetTimes;
	}

	public Long getEsportWinTimes() {
		return esportWinTimes;
	}

	public void setEsportWinTimes(Long esportWinTimes) {
		this.esportWinTimes = esportWinTimes;
	}

	public BigDecimal getFishingBetAmount() {
		return fishingBetAmount;
	}

	public void setFishingBetAmount(BigDecimal fishingBetAmount) {
		this.fishingBetAmount = fishingBetAmount;
	}

	public BigDecimal getFishingWinAmount() {
		return fishingWinAmount;
	}

	public void setFishingWinAmount(BigDecimal fishingWinAmount) {
		this.fishingWinAmount = fishingWinAmount;
	}

	public BigDecimal getFishingRebateAmount() {
		return fishingRebateAmount;
	}

	public void setFishingRebateAmount(BigDecimal fishingRebateAmount) {
		this.fishingRebateAmount = fishingRebateAmount;
	}

	public Long getFishingBetTimes() {
		return fishingBetTimes;
	}

	public void setFishingBetTimes(Long fishingBetTimes) {
		this.fishingBetTimes = fishingBetTimes;
	}

	public Long getFishingWinTimes() {
		return fishingWinTimes;
	}

	public void setFishingWinTimes(Long fishingWinTimes) {
		this.fishingWinTimes = fishingWinTimes;
	}

	public BigDecimal getLotBetAmount() {
		return lotBetAmount;
	}

	public void setLotBetAmount(BigDecimal lotBetAmount) {
		this.lotBetAmount = lotBetAmount;
	}

	public BigDecimal getLiveBetNum() {
		return liveBetNum;
	}

	public void setLiveBetNum(BigDecimal liveBetNum) {
		this.liveBetNum = liveBetNum;
	}

	public BigDecimal getEgameBetNum() {
		return egameBetNum;
	}

	public void setEgameBetNum(BigDecimal egameBetNum) {
		this.egameBetNum = egameBetNum;
	}

	public BigDecimal getSportBetNum() {
		return sportBetNum;
	}

	public void setSportBetNum(BigDecimal sportBetNum) {
		this.sportBetNum = sportBetNum;
	}

	public BigDecimal getChessBetNum() {
		return chessBetNum;
	}

	public void setChessBetNum(BigDecimal chessBetNum) {
		this.chessBetNum = chessBetNum;
	}

	public BigDecimal getEsportBetNum() {
		return esportBetNum;
	}

	public void setEsportBetNum(BigDecimal esportBetNum) {
		this.esportBetNum = esportBetNum;
	}

	public BigDecimal getFishingBetNum() {
		return fishingBetNum;
	}

	public void setFishingBetNum(BigDecimal fishingBetNum) {
		this.fishingBetNum = fishingBetNum;
	}

	public BigDecimal getLotBetNum() {
		return lotBetNum;
	}

	public void setLotBetNum(BigDecimal lotBetNum) {
		this.lotBetNum = lotBetNum;
	}

	public BigDecimal getLotWinAmount() {
		return lotWinAmount;
	}

	public void setLotWinAmount(BigDecimal lotWinAmount) {
		this.lotWinAmount = lotWinAmount;
	}

	public BigDecimal getLotRebateAmount() {
		return lotRebateAmount;
	}

	public void setLotRebateAmount(BigDecimal lotRebateAmount) {
		this.lotRebateAmount = lotRebateAmount;
	}

	public Long getLotBetTimes() {
		return lotBetTimes;
	}

	public void setLotBetTimes(Long lotBetTimes) {
		this.lotBetTimes = lotBetTimes;
	}

	public Long getLotWinTimes() {
		return lotWinTimes;
	}

	public void setLotWinTimes(Long lotWinTimes) {
		this.lotWinTimes = lotWinTimes;
	}

	public BigDecimal getProxyRebateAmount() {
		return proxyRebateAmount;
	}

	public void setProxyRebateAmount(BigDecimal proxyRebateAmount) {
		this.proxyRebateAmount = proxyRebateAmount;
	}

	public BigDecimal getActiveAwardAmount() {
		return activeAwardAmount;
	}

	public void setActiveAwardAmount(BigDecimal activeAwardAmount) {
		this.activeAwardAmount = activeAwardAmount;
	}

	public Long getActiveAwardTimes() {
		return activeAwardTimes;
	}

	public void setActiveAwardTimes(Long activeAwardTimes) {
		this.activeAwardTimes = activeAwardTimes;
	}

	public BigDecimal getScoreToMoney() {
		return scoreToMoney;
	}

	public void setScoreToMoney(BigDecimal scoreToMoney) {
		this.scoreToMoney = scoreToMoney;
	}

	public Long getScoreToMoneyTimes() {
		return scoreToMoneyTimes;
	}

	public void setScoreToMoneyTimes(Long scoreToMoneyTimes) {
		this.scoreToMoneyTimes = scoreToMoneyTimes;
	}

	public BigDecimal getMoneyToScore() {
		return moneyToScore;
	}

	public void setMoneyToScore(BigDecimal moneyToScore) {
		this.moneyToScore = moneyToScore;
	}

	public Long getMoneyToScoreTimes() {
		return moneyToScoreTimes;
	}

	public void setMoneyToScoreTimes(Long moneyToScoreTimes) {
		this.moneyToScoreTimes = moneyToScoreTimes;
	}

	public BigDecimal getJackpot() {
		return jackpot;
	}

	public void setJackpot(BigDecimal jackpot) {
		this.jackpot = jackpot;
	}

	public BigDecimal getThirdTip() {
		return thirdTip;
	}

	public void setThirdTip(BigDecimal thirdTip) {
		this.thirdTip = thirdTip;
	}

	public BigDecimal getThirdOtherMoney() {
		return thirdOtherMoney;
	}

	public void setThirdOtherMoney(BigDecimal thirdOtherMoney) {
		this.thirdOtherMoney = thirdOtherMoney;
	}

	public BigDecimal getGiftOtherAmount() {
		return giftOtherAmount;
	}

	public void setGiftOtherAmount(BigDecimal giftOtherAmount) {
		this.giftOtherAmount = giftOtherAmount;
	}

	public BigDecimal getRechargeCardAmount() {
		return rechargeCardAmount;
	}

	public void setRechargeCardAmount(BigDecimal rechargeCardAmount) {
		this.rechargeCardAmount = rechargeCardAmount;
	}

	public Long getRechargeCardTimes() {
		return rechargeCardTimes;
	}

	public void setRechargeCardTimes(Long rechargeCardTimes) {
		this.rechargeCardTimes = rechargeCardTimes;
	}

	public BigDecimal getCouponsAmount() {
		return couponsAmount;
	}

	public void setCouponsAmount(BigDecimal couponsAmount) {
		this.couponsAmount = couponsAmount;
	}

	public Long getCouponsTimes() {
		return couponsTimes;
	}

	public void setCouponsTimes(Long couponsTimes) {
		this.couponsTimes = couponsTimes;
	}

	public BigDecimal getSubGiftAmount() {
		return subGiftAmount;
	}

	public void setSubGiftAmount(BigDecimal subGiftAmount) {
		this.subGiftAmount = subGiftAmount;
	}

	public BigDecimal getRedActiveAwardAmount() {
		return redActiveAwardAmount;
	}

	public void setRedActiveAwardAmount(BigDecimal redActiveAwardAmount) {
		this.redActiveAwardAmount = redActiveAwardAmount;
	}

	public Long getRedActiveAwardTimes() {
		return redActiveAwardTimes;
	}

	public void setRedActiveAwardTimes(Long redActiveAwardTimes) {
		this.redActiveAwardTimes = redActiveAwardTimes;
	}

	public BigDecimal getProfitAndLoss() {
		return profitAndLoss;
	}

	public void setProfitAndLoss(BigDecimal profitAndLoss) {
		this.profitAndLoss = profitAndLoss;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
