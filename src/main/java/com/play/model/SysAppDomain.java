package com.play.model;


import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "sys_app_domain")
public class SysAppDomain {

	@Column(name = "id",primarykey = true)
	private Long id;
	/**
	 * app 域名
	 */
	@Column(name = "domain_url")
	private String domainUrl;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 是否已经做了https证书  2--是   1--否
	 */
	@Column(name = "has_vertify")
	private Integer hasVertify;
	/**
	 * 2--启动 1--禁用
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 站点编号
	 */
	@Column(name = "station_id")
	private String stationId;

	@Column(name = "source")
	private Integer source;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getHasVertify() {
		return hasVertify;
	}

	public void setHasVertify(Integer hasVertify) {
		this.hasVertify = hasVertify;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}
