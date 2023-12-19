package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 操作日志
 *
 * @author admin
 *
 */
@Table(name = "sys_log")
public class SysLog {

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 站点下代理商名称
	 */
	@Column(name = "agent_name")
	private String agentName;

	/**
	 * 操作员ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 操作员
	 */
	@Column(name = "username")
	private String username;

	@Column(name = "type")
	private Integer type;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "ip")
	private String ip;

	@Column(name = "content")
	private String content;
	/**
	 * 参数
	 */
	@Column(name = "params")
	private String params;
	/**
	 * 用户类型(UserTypeEnum的值)
	 */
	@Column(name = "user_type")
	private Integer userType;

	private String ipStr;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getIpStr() {
		return ipStr;
	}

	public void setIpStr(String ipStr) {
		this.ipStr = ipStr;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

}
