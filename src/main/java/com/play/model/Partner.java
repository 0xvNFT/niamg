package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 合作商信息表 
 *
 * @author admin
 *
 */
@Table(name = "partner")
public class Partner {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
