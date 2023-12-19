package com.play.pay.baxitrustpay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxitrustpay.para.TrustpayParamCollect;
import com.play.pay.baxitrustpay.para.TrustpayParamPay;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryBalance;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryOrder;
import com.play.pay.baxitrustpay.result.TrustpayResultBalance;
import com.play.pay.baxitrustpay.result.TrustpayResultCollect;
import com.play.pay.baxitrustpay.result.TrustpayResultOrderCollect;
import com.play.pay.baxitrustpay.result.TrustpayResultOrderPay;
import com.play.pay.baxitrustpay.result.TrustpayResultPay;

public class TrustpayPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(TrustpayPayApiTest.class);
	private static final String merchantID = "6f93711ea1a145f8a1b3a5685cc57f1e";
	private static final String key = "BVE7S1RO9P6MHKTDFVO45GG4MPXK2QCVLKPZ1UAVID7QK304U4ZYNQ84QD3WV4WD";
	private static final String queryUrl = "https://api.tustpay.biz/";


	public static void collectionTest() {
		TrustpayParamCollect param = new TrustpayParamCollect();
		param.setMerchantId(merchantID);
		param.setOrderNo("1223333222q331");
		param.setAmount("100.02");
		param.setNotifyUrl("http://www.baidu.com");
		System.out.println(param.toJsonString());
		TrustpayResultCollect result = new TrustpayPayApi().collection(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	/**
	 * 收款
	 */
	public static void payTest() {
		TrustpayParamPay param = new TrustpayParamPay();
		param.setMerchantId(merchantID);
		param.setPixType("CPF");
		param.setOrderNo("11223212");
		param.setAmount("10.02");
		param.setCert("12345678901");
		param.setAccountNum("12345678901");
		param.setNotifyUrl("http://www.baidu.com");
		TrustpayResultPay result = new TrustpayPayApi().agentpay(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	/**
	 * {"code":"200","data":{"amount":"10.02","channel":"CPF","currency":"BRL","fee":"0.0000","merchantId":"6f93711ea1a145f8a1b3a5685cc57f1e","orderNo":"11223262","payNo":"20230828060653payout6x6y7t"},"msg":"Success"}
	 */
	public static void payOutTest() {
		TrustpayParamPay param = new TrustpayParamPay();
		param.setMerchantId(merchantID);
		param.setPixType("CPF");
		param.setOrderNo("11223262");
		param.setAmount("10.02");
		param.setCert("12345678901");
		param.setAccountNum("12345678901");
		param.setNotifyUrl("http://www.baidu.com");
		TrustpayResultPay result = new TrustpayPayApi().agentpay(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}


	/**
	 * 查询订单会报订单不存在
	 */
	public static void queryOrderCollectStatusTest() {
		TrustpayParamQueryOrder param = new TrustpayParamQueryOrder();
		param.setMerchantId(merchantID);
		param.setPayNo("20230828062157payinnrzt00");
		TrustpayResultOrderCollect result = new TrustpayPayApi().queryCollectStatus(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	/**
	 * 代付查询 success
	 */
	public static void queryOrderPayStatusTest() {
		TrustpayParamQueryOrder param = new TrustpayParamQueryOrder();
		param.setMerchantId(merchantID);
		param.setPayNo("20230828055302payout2whcea");
		TrustpayResultOrderPay result = new TrustpayPayApi().queryPayStatus(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	/**
	 * 查询余额 success
	 */
	public static void queryBalance() {
		/**
		 *
		 */
		TrustpayParamQueryBalance param = new TrustpayParamQueryBalance();
		param.setMerchantId(merchantID);
		TrustpayResultBalance result = new TrustpayPayApi().queryBalance(key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {

		/*
		* {"code":"200","data":{"amount":"10.02","channel":"CPF","currency":"BRL","fee":"0.0000","merchantId":"6f93711ea1a145f8a1b3a5685cc57f1e","orderNo":"11223212","payNo":"20230828061355payout3ipskw"},"msg":"Success"}
		* */
	//	TrustpayPayApiTest.payTest();
		/**
		 * {"code":"200","data":{"amount":"100.02","currency":"BRL","fee":"0.60012","jumpUrl":"https://h5.tustpay.biz/payment/pay/rpopay/bfd4fb734ebe441aaa370f6f8a410d16","merchantId":"6f93711ea1a145f8a1b3a5685cc57f1e","orderNo":"1223333222331","payNo":"20230828062157payinnrzt00","realAmount":"100.02","rqcode":""},"msg":"Success"}
		 */
	//	TrustpayPayApiTest.collectionTest();
		TrustpayPayApiTest.queryOrderPayStatusTest(); //查不到订单
	//	TrustpayPayApiTest.queryBalance();
	//	TrustpayPayApiTest.payOutTest();
	//	TrustpayPayApiTest.queryOrderPayStatusTest();
	}
}
