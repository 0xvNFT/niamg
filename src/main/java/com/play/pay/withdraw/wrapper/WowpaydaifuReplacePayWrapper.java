package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxitrustpay.TrustpayPayApi;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryBalance;
import com.play.pay.baxitrustpay.result.TrustpayResultBalance;
import com.play.pay.baxiuzpay.UzpayPayApi;
import com.play.pay.baxiuzpay.params.UzpayParamQueryBalance;
import com.play.pay.baxiuzpay.result.UzpayResultBalance;
import com.play.pay.baxiwowpay.WowpayPayApi;
import com.play.pay.baxiwowpay.params.WowpayParamPay;
import com.play.pay.baxiwowpay.result.WowpayResultOrder;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 出款 */
public class WowpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(WowpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "wowpaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		String mechCode = params[0];
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		// 保留两位小数 不足补0
		orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
		String merchantDomain = params[4];
		String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String cardName = params[15];
		String cardNo = params[18];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		WowpayParamPay param = new WowpayParamPay();
		param.setMch_id(mechCode);
		param.setMch_transferId(orderNo);
		param.setTransfer_amount(orderAmount);
		param.setReceive_name(cardName);
		param.setReceive_account(cardNo);
		param.setBack_url(notify_url);
		WowpayResultOrder result = new WowpayPayApi().agentpay(mechKey,merchantAccount, payGetway,param);
		JSONObject object = new JSONObject();
		if(result.getRespCode().equals("SUCCESS")){
			object.put("thirdOrderId", result.getMerTransferId());
		}
		return object;
	}

	@Override
	public List<String> getSupportBankList() {
		List<String> list = new ArrayList<>();
		Map<String, Object> object = bankcodes.getInnerMap();
		object.forEach((k, v) -> list.add(k));
		return list;
	}

	@Override
	public void searchBalance(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) throws Exception {
		String payGate = replaceWithDraw.getSearchGetway();
		TrustpayParamQueryBalance param = new TrustpayParamQueryBalance();
		param.setMerchantId(replaceWithDraw.getMerchantCode());
		TrustpayResultBalance result = new TrustpayPayApi().queryBalance(replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
		String payGate = replaceWithDraw.getPayGetway();
		log.error("查询代付==商户号：" + replaceWithDraw.getMerchantCode());
		UzpayParamQueryBalance param = new UzpayParamQueryBalance();
		param.setMch_id(replaceWithDraw.getMerchantCode());
		param.setMch_transferId(mnyDrawRecord.getOrderId());
		log.error("查询代付==订单编号：" + mnyDrawRecord.getPayPlatformNo());
		UzpayResultBalance result = new UzpayPayApi().queryBalance(replaceWithDraw.getAccount(),replaceWithDraw.getAccount(), payGate, param);
		log.error("查询代付==三方查询结果"+JSON.toJSONString(result));
		if (result.getTradeResult().equals("1")) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "trustpay代付成功");
			}
		} else if (result.getTradeResult().equals("2")||result.getTradeResult().equals("3")) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "trustpay代付失败");
			}
		}
	}
}