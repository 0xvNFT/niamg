package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "station_float_frame")
public class StationFloatFrame {

	public static final Integer show_page_index = 1;// 网站首页
	public static final Integer show_page_user_center = 2;// 个人中心
	public static final Integer show_page_login = 3;//登录页面
	public static final Integer show_page_reg = 4;//注册页面
	
	public static final Integer show_platform_web = 1;
	public static final Integer show_platform_wap = 2;
	public static final Integer show_platform_app = 3;
	
	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "language")
	private String language;
	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 显示页面1主站 2个人中心
	 */
	@Column(name = "show_page")
	private Integer showPage;
	/**
	 * 显示位置
	 */
	@Column(name = "show_position")
	private String showPosition;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 图片类型1盖楼2轮播
	 */
	@Column(name = "img_type")
	private Integer imgType;

	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 创建时间
	 */
	@Column(name = "update_time")
	private Date updateTime;
	/**
	 * 开始时间
	 */
	@Column(name = "begin_time")
	private Date beginTime;
	/**
	 * 过期时间
	 */
	@Column(name = "over_time")
	private Date overTime;
	/**
	 * 会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	/**
	 * 显示终端
	 */
	@Column(name = "platform")
	private Integer platform;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getShowPage() {
		return showPage;
	}

	public void setShowPage(Integer showPage) {
		this.showPage = showPage;
	}

	public String getShowPosition() {
		return showPosition;
	}

	public void setShowPosition(String showPosition) {
		this.showPosition = showPosition;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getImgType() {
		return imgType;
	}

	public void setImgType(Integer imgType) {
		this.imgType = imgType;
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

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

}
