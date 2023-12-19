package com.play.pay.damson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.play.pay.damson.params.BaseParam;
import com.play.pay.damson.params.CollectionParam;
import com.play.pay.damson.params.PayParams;
import com.play.pay.damson.params.query.QueryParam;
import com.play.pay.damson.result.CollectionResult;
import com.play.pay.damson.result.PayResult;
import com.play.pay.damson.result.query.QueryBalanceResult;
import com.play.pay.damson.result.query.QueryOrderResult;

public class DamsonPayApiTest {
	private final static Logger log = LoggerFactory.getLogger(DamsonPayApiTest.class);
	private static final Long mchId = 10000001L;
	private static final String key = "EFE4910497DD9C92302259F1B478204DFDD1FBC05D97F9E2C3CF7C8065A799C557FED460EB9A9832FF0CABDF04196FE9D9AC9338DE4780F1ED599349B963E9A6";
	private static final String appId = "36bb63c0efef0985ea2580139841559f";
	private static final String queryUrl = "http://staging-gw.damsonpay.com/api";
	private static final String payUrl = "http://staging-gw.damsonpay.com/api";

	public static void queryBalance() {
		BaseParam param = new BaseParam();
		param.setAppId(appId);
		QueryBalanceResult result = new DamsonPayApi().queryBalance(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void agentpay() {
		PayParams param = new PayParams();
		param.setAmount(1000L);
		param.setAppId(appId);
		param.setAccountNo("+5511986220287");
		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		param.setAccountType(4);
		param.setRemark("TSET");
		param.setMchOrderNo(System.currentTimeMillis() + "");
		param.setNotifyUrl("https://webhook.site/c30f84f0-29da-45cd-ad33-cad5cc3c649f");
		// tetst param
		param.setAccountName("test");
		param.setEmail("email@example.com");
		param.setPhone("+5511986220287");
		PayResult result = new DamsonPayApi().agentpay(mchId, key, payUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void collectionTest() {
		CollectionParam param = new CollectionParam();
		param.setAppId(appId);
		param.setPayType(140);
		param.setAmount(2000L);
		param.setEmail("c30f84f0-29da-45cd-ad33-cad5cc3c649f@email.webhook.site");
		param.setBody("test");
		param.setName("zhang san");
		param.setExpireTime(60 * 60L);
		param.setMchOrderNo(System.currentTimeMillis() + "");
		param.setNotifyUrl("https://webhook.site/c30f84f0-29da-45cd-ad33-cad5cc3c649f");
		param.setSuccessUrl("http://www.baidu.com");
		CollectionResult result = new DamsonPayApi().collection(mchId, key, payUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryCollection() {
		QueryParam param = new QueryParam();
		param.setMchOrderNo("1673847801232");
		// param.setOrderNo("C1077040245410627584");
		QueryOrderResult result = new DamsonPayApi().queryCollection(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryAgentpay() {
		QueryParam param = new QueryParam();
		// param.setMchOrderNo("1671019267041");
		param.setMchOrderNo("D23062822242000007");
		QueryOrderResult result = new DamsonPayApi().queryAgentpay(mchId, key, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) {
//		DamsonPayApiTest.queryBalance();
//		DamsonPayApiTest.agentpay();
//		DamsonPayApiTest.collectionTest();
//		DamsonPayApiTest.queryCollection();
		DamsonPayApiTest.collectionTest();
	}
}
