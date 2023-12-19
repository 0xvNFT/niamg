package com.play.pay.baxicashpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxicashpay.http.HttpClientForTopay;
import com.play.pay.baxicashpay.params.*;
import com.play.pay.baxicashpay.result.CashResultOrder;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CashpayApi {
	private final static Logger log = LoggerFactory.getLogger(CashpayApi.class);
	//代收
	private final static String COLLECTION_SUFFIX = "%s/open-api/pay/payment";
	//代付
	private final static String AGENTPAY_SUFFIX = "%s/open-api/pay/transfer_fast";
	//代收代付查询订单
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/open-api/pay/query";


	public CashResultOrder collection(String publicKey, String privateKey, String url, CashpayParamCollect param, String authorizationHeaderValue) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, CashResultOrder.class,authorizationHeaderValue);
	}

	public CashResultOrder agentpay(String publicKey, String privateKey, String url, CashpayParamPay param,String headerAuthorization) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, CashResultOrder.class,headerAuthorization);
	}

	public CashResultOrder queryOrderStatus(String publicKey, String privateKey, String url, CashpayParamQueryOrder param, String headerAuthorization) {
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, CashResultOrder.class,headerAuthorization);
	}
	private <T> T getResult(String publicKey, String privateKey, CashpayParamBase param, String url, Class<T> agentResultClass, String authorizationHeaderValue) {
		try {
			String response = "";
			if(param instanceof  CashpayParamCollect|| param instanceof CashpayParamPay ){
				 response = post(param, url,authorizationHeaderValue);
			}else {
				response = get(param, url,authorizationHeaderValue);
			}
			JSONObject retMap = JSONObject.parseObject(response);
		//	log.error("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("425".equals(retMap.getString("code"))) {
				throw new RuntimeException(retMap.getString("msg"));
			}
			//500
			if ("500".equals(retMap.getString("code"))) {
				throw new RuntimeException(retMap.getString("msg"));
			}
			// 成功
			if ("200".equals(retMap.getString("code"))) {
				AssertUtils.assertNotNull(agentResultClass, "class must not be null");
				return JSONObject.parseObject(response, agentResultClass);
			}
			throw new RuntimeException(retMap.getString("msg"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	private static String getSignedValue(Map<String, Object> reqMap) {
		Map<String, String> copy = new TreeMap<>();
		reqMap.forEach((k, v) -> {
			if (v != null && !"".equals(v)) {
				copy.put(k, v.toString());
			}
		});
		StringBuilder sb = new StringBuilder();
		copy.forEach((k, v) -> {
			if (v != null) {
				sb.append(k).append("=").append(v).append("&");
			}
		});
		return sb.toString();
	}

	private String get(CashpayParamBase param, String url, String authorizationHeaderValue) {
	//	log.info("url:{}", url);
		HttpClientForTopay httpClientForTopay = HttpClientForTopay.newClient().addContent(param.toJsonString());
		httpClientForTopay.set_Content(null);
		httpClientForTopay.getHeaderList().add(new BasicHeader("Authorization", authorizationHeaderValue));
		List<NameValuePair> parametersCash = new ArrayList<>();
		param.toMap().forEach((k,v)->{
			NameValuePair nameValuePair = new BasicNameValuePair(k,(String) v);
			parametersCash.add(nameValuePair);
		});

		httpClientForTopay.setParametersCash(parametersCash);
		Response response = httpClientForTopay.curlGet(url);

	//	log.error(JSON.toJSONString(response));
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
	//	log.info("response:{}", body);
		return body;
	}

	private String post(CashpayParamBase param, String url, String authorizationHeaderValue) {
		log.info("url:{}", url);
		HttpClientForTopay httpClientForTopay = HttpClientForTopay.newClient().addContent(param.toJsonString());
		httpClientForTopay.set_Content(null);
		httpClientForTopay.getHeaderList().add(new BasicHeader("Authorization", authorizationHeaderValue));
		Response response = httpClientForTopay.curl(url);

		log.error(JSON.toJSONString(response));
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
		log.info("response:{}", body);
		return body;
	}


	private static String urlFormat(String url) {
		if (StringUtils.isBlank(url))
			return url;
		url = url.trim();
		if (url.endsWith("/"))
			return url.substring(0, url.length() - 1);
		return url;
	}
}
