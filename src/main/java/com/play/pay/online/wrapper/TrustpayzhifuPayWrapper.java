package com.play.pay.online.wrapper;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxitrustpay.TrustpayPayApi;
import com.play.pay.baxitrustpay.para.TrustpayParamCollect;
import com.play.pay.baxitrustpay.result.TrustpayResultCollect;

/** 入款 */
public class TrustpayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(TrustpayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "trustpayzhifu";

	@Override
	public JSONObject wrap(String... params) {
		/*log.error("==========>>>>");
		for (int i = 0; i < params.length; i++) {
			log.error("item[{}]= {}", i, params[i]);
		}*/
		String merchantCode = params[0];
		String merchantKey = params[1];
		String orderId = params[2];
		String amount = params[3];
		// 保留两位小数 不足补0
		amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
		String merchantDomain = params[4];
		//String bankcode = params[5];
	//	String account = params[6];
	//	String clientIp = params[7];
		String payGetway = params[10];
	//	String appId = params[11];
	//	String orderTime = params[14];
	//	String userId = params[16];
		JSONObject object = new JSONObject();
		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		//log.error("notifyUrl = {}", notifyUrl);
		TrustpayParamCollect param = new TrustpayParamCollect();
		param.setMerchantId(merchantCode);
		param.setOrderNo(orderId);
		param.setAmount(amount);
		param.setNotifyUrl(notifyUrl);
		TrustpayResultCollect result = new TrustpayPayApi().collection(merchantKey, payGetway, param);
	//	log.error(JSON.toJSONString(result));
		object.put("success", true);
		object.put("url", result.getData().getJumpUrl());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		object.put("thirdOrderId", result.getData().getPayNo());
		return object;
	}
}