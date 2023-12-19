package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 会员头像记录 
 *
 * @author admin
 *
 */
@Table(name = "sys_user_avatar_record")
public class SysUserAvatarRecord {
	public static final int AVATAR_NOW = 1;// 当前头像
	public static final int AVATAR_HIS = 2;// 历史头像
	
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
	 * 头像ID
	 */
	@Column(name = "avatar_id")
	private Long avatarId;
	/**
	 * 头像url
	 */
	@Column(name = "url")
	private String url;
	/**
	 * 头像类型:1.当前头像 2.历史头像
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;

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

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

}
