package com.play.model.bo;

public class StationMessageBo {
	private String title;
	private String content;
	private Integer receiveType;
	private Long degreeId;
	private Long groupId;
	private String usernames;
	private String proxyName;
	private String receiveUsername;
	/**
	 * 群发时会员最后登陆时间 1：一周内 2：两周内 3：一个月内 4两个月内 5三个月内
	 */
	private Integer lastLogin;
	private Integer popStatus;
	private Long stationId;
	private Integer sendType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getUsernames() {
		return usernames;
	}

	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getReceiveUsername() {
		return receiveUsername;
	}

	public void setReceiveUsername(String receiveUsername) {
		this.receiveUsername = receiveUsername;
	}

	public Integer getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Integer lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getPopStatus() {
		return popStatus;
	}

	public void setPopStatus(Integer popStatus) {
		this.popStatus = popStatus;
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

}
