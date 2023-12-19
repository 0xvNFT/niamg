package com.play.model;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 用户提款策略
 *
 * @author admin
 *
 */
@Table(name = "station_draw_fee_strategy")
public class StationDrawFeeStrategy {
	/**
	 * 固定手续费
	 */
	public static final int FEE_TYPE_FIXED = 1;
	/**
	 * 浮动手续费
	 */
	public static final int FEE_TYPE_FLOAT = 2;
	
	@Column(name = "id", primarykey = true)
	private Long id;
	@JSONField(serialize = false)
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 手续费类型 1:固定手续费 2:浮动手续费
	 */
	@Column(name = "fee_type")
	private Integer feeType;
	/**
	 * 手续费值
	 */
	@Column(name = "fee_value")
	private BigDecimal feeValue;
	/**
	 * 免费提款次数
	 */
	@Column(name = "draw_num")
	private Integer drawNum;
	/**
	 * 手续费上限
	 */
	@Column(name = "upper_limit")
	private BigDecimal upperLimit;
	/**
	 * 手续费下限
	 */
	@Column(name = "lower_limit")
	private BigDecimal lowerLimit;

	@Column(name = "remark")
	private String remark;
	/**
	 * 会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;

	/**
	 * 会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	/**
	 * 启用状态
	 */
	@Column(name = "status")
	private Integer status;

	private String degreeNames;

	private String groupNames;

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

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
	}

	public Integer getDrawNum() {
		return drawNum;
	}

	public void setDrawNum(Integer drawNum) {
		this.drawNum = drawNum;
	}

	public BigDecimal getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(BigDecimal upperLimit) {
		this.upperLimit = upperLimit;
	}

	public BigDecimal getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(BigDecimal lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getDegreeNames() {
		return degreeNames;
	}

	public void setDegreeNames(String degreeNames) {
		this.degreeNames = degreeNames;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

}
