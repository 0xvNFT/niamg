package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员组别信息
 *
 * @author admin
 *
 */
@Table(name = "sys_user_group")
public class SysUserGroup {

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
	 * 组别名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 成员数量
	 */
	@Column(name = "member_count")
	private Long memberCount;
	/**
	 * 状态(1禁用，2启用)
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 每日最高提款次数
	 */
	@Column(name = "daily_draw_num")
	private Integer dailyDrawNum;
	/**
	 * 最大提款金额
	 */
	@Column(name = "max_draw_money")
	private BigDecimal maxDrawMoney;
	/**
	 * 最小提款金额
	 */
	@Column(name = "min_draw_money")
	private BigDecimal minDrawMoney;
	/**
	 * 是否默认组别(1否，2是)
	 */
	@Column(name = "original")
	private Integer original;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getDailyDrawNum() {
		return dailyDrawNum;
	}

	public void setDailyDrawNum(Integer dailyDrawNum) {
		this.dailyDrawNum = dailyDrawNum;
	}

	public BigDecimal getMaxDrawMoney() {
		return maxDrawMoney;
	}

	public void setMaxDrawMoney(BigDecimal maxDrawMoney) {
		this.maxDrawMoney = maxDrawMoney;
	}

	public BigDecimal getMinDrawMoney() {
		return minDrawMoney;
	}

	public void setMinDrawMoney(BigDecimal minDrawMoney) {
		this.minDrawMoney = minDrawMoney;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

}
