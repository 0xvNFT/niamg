package com.play.model.vo;

import java.util.List;

import com.play.model.StationFloatFrameSetting;

public class FloatFrameVo {
	private Long id;
	/**
	 * 显示页面1主站 2个人中心
	 */
	private Integer showPage;
	
	private Integer imgType;
	/**
	 * 显示位置left_top,left_middle,left_bottom,right_top,right_middle,right_bottom
	 */
	private String showPosition;
	
	/**
	 * 图片
	 */
	private List<StationFloatFrameSetting> afsList;

	public Integer getShowPage() {
		return showPage;
	}

	public void setShowPage(Integer showPage) {
		this.showPage = showPage;
	}

	public List<StationFloatFrameSetting> getAfsList() {
		return afsList;
	}

	public void setAfsList(List<StationFloatFrameSetting> afsList) {
		this.afsList = afsList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(String showPosition) {
		this.showPosition = showPosition;
	}

	public Integer getImgType() {
		return imgType;
	}

	public void setImgType(Integer imgType) {
		this.imgType = imgType;
	}

}
