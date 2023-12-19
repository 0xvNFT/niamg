package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_float_frame_setting")
public class StationFloatFrameSetting {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 浮窗id
	 */
	@Column(name = "afr_id")
	private Long afrId;
	/**
	 * 图片地址
	 */
	@Column(name = "img_url")
	private String imgUrl;
	/**
	 * 鼠标移入图片显示地址
	 */
	@Column(name = "img_hover_url")
	private String imgHoverUrl;
	/**
	 * 图片顺序
	 */
	@Column(name = "img_sort")
	private Integer imgSort;
	/**
	 * 链接地址
	 */
	@Column(name = "link_url")
	private String linkUrl;
	/**
	 * 链接类型(1普通跳转,2新窗打开,3关闭按钮)
	 */
	@Column(name = "link_type")
	private Integer linkType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAfrId() {
		return afrId;
	}

	public void setAfrId(Long afrId) {
		this.afrId = afrId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgHoverUrl() {
		return imgHoverUrl;
	}

	public void setImgHoverUrl(String imgHoverUrl) {
		this.imgHoverUrl = imgHoverUrl;
	}

	public Integer getImgSort() {
		return imgSort;
	}

	public void setImgSort(Integer imgSort) {
		this.imgSort = imgSort;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

}
