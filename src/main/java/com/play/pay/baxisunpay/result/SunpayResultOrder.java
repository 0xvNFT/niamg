package com.play.pay.baxisunpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class SunpayResultOrder implements Serializable {
	private String message;
	private Integer code;
	private boolean is_success;




	private SunpayQueryData data;

	public void setData(SunpayQueryData data) {
		this.data = data;
	}

	public boolean isIs_success() {
		return is_success;
	}

	public void setIs_success(boolean is_success) {
		this.is_success = is_success;
	}

	public SunpayQueryData getData() {
		return data;
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
