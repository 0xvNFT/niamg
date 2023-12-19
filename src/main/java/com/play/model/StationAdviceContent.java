package com.play.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

/**
 * 建议回复记录表
 *
 * @author admin
 */
@Table(name = "station_advice_content")
public class StationAdviceContent {
	/**
	 * 回复类型 后台管理员
	 */
	public static int CONTENT_TYPE_ADMIN = 1;

	/**
	 * 回复类型 会员
	 */
	public static int CONTENT_TYPE_USER = 2;

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 会员ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 会员账号
	 */
	@Column(name = "username")
	private String username;

	/**
	 * 回复类型（1、后台管理员，2、会员）;
	 */
	@Column(name = "content_type")
	private Integer contentType;

	/**
	 * 回复内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 建议ID
	 */
	@Column(name = "advice_id")
	private Long adviceId;

	/**
	 * 回复时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getAdviceId() {
		return adviceId;
	}

	public void setAdviceId(Long adviceId) {
		this.adviceId = adviceId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
