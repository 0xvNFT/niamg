package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 游戏大开关
 *
 * @author admin
 *
 */
@Table(name = "third_game")
public class ThirdGame {
	/**
	 * 站点id
	 */
	@Column(name = "station_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long stationId;
	@Column(name = "partner_id")
	private Long partnerId;

	/**
	 * 真人视讯，1=关，2=开
	 */
	@Column(name = "live")
	private Integer live;
	/**
	 * 电子
	 */
	@Column(name = "egame")
	private Integer egame;
	/**
	 * 棋牌
	 */
	@Column(name = "chess")
	private Integer chess;
	/**
	 * 捕鱼
	 */
	@Column(name = "fishing")
	private Integer fishing;
	/**
	 * 电竞
	 */
	@Column(name = "esport")
	private Integer esport;
	/**
	 * 体育
	 */
	@Column(name = "sport")
	private Integer sport;
	/**
	 * 彩票
	 */
	@Column(name = "lottery")
	private Integer lottery;

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

	public Integer getLive() {
		return live;
	}

	public void setLive(Integer live) {
		this.live = live;
	}

	public Integer getEgame() {
		return egame;
	}

	public void setEgame(Integer egame) {
		this.egame = egame;
	}

	public Integer getChess() {
		return chess;
	}

	public void setChess(Integer chess) {
		this.chess = chess;
	}

	public Integer getFishing() {
		return fishing;
	}

	public void setFishing(Integer fishing) {
		this.fishing = fishing;
	}

	public Integer getEsport() {
		return esport;
	}

	public void setEsport(Integer esport) {
		this.esport = esport;
	}

	public Integer getSport() {
		return sport;
	}

	public void setSport(Integer sport) {
		this.sport = sport;
	}

	public Integer getLottery() {
		return lottery;
	}

	public void setLottery(Integer lottery) {
		this.lottery = lottery;
	}

}
