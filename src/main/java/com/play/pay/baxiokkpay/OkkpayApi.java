package com.play.pay.baxiokkpay;


import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiokkpay.http.HttpClientForTopay;
import com.play.pay.baxiokkpay.params.*;
import com.play.pay.baxiokkpay.result.OkkPayqueryOrderResultStatus;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import com.play.pay.baxiokkpay.util.RSA2Util;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OkkpayApi {
	private final static Logger log = LoggerFactory.getLogger(OkkpayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/api/unifiedOrder";
	private final static String AGENTPAY_SUFFIX = "%s/api/payoutCreate";
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/api/queryOrder";
	private final static String QUERY_BALANCE_SUFFIX = "%s/api/queryOutOrder";

	public OkkpayResultOrder collection(String publicKey, String privateKey, String url, OkkpayParamCollect param) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		Map<String,String> map = new HashMap<String,String>();
		map.put("appId",param.getAppId());
		map.put("channelCode",param.getChannelCode());
		map.put("apiOrderNo",param.getApiOrderNo());
		map.put("totalFee",param.getTotalFee());
		map.put("charset",param.getCharset());
		map.put("notifyUrl",param.getNotifyUrl());
		param.setSign(getSign(map,privateKey));
		return getResult(publicKey, privateKey, param, formattedURL, OkkpayResultOrder.class);
	}

	public OkkpayResultOrder agentpay(String publicKey, String privateKey, String url, OkkpayParamPay param) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		Map<String,String> map = new HashMap<String,String>();
		map.put("appId",param.getAppId());
		map.put("channelCode",param.getChannelCode());
		map.put("apiOrderNo",param.getApiOrderNo());
		map.put("totalFee",param.getTotalFee());
		map.put("charset",param.getCharset());
		map.put("notifyUrl",param.getNotifyUrl());
		map.put("bankcardNo",param.getBankcardNo());
		map.put("bankName",param.getBankName());
		map.put("ifsc",param.getIfsc());
		map.put("name",param.getName());
		map.put("email",param.getEmail());
		map.put("mode",param.getMode());
		map.put("phone",param.getPhone());
		param.setSign(getSign(map,privateKey));
		return getResult(publicKey, privateKey, param, formattedURL, OkkpayResultOrder.class);
	}

	public OkkPayqueryOrderResultStatus queryOrderStatus(String publicKey, String privateKey, String url, OkkPayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		Map<String,String> map = new HashMap<String,String>();
		map.put("appId",param.getAppId());
		map.put("apiOrderNo",param.getApiOrderNo());
		map.put("charset",param.getCharset());
		param.setSign(getSign(map,privateKey));
		return getResult(publicKey, privateKey, param, formattedURL, OkkPayqueryOrderResultStatus.class);
	}

	public OkkPayqueryOrderResultStatus queryBalance(String publicKey, String privateKey, String url, OkkPayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_BALANCE_SUFFIX, urlFormat(url));
		Map<String,String> map = new HashMap<String,String>();
		map.put("appId",param.getAppId());
		map.put("apiOrderNo",param.getApiOrderNo());
		map.put("charset",param.getCharset());
		param.setSign(getSign(map,privateKey));
		return getResult(publicKey, privateKey, param, formattedURL, OkkPayqueryOrderResultStatus.class);
	}

	private <T> T getResult(String publicKey, String privateKey, ITopayParamBase param, String url, Class<T> agentResultClass) {
		try {
			String response = post(param, url);
			System.out.println(response);
			JSONObject retMap = JSONObject.parseObject(response);
			//log.info("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("4011".equals(retMap.getString("code"))) {
				throw new RuntimeException(retMap.getString("message"));
			}
			// 成功
			if ("2000".equals(retMap.getString("code"))) {
				AssertUtils.assertNotNull(agentResultClass, "class must not be null");
				return JSONObject.parseObject(response, agentResultClass);
			}
			throw new RuntimeException(retMap.getString("message"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	private String getSign(Map<String,String> map, String privateKey){
		try {
			return RSA2Util.rsaSign(map, privateKey);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
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
		//log.info("url:{}", url);
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
