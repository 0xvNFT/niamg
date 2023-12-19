package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxiokkpay.OkkpayApi;
import com.play.pay.baxiokkpay.params.OkkPayParamQueryOrder;
import com.play.pay.baxiokkpay.params.OkkpayParamPay;
import com.play.pay.baxiokkpay.result.OkkPayqueryOrderResultStatus;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import com.play.pay.baxitopay.TopayPayApi;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.params.TopayParamQueryOrder;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.pay.baxitopay.result.TopayResultOrder;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 出款 */
public class OkkpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(OkkpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "okkpaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		// 保留两位小数 不足补0
		orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
		String merchantDomain = params[4];
	//	String bankCode = params[5];
	//	String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String appId = params[11];
		String account = params[13];
	//	String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];
	//	String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		OkkpayParamPay okkpayResultOrder = new OkkpayParamPay();
		okkpayResultOrder.setAppId(appId);
		okkpayResultOrder.setApiOrderNo(orderNo);
		okkpayResultOrder.setTotalFee(orderAmount);
		okkpayResultOrder.setNotifyUrl(notify_url);
		okkpayResultOrder.setBankcardNo(cardNo);
		OkkpayResultOrder result = new OkkpayApi().agentpay(account, mechKey, payGetway, okkpayResultOrder);
		JSONObject object = new JSONObject();
		if(result.getCode()==2000){
			object.put("thirdOrderId", orderNo);
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
		TopayParamQueryBalance param = new TopayParamQueryBalance();
		param.setMerchant_no(replaceWithDraw.getMerchantCode());
		param.setTimestamp(String.valueOf((Long) (System.currentTimeMillis() / 1000)));
		TopayResultBalance result = new TopayPayApi().queryBalance(replaceWithDraw.getAccount(), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
		String payGate = replaceWithDraw.getSearchGetway();
/*		TopayParamQueryOrder param = new TopayParamQueryOrder();
		param.setMerchant_no(replaceWithDraw.getMerchantCode());
		param.setOut_trade_no(mnyDrawRecord.getOrderId());*/
		/*OkkPayParamQueryOrder param = new OkkPayParamQueryOrder();
		param.setAppId(replaceWithDraw.getAppid());
		param.setApiOrderNo(mnyDrawRecord.getOrderId());
		param.setCharset("utf-8");*/
		OkkPayParamQueryOrder param = new OkkPayParamQueryOrder();
		param.setAppId(replaceWithDraw.getAppid());
		param.setApiOrderNo(mnyDrawRecord.getOrderId());
		param.setCharset("utf-8");
		OkkPayqueryOrderResultStatus result = new OkkpayApi().queryBalance(replaceWithDraw.getAccount(), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
		    //2表示支付成功
		if (result.getData().getStatus().equals("2")) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Okkpay代付成功");
			}
			//0是失败 7是被驳回
		} else if (result.getData().getStatus().equals("0")||result.getData().getStatus().equals("7")){
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Okkpay代付失败");
			}
		}
	}
}