package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 代理推会员模式的推广链接表
 *
 * @author admin
 *
 */
@Table(name = "station_promotion")
public class StationPromotion {

	public static final int ACCESS_PAGE_TYPE_REGISTER = 1;// 注册页面
	public static final int ACCESS_PAGE_TYPE_INDEX = 2;// 首页
	public static final int ACCESS_PAGE_TYPE_ACTIVITY = 3;// 优惠活动页面

	public static final int MODE_PROXY = 1;// 代理推广代理或会员
	public static final int MODE_MEMBER = 2;// 会员推会员

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 会员ID
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 推广会员账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 链接注册进来的会员类型UserTypeEnum.PROXY 和 UserTypeEnum.USER
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 推广码类型，1=代理推广代理或会员，2=会员推会员
	 */
	@Column(name = "mode")
	private Integer mode;
	/**
	 * 推广码
	 */
	@Column(name = "code")
	private String code;
	/**
	 * 前端域名
	 */
	@Column(name = "domain")
	private String domain;
	/**
	 * 注册人数
	 */
	@Column(name = "register_num")
	private Long registerNum;
	/**
	 * 访问人数
	 */
	@Column(name = "access_num")
	private Long accessNum;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 链接访问页面 1：注册页面，2：首页,3:优惠活动，4：游戏大厅
	 */
	@Column(name = "access_page")
	private Integer accessPage;

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

	/**
	 * 链接地址（不存库）
	 */
	private String linkUrl;

	/**
	 * 加密链接地址(不存库)
	 */
	private String linkUrlEn;

	/**
	 * 时区创建时间（根据站点币种设置时区）
	 */
	private String tzCreateTime;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Long getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(Long registerNum) {
		this.registerNum = registerNum;
	}

	public Long getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(Long accessNum) {
		this.accessNum = accessNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAccessPage() {
		return accessPage;
	}

	public void setAccessPage(Integer accessPage) {
		this.accessPage = accessPage;
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

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkUrlEn() {
		return linkUrlEn;
	}

	public void setLinkUrlEn(String linkUrlEn) {
		this.linkUrlEn = linkUrlEn;
	}

	public String getTzCreateTime() {
		return tzCreateTime;
	}

	public void setTzCreateTime(String tzCreateTime) {
		this.tzCreateTime = tzCreateTime;
	}
}
