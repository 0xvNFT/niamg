package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.common.PayNotifyWrapper;

import com.play.common.ip.IpUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.yespay.result.YesOrderResult;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import java.util.List;
import java.util.SortedMap;

public class YespayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
	//private static Logger logger = LoggerFactory.getLogger(YespayzhifuNotifyController.class);

	@Override
	public void notify(HttpServletRequest request, HttpServletResponse response, String name, MnyDepositRecordService mnyDepositRecordService, StationDepositOnlineService stationDepositOnlineService) throws IOException {
	//	logger.error("---------------- YesPay zhifu notify ----------------");

		PrintWriter pWriter = response.getWriter();
		SortedMap<String, Object> map = getRequestJsonData(request);
		logger.error("YesPay zhifu notify, map:{}", JSONObject.toJSON(map));

		if(!"1".equals(map.get("code").toString())) {
			logger.error("YesPay zhifu notify, error:{}", map.get("msg").toString());
			return;
		}

		Object resultStr = map.get("data");
		YesOrderResult yesOrderResult = JSONObject.parseObject(resultStr.toString(), YesOrderResult.class);

		String orderId = yesOrderResult.getOrderid();

		// 用户充值记录
		MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderId);
		if (record == null) {
			logger.error("YesPay zhifu notify, order is not exist, orderId:{}", orderId);
			return;
		}
		StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
		if (online == null) {
			logger.error("YesPay zhifu notify, online payment is null");
			return;
		}
		if (!authenticatedIP(request, online.getWhiteListIp())) {
			logger.error("YesPay zhifu notify, IP is not in the white list, ip:{}", IpUtils.getSafeIpAdrress(request));
			return;
		}

		String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
		String apiKey = online.getMerchantKey();
		BigDecimal amount = yesOrderResult.getAmount();
		logger.error("YesPay zhifu notify, payName:{}, amount:{}", payName, amount);

		// 签名校验
		if (checkSign(apiKey, orderId, amount.toString(), yesOrderResult.getSign())) {
			// 状态判断
			if (1 ==  yesOrderResult.getIspay()) {
				synchronized (this) {
					//mnyDepositRecordService.onlineDepositNotifyOpe(orderId, MnyDepositRecord.STATUS_SUCCESS, amount, String.format("%s在线充值成功", payName));
					List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
					mnyDepositRecordService.onlineDepositNotifyOpe(orderId, MnyDepositRecord.STATUS_SUCCESS, amount, I18nTool.convertCodeToArrStr(remarkList));
				}
				pWriter.print("success");
			}
		}
	}

	private boolean checkSign(String apiKey, String orderId, String amount, String sign) {
		StringBuffer sb = new StringBuffer(apiKey).append(orderId).append(amount);
		String signedValue = MD5Util.md5(sb.toString().toLowerCase()).toLowerCase();
		boolean bool = sign.equals(signedValue);
	//	logger.error("YesPay zhifu notify, checkSign:{}", bool);
		return bool;
	}
}
