package com.play.pay.baxiwowpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxiwowpay.params.TopayParamQueryOrder;
import com.play.pay.baxiwowpay.params.WowpayParamCollect;
import com.play.pay.baxiwowpay.params.WowpayParamPay;
import com.play.pay.baxiwowpay.params.UzpayParamQueryBalance;
import com.play.pay.baxiwowpay.result.WowpayResultBalance;
import com.play.pay.baxiwowpay.result.WowpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WowpayPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(WowpayPayApiTest.class);
	private static final String privateKey = "TZLMQ1QWJCUSFLH02LAYRZBJ1WK7IHSG";//支付秘钥
	private static final String publicKey = "MZBG89MDIBEDWJOJQYEZVSNP8EEVMSPM";//代付秘钥
	private static final String queryUrl = "https://pay6de1c7.wowpayglb.com";

	/**
	 * 测试通过
	 */
	public static void collectionTest() {
		WowpayParamCollect param = new WowpayParamCollect();
		param.setMch_id("222887002");
		param.setNotify_url("http://www.baidu.com");
		param.setMch_order_no("2sdfsdf484bdderthr");
		param.setTrade_amount("1000");
		WowpayResultOrder result = new WowpayPayApi().collection(privateKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		WowpayParamPay param = new WowpayParamPay();
		param.setMch_id("100999002");
		param.setMch_transferId("488488992222kkkjj");
		param.setTransfer_amount("100.25");
		param.setReceive_name("matrtv");
		param.setRemark("12340433333");
		param.setReceive_account("12345678901010101");
		param.setBack_url("https://www.baidu.com");
		WowpayResultOrder result = new WowpayPayApi().agentpay(publicKey, publicKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		TopayParamQueryOrder param = new TopayParamQueryOrder();
		param.setMerchant_no("M23082209342195");
		param.setOut_trade_no("PY1234565");
		WowpayResultOrder result = new WowpayPayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryBalance() {
		UzpayParamQueryBalance param = new UzpayParamQueryBalance();
		param.setMch_id("100999002");
		param.setMch_transferId("488488992222kkkjj");
		WowpayResultBalance result = new WowpayPayApi().queryBalance(publicKey, publicKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		WowpayPayApiTest.collectionTest();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
