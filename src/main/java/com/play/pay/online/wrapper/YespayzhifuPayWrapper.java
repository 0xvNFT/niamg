package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.yespay.YesPayApi;
import com.play.pay.yespay.param.YesCollectionParam;
import com.play.pay.yespay.result.YesCollectionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class YespayzhifuPayWrapper implements PayWrapper {
	/** log */
	static Logger logger = LoggerFactory.getLogger(YespayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "yespayzhifu";

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
		logger.error("YesPayzhifuPayWrapper notifyUrl = {}", notifyUrl);

		YesCollectionParam param = new YesCollectionParam();
		param.setUserid(merchantCode);
		param.setOrderid(orderId);
		param.setAmount(new BigDecimal(amount).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		param.setType(bankcode.toLowerCase());
		param.setNotifyurl(notifyUrl);
		param.setReturnurl(merchantDomain);
		param.setNote("");

		logger.error("YesPayzhifuPayWrapper param:{}", JSON.toJSON(param));
		YesCollectionResult result = new YesPayApi().collection(merchantKey, payGetway, param);
		logger.error("YesPayzhifuPayWrapper result:{}", JSON.toJSON(result));

		object.put("success", true);
		object.put("url", result.getPageurl());
		object.put("returnType", "href");
		object.put("orderId", orderId);
		object.put("thirdOrderId", result.getTicket());
		return object;
	}
}