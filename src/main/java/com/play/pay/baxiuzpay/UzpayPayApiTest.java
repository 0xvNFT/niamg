package com.play.pay.baxiuzpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxiuzpay.params.UzpayParamCollect;
import com.play.pay.baxiuzpay.params.UzpayParamPay;
import com.play.pay.baxiuzpay.params.UzpayParamQueryBalance;
import com.play.pay.baxiuzpay.params.TopayParamQueryOrder;
import com.play.pay.baxiuzpay.result.UzpayResultBalance;
import com.play.pay.baxiuzpay.result.UzpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UzpayPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(UzpayPayApiTest.class);
	private static final String privateKey = "imzdixz6zub3jc2whtcw47wxhsluuy5y";//支付秘钥
	private static final String publicKey = "4AZQ1RE1ZPNJTHO0ZG3T8M6MKV16II0U";//代付秘钥
	private static final String queryUrl = "https://payment.dzxum.com";

	/**
	 * 测试通过
	 */
	public static void collectionTest() {
		UzpayParamCollect param = new UzpayParamCollect();
		param.setMch_id("100999002");
		param.setNotify_url("http://www.baidu.com");
		param.setMch_order_no("2sdfsdf484bdderthr");
		param.setTrade_amount("1000");
		UzpayResultOrder result = new UzpayPayApi().collection(privateKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		UzpayParamPay param = new UzpayParamPay();
		param.setMch_id("100999002");
		param.setMch_transferId("488488992222kkkjj");
		param.setTransfer_amount("100.25");
		param.setReceive_name("matrtv");
		param.setRemark("12340433333");
		param.setReceive_account("12345678901010101");
		param.setBack_url("https://www.baidu.com");
		UzpayResultOrder result = new UzpayPayApi().agentpay(publicKey, publicKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		TopayParamQueryOrder param = new TopayParamQueryOrder();
		param.setMerchant_no("M23082209342195");
		param.setOut_trade_no("PY1234565");
		UzpayResultOrder result = new UzpayPayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryBalance() {
		UzpayParamQueryBalance param = new UzpayParamQueryBalance();
		param.setMch_id("100999002");
		param.setMch_transferId("488488992222kkkjj");
		UzpayResultBalance result = new UzpayPayApi().queryBalance(publicKey, publicKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		UzpayPayApiTest.queryBalance();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
