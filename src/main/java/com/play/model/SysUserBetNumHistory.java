package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员打码量变动记录
 *
 * @author admin
 *
 */
@Table(name = "sys_user_bet_num_history")
public class SysUserBetNumHistory {

	public static final Integer TYPE_DRAWNEED_CLEAR = 1;//清除出款所需打码量

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;

	@Column(name = "station_id")
	private Long stationId;

	/**
	 * 代理商名称
	 */
	@Column(name = "agent_name")
	private String agentName;

	@Column(name = "proxy_id")
	private Long proxyId;

	@Column(name = "proxy_name")
	private String proxyName;
	/**
	 * 用户父级
	 */
	@Column(name = "parent_ids")
	private String parentIds;
	/**
	 * 账号id
	 */
	@Column(name = "user_id")
	private Long userId;
	/**
	 * 用户账号
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 变动的打码量
	 */
	@Column(name = "bet_num")
	private BigDecimal betNum;
	/**
	 * 变动前打码量
	 */
	@Column(name = "before_num")
	private BigDecimal beforeNum;
	/**
	 * 变动后打码量
	 */
	@Column(name = "after_num")
	private BigDecimal afterNum;
	/**
	 * 变动类型
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 描述
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 操作人id
	 */
	@Column(name = "operator_id")
	private Long operatorId;
	/**
	 * 操作人账号
	 */
	@Column(name = "operator_name")
	private String operatorName;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 订单号
	 */
	@Column(name = "order_id")
	private String orderId;
	/**
	 * 提款所需打码量
	 */
	@Column(name = "draw_need")
	private BigDecimal drawNeed;
	/**
	 * 变动前提款所需打码量
	 */
	@Column(name = "before_draw_need")
	private BigDecimal beforeDrawNeed;
	/**
	 * 变动后提款所需打码量
	 */
	@Column(name = "after_draw_need")
	private BigDecimal afterDrawNeed;
	/**
	 * 投注时间
	 */
	@Column(name = "biz_datetime")
	private Date bizDatetime;

	private String createDatetimeStr;

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

	public BigDecimal getBetNum() {
		return betNum;
	}

	public void setBetNum(BigDecimal betNum) {
		this.betNum = betNum;
	}

	public BigDecimal getBeforeNum() {
		return beforeNum;
	}

	public void setBeforeNum(BigDecimal beforeNum) {
		this.beforeNum = beforeNum;
	}

	public BigDecimal getAfterNum() {
		return afterNum;
	}

	public void setAfterNum(BigDecimal afterNum) {
		this.afterNum = afterNum;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public BigDecimal getDrawNeed() {
		return drawNeed;
	}

	public void setDrawNeed(BigDecimal drawNeed) {
		this.drawNeed = drawNeed;
	}

	public BigDecimal getBeforeDrawNeed() {
		return beforeDrawNeed;
	}

	public void setBeforeDrawNeed(BigDecimal beforeDrawNeed) {
		this.beforeDrawNeed = beforeDrawNeed;
	}

	public BigDecimal getAfterDrawNeed() {
		return afterDrawNeed;
	}

	public void setAfterDrawNeed(BigDecimal afterDrawNeed) {
		this.afterDrawNeed = afterDrawNeed;
	}

	public Date getBizDatetime() {
		return bizDatetime;
	}

	public void setBizDatetime(Date bizDatetime) {
		this.bizDatetime = bizDatetime;
	}

	public String getCreateDatetimeStr() {
		return createDatetimeStr;
	}

	public void setCreateDatetimeStr(String createDatetimeStr) {
		this.createDatetimeStr = createDatetimeStr;
	}
}
