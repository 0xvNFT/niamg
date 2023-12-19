package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.common.PayNotifyWrapper;
import com.play.common.ip.IpUtils;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.speedlypay.result.SpeedlyOrderResult;
import com.play.pay.speedlypay.util.SecretUtils;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;

public class SpeedlyzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
	private static Logger logger = LoggerFactory.getLogger(SpeedlyzhifuNotifyController.class);

	@Override
	public void notify(HttpServletRequest request, HttpServletResponse response, String name, MnyDepositRecordService mnyDepositRecordService, StationDepositOnlineService stationDepositOnlineService) throws IOException {
		//logger.error("---------------- SpeedlyPay zhifu notify ----------------");

		PrintWriter pWriter = response.getWriter();
		SortedMap<String, Object> map = getRequestJsonData(request);
		Object reqData = JSONObject.toJSON(map);
		logger.error("Speedly zhifu notify, map:{}", reqData);

		SpeedlyOrderResult result = JSONObject.parseObject(reqData.toString(), SpeedlyOrderResult.class);
		String orderId = result.getOrder_id();

		// 用户充值记录
		MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderId);
		if (record == null) {
			logger.error("Speedly zhifu notify, order is not exist, orderId:{}", orderId);
			return;
		}
		StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
		if (online == null) {
			logger.error("Speedly zhifu notify, online payment is null, orderId:{}, payId:{}", orderId, record.getPayId());
			return;
		}
		if (!authenticatedIP(request, online.getWhiteListIp())) {
			logger.error("Speedly zhifu notify, IP is not in the white list, orderId:{}, ip:{}", orderId, IpUtils.getSafeIpAdrress(request));
			return;
		}

		String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
		String collKey = online.getMerchantKey();
		BigDecimal amount = result.getAmount();

		// 签名校验
		if (checkSign(map, collKey)) {
			// 状态判断
			if (100 ==  result.getStatus_code()) {
				synchronized (this) {
					//mnyDepositRecordService.onlineDepositNotifyOpe(orderId, MnyDepositRecord.STATUS_SUCCESS, amount, String.format("%s在线充值成功", payName));
					List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
					mnyDepositRecordService.onlineDepositNotifyOpe(orderId, MnyDepositRecord.STATUS_SUCCESS, amount, I18nTool.convertCodeToArrStr(remarkList));
				}
				logger.error("Speedly zhifu notify Success, orderId:{}", orderId);
				pWriter.print("SUCCESS");
			} else {
				logger.error("Speedly zhifu notify error, orderId:{}, msg:{}", orderId, result.getStatus_detail());
			}
		}
	}

	private boolean checkSign(SortedMap<String, Object> map, String key) {
		if (SecretUtils.verify(map, key)) {
			return true;
		}
		logger.error("Speedly zhifu notify checkSign false, orderId:{}", map.get("order_id"));
		return false;
	}
}
