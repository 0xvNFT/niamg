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
import com.play.pay.baxitopay.util.TopayRSAEncrypt;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;

/**
 * 回调代付controller类
 */

public class GopaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
	//private static Logger logger = LoggerFactory.getLogger(GopaydaifuReplaceNotifyController.class);

	public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss, MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
		PrintWriter pWriter = response.getWriter();
		SortedMap<String, Object> map = getRequestJsonData(request);
//		logger.error("回调gopay代付数据:" + JSONObject.toJSONString(map));
		String ordernumber = map.get("out_trade_no").toString();
		// 用户提款记录
	//	logger.error("gopay代付成功收到回调通知，订单号：" + ordernumber);
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
		if (checkSign(map, replace.getAccount(), replace.getMerchantKey())) {
			if ("1".equals(map.get("status").toString())) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "gopay代付成功-");
				}
				pWriter.print("SUCCESS");
			} else if ("2".equals(map.get("status").toString())) {
				if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
					mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "gopay代付失败-");
				}
				pWriter.print("SUCCESS");
			} else {
	//			logger.error("unknow status:{}", map.get("status"));
			}
		}
	}

	/**
	 * 这个是用对方【私钥】加密的
	 * <p>
	 * 只要能解开 就没有问题了
	 */
	private boolean checkSign(SortedMap<String, Object> map, String publicKey, String privateKey) {
		try {
			String sign = map.get("sign").toString();
			String encodedString = TopayRSAEncrypt.decrypt(publicKey, sign);
		//	logger.error("三方签名认证成功" + ":" + encodedString);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		//	logger.error("三方签名认证失败：" + e);
			return false;
		}
	}
}