package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_banner")
public class StationBanner {
	// 首页轮播
	public static final Integer INDEX_BANNER_TYPE = 1;
	// 手机轮播轮播
	public static final Integer MOBILE_BANNER_TYPE = 5;

	// 在线入款轮播轮播
	public static final Integer ONLINE_BANNER_TYPE = 8;
	// 快速入款轮播
	public static final Integer FAST_BANNER_TYPE = 6;
	// 银行入款轮播
	public static final Integer BANK_BANNER_TYPE = 7;
	
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
	 * 对应语种
	 */
	@Column(name = "language")
	private String language;
	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 标题图片地址
	 */
	@Column(name = "title_img")
	private String titleImg;
	/**
	 * 轮播链接地址
	 */
	@Column(name = "title_url")
	private String titleUrl;

	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;
	/**
	 * 结束时间
	 */
	@Column(name = "over_time")
	private Date overTime;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 排序，大的排前面
	 */
	@Column(name = "sort_no")
	private Integer sortNo;

	@Column(name = "code")
	private Integer code;

	@Column(name = "app_type")
	private Integer appType;

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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}
}
