package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxiokkpay.OkkpayApi;
import com.play.pay.baxiokkpay.params.OkkpayParamCollect;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import com.play.pay.baxiowenpay.OwenpayPayApi;
import com.play.pay.baxiowenpay.params.OwenFormValue;
import com.play.pay.baxiowenpay.params.OwenpayParamCollect;
import com.play.pay.baxiowenpay.result.OwenResultOrder;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/** 入款 */
public class OwenpayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(OwenpayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "owenpayzhifu";

	@Override
	public JSONObject wrap(String... params) {
		//log.error("==========>>>>");
		/*for (int i = 0; i < params.length; i++) {
			log.error("item[{}]= {}", i, params[i]);
		}*/
		String merchantCode = params[0];
		String merchantKey = params[1];
		String orderId = params[2];
		String amount = params[3];
		// 保留两位小数 不足补0
		amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).toPlainString();

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
		OwenpayParamCollect param = new OwenpayParamCollect();
		param.setAppID(appId);
		param.setOutTradeNo(orderId);

		param.setTotalAmount(OwenRSAEncrypt.numberRemoveZero(amount));
		param.setNotifyUrl(notifyUrl);
		OwenResultOrder result = new OwenpayPayApi().collection(account, merchantKey, payGetway, param);
		object.put("success", result.getResultCode().equals("0000"));
		object.put("url", result.getPayURL());
		object.put("returnType", "href");
		object.put("orderId", result.getOutTradeNo());
		return object;
	}
}