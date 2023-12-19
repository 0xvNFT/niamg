package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 代理返点设置
 *
 * @author admin
 *
 */
@Table(name = "sys_user_rebate")
public class SysUserRebate {

	/**
	 * 会员ID
	 */
	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;

	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "live")
	private BigDecimal live;

	@Column(name = "egame")
	private BigDecimal egame;

	@Column(name = "chess")
	private BigDecimal chess;

	@Column(name = "esport")
	private BigDecimal esport;

	@Column(name = "sport")
	private BigDecimal sport;

	@Column(name = "fishing")
	private BigDecimal fishing;

	@Column(name = "lottery")
	private BigDecimal lottery;

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

	public BigDecimal getLive() {
		return live;
	}

	public void setLive(BigDecimal live) {
		this.live = live;
	}

	public BigDecimal getEgame() {
		return egame;
	}

	public void setEgame(BigDecimal egame) {
		this.egame = egame;
	}

	public BigDecimal getChess() {
		return chess;
	}

	public void setChess(BigDecimal chess) {
		this.chess = chess;
	}

	public BigDecimal getEsport() {
		return esport;
	}

	public void setEsport(BigDecimal esport) {
		this.esport = esport;
	}

	public BigDecimal getSport() {
		return sport;
	}

	public void setSport(BigDecimal sport) {
		this.sport = sport;
	}

	public BigDecimal getFishing() {
		return fishing;
	}

	public void setFishing(BigDecimal fishing) {
		this.fishing = fishing;
	}

	public BigDecimal getLottery() {
		return lottery;
	}

	public void setLottery(BigDecimal lottery) {
		this.lottery = lottery;
	}

}
