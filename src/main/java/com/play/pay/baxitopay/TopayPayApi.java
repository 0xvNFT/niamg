package com.play.pay.baxitopay;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxitopay.http.HttpClientForTopay;
import com.play.pay.baxitopay.params.ITopayParamBase;
import com.play.pay.baxitopay.params.TopayParamCollect;
import com.play.pay.baxitopay.params.TopayParamPay;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.params.TopayParamQueryOrder;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.pay.baxitopay.result.TopayResultOrder;
import com.play.pay.baxitopay.util.TopayRSAEncrypt;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;

public class TopayPayApi {
	private final static Logger log = LoggerFactory.getLogger(TopayPayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/api/trade/payin";
	private final static String AGENTPAY_SUFFIX = "%s/api/trade/payout";
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/api/trade/query";
	private final static String QUERY_BALANCE_SUFFIX = "%s/api/balance";

	public TopayResultOrder collection(String publicKey, String privateKey, String url, TopayParamCollect param) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, TopayResultOrder.class);
	}

	public TopayResultOrder agentpay(String publicKey, String privateKey, String url, TopayParamPay param) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, TopayResultOrder.class);
	}

	public TopayResultOrder queryOrderStatus(String publicKey, String privateKey, String url, TopayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, TopayResultOrder.class);
	}

	public TopayResultBalance queryBalance(String publicKey, String privateKey, String url, TopayParamQueryBalance param) {
		String formattedURL = String.format(QUERY_BALANCE_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, TopayResultBalance.class);
	}

	private <T> T getResult(String publicKey, String privateKey, ITopayParamBase param, String url, Class<T> agentResultClass) {
		try {
			String prepareParam = getSignedValue(param.toMap());
			String encodedString = TopayRSAEncrypt.encrypt(privateKey, prepareParam);
			param.setSign(encodedString);
			String response = post(param, url);
			JSONObject retMap = JSONObject.parseObject(response);
	//		log.info("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("10028".equals(retMap.getString("code"))) {
				throw new RuntimeException(retMap.getString("msg"));
			}
			// 成功
			if ("0".equals(retMap.getString("code"))) {
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

	private String post(ITopayParamBase param, String url) {
	//	log.info("url:{}", url);
		Response response = HttpClientForTopay.newClient().addContent(param.toJsonString()).curl(url);
	//	log.info(JSON.toJSONString(response));
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
	//	log.info("response:{}", body);
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
