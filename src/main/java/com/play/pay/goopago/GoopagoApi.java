package com.play.pay.goopago;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.goopago.http.HttpClientForGoopago;
import com.play.pay.goopago.param.BaseParam;
import com.play.pay.goopago.result.AgentpayResult;
import com.play.pay.goopago.result.CollectionResult;
import com.play.pay.goopago.result.query.QueryAgentpayProofResult;
import com.play.pay.goopago.result.query.QueryBalanceResult;
import com.play.pay.goopago.result.query.QueryOrderResult;
import com.play.pay.goopago.util.AssertUtils;
import com.play.pay.goopago.util.SecretUtils;
import com.play.web.utils.http.Response;

/**
 * @author zzn
 */
public class GoopagoApi {
	private final static Logger log = LoggerFactory.getLogger(GoopagoApi.class);
	private final static String AGENTPAY_SUFFIX = "%s/unified/agentpay/apply";
	private final static String COLLECTION_SUFFIX = "%s/unified/collection/create";
	private final static String QUERY_COLLECTION_SUFFIX = "%s/query/pay_order";
	private final static String QUERY_AGENTPAY_SUFFIX = "%s/query/agentpay_order";
	private final static String QUERY_BALANCE_SUFFIX = "%s/query/balance";
	private final static String QUERY_ALL_BALANCE_SUFFIX = "%s/query/allBalance";
	private final static String QUERY_AGENTPAY_PROOF = "%s/query/agentpay_proof";

	public AgentpayResult agentpay(Long mchId, String key, String payUrl, BaseParam param) {
		String url = String.format(AGENTPAY_SUFFIX, urlFormat(payUrl));
		return getResult(mchId, key, param, url, AgentpayResult.class);
	}

	public CollectionResult collection(Long mchId, String key, String payUrl, BaseParam param) {
		String url = String.format(COLLECTION_SUFFIX, urlFormat(payUrl));
		return getResult(mchId, key, param, url, CollectionResult.class);
	}

	public QueryOrderResult queryCollection(Long mchId, String key, String queryUrl, BaseParam param) {
		String url = String.format(QUERY_COLLECTION_SUFFIX, urlFormat(queryUrl));
		return getResult(mchId, key, param, url, QueryOrderResult.class);
	}

	public QueryOrderResult queryAgentpay(Long mchId, String key, String queryUrl, BaseParam param) {
		String url = String.format(QUERY_AGENTPAY_SUFFIX, urlFormat(queryUrl));
		return getResult(mchId, key, param, url, QueryOrderResult.class);
	}

	public QueryBalanceResult queryBalance(Long mchId, String key, String queryUrl, BaseParam param) {
		String url = String.format(QUERY_BALANCE_SUFFIX, urlFormat(queryUrl));
		return getResult(mchId, key, param, url, QueryBalanceResult.class);
	}

	public QueryBalanceResult queryAllBalance(Long mchId, String key, String queryUrl, BaseParam param) {
		String url = String.format(QUERY_ALL_BALANCE_SUFFIX, urlFormat(queryUrl));
		return getResult(mchId, key, param, url, QueryBalanceResult.class);
	}

	public QueryAgentpayProofResult queryAgentpayProof(Long mchId, String key, String queryUrl, BaseParam param) {
		String url = String.format(QUERY_AGENTPAY_PROOF, urlFormat(queryUrl));
		return getResult(mchId, key, param, url, QueryAgentpayProofResult.class);
	}

	private <T> T getResult(Long mchId, String key, BaseParam param, String url, Class<T> agentResultClass) {
		BaseParam baseParam = assembleParam(mchId, key, param);
	//	log.info("request:{}", JSONObject.toJSONString(baseParam, true));
		String response = post(baseParam, url);
		JSONObject retMap = JSONObject.parseObject(response);
	//	log.info("response:{}", JSONObject.toJSONString(retMap, true));
		if ("SUCCESS".equals(retMap.getString("resCode"))) {
			if (SecretUtils.verify(retMap, key)) {
				AssertUtils.assertNotNull(agentResultClass, "class must not be null");
				return JSONObject.parseObject(response, agentResultClass);
			}
		}
		throw new RuntimeException(retMap.getString("errDes"));
	}

	private BaseParam assembleParam(Long mchId, String key, BaseParam param) {
		param.setMchId(mchId);
		param.setNonceStr(System.currentTimeMillis() + "");
		String reqSign = SecretUtils.sign(param.toMap(), key);
		param.setSign(reqSign);
		return param;
	}

	private String post(BaseParam param, String url) {
	//	log.info("url:{}", url);
		Response response = HttpClientForGoopago.newClient().addContent(param.toJsonString()).curl(url);
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
