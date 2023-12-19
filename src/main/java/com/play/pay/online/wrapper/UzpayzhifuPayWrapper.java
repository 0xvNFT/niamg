package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxiuzpay.UzpayPayApi;
import com.play.pay.baxiuzpay.params.UzpayParamCollect;
import com.play.pay.baxiuzpay.result.UzpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/** 入款 */
public class UzpayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(UzpayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "uzpayzhifu";

	@Override
	public JSONObject wrap(String... params) {
	/*	log.error("==========>>>>");
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
	//	String bankcode = params[5];
		String account = params[6];
		//String clientIp = params[7];
		String payGetway = params[10];
		//String appId = params[11];
		//String orderTime = params[14];
		//String userId = params[16];

		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
	//	log.error("notifyUrl = {}", notifyUrl);
		UzpayParamCollect param = new UzpayParamCollect();
		param.setMch_id(merchantCode);
		param.setNotify_url(notifyUrl);
		param.setMch_order_no(orderId);
		param.setTrade_amount(amount);
		UzpayResultOrder result = new UzpayPayApi().collection(account, merchantKey, payGetway, param);
	//	log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		if(result.getRespCode().equals("SUCCESS")){
			object.put("success", true);
		}
		object.put("url", result.getPayInfo());
		object.put("returnType", "href");
		object.put("thirdOrderId", result.getMerTransferId());
		return object;
	}
}