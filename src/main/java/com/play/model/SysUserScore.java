package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 会员积分信息 
 *
 * @author admin
 *
 */
@Table(name = "sys_user_score")
public class SysUserScore {
	
	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;
	/**
	 * 会员积分
	 */
	@Column(name = "score")
	private Long score;
	/**
	 * 最后一次签到时间
	 */
	@Column(name = "last_sign_date")
	private Date lastSignDate;
	/**
	 * 连续签到次数
	 */
	@Column(name = "sign_count")
	private Integer signCount;
	@Column(name = "station_id")
	private Long stationId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Date getLastSignDate() {
		return lastSignDate;
	}

	public void setLastSignDate(Date lastSignDate) {
		this.lastSignDate = lastSignDate;
	}

	public Integer getSignCount() {
		return signCount;
	}

	public void setSignCount(Integer signCount) {
		this.signCount = signCount;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

}
