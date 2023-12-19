package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;

/**
 * 会员层级信息
 *
 * @author admin
 *
 */
@Table(name = "sys_user_degree")
public class SysUserDegree {

	public static int TYPE_DEPOSIT = 1;
	public static int TYPE_BETNUM = 2;
	public static int TYPE_DEPSOIT_AND_BETNUM = 3;

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
	 * 成员数量
	 */
	@Column(name = "member_count")
	private Long memberCount;
	/**
	 * 充值金额
	 */
	@Column(name = "deposit_money")
	private BigDecimal depositMoney;
	/**
	 * 状态(1禁用，2启用)
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 等级名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 是否默认未分层(1否，2是)
	 */
	@Column(name = "original")
	private Integer original;
	/**
	 * 用户
	 */
	@Column(name = "icon")
	private String icon;
	/**
	 * 等级升级奖金
	 */
	@Column(name = "upgrade_money")
	private BigDecimal upgradeMoney;
	/**
	 * 升级是否发送站内信通知,1=否，2=是
	 */
	@Column(name = "upgrade_send_msg")
	private Integer upgradeSendMsg;
	/**
	 * 升级赠送金额打码量倍数
	 */
	@Column(name = "bet_rate")
	private BigDecimal betRate;
	/**
	 * 打码量
	 */
	@Column(name = "bet_num")
	private BigDecimal betNum;
	/**
	 * 1=使用充值金额，2使用打码量，3=充值与打码量并存
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 跨级奖励金额
	 */
	@Column(name = "skip_money")
	private BigDecimal skipMoney;

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

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public BigDecimal getDepositMoney() {
		return depositMoney;
	}

	public void setDepositMoney(BigDecimal depositMoney) {
		this.depositMoney = depositMoney;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOriginal() {
		return original;
	}

	public void setOriginal(Integer original) {
		this.original = original;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public BigDecimal getUpgradeMoney() {
		return upgradeMoney;
	}

	public void setUpgradeMoney(BigDecimal upgradeMoney) {
		this.upgradeMoney = upgradeMoney;
	}

	public Integer getUpgradeSendMsg() {
		return upgradeSendMsg;
	}

	public void setUpgradeSendMsg(Integer upgradeSendMsg) {
		this.upgradeSendMsg = upgradeSendMsg;
	}

	public BigDecimal getBetRate() {
		return betRate;
	}

	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}

	public BigDecimal getBetNum() {
		return betNum;
	}

	public void setBetNum(BigDecimal betNum) {
		this.betNum = betNum;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getSkipMoney() {
		return skipMoney;
	}

	public void setSkipMoney(BigDecimal skipMoney) {
		this.skipMoney = skipMoney;
	}

}
