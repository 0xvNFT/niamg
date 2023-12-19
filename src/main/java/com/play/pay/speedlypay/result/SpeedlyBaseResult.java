package com.play.pay.speedlypay.result;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpeedlyBaseResult implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 国家代码，如巴西为BR
	 * 必填
	 */
	private String country;

	/**
	 * 货币代码，如巴西为BRL
	 * 必填
	 */
	private String currency;

	/**
	 * 商户订单编号
	 * 必填
	 */
	private String order_id;

	/**
	 * 固定2位小数点的浮点数金额，如：10.00
	 */
	private BigDecimal amount;

	/**
	 * 时间戳 毫秒级UTC时间戳（13位）
	 */
	private Long timestamp;

	/**
	 * 支付结果状态
	 * 代收订单状态码：1下单 2等待支付 3交易中 4交易失败 5交易取消 6交易超时 100交易成功
	 * 代付订单状态码：1下单 2待审核 3出款中 4出款失败 5退回 100出款成功
	 */
	private String status;

	/**
	 * 支付结果状态码
	 * 代收订单状态码：1下单 2等待支付 3交易中 4交易失败 5交易取消 6交易超时 100交易成功
	 * 代付订单状态码：1下单 2待审核 3出款中 4出款失败 5退回 100出款成功
	 */
	private Integer status_code;

	/**
	 * 支付结果状态描述
	 */
	private String status_detail;

	/**
	 * MD5签名，32位大写字母
	 * 必填
	 */
	private String signature;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatus_code() {
		return status_code;
	}

	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}

	public String getStatus_detail() {
		return status_detail;
	}

	public void setStatus_detail(String status_detail) {
		this.status_detail = status_detail;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
