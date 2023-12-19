package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 用户足迹
 *
 * @author admin
 *
 */
@Table(name = "app_game_foot")
public class AppGameFoot {
	public static final Integer CAIPIAO = 1;
    public static final Integer ZHENREN = 2;
    public static final Integer DIANZI = 3;
    public static final Integer SPORT = 4;
    public static final Integer DIANJING = 5;
    public static final Integer BUYU = 6;
    public static final Integer QIPAI = 7;
    public static final Integer SELF_DEFINE = 8;
    public static final Integer HONGBAO = 9;

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 游戏名称
	 */
	@Column(name = "game_name")
	private String gameName;
	/**
	 * 游戏code
	 */
	@Column(name = "game_code")
	private String gameCode;
	/**
	 * 子游戏时对应的父游戏code(只有在子游戏时存在)
	 */
	@Column(name = "parent_game_code")
	private String parentGameCode;
	/**
	 * 游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义 9--其他
	 */
	@Column(name = "game_type")
	private Integer gameType;
	/**
	 * 点击跳转链接 gameType 为自定义时使用
	 */
	@Column(name = "custom_link")
	private String customLink;
	/**
	 * 图标icon gameType 为自定义时使用
	 */
	@Column(name = "custom_icon")
	private String customIcon;
	/**
	 * 访问量
	 */
	@Column(name = "visit_num")
	private Long visitNum;
	/**
	 * 是否某类大游戏下的子游戏 1--是 0--否
	 */
	@Column(name = "is_sub_game")
	private Integer isSubGame;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 状态  2--开启 1--关闭
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 足迹类型 1--租户配置的足迹 0--用户足迹
	 */
	@Column(name = "type")
	private Integer type;

	Integer lotType;

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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getParentGameCode() {
		return parentGameCode;
	}

	public void setParentGameCode(String parentGameCode) {
		this.parentGameCode = parentGameCode;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public String getCustomLink() {
		return customLink;
	}

	public void setCustomLink(String customLink) {
		this.customLink = customLink;
	}

	public String getCustomIcon() {
		return customIcon;
	}

	public void setCustomIcon(String customIcon) {
		this.customIcon = customIcon;
	}

	public Long getVisitNum() {
		return visitNum;
	}

	public void setVisitNum(Long visitNum) {
		this.visitNum = visitNum;
	}

	public Integer getIsSubGame() {
		return isSubGame;
	}

	public void setIsSubGame(Integer isSubGame) {
		this.isSubGame = isSubGame;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLotType() {
		return lotType;
	}

	public void setLotType(Integer lotType) {
		this.lotType = lotType;
	}
}
