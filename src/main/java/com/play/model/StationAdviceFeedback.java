package com.play.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

/**
 * 建议反馈记录表
 *
 * @author admin
 */
@Table(name = "station_advice_feedback")
public class StationAdviceFeedback {
	/**
	 * 状态 待回复
	 */
	public static int STATUS_DEFAULT = 1;
	/**
	 * 状态 已回复
	 */
	public static int STATUS_ALL = 2;

	/**
	 * 建议类型 提交建议
	 */
	public static int SEND_TYPE_ADVICE = 1;

	/**
	 * 建议类型 我要投诉
	 */
	public static int SEND_TYPE_FEEDBACK = 2;

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;

	/**
	 * 建议类型，1提交建议、2我要投诉;
	 */
	@Column(name = "send_type")
	private Integer sendType;

	/**
	 * 建议内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 提交时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 最后反馈时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "final_time")
	private Date finalTime;

	/**
	 * 状态 1待回复 2 已回复
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 发送方账号
	 */
	@Column(name = "send_username")
	private String sendUsername;

	/**
	 * 发送方ID
	 */
	@Column(name = "send_user_id")
	private Long sendUserId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

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

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSendUsername() {
		return sendUsername;
	}

	public void setSendUsername(String sendUsername) {
		this.sendUsername = sendUsername;
	}

	public Long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}

}
