package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 存储会员基本信息
 *
 * @author admin
 *
 */
@Table(name = "sys_user_info")
public class SysUserInfo {

	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 真实姓名
	 */
	@Column(name = "real_name")
	private String realName;

	@Column(name = "neck_name")
	private String neckName;
	/**
	 * 提款密码
	 */
	@Column(name = "receipt_pwd")
	private String receiptPwd;
	/**
	 * 手机
	 */
	@Column(name = "phone")
	private String phone;
	/**
	 * 电子邮箱
	 */
	@Column(name = "email")
	private String email;
	/**
	 * QQ号码
	 */
	@Column(name = "qq")
	private String qq;
	/**
	 * 微信号
	 */
	@Column(name = "wechat")
	private String wechat;
	/**
	 * 脸书
	 */
	@Column(name = "facebook")
	private String facebook;
	/**
	 * line
	 */
	@Column(name = "line")
	private String line;
	/**
	 * 省份
	 */
	@Column(name = "province")
	private String province;
	/**
	 * 城市
	 */
	@Column(name = "city")
	private String city;
	/**
	 * 新增或修改银行卡时间
	 */
	@Column(name = "modify_bankcard_time")
	private Date modifyBankcardTime;

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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getReceiptPwd() {
		return receiptPwd;
	}

	public void setReceiptPwd(String receiptPwd) {
		this.receiptPwd = receiptPwd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getModifyBankcardTime() {
		return modifyBankcardTime;
	}

	public void setModifyBankcardTime(Date modifyBankcardTime) {
		this.modifyBankcardTime = modifyBankcardTime;
	}

	public String getNeckName() {
		return neckName;
	}

	public void setNeckName(String neckName) {
		this.neckName = neckName;
	}
}
