package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员按日反水策略
 *
 * @author admin
 *
 */
@Table(name = "station_kickback_strategy")
public class StationKickbackStrategy {

	@Column(name = "id", primarykey = true)
	private Long id;
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 反水比例
	 */
	@Column(name = "kickback")
	private BigDecimal kickback;
	/**
	 * 有效投注（大于等于）
	 */
	@Column(name = "min_bet")
	private BigDecimal minBet;
	/**
	 * 打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 反水上限
	 */
	@Column(name = "max_back")
	private BigDecimal maxBack;

	@Column(name = "status")
	private Integer status;

	@Column(name = "remark")
	private String remark;
	/**
	 * 类型：1：彩票反水 ，2：体育反水 ，3：真人反水，4：电子反水，5：六合彩反水
	 */
	@Column(name = "type")
	private Integer type;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 有效会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;


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

	public BigDecimal getKickback() {
		return kickback;
	}

	public void setKickback(BigDecimal kickback) {
		this.kickback = kickback;
	}

	public BigDecimal getMinBet() {
		return minBet;
	}

	public void setMinBet(BigDecimal minBet) {
		this.minBet = minBet;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getMaxBack() {
		return maxBack;
	}

	public void setMaxBack(BigDecimal maxBack) {
		this.maxBack = maxBack;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
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

}
