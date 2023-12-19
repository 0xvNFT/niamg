package com.play.pay.baxitrustpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TrustpayResultBalance implements Serializable {
	private String msg;
	private String code;
	private TrustpayResultBalanceData data;

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

	public TrustpayResultBalanceData getData() {
		return data;
	}

	public void setData(TrustpayResultBalanceData data) {
		this.data = data;
	}

	public static class TrustpayResultBalanceData implements Serializable {
		private String balance;
		private String waitSettle;
		private String frozen;
		private String payoutBalance;

		public String getBalance() {
			return balance;
		}

		public void setBalance(String balance) {
			this.balance = balance;
		}

		public String getWaitSettle() {
			return waitSettle;
		}

		public void setWaitSettle(String waitSettle) {
			this.waitSettle = waitSettle;
		}

		public String getFrozen() {
			return frozen;
		}

		public void setFrozen(String frozen) {
			this.frozen = frozen;
		}

		public String getPayoutBalance() {
			return payoutBalance;
		}

		public void setPayoutBalance(String payoutBalance) {
			this.payoutBalance = payoutBalance;
		}
	}
}
