package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@Table(name = "announcement_station")
public class AnnouncementStation {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "station_id")
	private Long stationId;
	
	@Column(name = "announcement_id")
	private Long announcementId;
	/**
	 * 已读状态 1未读 2已读
	 */
	@Column(name = "status")
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

	public Long getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Long announcementId) {
		this.announcementId = announcementId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
