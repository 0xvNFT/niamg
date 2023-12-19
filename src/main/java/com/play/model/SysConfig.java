package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 系统配置信息 
 *
 * @author admin
 *
 */
@Table(name = "sys_config")
public class SysConfig {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 唯一键
	 */
	@Column(name = "key")
	private String key;
	/**
	 * 值
	 */
	@Column(name = "value")
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
