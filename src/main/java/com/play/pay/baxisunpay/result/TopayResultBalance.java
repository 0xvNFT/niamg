package com.play.pay.baxisunpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class TopayResultBalance implements Serializable {
	private String msg;
	private String code;
	private TopayResultBalanceData data;

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

	public TopayResultBalanceData getData() {
		return data;
	}

	public void setData(TopayResultBalanceData data) {
		this.data = data;
	}
}
