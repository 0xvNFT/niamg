package com.play.pay.goopago.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class BaseResult implements Serializable {

	private String resMsg;// ; //错误信息
	private String resCode;// ;//SUCCESS/FAIL
	private Integer errCode;// ;//异常编码 10001
	private String errDes;// ;//异常说明 缺少参数
	private Long mchId;// 商户ID
	private String nonceStr;// 随机字符串
	private String sign;// 签名

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

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
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
