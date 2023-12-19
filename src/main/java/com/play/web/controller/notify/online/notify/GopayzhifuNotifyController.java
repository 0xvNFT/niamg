package com.play.web.controller.notify.online.notify;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.play.common.PayNotifyWrapper;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.baxitopay.util.TopayRSAEncrypt;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;

public class GopayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
	private static Logger logger = LoggerFactory.getLogger(GopayzhifuNotifyController.class);

	@Override
	public void notify(HttpServletRequest request, HttpServletResponse response, String name, MnyDepositRecordService mnyDepositRecordService, StationDepositOnlineService stationDepositOnlineService) throws IOException {
		//logger.error("开始进入Gopay支付回调");
		PrintWriter pWriter = response.getWriter();
		SortedMap<String, Object> map = getRequestJsonData(request);
	//	logger.error("Gopay支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
		String orderNumber = map.get("out_trade_no").toString();
	//	logger.error("Gopay支付成功收到回调通知，订单号：" + orderNumber);
		MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
		if (record == null) {
	//		logger.error("订单号不存在");
			return;
		}
		StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
		if (online == null) {
	//		logger.error("付款方式为空");
			return;
		}
		if (!authenticatedIP(request, online.getWhiteListIp())) {
	//		logger.error("回调IP不在白名单内订单号:" + orderNumber);
			return;
		}
		String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
		BigDecimal amount = new BigDecimal(map.get("pay_amount").toString());
	//	logger.info(payName + "三方回调金额：" + amount);
		// 签名校验
		if (checkSign(map, online.getAccount(), online.getMerchantKey(), payName)) {
			// 状态判断
			if (map.get("status").toString().equalsIgnoreCase("1")) {
				synchronized (this) {
					//mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS, amount, String.format("%s在线充值成功", payName));
					List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
					mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS, amount, I18nTool.convertCodeToArrStr(remarkList));
				}
				pWriter.print("SUCCESS");
			}
		}
	}

	/**
	 * 这个是用对方【私钥】加密的
	 * <p>
	 * 只要能解开 就没有问题了
	 */
	private boolean checkSign(SortedMap<String, Object> map, String publicKey, String privateKey, String payName) {
		try {
			String sign = map.get("sign").toString();
			String encodedString = TopayRSAEncrypt.decrypt(publicKey, sign);
		//	logger.error(payName + " 三方签名认证成功" + ":" + encodedString);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		//	logger.error(payName + ":" + "三方签名认证失败：" + e);
			return false;
		}
	}
}
