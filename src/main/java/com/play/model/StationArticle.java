package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 网站资料表(系统公告,玩法介绍,关于我们)
 *
 * @author admin
 *
 */
@Table(name = "station_article")
public class StationArticle {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
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
	 * 类型 1:关于我们,2:取款帮助,3:存款帮助,4:合作伙伴->联盟方案,5:合作伙伴->联盟协议,6:联系我们,7:常见问题
	 * ,8:玩法介绍,9:彩票公告,10:视讯公告,11:体育公告,12:电子公告,13:最新公告,14:页面弹窗,15签到公告,16最新资讯,17签到规则,18新手教程,19手机弹窗20责任条款21红包规则
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 文本内容
	 */
	@Column(name = "content")
	private String content;
	/**
	 * 状态 1:禁用 2:启用
	 */
	@Column(name = "status")
	private Integer status;

	@Column(name = "popup_status")
	private Integer popupStatus;

	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 更新/插入时间
	 */
	@Column(name = "update_time")
	private Date updateTime;
	/**
	 * 过期时间
	 */
	@Column(name = "over_time")
	private Date overTime;
	/**
	 * 是否首页弹窗
	 */
	@Column(name = "is_index")
	private String isIndex;
	/**
	 * 是否注册页面弹窗
	 */
	@Column(name = "is_reg")
	private String isReg;
	/**
	 * 是否充值页面弹窗
	 */
	@Column(name = "is_deposit")
	private String isDeposit;
	/**
	 * 是否显示投注页弹窗
	 */
	@Column(name = "is_bet")
	private String isBet;
	/**
	 * 弹窗宽度
	 */
	@Column(name = "frame_width")
	private Long frameWidth;
	/**
	 * 弹窗高度
	 */
	@Column(name = "frame_height")
	private Long frameHeight;
	/**
	 * 序号
	 */
	@Column(name = "sort_no")
	private Long sortNo;
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

	private boolean messageFlag;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPopupStatus() {
		return popupStatus;
	}

	public void setPopupStatus(Integer popupStatus) {
		this.popupStatus = popupStatus;
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

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getIsReg() {
		return isReg;
	}

	public void setIsReg(String isReg) {
		this.isReg = isReg;
	}

	public String getIsDeposit() {
		return isDeposit;
	}

	public void setIsDeposit(String isDeposit) {
		this.isDeposit = isDeposit;
	}

	public String getIsBet() {
		return isBet;
	}

	public void setIsBet(String isBet) {
		this.isBet = isBet;
	}

	public Long getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(Long frameWidth) {
		this.frameWidth = frameWidth;
	}

	public Long getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(Long frameHeight) {
		this.frameHeight = frameHeight;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
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

	public boolean isMessageFlag() {
		return messageFlag;
	}

	public void setMessageFlag(boolean messageFlag) {
		this.messageFlag = messageFlag;
	}

}
