package com.play.pay.baxiowenpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxiowenpay.params.OwenpayParamCollect;
import com.play.pay.baxiowenpay.params.OwenpayParamPay;
import com.play.pay.baxiowenpay.params.OwenpayParamQueryOrder;
import com.play.pay.baxiowenpay.result.OwenResultOrder;

import com.play.pay.baxiowenpay.result.OwenpayResultBalance;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import com.play.pay.baxiwowpay.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OwenpayPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(OwenpayPayApiTest.class);
	private static final String privateKey = "AA26A20C7C332D9E74ED6BE6B6E06754";
	private static final String publicKey = "065286801107508841103316";
	private static final String queryUrl = "http://api.owenpay.com:8008";

	public static void collectionTest() {
		OwenpayParamCollect param = new OwenpayParamCollect();
		param.setAppID("OW230909235705371");
		param.setOutTradeNo("CO123dssfdgfgdddg4564");
		param.setTotalAmount("1025");
		param.setNotifyUrl("http://www.baidu.com");
		OwenResultOrder result = new OwenpayPayApi().collection(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		OwenpayParamPay param = new OwenpayParamPay(publicKey);
		param.setAppID("OW230909235705371");
		param.setOutTradeNo("gfggfh4556ddghgfhfg346445645");
		param.setBankAcctNo(OwenRSAEncrypt.Encrypt3DES("+5511113333333",publicKey));
		param.setTotalAmount(OwenRSAEncrypt.Encrypt3DES("1342",publicKey));
		param.setBankAcctName(OwenRSAEncrypt.Encrypt3DES("xiaoming",publicKey));
		param.setAccPhone(OwenRSAEncrypt.Encrypt3DES("+552112345678",publicKey));
		param.setNotifyUrl("https://www.baidu.com");
		OwenResultOrder result = new OwenpayPayApi().agentpay(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		OwenpayParamQueryOrder param = new OwenpayParamQueryOrder();
		param.setRemitDate(DateUtil.getToday());
		param.setOutTradeNo("gfggfh4556ddghgfhfg346445645");
		param.setAppID("OW230909235705371");
		OwenpayResultBalance result = new OwenpayPayApi().queryBalance(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		OwenpayPayApiTest.queryOrderStatusTest();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
