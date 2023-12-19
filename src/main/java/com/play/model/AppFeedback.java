package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "app_feedback")
public class AppFeedback {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 发建议的用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 意见内容
	 */
	@Column(name = "content")
	private String content;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 消息类型 2--管理员消息 1--用户消息
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 被引用消息的id
	 */
	@Column(name = "parent_id")
	private Long parentId;
	/**
	 * 是否已回复 2--已回复0--未回复
	 */
	@Column(name = "is_reply")
	private Integer isReply;

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getIsReply() {
		return isReply;
	}

	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}

}
