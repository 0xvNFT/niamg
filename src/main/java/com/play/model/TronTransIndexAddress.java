package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "tron_trans_index_address")
public class TronTransIndexAddress {
	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "last_timestamp")
	private Date lastTimestamp;

	@Column(name = "tron_link_addr")
	private String tronLinkAddr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(Date lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public String getTronLinkAddr() {
		return tronLinkAddr;
	}

	public void setTronLinkAddr(String tronLinkAddr) {
		this.tronLinkAddr = tronLinkAddr;
	}
}
