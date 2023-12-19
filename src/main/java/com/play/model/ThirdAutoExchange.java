package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 第三方游戏额度自动转换记录表 
 *
 * @author admin
 *
 */
@Table(name = "third_auto_exchange")
public class ThirdAutoExchange {

	public final static int type_sys_to_third = 2;
	public final static int type_third_to_sys = 1;
	
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
	 * 会员id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 第三方游戏类型
	 */
	@Column(name = "platform")
	private Integer platform;
	/**
	 * 最后操作类型，2=系统转入第三方，1=第三方转入系统
	 */
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "update_time")
	private Date updateTime;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
