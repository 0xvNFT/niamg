package com.play.model;

import java.io.Serializable;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

@SuppressWarnings("serial")
@Table(name = "sys_user_footprint_games")
public class SysUserFootprintGames implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义
	 */
	@Column(name = "game_type")
	private Integer gameType;
	/**
	 * 平台（ag bbin等）
	 */
	@Column(name = "platform")
	private String platform;
	/**
	 * 游戏代码
	 */
	@Column(name = "game_code")
	private String gameCode;

	/**
	 * 是否某类大游戏下的子游戏 1--是 0--否
	 */
	@Column(name = "is_sub_game")
	private Integer isSubGame;

	/**
	 * 登录游戏次数
	 */
	@Column(name = "login_times")
	private Integer loginTimes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public Integer getIsSubGame() {
		return isSubGame;
	}

	public void setIsSubGame(Integer isSubGame) {
		this.isSubGame = isSubGame;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@Override
	public String toString() {
		return "SysUserFootprintGames{" +
				"id=" + id +
				", stationId=" + stationId +
				", userId=" + userId +
				", gameType='" + gameType + '\'' +
				", platform='" + platform + '\'' +
				", gameCode='" + gameCode + '\'' +
				", isSubGame=" + isSubGame +
				", loginTimes=" + loginTimes +
				'}';
	}
}
