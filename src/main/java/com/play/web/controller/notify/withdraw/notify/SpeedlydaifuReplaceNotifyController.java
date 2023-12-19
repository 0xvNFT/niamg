package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.common.ip.IpUtils;
import com.play.common.utils.security.MD5Util;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.speedlypay.result.SpeedlyOrderResult;
import com.play.pay.speedlypay.util.SecretUtils;
import com.play.pay.yespay.result.YesOrderResult;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;

/**
 * 回调代付controller类
 */

public class SpeedlydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
	private static Logger logger = LoggerFactory.getLogger(SpeedlydaifuReplaceNotifyController.class);

	public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss, MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
		//	logger.error("---------------- Speedly daifu notify ----------------");
		PrintWriter pWriter = response.getWriter();

		SortedMap<String, Object> map = getRequestJsonData(request);
		logger.error("Speedly daifu notify, map:{}", JSONObject.toJSON(map));

		if(!"100".equals(map.get("status_code").toString())) {
			logger.error("Speedly daifu notify, error:{}", map.get("status_detail").toString());
			return;
		}

		Object reqData = JSONObject.toJSON(map);
		SpeedlyOrderResult result = JSONObject.parseObject(reqData.toString(), SpeedlyOrderResult.class);
		String orderId = result.getOrder_id();

		// 用户提款记录
		MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(orderId, SystemUtil.getStationId());
		if (mnyDrawRecord == null) {
			logger.error("Speedly daifu notify, order is not exist, orderId:{}, stationId:{}", orderId, SystemUtil.getStationId());
			return;
		}
		// 判断记录是否处理未处理状态
		if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
			logger.error("Speedly daifu notify, order status is error, orderStatus:{}, orderRemark:{}", mnyDrawRecord.getStatus(), mnyDrawRecord.getRemark());
			return;
		}
		StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
		if (replace == null) {
			logger.error("Speedly daifu notify, online payment is null, payId:{}, stationId:{}", mnyDrawRecord.getPayId(), SystemUtil.getStationId());
			return;
		}
		if (!authenticatedIP(request, replace.getWhiteListIp())) {
			logger.error("Speedly daifu notify, IP is not in the white list, ip:{}", IpUtils.getSafeIpAdrress(request));
			return;
		}

		// 签名校验
		if (checkSign(map, replace.getMerchantKey())) {
			if (100 == result.getStatus_code()) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Speedly代付成功");
				}
				pWriter.print("success");
			} else if (4 == result.getStatus_code() || 5 == result.getStatus_code()) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_FAIL, "Speedly代付失败");
				}
				pWriter.print("success");
			} else {
				logger.error("Speedly daifu notify, statusDetail:{}", result.getStatus_detail());
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Speedly代付未处理");
			}
		}
	}

	private boolean checkSign(SortedMap<String, Object> map, String key) {
		if (SecretUtils.verify(map, key)) {
			return true;
		}
		logger.error("Speedly daifu notify checkSign false, orderId:{}", map.get("order_id"));
		return false;
	}
}