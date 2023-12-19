package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 合作商后台管理员 
 *
 * @author admin
 *
 */
@Table(name = "partner_user")
public class PartnerUser {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	/**
	 * 二级密码
	 */
	@Column(name = "password2")
	private String password2;
	
	@Column(name = "salt")
	private String salt;
	/**
	 * 状态  1 停用  2启用
	 */
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * UserTypeEnum的值
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 合作商id
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 默认账号，2=默认，1=后期添加
	 */
	@Column(name = "original")
	private Integer original;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

}
