package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * admin user 权限表 
 *
 * @author admin
 *
 */
@Table(name = "admin_menu")
public class AdminMenu {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 菜单名称
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 菜单名称多语言的code
	 */
	@Column(name = "code")
	private String code;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "parent_id")
	private Long parentId;
	/**
	 * 序号
	 */
	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 图标
	 */
	@Column(name = "icon")
	private String icon;
	/**
	 * 权限名称
	 */
	@Column(name = "perm_name")
	private String permName;
	/**
	 * 菜单类别  1：菜单目录  2：外部跳转页面，3: 页面  4：操作功能
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
