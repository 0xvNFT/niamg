package com.play.pay.yespay.param;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class YesBaseParam implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 商户编号
	 * 必填
	 */
	private String userid;

	/**
	 * 商户订单号
	 * 必填
	 */
	private String orderid;

	/**
	 * 金额(元)
	 */
	private BigDecimal amount;

	/**
	 * 随机字符串
	 * 必填
	 */
	private String noncestr;

	/**
	 * 访问票据
	 */
	private String ticket;

	/**
	 * 时间戳
	 */
	private String timestamp;

	/**
	 * 签名
	 * 必填
	 */
	private String sign;

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
