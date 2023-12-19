package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点域名信息
 *
 * @author admin
 *
 */
@Table(name = "station_domain")
public class StationDomain {

	public static final int TYPE_COMMON = 1;// 通用域名
	public static final int TYPE_ADMIN = 2;// 站点后台域名
	public static final int TYPE_AGENT = 3;// 代理商后台域名
	public static final int TYPE_FRONT = 4;// 前台域名
	public static final int TYPE_APP = 5;// APP域名

	public static final int MODE_SAFE = 1;// 安全模式
	public static final int MODE_COMMON = 2;// 普通模式

	public static final int PLATFORM_PARTNER = 1;// 合作商后台域名
	public static final int PLATFORM_STATION = 2;// 站点域名

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 合作商id
	 */
	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	@Column(name = "agent_name")
	private String agentName;
	@Column(name = "agent_promo_code")
	private String agentPromoCode;
	/**
	 * 域名
	 */
	@Column(name = "name")
	private String name;

	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 域名类型(1-站点通用，2-站点前台,3-站点后台,4-合作商后台)
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 前台默认主页，不填时默认访问index.do
	 */
	@Column(name = "default_home")
	private String defaultHome;
	/**
	 * 所属代理id
	 */
	@Column(name = "proxy_id")
	private Long proxyId;
	/**
	 * 所属代理账号
	 */
	@Column(name = "proxy_username")
	private String proxyUsername;
	/**
	 * 状态 1-停用 2-启用
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 线路检测开关
	 */
	@Column(name = "switch_domain")
	private Integer switchDomain;
	/**
	 * 序号
	 */
	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 备注内容
	 */
	@Column(name = "remark")
	private String remark;
	/**
	 * 获取ip的模式，1=安全模式，2=普通模式
	 */
	@Column(name = "ip_mode")
	private Integer ipMode;
	/**
	 * 平台类型，1=合作商后台域名，2=站点域名
	 */
	@Column(name = "platform")
	private Integer platform;

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

	public String getAgentPromoCode() {
		return agentPromoCode;
	}

	public void setAgentPromoCode(String agentPromoCode) {
		this.agentPromoCode = agentPromoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDefaultHome() {
		return defaultHome;
	}

	public void setDefaultHome(String defaultHome) {
		this.defaultHome = defaultHome;
	}

	public Long getProxyId() {
		return proxyId;
	}

	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSwitchDomain() {
		return switchDomain;
	}

	public void setSwitchDomain(Integer switchDomain) {
		this.switchDomain = switchDomain;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIpMode() {
		return ipMode;
	}

	public void setIpMode(Integer ipMode) {
		this.ipMode = ipMode;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

}
