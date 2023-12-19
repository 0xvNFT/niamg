package com.play.pay.goopago.param;

/**
 * @author zzn
 */
public class CollectionParam extends BaseParam {
	/**
	 * 回调地址
	 */
	private String notifyUrl;
	/**
	 * 支付类型
	 */
	private Integer payType;
	/**
	 * 支付金额
	 */
	private Long amount;
	/**
	 * 最大金额支付金额
	 */
	private Long maxAmount;
	/**
	 * 最小金额支付金额
	 */
	private Long minAmount;
	/**
	 * 过期时间 单位:秒 默认验证范围 300 - 3153600000
	 */
	private Long expireTime;
	/**
	 * 单次付款
	 */
	private Boolean single;
	/**
	 * 货币单位,mxn
	 */
	private String currency;
	/**
	 * 商品标题
	 */
	private String subject;
	/**
	 * 商品描述信息
	 */
	private String body;
	/**
	 * 顾客姓名
	 */
	private String name;
	/**
	 * 顾客邮件
	 */
	private String email;
	/**
	 * 顾客电话
	 */
	private String phone;
	/**
	 * 支付失败跳转页面
	 */
	private String failUrl;
	/**
	 * 支付成功跳转页面
	 */
	private String successUrl;

	/**
	 * 渠道付款信息 json 字符串
	 */
	private String channelInfo;

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Long getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Long minAmount) {
		this.minAmount = minAmount;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public Boolean getSingle() {
		return single;
	}

	public void setSingle(Boolean single) {
		this.single = single;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFailUrl() {
		return failUrl;
	}

	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getChannelInfo() {
		return channelInfo;
	}

	public void setChannelInfo(String channelInfo) {
		this.channelInfo = channelInfo;
	}
}
