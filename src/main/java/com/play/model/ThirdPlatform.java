package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 第三方游戏开关 
 *
 * @author admin
 *
 */
@Table(name = "third_platform")
public class ThirdPlatform {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 状态，1=关，2=开
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 平台类型，AG、BBIN
	 */
	@Column(name = "platform")
	private Integer platform;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

}
