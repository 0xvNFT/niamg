package com.play.pay.online.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.damson.DamsonPayApi;
import com.play.pay.damson.params.CollectionParam;
import com.play.pay.damson.result.CollectionResult;

public class DamsonzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(DamsonzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "damsonzhifu";

	@Override
	public JSONObject wrap(String... params) {
		log.error("==========>>>>");
//		for (int i = 0; i < params.length; i++) {
//			log.error("item[{}]= {}", i, params[i]);
//		}
		String merchantCode = params[0];
		String merchantKey = params[1];
		String orderId = params[2];
		String amount = params[3];
		String merchantDomain = params[4];
		String bankcode = params[5];
		String account = params[6];
		String clientIp = params[7];
		String payGetway = params[10];
		String appId = params[11];
		String orderTime = params[14];
		String userId = params[16];
		JSONObject object = new JSONObject();
		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		log.error("notifyUrl = {}", notifyUrl);
		CollectionParam param = new CollectionParam();
		param.setAppId(appId);
		param.setPayType(140);
		// 以分为单位
		param.setAmount(Long.valueOf(amount) * 100);
		param.setName(userId);
		param.setExpireTime(60 * 60L);
		param.setMchOrderNo(orderId);
		param.setNotifyUrl(notifyUrl);
		// email body 不可以删除 否则报错
		param.setEmail("email@example.com");
		param.setBody("damson pay");
		param.setSuccessUrl("");
		CollectionResult result = new DamsonPayApi().collection(Long.valueOf(merchantCode), merchantKey, payGetway, param);
		log.error(JSON.toJSONString(result));
		object.put("success", true);
		object.put("url", result.getUrl());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		object.put("thirdOrderId", result.getOrderNo());
		return object;
	}
}