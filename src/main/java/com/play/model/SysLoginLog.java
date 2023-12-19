package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 登陆日志
 *
 * @author admin
 */
@Table(name = "sys_login_log")
public class SysLoginLog {

	public static int STATUS_FAILED = 1;// 失败
	public static int STATUS_SUCCESS = 2;// 登录成功

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
	 * 用户类型(UserTypeEnum的值)
	 */
	@Column(name = "user_type")
	private Integer userType;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 操作员IP
	 */
	@Column(name = "login_ip")
	private String loginIp;

	@Column(name = "login_os")
	private String loginOs;

	/**
	 * 域名
	 */
	@Column(name = "domain")
	private String domain;

	/**
	 * 登陆浏览器
	 */
	@Column(name = "user_agent")
	private String userAgent;

	/**
	 * 状态，2=成功，1=失败
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	private String loginIpStr;

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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginIpStr() {
		return loginIpStr;
	}

	public void setLoginIpStr(String loginIpStr) {
		this.loginIpStr = loginIpStr;
	}

	public String getLoginOs() {
		return loginOs;
	}

	public void setLoginOs(String loginOs) {
		this.loginOs = loginOs;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
