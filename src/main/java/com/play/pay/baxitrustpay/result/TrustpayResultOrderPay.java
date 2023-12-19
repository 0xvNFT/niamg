package com.play.pay.baxitrustpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TrustpayResultOrderPay implements Serializable {
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
		/** 实际充值金额，一般情况下与 Amount 一致 */
		private String realAmount;
		/** 状态 ： 0: “发起”, 1: “交易中”, 2: “成功”, 3: “失败”, 4: “失效”, 5: “退款” */
		private String status;

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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
