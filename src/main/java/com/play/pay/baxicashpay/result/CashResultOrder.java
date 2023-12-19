package com.play.pay.baxicashpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class CashResultOrder implements Serializable {
	private String msg;
	private String traceId;
	private String amount;

	private String code;

	private String qrcodeRaw;

	private String orderId;
	private String fee;

	private String sign;

	private String payUrl;
	private String merchantOrderId;
	private String payType;
	private String createTimeL;
	private String status;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQrcodeRaw() {
		return qrcodeRaw;
	}

	public void setQrcodeRaw(String qrcodeRaw) {
		this.qrcodeRaw = qrcodeRaw;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCreateTimeL() {
		return createTimeL;
	}

	public void setCreateTimeL(String createTimeL) {
		this.createTimeL = createTimeL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
