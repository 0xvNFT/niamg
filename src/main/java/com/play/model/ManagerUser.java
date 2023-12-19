package com.play.model;

import java.io.Serializable;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 总控管理员
 *
 * @author admin
 *
 */
@Table(name = "manager_user")
public class ManagerUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "password2")
	private String password2;

	@Column(name = "salt")
	private String salt;
	/**
	 * 状态 1 停用 2启用
	 */
	@Column(name = "status")
	private Integer status;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 用户类型(UserTypeEnum的值)
	 */
	@Column(name = "type")
	private Integer type;

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

}
