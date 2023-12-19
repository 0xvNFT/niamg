package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员权限
 *
 * @author admin
 *
 */
@Table(name = "sys_user_perm")
public class SysUserPerm {
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 会员ID
	 */
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 用户类型
	 */
	@Column(name = "user_type")
	private Integer userType;
	/**
	 * 权限类型，UserPermEnum
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 状态，1=禁用，2=启用
	 */
	@Column(name = "status")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
