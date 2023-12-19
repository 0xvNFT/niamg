package com.play.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员绑定的USDT地址表
 * @author admin
 *
 */
@Table(name = "sys_user_tron_link")
public class SysUserTronLink implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Column(name = "id", primarykey = true)
	private Long id;
 
	/**
	 * 站点ID
	 */
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
	@Column(name = "user_name")
	private String userName;

	/**
	 * Tron链地址
	 */
	@Column(name = "tron_link_addr")
	private String tronLinkAddr;

	/**
	 * 初始化trx金额
	 */
	@Column(name = "init_trx")
	private BigDecimal initTrx;

	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_datetime")
	private Date createDatetime;

	/**
	 * 初始化转账最后时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "init_valid_datetime")
	private Date initValidDatetime;

	/**
	 * 绑定状态（1未绑定 2已绑定）
	 */
	@Column(name = "bind_status")
	private Integer bindStatus;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTronLinkAddr() {
		return tronLinkAddr;
	}

	public void setTronLinkAddr(String tronLinkAddr) {
		this.tronLinkAddr = tronLinkAddr;
	}

	public BigDecimal getInitTrx() {
		return initTrx;
	}

	public void setInitTrx(BigDecimal initTrx) {
		this.initTrx = initTrx;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getInitValidDatetime() {
		return initValidDatetime;
	}

	public void setInitValidDatetime(Date initValidDatetime) {
		this.initValidDatetime = initValidDatetime;
	}

	public Integer getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(Integer bindStatus) {
		this.bindStatus = bindStatus;
	}
}
