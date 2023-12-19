package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点站内信接收表
 *
 * @author admin
 *
 */
@Table(name = "station_message_receive")
public class StationMessageReceive {
	/**
	 * 未读
	 */
	public static int STATUS_UNREAD = 1;
	/**
	 * 已读
	 */
	public static int STATUS_READ = 2;
	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 站内信ID
	 */
	@Column(name = "message_id")
	private Long messageId;
	/**
	 * 状态（1、未读，2、已读）
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 是否弹窗显示 1否 2是
	 */
	@Column(name = "pop_status")
	private Integer popStatus;
	/**
	 * 发送方类型，1租户后台，2会员
	 */
	@Column(name = "send_type")
	private Integer sendType;

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

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPopStatus() {
		return popStatus;
	}

	public void setPopStatus(Integer popStatus) {
		this.popStatus = popStatus;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

}
