package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点银行信息表
 *
 * @author admin
 *
 */
@Table(name = "station_bank")
public class StationBank {

	public static final String otherCode = "other";

	@Column(name = "id", primarykey = true)
	private Long id;
	/**
	 * 银行名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 编号
	 */
	@Column(name = "code")
	private String code;
	/**
	 * 排序序号，大的排前面
	 */
	@Column(name = "sort_no")
	private Integer sortNo;

	@Column(name = "station_id")
	private Long stationId;

	@Column(name = "create_time")
	private Date createTime;

	String bankCode;//固定银行编码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
}
