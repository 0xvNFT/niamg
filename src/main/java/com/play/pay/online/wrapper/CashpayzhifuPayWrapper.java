package com.play.pay.online.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.PayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.pay.baxicashpay.CashpayApi;
import com.play.pay.baxicashpay.params.CashpayParamCollect;
import com.play.pay.baxicashpay.result.CashResultOrder;
import com.play.pay.baxicashpay.util.CashRSAEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class CashpayzhifuPayWrapper implements PayWrapper {
	/** log */
	//static Logger log = LoggerFactory.getLogger(CashpayzhifuPayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/notify/v2/";
	private static String iconCss = "cashpayzhifu";

	@Override
	public JSONObject wrap(String... params) {
		JSONObject object = new JSONObject();
		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, params[4]) + iconCss + ".do";
		CashpayParamCollect param = new CashpayParamCollect();
		// 保留两位小数 不足补0
		String amount = params[3];
		//支付公司接收的参数是分,所以需要 乘以100
		amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).toPlainString();
		// 保留两位小数 不足补0
		param.setAmount(amount);
		param.setNotifyUrl(notifyUrl);
		param.setMerchantOrderId(params[2]);
		CashResultOrder result = new CashpayApi().collection("", "", params[10], param,CashRSAEncrypt.build(params[11],params[1]));
		object.put("success", true);
		object.put("url", result.getPayUrl());
		object.put("returnType", "href");
		object.put("thirdOrderId", result.getMerchantOrderId());
		return object;
	}
}