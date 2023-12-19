package com.play.model;

import java.math.BigDecimal;


import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_score_exchange")
public class StationScoreExchange {

	public static final Integer MNY_TO_SCORE = 1;
	public static final Integer SCORE_TO_MNY = 2;

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 配置名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 配置类型
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 兑换分子数
	 */
	@Column(name = "numerator")
	private BigDecimal numerator;
	/**
	 * 兑换分母数
	 */
	@Column(name = "denominator")
	private BigDecimal denominator;
	/**
	 * 单次兑换最大值
	 */
	@Column(name = "max_val")
	private BigDecimal maxVal;
	/**
	 * 单次兑换最小值
	 */
	@Column(name = "min_val")
	private BigDecimal minVal;
	/**
	 * 状态(1、禁用，2、启用)
	 */
	@Column(name = "status")
	private Integer status;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getNumerator() {
		return numerator;
	}

	public void setNumerator(BigDecimal numerator) {
		this.numerator = numerator;
	}

	public BigDecimal getDenominator() {
		return denominator;
	}

	public void setDenominator(BigDecimal denominator) {
		this.denominator = denominator;
	}

	public BigDecimal getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(BigDecimal maxVal) {
		this.maxVal = maxVal;
	}

	public BigDecimal getMinVal() {
		return minVal;
	}

	public void setMinVal(BigDecimal minVal) {
		this.minVal = minVal;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

}
