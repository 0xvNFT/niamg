package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "tron_trans_user")
public class TronTransUser {
	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "trans_from")
	private String transFrom;

	@Column(name = "trans_to")
	private String transTo;

	@Column(name = "trans_value")
	private Double transValue;

	@Column(name = "block")
	private Long block;

	@Column(name = "transaction_id")
	private String transactionID;

	/** 转账类型（1trx 2usdt） */
	@Column(name = "transaction_type")
	private Integer transactionType;

	/** 转账时间 */
	@Column(name = "transaction_timestamp")
	private Long transactionTimestamp;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "station_id")
	private Long stationId;

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransFrom() {
		return transFrom;
	}

	public void setTransFrom(String transFrom) {
		this.transFrom = transFrom;
	}

	public String getTransTo() {
		return transTo;
	}

	public void setTransTo(String transTo) {
		this.transTo = transTo;
	}

	public Double getTransValue() {
		return transValue;
	}

	public void setTransValue(Double transValue) {
		this.transValue = transValue;
	}

	public Long getBlock() {
		return block;
	}

	public void setBlock(Long block) {
		this.block = block;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Long getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestamp(Long transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}
}
