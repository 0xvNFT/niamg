package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 会员签到日志表 
 *
 * @author admin
 *
 */
@Table(name = "station_sign_record")
public class StationSignRecord {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 会员id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 会员账号
	 */
	@Column(name = "username")
	private String username;
	
	@Column(name = "sign_date")
	private Date signDate;
	/**
	 * 签到获得积分
	 */
	@Column(name = "score")
	private Long score;
	/**
	 * 连续签到天数
	 */
	@Column(name = "sign_days")
	private Integer signDays;
	/**
	 * 签到规则ID
	 */
	@Column(name = "rule_id")
	private Long ruleId;
	/**
	 * 签到获得彩金
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 签到IP
	 */
	@Column(name = "ip")
	private String ip;

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

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Integer getSignDays() {
		return signDays;
	}

	public void setSignDays(Integer signDays) {
		this.signDays = signDays;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
