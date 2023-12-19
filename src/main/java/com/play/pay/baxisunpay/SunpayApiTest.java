package com.play.pay.baxisunpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxisunpay.params.SunPayParamQueryOrder;
import com.play.pay.baxisunpay.params.SunpayParamCollect;
import com.play.pay.baxisunpay.params.SunpayParamPay;
import com.play.pay.baxisunpay.result.SunPayqueryOrderResultStatus;
import com.play.pay.baxisunpay.result.SunpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SunpayApiTest {
	private final static Logger log = LoggerFactory.getLogger(SunpayApiTest.class);
	private static final String privateKey = "08db9d77-606c-4d4c-84b9-59f8c2a11d74";
	private static final String publicKey = "a842bf9e97b3790c707f07fffc47b7267b6377f6b9897acc7b47e8d4012c7f04";
	private static final String queryUrl = "https://sandbox-oapi.sunpay.pro";

	public static void collectionTest() throws Exception {
		SunpayParamCollect param = new SunpayParamCollect();
		param.setAmount("10.32");
		param.setOut_order_no("M2d30aa84456f256");
		param.setWebhook_url("https://www.baidu.com");
		SunpayResultOrder result = new SunpayApi().collection(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		SunpayParamPay param = new SunpayParamPay();
		param.setOut_order_no("5564aa6dg34df5634534");
		param.setAmount("10.21");
		param.setWebhook_url("https://www.baidu.com");
		SunpayResultOrder result = new SunpayApi().agentpay(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		SunPayParamQueryOrder param = new SunPayParamQueryOrder();
		SunPayqueryOrderResultStatus result = new SunpayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryBalance() {
		SunPayParamQueryOrder param = new SunPayParamQueryOrder();
		param.setOrderNo("W202309201704408354136416256");
		SunPayqueryOrderResultStatus result = new SunpayApi().queryBalance(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) throws Exception {
		//SunpayApiTest.collectionTest();
		SunpayApiTest.queryBalance();

	}
}
