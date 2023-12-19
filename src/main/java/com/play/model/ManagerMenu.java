package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 总控菜单
 *
 * @author admin
 *
 */
@Table(name = "manager_menu")
public class ManagerMenu {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "url")
	private String url;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "sort_no")
	private Long sortNo;

	@Column(name = "icon")
	private String icon;
	/**
	 * 1=禁用，2=启用
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 权限名称
	 */
	@Column(name = "perm_name")
	private String permName;
	/**
	 * 菜单类别 1：菜单目录 2：外部跳转页面,3:页面 4：操作功能
	 */
	@Column(name = "type")
	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPermName() {
		return permName;
	}

	public void setPermName(String permName) {
		this.permName = permName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
