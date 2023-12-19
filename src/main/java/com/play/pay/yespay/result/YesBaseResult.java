package com.play.pay.yespay.result;

import java.io.Serializable;

public class YesBaseResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private String resMsg;
	private String resCode;
	private Integer errCode;
	private String errDes;

	/**
	 * 商户编号
	 * 必填
	 */
	private String userid;

	/**
	 * 商户订单号
	 */
	private String orderid;

	/**
	 * 随机字符串
	 */
	private String nonceStr;

	/**
	 * 签名
	 */
	private String sign;

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrDes() {
		return errDes;
	}

	public void setErrDes(String errDes) {
		this.errDes = errDes;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
