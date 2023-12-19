package com.play.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserRegisterBo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long stationId;
	private String username;
	private String pwd;
	private String realName;
	private String phone;
	private String qq;
	private String email;
	private String receiptPwd;
	private String facebook;
	private String wechat;
	private String line;
	private String promoCode;
	private String captcha;
	private Integer userType;
	private String agentName;
	private int terminal;

	private String remark;// 备注
	private Long degreeId;// 等级ID
	private Long groupId;// 组别ID

	private String agentPromoCode;

	private String proxyName;
	private Long proxyId;
	private Long recommendId;// 推荐会员ID
	private String recommendUsername;// 推荐会员账号
	// 代理返点
	private BigDecimal live;
	private BigDecimal egame;
	private BigDecimal chess;
	private BigDecimal esport;
	private BigDecimal sport;
	private BigDecimal fishing;
	private BigDecimal lottery;
	private boolean addFromUser;
	private String code;//注册短信/邮箱验证码
	private String neckName;//昵称
	private String uid;//用户ID
	private String userRegisterType; // 会员注册类型（本系统为null, 三方Facebook、Google等）
	private boolean checkRegConfig = true;//是否需要检查注册选项

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReceiptPwd() {
		return receiptPwd;
	}

	public void setReceiptPwd(String receiptPwd) {
		this.receiptPwd = receiptPwd;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public int getTerminal() {
		return terminal;
	}

	public void setTerminal(int terminal) {
		this.terminal = terminal;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public Long getProxyId() {
		return proxyId;
	}

	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPromoCode() {
		return agentPromoCode;
	}

	public void setAgentPromoCode(String agentPromoCode) {
		this.agentPromoCode = agentPromoCode;
	}

	public Long getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
	}

	public String getRecommendUsername() {
		return recommendUsername;
	}

	public void setRecommendUsername(String recommendUsername) {
		this.recommendUsername = recommendUsername;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public boolean isAddFromUser() {
		return addFromUser;
	}

	public void setAddFromUser(boolean addFromUser) {
		this.addFromUser = addFromUser;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNeckName() {
		return neckName;
	}

	public void setNeckName(String neckName) {
		this.neckName = neckName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUserRegisterType() {
		return userRegisterType;
	}

	public void setUserRegisterType(String userRegisterType) {
		this.userRegisterType = userRegisterType;
	}

	public boolean isCheckRegConfig() {
		return checkRegConfig;
	}

	public void setCheckRegConfig(boolean checkRegConfig) {
		this.checkRegConfig = checkRegConfig;
	}
}
