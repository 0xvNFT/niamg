package com.play.model;

import com.play.core.UserTypeEnum;
import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 站点注册页面有哪些字段
 *
 * @author admin
 *
 */
@Table(name = "station_register_config")
public class StationRegisterConfig {

	public static final int platform_member = UserTypeEnum.MEMBER.getType();// 会员注册选项
	public static final int platform_proxy = UserTypeEnum.PROXY.getType();// 代理注册选项

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 字段名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 作用平台（1会员、2代理）
	 */
	@Column(name = "platform")
	private Integer platform;
	/**
	 * 对应用户的信息表的字段
	 */
	@Column(name = "ele_name")
	private String eleName;
	/**
	 * 是否展示（1否2是）
	 */
	@Column(name = "show")
	private Integer show;
	/**
	 * 是否需要验证（1否2是）
	 */
	@Column(name = "validate")
	private Integer validate;
	/**
	 * 是否必填（1否2是）
	 */
	@Column(name = "required")
	private Integer required;
	/**
	 * 是否唯一
	 */
	@Column(name = "uniqueness")
	private Integer uniqueness;
	/**
	 * 序号
	 */
	@Column(name = "sort_no")
	private Long sortNo;
	/**
	 * 字段类型，1=text,2=password
	 */
	@Column(name = "ele_type")
	private Integer eleType;
	/**
	 * 注册提示语
	 */
	@Column(name = "tips")
	private String tips;

	/**
	 * 多语言的code
	 */
	@Column(name = "code")
	private String code;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getEleName() {
		return eleName;
	}

	public void setEleName(String eleName) {
		this.eleName = eleName;
	}

	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}

	public Integer getValidate() {
		return validate;
	}

	public void setValidate(Integer validate) {
		this.validate = validate;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}

	public Integer getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(Integer uniqueness) {
		this.uniqueness = uniqueness;
	}

	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getEleType() {
		return eleType;
	}

	public void setEleType(Integer eleType) {
		this.eleType = eleType;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
