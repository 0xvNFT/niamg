package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.PayUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.core.BrazilPixAccountTypeEnum;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxiokkpay.OkkpayApi;
import com.play.pay.baxiokkpay.params.OkkPayParamQueryOrder;
import com.play.pay.baxiokkpay.params.OkkpayParamPay;
import com.play.pay.baxiokkpay.result.OkkPayqueryOrderResultStatus;
import com.play.pay.baxiokkpay.result.OkkpayResultOrder;
import com.play.pay.baxiowenpay.OwenpayPayApi;
import com.play.pay.baxiowenpay.params.OwenpayParamPay;
import com.play.pay.baxiowenpay.params.OwenpayParamQueryOrder;
import com.play.pay.baxiowenpay.result.OwenResultOrder;
import com.play.pay.baxiowenpay.result.OwenpayResultBalance;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import com.play.pay.baxitopay.TopayPayApi;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.pay.baxiwowpay.util.DateUtil;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 出款 */
public class OwenpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(OwenpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "owenpaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		// 保留两位小数 不足补0
		orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).toPlainString();
		String merchantDomain = params[4];
	//	String bankCode = params[5];
		String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String appId = params[11];
		//String account = params[13];
	//	String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];
	//	String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		OwenpayParamPay param = new OwenpayParamPay(merchantAccount);
		param.setAppID(appId);
		param.setOutTradeNo(orderNo);
		param.setBankAcctNo(OwenRSAEncrypt.Encrypt3DES(cardNo,merchantAccount));
		param.setTotalAmount(OwenRSAEncrypt.Encrypt3DES(OwenRSAEncrypt.numberRemoveZero(orderAmount),merchantAccount));
		param.setAccPhone(OwenRSAEncrypt.Encrypt3DES("+552112345678",merchantAccount));
		BrazilPixAccountTypeEnum accountType = PayUtils.getBrazilPixAccountType(cardNo);
		if (accountType == null)
			throw new RuntimeException("accoutType invalid...");
		param.setIdentityType(accountType.name());
		param.setNotifyUrl(notify_url);
		OwenResultOrder result = new OwenpayPayApi().agentpay("", mechKey, payGetway, param);
		JSONObject object = new JSONObject();
		if(result.getResultCode().equals("0000")){
			object.put("thirdOrderId", result.getOutTradeNo());
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
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
		String payGate = replaceWithDraw.getSearchGetway();
		OwenpayParamQueryOrder param = new OwenpayParamQueryOrder();
		param.setRemitDate(DateUtil.date2String(mnyDrawRecord.getCreateDatetime(), DateUtil.YYYY_MM_DD));
		param.setAppID(replaceWithDraw.getAppid());
		param.setOutTradeNo(mnyDrawRecord.getOrderId());
		OwenpayResultBalance result = new OwenpayPayApi().queryBalance(replaceWithDraw.getAccount(), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
		if (result.getResultCode().equals("0000")&&result.getRemitResult().equals("00")) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Owenpay代付成功");
			}
		} else if (result.getResultCode().equals("0000")&&result.getRemitResult().equals("02")){
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Owenpay代付失败");
			}
		}
		else if (result.getResultCode().equals("0000")&&result.getRemitResult().equals("1000")){
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Owenpay代付失败");
			}
		}
	}
}