package com.play.pay.baxiokkpay;

import com.alibaba.fastjson.JSON;
import com.play.pay.baxiokkpay.params.OkkpayParamCollect;
import com.play.pay.baxiokkpay.params.OkkpayParamPay;
import com.play.pay.baxiokkpay.params.OkkPayParamQueryOrder;
import com.play.pay.baxiokkpay.result.OkkPayqueryOrderResultStatus;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkkpayApiTest {
	private final static Logger log = LoggerFactory.getLogger(OkkpayApiTest.class);
	private static final String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCAQrNCaEfox1m9gak44Yg4F1hvx8NI+oddpr17jJKAqeLnv+vzAbFiaqNXw4qbhq+1ee5sg5/S1MMHDgj/BOBAWsQyxAqBkcTqudJSEhkqeG6vVXbhREpZZWMzjNRViX4GaMySK8GKqjMEbBAy54JfBgnNSxTbN+mRMsKPsZqpLPkgC82kyxG7SGMEeqWVy97lB7PxKhGyBaxm/t+Fn4pxatY12Y8I8Q6skjwbQfNKREUnBQp2FewY3rXKDQkfJPsx13IQsMCbrv2CbUJNpBXns2aGgf3S0vrlvdHDZ88SrVXzOWqOy/59W/9FL7xZGnBgJ85/yeiGUCk0y3Dge+2bAgMBAAECggEAGx9/zwREaKp3hMa9pWS0LMBrGEBTQMSSCbDwEwd4F7NVY5LHb70k6N4KKs4UKq2BzMu7KsFu8+PAVmaWam96iMFpCh9A5YOQ895V5rsjDUMojPVyzVJmsp7pKoaWhHwt5WcWE7cjF5LEdSPxDQK+JCAfxIUjJPSe9PCaCk8RmMK4FnLqIv8v+6kOVZUViKqg30P/BCrWYioYvtQ0E9URj1H7kKbN+KiWNpUJJYmbX4giZ6BywB4Wm/rffJyAHxjSKLMjnb3OBmsfEN7fyKvYUThnQSIqV58BDQlPp4tc3oewAXA52tBTa9E8UCaKp6HFvWtMRoTrKc0p7RqsjKtSYQKBgQDDLD8IZt0tNKz2X3NNJmhru9SgchSAKlL+0zHLuCSSN2IjAMNC0jCbmSm7KXa6CggMqsG+lv7nmsApBoUc96k//EIcR+WBzKhHRuQcfdvi4plG3hY2o4Sxd89PnnE/QB4834Vz0DCaFXt4q5P/YXGDijAjvSPflDTCRNuK9L96qwKBgQCoO+UkcmuE+WiI6cEdG8OMZTK9C6zqc/rP2ASpG/IWMvx5TiDCet2CCOli1+tidXYheVjYj3FUu5Dhoe5acCI/2GgEnyacZqO/7i1Zd5FbXGxVKR/ZsoqMOPdq32XwVuFcemIXdRFPwG/dObSo/wpphD9qvZop2V4I5IKuWfpY0QKBgQCDJo1o0O6ZfQcpO7TYSBHtKzfTZTV/dnF1CWgx1BjHJG5OxSlxEptVQ9P2EAPXOfx0vp4Yu5CpA6VTLC1w7L+P3qxyIqIH2gXOd8W7m5uR1+FV7NESgEkflDn776V78lMfZrhfvaubLwpcaTe3abyEvuxgZ63ehNH7JZg9xP+SpwKBgQCbjOXN0U3A4araDOhVtboVMey8BvOWxC3/x0hn5ELua64tqyN3LotpFDxW/DXUD69uO/j+I4MySUsolMUCMfQ4/CsROuZEblKxv9OGiZuI0egIxcGBVCfuhy2dT3m9w1cUXsQb1E5xYwkLj7OPHmHONk08sJq+XxEnP8Hk26UyQQKBgQCSvWDBqSoREqPNZumSYzI2pg9A3qrUVl9mlDa5n4ygNSyoogdet6tt4iob+p45VZcyBQHps3w5A9U0r3RY/FITX07olmwLP/fjMJng/K9Xhz1dnxABpohtehD/yo9VcOgINRxXOdqOUMbNtGmTQ5cKGrBHzGfW19FzGiTnoBaPuA==";
	private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgEKzQmhH6MdZvYGpOOGIOBdYb8fDSPqHXaa9e4ySgKni57/r8wGxYmqjV8OKm4avtXnubIOf0tTDBw4I/wTgQFrEMsQKgZHE6rnSUhIZKnhur1V24URKWWVjM4zUVYl+BmjMkivBiqozBGwQMueCXwYJzUsU2zfpkTLCj7GaqSz5IAvNpMsRu0hjBHqllcve5Qez8SoRsgWsZv7fhZ+KcWrWNdmPCPEOrJI8G0HzSkRFJwUKdhXsGN61yg0JHyT7MddyELDAm679gm1CTaQV57NmhoH90tL65b3Rw2fPEq1V8zlqjsv+fVv/RS+8WRpwYCfOf8nohlApNMtw4HvtmwIDAQAB";
	private static final String queryUrl = "https://www.okkpay.net/cx";

	public static void collectionTest() throws Exception {
		OkkpayParamCollect param = new OkkpayParamCollect();
		param.setApiOrderNo("M2d3084456256");
		param.setAppId("32NL0R54QCXH69481C85HU9M4JBXW4HC");
		param.setChannelCode("FYS");
		param.setCharset("utf-8");
		param.setTotalFee("3.00");
		param.setNotifyUrl("http://www.baidu.com");
		OkkpayResultOrder result = new OkkpayApi().collection(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void payTest() {
		OkkpayParamPay param = new OkkpayParamPay();
		param.setApiOrderNo("M2d3084456256");
		param.setAppId("32NL0R54QCXH69481C85HU9M4JBXW4HC");
		param.setChannelCode("UPI");
		param.setCharset("utf-8");
		param.setTotalFee("3.00");
		param.setNotifyUrl("http://www.baidu.com");
		param.setBankcardNo("5511113333333");
		param.setBankName("ICICBANK");
		param.setIfsc("icic0556623");
		param.setName("Jack Chen");
		param.setEmail("hjackchen@gmail.com");
		param.setMode("IMPS");
		param.setPhone("9152565523655");
		OkkpayResultOrder result = new OkkpayApi().agentpay(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryOrderStatusTest() {
		OkkPayParamQueryOrder param = new OkkPayParamQueryOrder();
		param.setAppId("32NL0R54QCXH69481C85HU9M4JBXW4HC");
	    param.setApiOrderNo("M2d308445656");
		param.setCharset("utf-8");
		OkkPayqueryOrderResultStatus result = new OkkpayApi().queryOrderStatus(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void queryBalance() {
		OkkPayParamQueryOrder param = new OkkPayParamQueryOrder();
		param.setAppId("32NL0R54QCXH69481C85HU9M4JBXW4HC");
		param.setApiOrderNo("M2d3084456256");
		param.setCharset("utf-8");
		OkkPayqueryOrderResultStatus result = new OkkpayApi().queryBalance(publicKey, privateKey, queryUrl, param);
		log.error(JSON.toJSONString(result));
	}

	public static void main(String[] args) throws Exception {
//		DamsonPayApiTest.queryBalance();
//		TopayPayApiTest.collectionTest();
//		TopayPayApiTest.payTest();
		OkkpayApiTest.queryOrderStatusTest();
//		TopayPayApiTest.queryBalance();
//		BigDecimal mBigDecimal = new BigDecimal("12.");
//		BigDecimal result2 = mBigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
//		System.out.println(result2.toPlainString());
	}
}
