package com.play.model.vo;

import java.math.BigDecimal;

public class RiskReportVo {
	private Long userId;
	private String username;
	private Integer userType;
	private String realName;
	private Long degreeId;
	private String degreeName;
	private BigDecimal withdrawAmount;
	private Integer withdrawTimes;
	private BigDecimal depositAmount;
	private Integer depositTimes;
	private BigDecimal depositArtificial;
	private BigDecimal depositAmountSum;
	private BigDecimal betAmount;
	private BigDecimal winAmount;
	private BigDecimal rebateAmount;
	private BigDecimal profit;
	private Integer betTimes;
	private Integer winTimes;

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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public BigDecimal getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(BigDecimal withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public Integer getWithdrawTimes() {
		return withdrawTimes;
	}

	public void setWithdrawTimes(Integer withdrawTimes) {
		this.withdrawTimes = withdrawTimes;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Integer getDepositTimes() {
		return depositTimes;
	}

	public void setDepositTimes(Integer depositTimes) {
		this.depositTimes = depositTimes;
	}

	public BigDecimal getDepositArtificial() {
		return depositArtificial;
	}

	public void setDepositArtificial(BigDecimal depositArtificial) {
		this.depositArtificial = depositArtificial;
	}

	public BigDecimal getDepositAmountSum() {
		return depositAmountSum;
	}

	public void setDepositAmountSum(BigDecimal depositAmountSum) {
		this.depositAmountSum = depositAmountSum;
	}

	public BigDecimal getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}

	public BigDecimal getWinAmount() {
		return winAmount;
	}

	public void setWinAmount(BigDecimal winAmount) {
		this.winAmount = winAmount;
	}

	public BigDecimal getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(BigDecimal rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public Integer getBetTimes() {
		return betTimes;
	}

	public void setBetTimes(Integer betTimes) {
		this.betTimes = betTimes;
	}

	public Integer getWinTimes() {
		return winTimes;
	}

	public void setWinTimes(Integer winTimes) {
		this.winTimes = winTimes;
	}

}
