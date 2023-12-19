package com.play.model.so;

import java.math.BigDecimal;
import java.util.Date;

public class UserSo {

	public static final int DEPOSIT_STATUS_YES = 1;// 充值过
	public static final int DEPOSIT_STATUS_NO = 2;// 从未充值
	public static final int DEPOSIT_STATUS_FIRST = 3;// 首充

	public static final int DRAW_STATUS_YES = 1;// 提款过
	public static final int DRAW_STATUS_NO = 2;// 从未提款

	public static final int KEYWORD_REAL_NAME = 1;// 关键字真实姓名
	public static final int KEYWORD_PHONE = 2;// 关键字手机号
	public static final int KEYWORD_QQ = 3;// 关键字QQ
	public static final int KEYWORD_MAIL = 4;// 关键字邮箱
	public static final int KEYWORD_WEICHAT = 5;// 关键字微信
	public static final int KEYWORD_FACEBOOK = 6;// 关键字脸书
	public static final int KEYWORD_REGISTER_IP = 7;// 关键字注册IP
	public static final int KEYWORD_REGISTER_OS = 8;// 关键字注册来源
	public static final int KEYWORD_REMARK = 9;// 关键字会员备注

	private Long stationId;
	private String usernameLike;// 账号(模糊)
	private String username;// 账号(精确)
	private String degreeIds;// 等级
	private String startTime;// 注册开始时间
	private String endTime;// 注册结束时间
	private Integer userType;// 类型
	private String agentName;// 所属代理商名称
	private Integer depositStatus;// 充值情况
	private Integer drawStatus;// 提款情况
	private BigDecimal minMoney;// 余额大于
	private Integer level;
	private Integer notLoginDay;// 未登录天数
	private String lastLoginIp;// 最后登录IP
	private Integer keywordType;// 搜索关键字类型
	private String keyword;// 搜索关键字
	private String proxyPromoCode;// 代理推广码
	private String proxyName;// 所属代理
	private String recommendName;// 推荐人
	private String groupIds;// 组别id

	private String sortName;
	private String sortOrder;

	private Date begin;// 注册开始时间
	private Date end;// 注册结束时间
	private Date lastLoginTime;// 最后登陆时间

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getUsernameLike() {
		return usernameLike;
	}

	public void setUsernameLike(String usernameLike) {
		this.usernameLike = usernameLike;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(Integer keywordType) {
		this.keywordType = keywordType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getProxyPromoCode() {
		return proxyPromoCode;
	}

	public void setProxyPromoCode(String proxyPromoCode) {
		this.proxyPromoCode = proxyPromoCode;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getRecommendName() {
		return recommendName;
	}

	public void setRecommendName(String recommendName) {
		this.recommendName = recommendName;
	}

	public Integer getDepositStatus() {
		return depositStatus;
	}

	public void setDepositStatus(Integer depositStatus) {
		this.depositStatus = depositStatus;
	}

	public Integer getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(Integer drawStatus) {
		this.drawStatus = drawStatus;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public Integer getNotLoginDay() {
		return notLoginDay;
	}

	public void setNotLoginDay(Integer notLoginDay) {
		this.notLoginDay = notLoginDay;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

}
