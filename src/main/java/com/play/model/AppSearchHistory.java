package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 历史搜索
 *
 * @author admin
 *
 */
@Table(name = "app_search_history")
public class AppSearchHistory {

	@Column(name = "id",primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username")
	private String username;
	/**
	 * 搜索关键字
	 */
	@Column(name = "keyword")
	private String keyword;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 搜索次数
	 */
	@Column(name = "count")
	private Long count;

	private String userName;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
