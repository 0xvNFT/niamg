package com.play.pay.baxitrustpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TrustpayResultCollect implements Serializable {
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
		/** 实际充值金额，一般情况下与 Amount 一致 */
		private String realAmount;
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
		/** 代收二维码内容（注意：有个别通道此字段为空） */
		private String rqcode;
		/** 扩展字段 JSON 字符串，不特殊情况，则此字段为空 */
		private String extended;
		/** 跳转地址,或者到这个地址，可以直接跳转 */
		private String jumpUrl;

		public String getRealAmount() {
			return realAmount;
		}

		public void setRealAmount(String realAmount) {
			this.realAmount = realAmount;
		}

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

		public String getRqcode() {
			return rqcode;
		}

		public void setRqcode(String rqcode) {
			this.rqcode = rqcode;
		}

		public String getExtended() {
			return extended;
		}

		public void setExtended(String extended) {
			this.extended = extended;
		}

		public String getJumpUrl() {
			return jumpUrl;
		}

		public void setJumpUrl(String jumpUrl) {
			this.jumpUrl = jumpUrl;
		}
	}
}
