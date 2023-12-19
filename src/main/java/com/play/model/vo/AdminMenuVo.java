package com.play.model.vo;

import java.util.ArrayList;
import java.util.List;

public class AdminMenuVo {
	private Long id;
	/**
	 * 菜单名称
	 */
	private String title;

	private Long parentId;
	private List<AdminMenuVo> childs;

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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<AdminMenuVo> getChilds() {
		return childs;
	}

	public void setChilds(List<AdminMenuVo> childs) {
		this.childs = childs;
	}

	public void addChild(AdminMenuVo m) {
		if (childs == null) {
			childs = new ArrayList<>();
		}
		childs.add(m);
	}
}
