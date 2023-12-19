package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_red_packet_record")
public class StationRedPacketRecord {
	// 未处理
	public static int STATUS_UNTREATED = 1;
	// 处理成功
	public static int STATUS_SUCCESS = 2;
	// 处理失败
	public static int STATUS_FAILED = 3;
	// 已取消
	public static int STATUS_CANCELED = 4;

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;

	@Column(name = "money")
	private BigDecimal money;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "packet_id")
	private Long packetId;
	/**
	 * 红包名称
	 */
	@Column(name = "packet_name")
	private String packetName;
	/**
	 * 处理状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * ip地址
	 */
	@Column(name = "ip")
	private String ip;
	/**
	 * 打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

	private Integer num;
	
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Long getPacketId() {
		return packetId;
	}

	public void setPacketId(Long packetId) {
		this.packetId = packetId;
	}

	public String getPacketName() {
		return packetName;
	}

	public void setPacketName(String packetName) {
		this.packetName = packetName;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
