package com.play.model;


import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
/**
 * 站点前台IP白名单列表 
 *
 * @author admin
 *
 */
@Table(name = "agent_white_ip")
public class AgentWhiteIp {
	
	@Column(name = "id", primarykey = true)
	private Long id;
	
	@Column(name = "station_id")
	private Long stationId;
	
	@Column(name = "agent_id")
	private Long agentId;
	
	@Column(name = "ip")
	private String ip;
	
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 2:白名单 ，1：黑名单
	 */
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "remark")
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
