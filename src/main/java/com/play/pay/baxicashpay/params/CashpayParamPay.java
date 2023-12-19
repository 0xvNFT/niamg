package com.play.pay.baxicashpay.params;

import com.alibaba.fastjson.JSONObject;

public class CashpayParamPay implements CashpayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String accountType;
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String accountNum;
	/**
	 * 描述，不超过200字符
	 * <p>
	 * 必填
	 */
	private String amount;
	/**
	 * 支付金额，保留两位小数，最小0.01
	 * <p>
	 * 必填
	 */
	private String merchantOrderId;
	/**
	 * 用户名，必须英文、葡萄牙文
	 * <p>
	 * 必填
	 */
	private String transferReceipt;
	/**
	 * 传用户身份证，长度固定11位数字
	 * <p>
	 * 必填
	 */
	private String customerCert;
	/**
	 * PIX 类型，
	 * <p>
	 * CPF 用户身份证，长度11位数字
	 * <p>
	 * CNPJ 实体登记号，长度14位数字
	 * <p>
	 * EMAIL 用户邮箱，合法邮箱格式
	 * <p>
	 * PHONE 用户手机号，长度11位数字
	 * <p>
	 * RANDOMKEY PIX key，长度36位uuid格式
	 * <p>
	 * EVP PIX key，长度36位uuid格式
	 * <p>
	 * 必填
	 */
	private String customerName;
	/**
	 * 配合【pix_type】
	 * <p>
	 * 必填
	 */
	private String notifyUrl;


	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}

	public CashpayParamPay() {
		this.setTransferReceipt("no");
		this.setCustomerName("HE YONG");
		this.setCustomerCert("32562523890");
	}

	@Override
	public void setSign(String sign) {

	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getTransferReceipt() {
		return transferReceipt;
	}

	public void setTransferReceipt(String transferReceipt) {
		this.transferReceipt = transferReceipt;
	}

	public String getCustomerCert() {
		return customerCert;
	}

	public void setCustomerCert(String customerCert) {
		this.customerCert = customerCert;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

}
