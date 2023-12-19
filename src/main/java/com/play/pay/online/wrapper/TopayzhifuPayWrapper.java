package com.play.pay.online.wrapper;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxitopay.TopayPayApi;
import com.play.pay.baxitopay.params.TopayParamCollect;
import com.play.pay.baxitopay.result.TopayResultOrder;

/** 入款 */
public class TopayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(TopayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "topayzhifu";

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
		String account = params[6];
		//String clientIp = params[7];
		String payGetway = params[10];
	//	String appId = params[11];
	//	String orderTime = params[14];
	//	String userId = params[16];
		JSONObject object = new JSONObject();
		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		//log.error("notifyUrl = {}", notifyUrl);
		TopayParamCollect param = new TopayParamCollect();
		param.setMerchant_no(merchantCode);
		param.setOut_trade_no(orderId);
		param.setPay_amount(amount);
		param.setNotify_url(notifyUrl);
		param.setTitle("gopay...");
		param.setDescription("gopay...");
		TopayResultOrder result = new TopayPayApi().collection(account, merchantKey, payGetway, param);
	//	log.error(JSON.toJSONString(result));
		object.put("success", true);
		object.put("url", result.getData().getPayment_link());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		object.put("thirdOrderId", result.getData().getTrade_no());
		return object;
	}
}