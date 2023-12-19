package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 积分变动记录表
 *
 * @author admin
 *
 */
@Table(name = "sys_user_score_history")
public class SysUserScoreHistory {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;
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
	@Column(name = "username")
	private String username;
	/**
	 * 变动前积分
	 */
	@Column(name = "before_score")
	private Long beforeScore;
	/**
	 * 变动积分
	 */
	@Column(name = "score")
	private Long score;
	/**
	 * 变动后积分
	 */
	@Column(name = "after_score")
	private Long afterScore;
	/**
	 * 账变类型
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 备注说明
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 连续签到次数
	 */
	@Column(name = "sign_count")
	private Integer signCount;

	private String createDatetimeStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getBeforeScore() {
		return beforeScore;
	}

	public void setBeforeScore(Long beforeScore) {
		this.beforeScore = beforeScore;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getAfterScore() {
		return afterScore;
	}

	public void setAfterScore(Long afterScore) {
		this.afterScore = afterScore;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSignCount() {
		return signCount;
	}

	public void setSignCount(Integer signCount) {
		this.signCount = signCount;
	}

	public String getCreateDatetimeStr() {
		return createDatetimeStr;
	}

	public void setCreateDatetimeStr(String createDatetimeStr) {
		this.createDatetimeStr = createDatetimeStr;
	}
}
