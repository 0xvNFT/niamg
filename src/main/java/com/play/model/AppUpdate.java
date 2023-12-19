package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "app_update")
public class AppUpdate {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * APP 版本号
	 */
	@Column(name = "version")
	private String version;
	/**
	 * 每个版本的更新内容
	 */
	@Column(name = "content")
	private String content;
	/**
	 * 开关状态  1-关闭 2-开启
	 */
	@Column(name = "status")
	private Long status;
	/**
	 * 更新的站点集，以逗号分隔。
	 */
	@Column(name = "station_ids")
	private String stationIds;
	
	@Column(name = "platform")
	private String platform;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getStationIds() {
		return stationIds;
	}

	public void setStationIds(String stationIds) {
		this.stationIds = stationIds;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

}
