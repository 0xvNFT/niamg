package com.play.model.vo;

public class ApiInfoVo2 {
	private Long id;
	/**
	 * api的代理名称
	 */
	private String name;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 状态，1=禁用，2=启用
	 */
	private Integer status;
	/**
	 * 会员账号前缀，自动赋值给sys_station表
	 */
	private String accountPref;
	/**
	 * api总额度
	 */
	private Long quota;
	/**
	 * 剩余额度
	 */
	private Long remainQuota;

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAccountPref() {
		return accountPref;
	}

	public void setAccountPref(String accountPref) {
		this.accountPref = accountPref;
	}

	public Long getQuota() {
		return quota;
	}

	public void setQuota(Long quota) {
		this.quota = quota;
	}

	public Long getRemainQuota() {
		return remainQuota;
	}

	public void setRemainQuota(Long remainQuota) {
		this.remainQuota = remainQuota;
	}

}
