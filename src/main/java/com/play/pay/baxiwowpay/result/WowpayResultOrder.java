package com.play.pay.baxiwowpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class WowpayResultOrder implements Serializable {
	private String respCode;
	private String orderNo;
	private String merTransferId;//商家转账单号

	private String payInfo;

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMerTransferId() {
		return merTransferId;
	}

	public void setMerTransferId(String merTransferId) {
		this.merTransferId = merTransferId;
	}
}
