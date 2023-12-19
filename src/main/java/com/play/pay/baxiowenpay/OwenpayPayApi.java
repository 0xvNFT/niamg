package com.play.pay.baxiowenpay;

import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiowenpay.http.HttpClientForTopay;
import com.play.pay.baxiowenpay.params.*;
import com.play.pay.baxiowenpay.result.OwenResultOrder;
import com.play.pay.baxiowenpay.result.OwenpayResultBalance;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

public class OwenpayPayApi {
	//private final static Logger log = LoggerFactory.getLogger(OwenpayPayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/pay/apply.shtml";
	private final static String AGENTPAY_SUFFIX = "%s/cashOutVp/apply.shtml";
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/remitQuery/apply.shtml";

	public OwenResultOrder collection(String publicKey, String privateKey, String url, OwenpayParamCollect param) {
		Map<String,String> map = param.toMap().toJavaObject(Map.class);
		Map<String,String> treemap = new TreeMap<>();
		map.remove("sign");
		treemap.putAll(map);
		String str = JSONObject.toJSONString(treemap) + privateKey;
		param.setSign(OwenRSAEncrypt.encrypt(str,"utf-8"));
		String JSONREQUEST = JSONObject.toJSONString(param);
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		map.clear();
		map.put("ApplyParams",JSONREQUEST);
		return getResultForJson(map, formattedURL, OwenResultOrder.class);
	}

	public OwenResultOrder agentpay(String publicKey, String privateKey, String url, OwenpayParamPay param) {
		Map<String,String> map = param.toMap().toJavaObject(Map.class);
		Map<String,String> treemap = new TreeMap<>();
		map.remove("sign");
		treemap.putAll(map);
		String str = JSONObject.toJSONString(treemap) + privateKey;
		param.setSign(OwenRSAEncrypt.encrypt(str,"utf-8"));
		String JSONREQUEST = JSONObject.toJSONString(param);
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		map.clear();
		String value = URLEncoder.encode(JSONREQUEST);
		map.put("CashOutParams",value);
		return getResultForJson(map, formattedURL, OwenResultOrder.class);
	}

	public OwenpayResultBalance queryBalance(String publicKey, String privateKey, String url, OwenpayParamQueryOrder param) {
		Map<String,String> map = param.toMap().toJavaObject(Map.class);
		Map<String,String> treemap = new TreeMap<>();
		map.remove("sign");
		treemap.putAll(map);
		String str = JSONObject.toJSONString(treemap) + privateKey;
		param.setSign(OwenRSAEncrypt.encrypt(str,"utf-8"));
		String JSONREQUEST = JSONObject.toJSONString(param);
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		map.clear();
		map.put("QueryParams",JSONREQUEST.toString());
		return getResultForJson(map, formattedURL, OwenpayResultBalance.class);
	}
	private <T> T getResultForJson(Map<String,String> param, String url, Class<T> agentResultClass) {
		try {
			String response = post(param, url);
			JSONObject retMap = JSONObject.parseObject(response);
		//	log.info("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("9999".equals(retMap.getString("resultCode"))) {
				throw new RuntimeException(retMap.getString("stateInfo"));
			}
			// 成功
			if ("0000".equals(retMap.getString("resultCode"))) {
				AssertUtils.assertNotNull(agentResultClass, "class must not be null");
				return JSONObject.parseObject(response, agentResultClass);
			}
			throw new RuntimeException(retMap.getString("stateInfo"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	private String post(Map<String,String> param, String url) {
		Response response = HttpClientForTopay.newClient().addContent(JSONObject.toJSONString(param)).curlPostForWowPay(url);
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
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
