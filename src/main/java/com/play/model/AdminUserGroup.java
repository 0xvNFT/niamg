package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点管理员组别信息
 *
 * @author admin
 *
 */
@Table(name = "admin_user_group")
public class AdminUserGroup {

	public static final int TYPE_MASTER = 1;// 总控可操作
	public static final int TYPE_PARTNER_VIEW = 2;// 合作商只可看
	public static final int TYPE_PARTNER_MODIFY = 3;// 合作商可操作
	public static final int TYPE_ADMIN_VIEW = 4;// 站点只可看
	public static final int TYPE_ADMIN_MODIFY = 5;// 站点可修改权限

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 分组名称
	 */
	@Column(name = "name")
	private String name;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 1=总控可操作，2=合作商只可看，3=合作商可操作，4=站点只可看，5=站点可修改权限
	 */
	@Column(name = "type")
	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
