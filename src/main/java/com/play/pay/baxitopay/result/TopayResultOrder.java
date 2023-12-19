package com.play.pay.baxitopay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TopayResultOrder implements Serializable {
	private String msg;
	private String code;
	private TopayResultOrderData data;

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

	public TopayResultOrderData getData() {
		return data;
	}

	public void setData(TopayResultOrderData data) {
		this.data = data;
	}
}
