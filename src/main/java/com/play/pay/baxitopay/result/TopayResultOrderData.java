package com.play.pay.baxitopay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TopayResultOrderData implements Serializable {
	private String merchant_no;
	private String fee_amount;
	private String out_trade_no;
	private String pay_amount;
	private String trade_no;
	private String payment_link;
	private String status;

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getFee_amount() {
		return fee_amount;
	}

	public void setFee_amount(String fee_amount) {
		this.fee_amount = fee_amount;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getPayment_link() {
		return payment_link;
	}

	public void setPayment_link(String payment_link) {
		this.payment_link = payment_link;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
