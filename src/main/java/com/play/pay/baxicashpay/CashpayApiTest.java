package com.play.pay.baxicashpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxicashpay.params.CashpayParamCollect;
import com.play.pay.baxicashpay.params.CashpayParamPay;
import com.play.pay.baxicashpay.params.CashpayParamQueryOrder;
import com.play.pay.baxicashpay.result.CashResultOrder;
import com.play.pay.baxicashpay.util.CashRSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashpayApiTest {
	private final static Logger log = LoggerFactory.getLogger(CashpayApiTest.class);
	private static final String privateKey = "sk5f9j9u0irkj9ulgd504ozgjpk7vkfqa8";
	private static final String publicKey = "4496cfaf892d2b28956594833b2a8ab4";
	private static final String queryUrl = "https://pix.cashpag.com";

	public static void collectionTest() {
		CashpayParamCollect param = new CashpayParamCollect();
		String headerAuthorization = CashRSAEncrypt.build("726422194448","sk5f9j9u0irkj9ulgd504ozgjpk7vkfqa8");
		param.setAmount("100");
		param.setNotifyUrl("https://www.baidu.com");
		param.setMerchantOrderId("sdfsd3a23fgdgsgfdfdfgsg");
		CashResultOrder result = new CashpayApi().collection(publicKey, privateKey, queryUrl, param,headerAuthorization);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		CashpayParamPay param = new CashpayParamPay();
		param.setAccountType("CPF");
		param.setAccountNum("32562523890");
		param.setAmount("100");
		param.setMerchantOrderId("ffa34237gf34gv43");
		param.setNotifyUrl("https://www.baidu.com");
		param.setCustomerCert("32562523890");
		String headerAuthorization = CashRSAEncrypt.build("726422194448","sk5f9j9u0irkj9ulgd504ozgjpk7vkfqa8");
		CashResultOrder result = new CashpayApi().agentpay(publicKey, privateKey, queryUrl, param,headerAuthorization);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		String headerAuthorization = CashRSAEncrypt.build("726422194448","sk5f9j9u0irkj9ulgd504ozgjpk7vkfqa8");
		CashpayParamQueryOrder param = new CashpayParamQueryOrder();
		param.setMerchantOrderId("sdfsd3a23fggsgfdfdfgsg");
		CashResultOrder result = new CashpayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param,headerAuthorization);
		log.error(JSON.toJSONString(result));
	}


	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		CashpayApiTest.collectionTest();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
