package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_red_packet_degree")
public class StationRedPacketDegree {

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 红包id
	 */
	@Column(name = "packet_id")
	private Long packetId;
	/**
	 * 会员等级id
	 */
	@Column(name = "degree_id")
	private Long degreeId;
	/**
	 * 单个等级红包数量
	 */
	@Column(name = "packet_number")
	private Integer packetNumber;
	/**
	 * 单个等级红包剩余数量
	 */
	@Column(name = "packet_number_got")
	private Integer packetNumberGot;
	/**
	 * 可抽取最小金额
	 */
	@Column(name = "red_bag_min")
	private BigDecimal redBagMin;
	/**
	 * 可抽取最大金额
	 */
	@Column(name = "red_bag_max")
	private BigDecimal redBagMax;
	/**
	 * 充值最小金额
	 */
	@Column(name = "red_bag_recharge_min")
	private BigDecimal redBagRechargeMin;
	/**
	 * 充值最大金额
	 */
	@Column(name = "red_bag_recharge_max")
	private BigDecimal redBagRechargeMax;
	
	//剩余红包个数
    private Integer remainNumber;

	private String degreeName;

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

	public Long getPacketId() {
		return packetId;
	}

	public void setPacketId(Long packetId) {
		this.packetId = packetId;
	}

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public Integer getPacketNumber() {
		return packetNumber;
	}

	public void setPacketNumber(Integer packetNumber) {
		this.packetNumber = packetNumber;
	}

	public Integer getPacketNumberGot() {
		return packetNumberGot;
	}

	public void setPacketNumberGot(Integer packetNumberGot) {
		this.packetNumberGot = packetNumberGot;
	}

	public BigDecimal getRedBagMin() {
		return redBagMin;
	}

	public void setRedBagMin(BigDecimal redBagMin) {
		this.redBagMin = redBagMin;
	}

	public BigDecimal getRedBagMax() {
		return redBagMax;
	}

	public void setRedBagMax(BigDecimal redBagMax) {
		this.redBagMax = redBagMax;
	}

	public BigDecimal getRedBagRechargeMin() {
		return redBagRechargeMin;
	}

	public void setRedBagRechargeMin(BigDecimal redBagRechargeMin) {
		this.redBagRechargeMin = redBagRechargeMin;
	}

	public BigDecimal getRedBagRechargeMax() {
		return redBagRechargeMax;
	}

	public void setRedBagRechargeMax(BigDecimal redBagRechargeMax) {
		this.redBagRechargeMax = redBagRechargeMax;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public Integer getRemainNumber() {
		return remainNumber;
	}

	public void setRemainNumber(Integer remainNumber) {
		this.remainNumber = remainNumber;
	}

}
