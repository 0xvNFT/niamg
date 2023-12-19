package com.play.model;

import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点代理商信息表
 *
 * @author admin
 *
 */
@Table(name = "agent")
public class Agent {

	public static final int ACCESS_PAGE_TYPE_REGISTER = 1;//注册页面
	public static final int ACCESS_PAGE_TYPE_INDEX = 2;//首页
	public static final int ACCESS_PAGE_TYPE_ACTIVITY = 3;//优惠活动

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 所属站点
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 代理商名称，作为推广码
	 */
	@Column(name = "name")
	private String name;

	@Column(name = "create_datetime")
	private Date createDatetime;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "promo_code")
	private String promoCode;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	@Column(name = "status")
	private Integer status;
	/**
	 * 链接访问页面 1:注册页面 2：首页 3:优惠活动
	 */
	@Column(name = "access_page")
	private Integer accessPage;

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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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

	public Integer getAccessPage() {
		return accessPage;
	}

	public void setAccessPage(Integer accessPage) {
		this.accessPage = accessPage;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

}
