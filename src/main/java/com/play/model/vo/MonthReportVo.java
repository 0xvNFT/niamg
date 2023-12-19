package com.play.model.vo;

import java.math.BigDecimal;

public class MonthReportVo {
	private String statDateStr;
	private BigDecimal betAmount;
	private BigDecimal lotProfit;
	private BigDecimal sportProfit;
	private BigDecimal liveProfit;
	private BigDecimal egameProfit;
	private BigDecimal chessProfit;
	private BigDecimal esportProfit;
	private BigDecimal fishingProfit;

	public String getStatDateStr() {
		return statDateStr;
	}

	public void setStatDateStr(String statDateStr) {
		this.statDateStr = statDateStr;
	}

	public BigDecimal getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}

	public BigDecimal getLotProfit() {
		return lotProfit;
	}

	public void setLotProfit(BigDecimal lotProfit) {
		this.lotProfit = lotProfit;
	}

	public BigDecimal getSportProfit() {
		return sportProfit;
	}

	public void setSportProfit(BigDecimal sportProfit) {
		this.sportProfit = sportProfit;
	}

	public BigDecimal getLiveProfit() {
		return liveProfit;
	}

	public void setLiveProfit(BigDecimal liveProfit) {
		this.liveProfit = liveProfit;
	}

	public BigDecimal getEgameProfit() {
		return egameProfit;
	}

	public void setEgameProfit(BigDecimal egameProfit) {
		this.egameProfit = egameProfit;
	}

	public BigDecimal getChessProfit() {
		return chessProfit;
	}

	public void setChessProfit(BigDecimal chessProfit) {
		this.chessProfit = chessProfit;
	}

	public BigDecimal getEsportProfit() {
		return esportProfit;
	}

	public void setEsportProfit(BigDecimal esportProfit) {
		this.esportProfit = esportProfit;
	}

	public BigDecimal getFishingProfit() {
		return fishingProfit;
	}

	public void setFishingProfit(BigDecimal fishingProfit) {
		this.fishingProfit = fishingProfit;
	}

}
