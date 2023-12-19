package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;

import com.play.common.ip.IpUtils;
import com.play.common.utils.security.MD5Util;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;

import com.play.pay.yespay.result.YesOrderResult;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;

/**
 * 回调代付controller类
 */

public class YespaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
	//private static Logger logger = LoggerFactory.getLogger(YespaydaifuReplaceNotifyController.class);

	public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss, MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
		//	logger.error("---------------- YesPay daifu notify ----------------");
		PrintWriter pWriter = response.getWriter();

		SortedMap<String, Object> map = getRequestJsonData(request);
		logger.error("YesPay daifu notify, map:{}", JSONObject.toJSON(map));

		if(!"1".equals(map.get("code").toString())) {
			logger.error("YesPay daifu notify, error:{}", map.get("msg").toString());
			return;
		}

		Object resultStr = map.get("data");
		YesOrderResult yesOrderResult = JSONObject.parseObject(resultStr.toString(), YesOrderResult.class);

		String orderId = yesOrderResult.getOrderid();

		// 用户提款记录
		MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(orderId, SystemUtil.getStationId());
		if (mnyDrawRecord == null) {
			logger.error("YesPay daifu notify, order is not exist, orderId:{}, stationId:{}", orderId, SystemUtil.getStationId());
			return;
		}
		// 判断记录是否处理未处理状态
		if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
			logger.error("YesPay daifu notify, order status is error, orderStatus:{}, orderRemark:{}", mnyDrawRecord.getStatus(), mnyDrawRecord.getRemark());
			return;
		}
		StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
		if (replace == null) {
			logger.error("YesPay daifu notify, online payment is null, payId:{}, stationId:{}", mnyDrawRecord.getPayId(), SystemUtil.getStationId());
			return;
		}
		if (!authenticatedIP(request, replace.getWhiteListIp())) {
			logger.error("YesPay daifu notify, IP is not in the white list, ip:{}", IpUtils.getSafeIpAdrress(request));
			return;
		}
		if (checkSign(replace.getMerchantKey(), orderId, yesOrderResult.getAmount().toString(), yesOrderResult.getSign())) {
			if (1 == yesOrderResult.getIspay()) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "YesPay代付成功");
				}
				pWriter.print("success");
			} else if (1 == yesOrderResult.getIscancel()) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_FAIL, "YesPay代付失败");
				}
				pWriter.print("success");
			} else {
				logger.error("YesPay daifu notify, isPay:{}, isCancel:{}", yesOrderResult.getIspay(), yesOrderResult.getIscancel());
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "YesPay代付未处理");
			}
		}
	}

	private boolean checkSign(String apiKey, String orderId, String amount, String sign) {
		StringBuffer sb = new StringBuffer(apiKey).append(orderId).append(amount);
		String signedValue = MD5Util.md5(sb.toString().toLowerCase()).toLowerCase();
		boolean bool = sign.equals(signedValue);
	//	logger.error("YesPay daifu notify, checkSign:{}", bool);
		return bool;
	}
}