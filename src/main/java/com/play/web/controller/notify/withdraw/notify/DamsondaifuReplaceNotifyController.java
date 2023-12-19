package com.play.web.controller.notify.withdraw.notify;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.damson.util.SecretUtils;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;

/**
 * 回调代付controller类
 */

public class DamsondaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
	//private static Logger logger = LoggerFactory.getLogger(DamsondaifuReplaceNotifyController.class);

	public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss, MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
		PrintWriter pWriter = response.getWriter();
		SortedMap<String, Object> map = getRequestJsonData(request);
	//	logger.error("回调damson代付数据:" + JSONObject.toJSONString(map));
		String ordernumber = map.get("mchOrderNo").toString();
		// 用户提款记录
	//	logger.error("damson代付成功收到回调通知，订单号：" + ordernumber);
		MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
		if (mnyDrawRecord == null) {
	//		logger.error("订单号不存在");
			return;
		}
		// 判断记录是否处理未处理状态
		if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
	//		logger.error("此记录已经被处理过！");
			return;
		}
		StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
		if (replace == null) {
	//		logger.error("付款方式为空");
			return;
		}
		if (!authenticatedIP(request, replace.getWhiteListIp())) {
	//		logger.error("回调IP不在白名单内订单号:" + ordernumber);
			return;
		}
		if (checkSign(map, replace.getMerchantKey())) {
			if ("2".equals(map.get("status").toString())) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "damson代付成功-");
				}
				pWriter.print("SUCCESS");
			} else if ("3".equals(map.get("status").toString())) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "damson代付失败-");
				}
				pWriter.print("SUCCESS");
			} else if ("1".equals(map.get("status").toString())) {
	//			logger.error("订单在处理中，订单号：" + ordernumber);
			} else {
	//			logger.error("unknow status:{}", map.get("status"));
			}
		}
	}

	private boolean checkSign(SortedMap<String, Object> map, String key) {
		String sign = map.get("sign").toString();
		if (SecretUtils.verify(map, key)) {
	//		logger.info("三方签名认证通过：" + sign);
			return true;
		} else {
	//		logger.error("三方签名认证失败：" + sign);
			return false;
		}
	}
}