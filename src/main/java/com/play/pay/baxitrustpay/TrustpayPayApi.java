package com.play.pay.baxitrustpay;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxitrustpay.http.HttpClientForTrustpay;
import com.play.pay.baxitrustpay.para.ITrustpayParamBase;
import com.play.pay.baxitrustpay.para.TrustpayParamCollect;
import com.play.pay.baxitrustpay.para.TrustpayParamPay;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryBalance;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryOrder;
import com.play.pay.baxitrustpay.result.TrustpayResultBalance;
import com.play.pay.baxitrustpay.result.TrustpayResultCollect;
import com.play.pay.baxitrustpay.result.TrustpayResultOrderCollect;
import com.play.pay.baxitrustpay.result.TrustpayResultOrderPay;
import com.play.pay.baxitrustpay.result.TrustpayResultPay;
import com.play.pay.baxitrustpay.util.TrustpayShaEncrypt;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;

public class TrustpayPayApi {
	private final static Logger log = LoggerFactory.getLogger(TrustpayPayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/payment/createOrder";
	/**
	 * 代付订单
	 */
	private final static String AGENTPAY_SUFFIX = "%s/payout/createOrder";
	private final static String QUERY_COLLECT_STATUS_SUFFIX = "%s/payment/orderStatus";
	private final static String QUERY_PAY_STATUS_SUFFIX = "%s/payout/orderStatus";

	private final static String QUERY_BALANCE_SUFFIX = "%s/payout/balance";



	public TrustpayResultCollect collection(String key, String url, TrustpayParamCollect param) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		return getResult(key, param, formattedURL, TrustpayResultCollect.class);
	}

	public TrustpayResultPay agentpay(String key, String url, TrustpayParamPay param) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		return getResult(key, param, formattedURL, TrustpayResultPay.class);
	}

	public TrustpayResultOrderCollect queryCollectStatus(String key, String url, TrustpayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_COLLECT_STATUS_SUFFIX, urlFormat(url));
		return getResult(key, param, formattedURL, TrustpayResultOrderCollect.class);
	}

	public TrustpayResultOrderPay queryPayStatus(String key, String url, TrustpayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_PAY_STATUS_SUFFIX, urlFormat(url));
		return getResult(key, param, formattedURL, TrustpayResultOrderPay.class);
	}

	public TrustpayResultBalance queryBalance(String key, String url, TrustpayParamQueryBalance param) {
		String formattedURL = String.format(QUERY_BALANCE_SUFFIX, urlFormat(url));
		return getResult(key, param, formattedURL, TrustpayResultBalance.class);
	}

	private <T> T getResult(String key, ITrustpayParamBase param, String url, Class<T> agentResultClass) {
		try {
			String encodedString = TrustpayShaEncrypt.genSign(param.toMap(), key);
			param.setSign(encodedString);
			String response = post(param, url);
			JSONObject retMap = JSONObject.parseObject(response);
	//		log.info("response:{}", JSONObject.toJSONString(retMap, true));
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
	private String post(ITrustpayParamBase param, String url) {
	//	log.info("url:{}", url);
		Response response = HttpClientForTrustpay.newClient().addParameter(param.toMap()).curl(url);
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
