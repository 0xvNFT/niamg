package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 站点配置信息 
 *
 * @author admin
 *
 */
@Table(name = "station_config")
public class StationConfig {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 唯一键
	 */
	@Column(name = "key")
	private String key;
	/**
	 * 描述标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 分组名称
	 */
	@Column(name = "group_name")
	private String groupName;
	/**
	 * 值
	 */
	@Column(name = "value")
	private String value;
	/**
	 * 前端输入的类型（text, switchSelect)
	 */
	@Column(name = "ele_type")
	private String eleType;
	/**
	 * 1=站点不可见，2=站点可见
	 */
	@Column(name = "visible")
	private Integer visible;
	/**
	 * 序号
	 */
	@Column(name = "sort_no")
	private Integer sortNo;

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getEleType() {
		return eleType;
	}

	public void setEleType(String eleType) {
		this.eleType = eleType;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

}
