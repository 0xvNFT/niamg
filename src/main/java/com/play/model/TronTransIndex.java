package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "tron_trans_index")
public class TronTransIndex {
	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "last_timestamp")
	private Date lastTimestamp;

	@Column(name = "last_transaction_id")
	private String lastTransactionID;

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

	public String getLastTransactionID() {
		return lastTransactionID;
	}

	public void setLastTransactionID(String lastTransactionID) {
		this.lastTransactionID = lastTransactionID;
	}
}
