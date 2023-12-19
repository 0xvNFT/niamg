package com.play.pay.goopago;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.goopago.param.AgentPayParam;
import com.play.pay.goopago.param.BaseParam;
import com.play.pay.goopago.param.CollectionParam;
import com.play.pay.goopago.param.query.QueryParam;
import com.play.pay.goopago.result.AgentpayResult;
import com.play.pay.goopago.result.CollectionResult;
import com.play.pay.goopago.result.query.QueryBalanceResult;
import com.play.pay.goopago.result.query.QueryOrderResult;

public class GoopagoApiTest {
	private final static Logger log = LoggerFactory.getLogger(GoopagoApiTest.class);
	private static final Long mchId = 20000070L;
	private static final String key = "DGM3OFOLWS8DPPEEWEV73O0KP3WLOYL1XAVRIIEKAWWHUJ2I099ASVUS7V3DG0UFM14I8HTZR1YOQHOFJ0JX98J3PUCVXDSB5XCRBME9MXHBYF8MQN0L7DLDHGCK2KSZ";
	private static final String appId = "94e7e4810c0a491d93fc5edc2e3c30ef";
	private static final String queryUrl = "http://47.88.18.111:18090/common";
	private static final String payUrl = "http://47.88.18.111:13020/api";

	public static void queryBalance() {
		BaseParam param = new BaseParam();
		param.setAppId(appId);
		QueryBalanceResult result = new GoopagoApi().queryBalance(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void agentpay() {
		AgentPayParam param = new AgentPayParam();
		param.setAmount(1000L);
		param.setAccountNo("+5511986220287");
		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		param.setAccountType(4);
		param.setRemark("TSET");
		param.setMchOrderNo(System.currentTimeMillis() + "");
		param.setNotifyUrl("http://webhook.site/9b6f9c61-9e30-4c8c-a24e-76b1ff1e517c");
		AgentpayResult result = new GoopagoApi().agentpay(mchId, key, payUrl, param);
		System.out.println(JSONObject.toJSONString(result, true));
	}

	public static void collectionTest() {
		CollectionParam param = new CollectionParam();
		param.setPayType(140);
		param.setAmount(500L);
		param.setEmail("123456789@qq.com");
		param.setBody("test");
		param.setName("zhang san");
		param.setExpireTime(60 * 60L);
		param.setMchOrderNo(System.currentTimeMillis() + "");
		param.setNotifyUrl("http://webhook.site/76dfb23d-c161-4a11-b62c-27d44dacb7d2");
		// 非必填mPIX二维码支付页面的返回地址
		param.setSuccessUrl("http://www.baidu.com");
		CollectionResult result = new GoopagoApi().collection(mchId, key, payUrl, param);
		System.out.println(JSONObject.toJSONString(result, true));
	}

	public static void queryCollection() {
		QueryParam param = new QueryParam();
		param.setMchOrderNo("1673847801232");
		QueryOrderResult result = new GoopagoApi().queryCollection(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryAgentpay() {
		QueryParam param = new QueryParam();
		param.setMchOrderNo("P1075770841330876416");
		QueryOrderResult result = new GoopagoApi().queryAgentpay(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		GoopagoApiTest.queryBalance();
//		GoopagoApiTest.agentpay();
//		GoopagoApiTest.collectionTest();
		GoopagoApiTest.queryCollection();
//		GoopagoApiTest.queryAgentpay();
	}
}
