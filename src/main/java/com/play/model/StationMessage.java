package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点站内信发送表
 *
 * @author admin
 *
 */
@Table(name = "station_message")
public class StationMessage {
	public static final int TYPE_SINGLE = 1;// 单个会员
	public static final int TYPE_SELECT_USER = 2;// 指定多个会员
	public static final int TYPE_ALL = 3;// 所有群发
	public static final int TYPE_DEGREE = 4;// 会员组别
	public static final int TYPE_GROUP = 5;// 会员组别
	public static final int TYPE_PROXY_NAME = 6;// 代理线

	public static final int SEND_TYPE_SYS = 1;// 发送方类型 系统
	public static final int SEND_TYPE_MEMBER = 2;// 发送方类型 会员
	public static final int SEND_TYPE_ADMIN = 3;// 发送方类型 站点管理员

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 信息内容
	 */
	@Column(name = "content")
	private String content;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 信息标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 接收类型 1个人 2群发 3层级 4上级 5下级
	 */
	@Column(name = "receive_type")
	private Integer receiveType;
	/**
	 * 用户等级ID
	 */
	@Column(name = "degree_id")
	private Long degreeId;
	/**
	 * 会员组别
	 */
	@Column(name = "group_id")
	private Long groupId;
	/**
	 * 发送方类型，1租户后台，2会员
	 */
	@Column(name = "send_type")
	private Integer sendType;
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
	/**
	 * 是否弹窗显示 1否 2是
	 */
	@Column(name = "pop_status")
	private Integer popStatus;
	@Column(name = "proxy_name")
	private String proxyName;

	private String receiveUsernames;
	 /**
     * 接收方消息ID
     */
	private Long receiveMessageId;
	  /**
     * 消息读取状态
     */
    private Integer status;
    
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
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

	public Integer getPopStatus() {
		return popStatus;
	}

	public void setPopStatus(Integer popStatus) {
		this.popStatus = popStatus;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getReceiveUsernames() {
		return receiveUsernames;
	}

	public void setReceiveUsernames(String receiveUsernames) {
		this.receiveUsernames = receiveUsernames;
	}

	public Long getReceiveMessageId() {
		return receiveMessageId;
	}

	public void setReceiveMessageId(Long receiveMessageId) {
		this.receiveMessageId = receiveMessageId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
