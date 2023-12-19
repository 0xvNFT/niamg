package com.play.pay.goopago.result;

/**
 * @author zzn
 */
@SuppressWarnings("serial")
public class CollectionResult extends BaseResult {

	private String orderId;// 订单号
	private Long fee;// 手续费 分
	private Long amount;// 订单金额 分
	private String reference;// 付款参考 clabe/现金码
	private String identifier;// 标识符 claveRastreo
	private String url;// 支付链接
	private String barcodeUrl;// 条形码链接
	private String qrCodeUrl;// 二维码链接
	private Long expiresAt;// 1639535085 秒

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

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBarcodeUrl() {
		return barcodeUrl;
	}

	public void setBarcodeUrl(String barcodeUrl) {
		this.barcodeUrl = barcodeUrl;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public Long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}
}
