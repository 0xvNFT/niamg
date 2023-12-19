package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.speedlypay.SpeedlyPayApi;
import com.play.pay.speedlypay.param.SpeedlyCollectionParam;
import com.play.pay.speedlypay.result.SpeedlyCollectionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;

public class SpeedlyzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger logger = LoggerFactory.getLogger(SpeedlyzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "speedlyzhifu";

	@Override
	public JSONObject wrap(String... params) {

		for (int i = 0; i < params.length; i++) {
			logger.error("item[{}]= {}", i, params[i]);
		}
		String merchantCode = params[0];
		String merchantKey = params[1];
		String orderId = params[2];
		String amount = params[3];
		String merchantDomain = params[4];
		String bankcode = params[5];
		String account = params[6];
		String clientIp = params[7];
		String referer = params[8];
		String payType = params[9];
		String payGetway = params[10];
		String appId = params[11];
		String domain = params[12];
		String payUser = params[13];
		String orderTime = params[14];
		String alternative = params[15];
		String userId = params[16];
		String agentUser = params[17];

		JSONObject object = new JSONObject();
		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		//logger.error("SpeedlyzhifuPayWrapper notifyUrl = {}", notifyUrl);

		SpeedlyCollectionParam param = new SpeedlyCollectionParam();
		param.setCountry("BR");
		param.setCurrency("BRL");
		param.setPayment_method_id("PIX");
		param.setPayment_method_flow("REDIRECT");
		param.setOrder_id(orderId);
		param.setAmount(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN));
		param.setNotification_url(notifyUrl);
		param.setSuccess_redirect_url(merchantDomain);
		param.setTimestamp(Instant.now().toEpochMilli());

		logger.error("SpeedlyzhifuPayWrapper param:{}", JSON.toJSON(param));
		SpeedlyCollectionResult result = new SpeedlyPayApi().collection(merchantCode, appId, merchantKey, payGetway, param, null);
		logger.error("SpeedlyzhifuPayWrapper result:{}", JSON.toJSON(result));

		object.put("success", true);
		object.put("url", result.getRedirect_url());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		object.put("thirdOrderId", result.getPayment_id());
		return object;
	}
}