package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 国家/地区限制 
 *
 * @author admin
 *
 */
@Table(name = "station_white_area")
public class StationWhiteArea {
	
	@Column(name = "id",primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 国家地区编码
	 */
	@Column(name = "country_code")
	private String countryCode;
	/**
	 * 国家地区名称
	 */
	@Column(name = "country_name")
	private String countryName;
	/**
	 * 类型状态
	 */
	@Column(name = "status")
	private Integer status;

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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
