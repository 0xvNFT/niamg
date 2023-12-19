package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 转盘活动奖项表
 *
 * @author admin
 *
 */
@Table(name = "station_turntable_award")
public class StationTurntableAward {
	public static int AWARD_TYPE_NONE = 1;// 未中奖
	public static int AWARD_TYPE_MONEY = 2;// 现金
	public static int AWARD_TYPE_GIFT = 3;// 奖品
	public static int AWARD_TYPE_SCORE = 4;// 积分

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 转盘活动ID
	 */
	@Column(name = "turntable_id")
	private Long turntableId;
	/**
	 * 奖项名称
	 */
	@Column(name = "award_name")
	private String awardName;
	/**
	 * 转盘奖品ID
	 */
	@Column(name = "gift_id")
	private Long giftId;
	/**
	 * 概率基数
	 */
	@Column(name = "chance")
	private BigDecimal chance;
	/**
	 * 奖项类型（1、不中奖，2、现金，3、奖品，4、积分）
	 */
	@Column(name = "award_type")
	private Integer awardType;
	/**
	 * 奖项数量
	 */
	@Column(name = "award_count")
	private Long awardCount;
	/**
	 * 面值
	 */
	@Column(name = "award_value")
	private BigDecimal awardValue;
	/**
	 * 奖项索引
	 */
	@Column(name = "award_index")
	private Long awardIndex;
	/**
	 * 打码量
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;

	private String giftImg;//奖品图片地址

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTurntableId() {
		return turntableId;
	}

	public void setTurntableId(Long turntableId) {
		this.turntableId = turntableId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public Long getGiftId() {
		return giftId;
	}

	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}

	public BigDecimal getChance() {
		return chance;
	}

	public void setChance(BigDecimal chance) {
		this.chance = chance;
	}

	public Integer getAwardType() {
		return awardType;
	}

	public void setAwardType(Integer awardType) {
		this.awardType = awardType;
	}

	public Long getAwardCount() {
		return awardCount;
	}

	public void setAwardCount(Long awardCount) {
		this.awardCount = awardCount;
	}

	public BigDecimal getAwardValue() {
		return awardValue;
	}

	public void setAwardValue(BigDecimal awardValue) {
		this.awardValue = awardValue;
	}

	public Long getAwardIndex() {
		return awardIndex;
	}

	public void setAwardIndex(Long awardIndex) {
		this.awardIndex = awardIndex;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public String getGiftImg() {
		return giftImg;
	}

	public void setGiftImg(String giftImg) {
		this.giftImg = giftImg;
	}
}
