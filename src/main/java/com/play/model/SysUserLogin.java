package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 存储会员最后登录信息
 *
 * @author admin
 *
 */
@Table(name = "sys_user_login")
public class SysUserLogin {
	public static final int STATUS_ONLINE_OFF = 1;// 下线
	public static final int STATUS_ONLINE_ON = 2;// 在线

	public static final int TERMINAL_PC = 1;// 电脑
	public static final int TERMINAL_WAP = 2;// wap
	public static final int TERMINAL_APP_ANDROID = 3;// 安卓app
	public static final int TERMINAL_APP_IOS = 4;// 苹果app
	public static final int TERMINAL_APP = 5;// app

	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_datetime")
	private Date lastLoginDatetime;
	/**
	 * 在线状态：1=下线，2=在线
	 */
	@Column(name = "online_status")
	private Integer onlineStatus;
	/**
	 * 最后登录ip
	 */
	@Column(name = "last_login_ip")
	private String lastLoginIp;
	/**
	 * 1=电脑端，2=手机web端，3=原生app端
	 */
	@Column(name = "terminal")
	private Integer terminal;
	/**
	 * 登录域名
	 */
	@Column(name = "domain")
	private String domain;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Date getLastLoginDatetime() {
		return lastLoginDatetime;
	}

	public void setLastLoginDatetime(Date lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getTerminal() {
		return terminal;
	}

	public void setTerminal(Integer terminal) {
		this.terminal = terminal;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
