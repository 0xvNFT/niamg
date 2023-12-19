package com.play.model.vo;

import java.math.BigDecimal;
import java.util.Date;

public class UserVo {

	private Long id;
	/**
	 * 合作商ID
	 */
	private Long partnerId;
	/**
	 * 站点ID
	 */
	private Long stationId;
	/**
	 * 代理商名称
	 */
	private String agentName;
	/**
	 * 上级代理ID
	 */
	private Long proxyId;
	/**
	 * 上级代理账号
	 */
	private String proxyName;
	/**
	 * 所有代理上级，以逗号隔开(,1,2,3,)
	 */
	private String parentIds;
	/**
	 * 所有上级代理账号，以逗号隔开(,a,b,c,)
	 */
	private String parentNames;
	/**
	 * 推荐会员ID
	 */
	private Long recommendId;
	/**
	 * 推荐会员账号
	 */
	private String recommendUsername;
	/**
	 * 会员等级ID
	 */
	private Long degreeId;
	private String degreeName;
	/**
	 * 会员组别ID
	 */
	private Long groupId;
	/**
	 * 账号
	 */
	private String username;

	private String registerOs;

	public String getRegisterOs() {
		return registerOs;
	}

	public void setRegisterOs(String registerOs) {
		this.registerOs = registerOs;
	}

	/**
	 * 用户类型(UserTypeEnum的值)
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createDatetime;
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	/**
	 * 创建者账号
	 */
	private String createUsername;
	/**
	 * 账号状态 1:禁用 2:启用
	 */
	private Integer status;
	/**
	 * 备注内容
	 */
	private String remark;
	/**
	 * 2=锁定会员等级，1=不锁定
	 */
	private Integer lockDegree;
	/**
	 * 会员层级
	 */
	private Integer level;
	/**
	 * 在线告警
	 */
	private Long onlineWarn;
	/**
	 * 最后一次修改密码时间
	 */
	private Date updatePwdDatetime;
	/**
	 * 账户余额状态，1=冻结，2=正常
	 */
	private Integer moneyStatus;
	/**
	 * 最后登录时间
	 */
	private Date lastLoginDatetime;
	/**
	 * 未登录天数描述
	 */
	private String unLoginDays;
	/**
	 * 在线状态：1=下线，2=在线
	 */
	private Integer onlineStatus;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	/**
	 * 1=电脑端，2=手机web端，3=原生app端
	 */
	private Integer terminal;

	private String registerIp;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 提款密码
	 */
	private String receiptPwd;
	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * QQ号码
	 */
	private String qq;
	/**
	 * 微信号
	 */
	private String wechat;
	/**
	 * 脸书
	 */
	private String facebook;

	private Long underNum;
	private BigDecimal money;
	private Long score;

	// 返点
	private BigDecimal live;

	private BigDecimal egame;

	private BigDecimal chess;

	private BigDecimal esport;

	private BigDecimal sport;

	private BigDecimal fishing;

	private BigDecimal lottery;

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

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Long getProxyId() {
		return proxyId;
	}

	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getParentNames() {
		return parentNames;
	}

	public void setParentNames(String parentNames) {
		this.parentNames = parentNames;
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

	public Long getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getLockDegree() {
		return lockDegree;
	}

	public void setLockDegree(Integer lockDegree) {
		this.lockDegree = lockDegree;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getOnlineWarn() {
		return onlineWarn;
	}

	public void setOnlineWarn(Long onlineWarn) {
		this.onlineWarn = onlineWarn;
	}

	public Date getUpdatePwdDatetime() {
		return updatePwdDatetime;
	}

	public void setUpdatePwdDatetime(Date updatePwdDatetime) {
		this.updatePwdDatetime = updatePwdDatetime;
	}

	public Integer getMoneyStatus() {
		return moneyStatus;
	}

	public void setMoneyStatus(Integer moneyStatus) {
		this.moneyStatus = moneyStatus;
	}

	public Date getLastLoginDatetime() {
		return lastLoginDatetime;
	}

	public void setLastLoginDatetime(Date lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

	public String getUnLoginDays() {
		return unLoginDays;
	}

	public void setUnLoginDays(String unLoginDays) {
		this.unLoginDays = unLoginDays;
	}

	public Integer getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getTerminal() {
		return terminal;
	}

	public void setTerminal(Integer terminal) {
		this.terminal = terminal;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
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

	public Long getUnderNum() {
		return underNum;
	}

	public void setUnderNum(Long underNum) {
		this.underNum = underNum;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
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
