package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 用户大转盘中奖记录 
 *
 * @author admin
 *
 */
@Table(name = "station_turntable_record")
public class StationTurntableRecord {
	
	  // 未处理
    public static int STATUS_UNTREATED = 1;
    // 处理成功
    public static int STATUS_SUCCESS = 2;
    // 处理失败
    public static int STATUS_FAILED = 3;

    
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
	 * 转盘活动ID
	 */
	@Column(name = "turntable_id")
	private Long turntableId;
	/**
	 * 奖项类型（1、不中奖，2、现金，3、商品，4、积分）
	 */
	@Column(name = "award_type")
	private Integer awardType;
	/**
	 * 奖品ID
	 */
	@Column(name = "gift_id")
	private Long giftId;
	/**
	 * 奖品名称
	 */
	@Column(name = "gift_name")
	private String giftName;
	/**
	 * 奖项面值
	 */
	@Column(name = "award_value")
	private BigDecimal awardValue;
	/**
	 * 中奖时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 处理状态(1、未处理，2、已处理，3、处理失败)
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 备注说明
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 现金打码量
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

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

	public Long getTurntableId() {
		return turntableId;
	}

	public void setTurntableId(Long turntableId) {
		this.turntableId = turntableId;
	}

	public Integer getAwardType() {
		return awardType;
	}

	public void setAwardType(Integer awardType) {
		this.awardType = awardType;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public BigDecimal getAwardValue() {
		return awardValue;
	}

	public void setAwardValue(BigDecimal awardValue) {
		this.awardValue = awardValue;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

}
