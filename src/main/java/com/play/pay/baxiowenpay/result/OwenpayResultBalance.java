package com.play.pay.baxiowenpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class OwenpayResultBalance implements Serializable {
	private String resultMsg;
	private String resultCode;

	private String remitResult;

	public String getRemitResult() {
		return remitResult;
	}

	public void setRemitResult(String remitResult) {
		this.remitResult = remitResult;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}
