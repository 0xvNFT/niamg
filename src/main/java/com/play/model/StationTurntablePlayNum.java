package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 用户当天参与活动次数表
 *
 * @author admin
 *
 */
@Table(name = "station_turntable_play_num")
public class StationTurntablePlayNum {

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 当前日期
	 */
	@Column(name = "cur_date")
	private Date curDate;
	/**
	 * 参与活动次数
	 */
	@Column(name = "active_num")
	private Integer activeNum;

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

	public Date getCurDate() {
		return curDate;
	}

	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}

	public Integer getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
	}

}
