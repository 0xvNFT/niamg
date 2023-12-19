package com.play.pay.baxiuzpay.params;

import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiuzpay.util.DateUtil;

import java.util.Date;

public class UzpayParamCollect implements ITopayParamBase {
	/**
	 * 	版本号
	 * <p>
	 * 必填
	 */
	private String version;
	/**
	 * 商户号
	 * <p>
	 * 必填
	 */
	private String mch_id;
	/**
	 * 异步通知地址
	 * <p>
	 * 必填
	 */
	private String notify_url;
	/**
	 * 商家订单号
	 * <p>
	 * 必填
	 */
	private String mch_order_no;
	/**
	 * 支付类型
	 * <p>
	 * 必填
	 */
	private String pay_type;
	/**
	 * 异步回调地址，如果不传，会读取商户后台设置好的
	 * <p>
	 * 必填
	 */
	private String trade_amount;

	/**
	 * 订单时间
	 * 时间格式yyyy-MM-dd HH:mm:ss
	 * <p>
	 * 必填
	 */
	private String order_date;
/*	*
	 * 银行代码
	 * 网银通道必填，其他类型一定不能填该参数
	 * <p>
	 * 必填
	private String bank_code;*/
	/**
	 商品名称
	 * <p>
	 * 必填
	 */
	private String goods_name;

	/**
	 * 签名
	 * <p>
	 * 必填
	 */
	private String sign;
	/**
	 * 同步跳转地址
	 * <p>
	 * 必填
	 */
	/*private String page_url;*/
	/**
	 * 签名类型 默认MDS
	 * <p>
	 * 必填
	 */
	private String sign_type;

/*	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}*/

	public UzpayParamCollect() {
		this.pay_type= "122";
		this.sign_type = "MD5";
		this.version = "1.0";
		this.goods_name= "recharge";
		this.order_date = DateUtil.date2String(new Date(),DateUtil.YYYY_MM_DD_HH_MM_SS);
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;

	}

	public String getSign_type() {
		return sign_type;
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

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getMch_order_no() {
		return mch_order_no;
	}

	public void setMch_order_no(String mch_order_no) {
		this.mch_order_no = mch_order_no;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getTrade_amount() {
		return trade_amount;
	}

	public void setTrade_amount(String trade_amount) {
		this.trade_amount = trade_amount;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
}
