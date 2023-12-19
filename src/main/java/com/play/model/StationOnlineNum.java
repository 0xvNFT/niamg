package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 *
 */
@Table(name = "station_online_num")
public class StationOnlineNum {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 具体时间
	 */
	@Column(name = "report_datetime")
	private Date reportDatetime;
	/**
	 * 统计的日期
	 */
	@Column(name = "stat_date")
	private String statDate;
	/**
	 * 统计的分钟
	 */
	@Column(name = "stat_minute")
	private String statMinute;
	/**
	 * 当前在线人数
	 */
	@Column(name = "online_num")
	private Integer onlineNum;
	/**
	 * 每分钟登陆的会员数
	 */
	@Column(name = "minute_login_num")
	private Integer minuteLoginNum;
	/**
	 * 当天总登陆个数
	 */
	@Column(name = "day_login_num")
	private Integer dayLoginNum;

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

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getStatMinute() {
		return statMinute;
	}

	public void setStatMinute(String statMinute) {
		this.statMinute = statMinute;
	}

	public Date getReportDatetime() {
		return reportDatetime;
	}

	public void setReportDatetime(Date reportDatetime) {
		this.reportDatetime = reportDatetime;
	}

	public Integer getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(Integer onlineNum) {
		this.onlineNum = onlineNum;
	}

	public Integer getMinuteLoginNum() {
		return minuteLoginNum;
	}

	public void setMinuteLoginNum(Integer minuteLoginNum) {
		this.minuteLoginNum = minuteLoginNum;
	}

	public Integer getDayLoginNum() {
		return dayLoginNum;
	}

	public void setDayLoginNum(Integer dayLoginNum) {
		this.dayLoginNum = dayLoginNum;
	}

}
