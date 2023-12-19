package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 租户登录谷歌验证
 *
 * @author admin
 */
@Table(name = "admin_login_google_auth")
public class AdminLoginGoogleAuth {
	public static final int STATUS_NO_VALID = 1;// 不需要验证
	public static final int STATUS_VALID = 2;// 需要验证

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 谷歌key
	 */
	@Column(name = "key")
	private String key;
	/**
	 * 管理员账号
	 */
	@Column(name = "admin_username")
	private String adminUsername;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 1=不验证，2=验证
	 * 
	 * @return
	 */
	@Column(name = "status")
	private Integer status;

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
