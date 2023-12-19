package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 热门游戏
 *
 * @author admin
 *
 */
@Table(name = "app_hot_game")
public class AppHotGame {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "name")
	private String name;
	/**
	 * 游戏code
	 */
	@Column(name = "code")
	private String code;
	/**
	 * 子游戏时对应的父游戏code(只有在子游戏时存在)
	 */
	@Column(name = "parent_code")
	private String parentCode;
	/**
	 * 三方游戏跳转链接
	 */
	@Column(name = "link")
	private String link;
	/**
	 * 三方游戏icon链接
	 */
	@Column(name = "icon")
	private String icon;

	@Column(name = "status")
	private Integer status;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义
	 */
	@Column(name = "type")
	private Integer type;

	private Integer isSubGame;//是否子游戏

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsSubGame() {
		return isSubGame;
	}

	public void setIsSubGame(Integer isSubGame) {
		this.isSubGame = isSubGame;
	}
}
