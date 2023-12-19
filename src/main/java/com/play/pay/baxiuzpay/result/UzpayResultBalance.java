package com.play.pay.baxiuzpay.result;

import java.io.Serializable;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class UzpayResultBalance implements Serializable {
	private String respCode;
	private String tradeResult;   //0 申请成功 1 转账成功 2 转账失败 3 转账拒绝 4 处理中


	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getTradeResult() {
		return tradeResult;
	}

	public void setTradeResult(String tradeResult) {
		this.tradeResult = tradeResult;
	}
}
