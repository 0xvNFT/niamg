package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员打码量信息
 *
 * @author admin
 *
 */
@Table(name = "sys_user_bet_num")
public class SysUserBetNum {

	@Column(name = "user_id", primarykey = true,generator = Column.PK_BY_HAND)
	private Long userId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 出款所需打码量
	 */
	@Column(name = "draw_need")
	private BigDecimal drawNeed;
	/**
	 * 当前打码量
	 */
	@Column(name = "bet_num")
	private BigDecimal betNum;
	/**
	 * 总打码量
	 */
	@Column(name = "total_bet_num")
	private BigDecimal totalBetNum;

	private BigDecimal beforeDrawNeed;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public BigDecimal getDrawNeed() {
		return drawNeed;
	}

	public void setDrawNeed(BigDecimal drawNeed) {
		this.drawNeed = drawNeed;
	}

	public BigDecimal getBetNum() {
		return betNum;
	}

	public void setBetNum(BigDecimal betNum) {
		this.betNum = betNum;
	}

	public BigDecimal getTotalBetNum() {
		return totalBetNum;
	}

	public void setTotalBetNum(BigDecimal totalBetNum) {
		this.totalBetNum = totalBetNum;
	}

	public BigDecimal getBeforeDrawNeed() {
		return beforeDrawNeed;
	}

	public void setBeforeDrawNeed(BigDecimal beforeDrawNeed) {
		this.beforeDrawNeed = beforeDrawNeed;
	}

}
