package com.play.pay.baxitrustpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TrustpayResultPay implements Serializable {
	private String msg;
	private String code;
	private TrustpayResultOrderData data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TrustpayResultOrderData getData() {
		return data;
	}

	public void setData(TrustpayResultOrderData data) {
		this.data = data;
	}

	public static class TrustpayResultOrderData implements Serializable {
		/** 平台订单号 记录一下，方便后续查账 */
		private String payNo;
		/** 发起的金额 */
		private String amount;
		/** 商户的订单号码 */
		private String orderNo;
		/** 商户号 */
		private String merchantId;
		/** 手续费 */
		private String fee;
		/** 币种 */
		private String currency;
		/** 扩展字段 JSON 字符串，不特殊情况，则此字段为空 */
		private String channel;

		public String getPayNo() {
			return payNo;
		}

		public void setPayNo(String payNo) {
			this.payNo = payNo;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getFee() {
			return fee;
		}

		public void setFee(String fee) {
			this.fee = fee;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
	}
}
