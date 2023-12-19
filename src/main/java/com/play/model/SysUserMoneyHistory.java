package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员金额变动表
 *
 * @author admin
 *
 */
@Table(name = "sys_user_money_history")
public class SysUserMoneyHistory {

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;
	@Column(name = "agent_name")
	private String agentName;

	@Column(name = "parent_ids")
	private String parentIds;
	/**
	 * 推荐者id
	 */
	@Column(name = "recommend_id")
	private Long recommendId;
	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username")
	private String username;
	/**
	 * 变动金额
	 */
	@Column(name = "money")
	private BigDecimal money;
	/**
	 * 变动前金额
	 */
	@Column(name = "before_money")
	private BigDecimal beforeMoney;
	/**
	 * 变动后金额
	 */
	@Column(name = "after_money")
	private BigDecimal afterMoney;
	/**
	 * 导致变动的类型
	 */
	@Column(name = "type")
	private Integer type;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 对应订单id
	 */
	@Column(name = "order_id")
	private String orderId;
	/**
	 * 业务时间
	 */
	@Column(name = "biz_datetime")
	private Date bizDatetime;
	/**
	 * 描述
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 后台备注
	 */
	@Column(name = "bg_remark")
	private String bgRemark;
	/**
	 * 操作者用户id
	 */
	@Column(name = "operator_id")
	private Long operatorId;
	/**
	 * 操作者账号
	 */
	@Column(name = "operator_name")
	private String operatorName;

	/**
	 * 类型名称
	 */
	private String typeCn;

	private String createDatetimeStr;

	/**
	 * true收入 false支出
	 */
	private boolean add;

	public String getCreateDatetimeStr() {
		return createDatetimeStr;
	}

	public void setCreateDatetimeStr(String createDatetimeStr) {
		this.createDatetimeStr = createDatetimeStr;
	}

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

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getBeforeMoney() {
		return beforeMoney;
	}

	public void setBeforeMoney(BigDecimal beforeMoney) {
		this.beforeMoney = beforeMoney;
	}

	public BigDecimal getAfterMoney() {
		return afterMoney;
	}

	public void setAfterMoney(BigDecimal afterMoney) {
		this.afterMoney = afterMoney;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getBizDatetime() {
		return bizDatetime;
	}

	public void setBizDatetime(Date bizDatetime) {
		this.bizDatetime = bizDatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBgRemark() {
		return bgRemark;
	}

	public void setBgRemark(String bgRemark) {
		this.bgRemark = bgRemark;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTypeCn() {
		return typeCn;
	}

	public void setTypeCn(String typeCn) {
		this.typeCn = typeCn;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

}
