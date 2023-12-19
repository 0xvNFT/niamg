package com.play.pay.baxiokkpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class OkkpayResultOrder implements Serializable {
	private String message;
	private Integer code;
	private boolean success;

	private String pageInfo;
	private String data;

	public String getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(String pageInfo) {
		this.pageInfo = pageInfo;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	//	private TopayResultOrderData data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
