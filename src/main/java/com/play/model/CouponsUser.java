package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "coupons_user")
public class CouponsUser {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "username")
	private String username;
	/**
	 * 是否可用(1可用2已用3过期)
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 领取时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 使用时间
	 */
	@Column(name = "used_datetime")
	private Date usedDatetime;
	/**
	 * 代金券名称
	 */
	@Column(name = "cname")
	private String cname;
	/**
	 * 优惠券关联id
	 */
	@Column(name = "cid")
	private Long cid;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getUsedDatetime() {
		return usedDatetime;
	}

	public void setUsedDatetime(Date usedDatetime) {
		this.usedDatetime = usedDatetime;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}
