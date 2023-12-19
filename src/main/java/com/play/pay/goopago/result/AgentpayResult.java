package com.play.pay.goopago.result;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class AgentpayResult extends BaseResult {

	private String orderId;// 订单号
	private Long fee;// 手续费 分
	private Long status;// 状态 1:处理中

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
