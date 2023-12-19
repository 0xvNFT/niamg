package com.play.pay.baxiuzpay.params;

import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiuzpay.util.DateUtil;

import java.util.Date;

public class UzpayParamPay implements ITopayParamBase {
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String sign_type;
	/**
	 * 商户唯一订单，最多32位
	 * <p>
	 * 必填
	 */
	private String mch_id;
	/**
	 * 描述，不超过200字符
	 * <p>
	 * 必填
	 */
	private String mch_transferId;
	/**
	 * 支付金额，保留两位小数，最小0.01
	 * <p>
	 * 必填
	 */
	private String transfer_amount;
	/**
	 * 用户名，必须英文、葡萄牙文
	 * <p>
	 * 必填
	 */
	private String apply_date;
	/**
	 * 传用户身份证，长度固定11位数字
	 * <p>
	 * 必填
	 */
	private String bank_code;
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
	private String receive_name;
	/**
	 * 配合【pix_type】
	 * <p>
	 * 必填
	 */
	private String receive_account;
	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String back_url;
	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public UzpayParamPay() {
		this.bank_code = "IDPT0001";
		this.setRemark("12340433333");
	this.sign_type = "MD5";
	this.apply_date = DateUtil.date2String(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS);
	}

	public JSONObject toMap() {
		return JSONObject.parseObject(JSONObject.toJSONString(this));
	}

	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}


	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getMch_transferId() {
		return mch_transferId;
	}

	public void setMch_transferId(String mch_transferId) {
		this.mch_transferId = mch_transferId;
	}

	public String getTransfer_amount() {
		return transfer_amount;
	}

	public void setTransfer_amount(String transfer_amount) {
		this.transfer_amount = transfer_amount;
	}

	public String getApply_date() {
		return apply_date;
	}

	public void setApply_date(String apply_date) {
		this.apply_date = apply_date;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getReceive_name() {
		return receive_name;
	}

	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}

	public String getReceive_account() {
		return receive_account;
	}

	public void setReceive_account(String receive_account) {
		this.receive_account = receive_account;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}
}
