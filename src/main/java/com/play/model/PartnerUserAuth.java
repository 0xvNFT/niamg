package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "partner_user_auth")
public class PartnerUserAuth {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "menu_id")
	private Long menuId;

	@Column(name = "partner_id")
	private Long partnerId;

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

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

}
