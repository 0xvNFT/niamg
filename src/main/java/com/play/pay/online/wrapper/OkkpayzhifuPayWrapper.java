package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxiokkpay.OkkpayApi;
import com.play.pay.baxiokkpay.params.OkkpayParamCollect;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/** 入款 */
public class OkkpayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger log = LoggerFactory.getLogger(OkkpayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "okkpayzhifu";

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
		amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
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
		//log.error("notifyUrl = {}", notifyUrl);
		OkkpayParamCollect param = new OkkpayParamCollect();
		param.setAppId(appId);
		param.setCharset("utf-8");
		param.setChannelCode("FYS");
		param.setNotifyUrl(notifyUrl);
		param.setApiOrderNo(orderId);
		param.setTotalFee(amount);

		OkkpayResultOrder result = new OkkpayApi().collection(account, merchantKey, payGetway, param);
	//	log.error(JSON.toJSONString(result));
		object.put("success", result.isSuccess());
		object.put("url", result.getData());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		return object;
	}
}