package com.play.pay.baxiwowpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxiwowpay.http.HttpClientForTopay;
import com.play.pay.baxiwowpay.params.*;
import com.play.pay.baxiwowpay.result.WowpayResultBalance;
import com.play.pay.baxiwowpay.result.WowpayResultOrder;
import com.play.pay.baxiwowpay.util.SignAPI;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

public class WowpayPayApi {
	private final static Logger log = LoggerFactory.getLogger(WowpayPayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/pay/web";
	private final static String AGENTPAY_SUFFIX = "%s/pay/transfer";
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/api/trade/query";
	private final static String QUERY_BALANCE_SUFFIX = "%s/query/transfer";

	public WowpayResultOrder collection(String publicKey, String privateKey, String url, WowpayParamCollect param) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		StringBuilder sb = new StringBuilder();
		sb.append("goods_name=").append(param.getGoods_name()).append("&");
		sb.append("mch_id=").append(param.getMch_id()).append("&");
		sb.append("mch_order_no=").append(param.getMch_order_no()).append("&");
		sb.append("notify_url=").append(param.getNotify_url()).append("&");
		sb.append("order_date=").append(param.getOrder_date()).append("&");
		sb.append("pay_type=").append(param.getPay_type()).append("&");
		sb.append("trade_amount=").append(param.getTrade_amount()).append("&");
		sb.append("version=").append(param.getVersion());
		//sb.append("key=").append(privateKey);
		param.setSign(SignAPI.sign(sb.toString(),privateKey));
	//	log.error(sb.toString());
	//	log.error(JSONObject.toJSONString(param));
		return getResult(publicKey, privateKey, param, formattedURL, WowpayResultOrder.class);
	}

	public WowpayResultOrder agentpay(String publicKey, String privateKey, String url, WowpayParamPay param) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		StringBuilder sb = new StringBuilder();
		sb.append("apply_date=").append(param.getApply_date()).append("&");
		sb.append("back_url=").append(param.getBack_url()).append("&");
		sb.append("bank_code=").append(param.getBank_code()).append("&");
		sb.append("mch_id=").append(param.getMch_id()).append("&");
		sb.append("mch_transferId=").append(param.getMch_transferId()).append("&");
		sb.append("receive_account=").append(param.getReceive_account()).append("&");
		sb.append("receive_name=").append(param.getReceive_name()).append("&");
		sb.append("remark=").append(param.getRemark()).append("&");
		sb.append("transfer_amount=").append(param.getTransfer_amount());
		param.setSign(SignAPI.sign(sb.toString(),privateKey));
		return getResult(publicKey, privateKey, param, formattedURL, WowpayResultOrder.class);
	}

	public WowpayResultOrder queryOrderStatus(String publicKey, String privateKey, String url, TopayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, WowpayResultOrder.class);
	}

	public WowpayResultBalance queryBalance(String publicKey, String privateKey, String url, UzpayParamQueryBalance param) {
		StringBuilder sb = new StringBuilder();
		sb.append("mch_id=").append(param.getMch_id()).append("&");
		sb.append("mch_transferId=").append(param.getMch_transferId());
		param.setSign(SignAPI.sign(sb.toString(),privateKey));
		String formattedURL = String.format(QUERY_BALANCE_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, WowpayResultBalance.class);
	}

	private <T> T getResult(String publicKey, String privateKey, ITopayParamBase param, String url, Class<T> agentResultClass) {
		try {
			String response = post(param, url);
			JSONObject retMap = JSONObject.parseObject(response);
		//	log.info("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("ORDER_REPEATED".equals(retMap.getString("tradeMsg"))) {
				throw new RuntimeException(retMap.getString("tradeMsg"));
			}
			// 成功
			if ("SUCCESS".equals(retMap.getString("respCode"))) {
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
		//log.info("url:{}", url);
		Response response = HttpClientForTopay.newClient().addContent(param.toJsonString()).curlPostXForm(url);
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
