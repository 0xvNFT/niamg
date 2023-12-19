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
import com.play.pay.speedlypay.SpeedlyPayApi;
import com.play.pay.speedlypay.param.SpeedlyBaseParam;
import com.play.pay.speedlypay.param.SpeedlyPayParam;
import com.play.pay.speedlypay.param.SpeedlyPayerOrPayeeParam;
import com.play.pay.speedlypay.result.SpeedlyBalanceResult;
import com.play.pay.speedlypay.result.SpeedlyOrderResult;
import com.play.pay.speedlypay.result.SpeedlyPayResult;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpeedlydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger logger = LoggerFactory.getLogger(SpeedlydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "speedlydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		logger.error("==========>>>>");
		for (int i = 0; i < params.length; i++) {
			logger.error("item[{}]= {}", i, params[i]);
		}
		String mechCode = params[0];
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		String merchantDomain = params[4];
		String bankCode = params[5];
		String merchantAccount = params[6]; // 此处表示支付密钥
		String customerIp = params[7];
		String referer = params[8];
		String payType = params[9];
		String payGetway = params[10];
		String appId = params[11];
		String domain = params[12];
		String account = params[13];
		String extra = params[14];
		String cardName = params[15];
		String cardId = params[16];
		String bankAddr = params[17];
		String cardNo = params[18];

		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		BrazilPixAccountTypeEnum accountType = PayUtils.getBrazilPixAccountType(cardNo);
		if (accountType == null) {
			logger.error("Speedlydaifu wrap error, accountType invalid, cardNo:{}", cardNo);
			throw new RuntimeException("accountType invalid...");
		}

		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";

		SpeedlyPayParam param = new SpeedlyPayParam();
		param.setCountry("BR");
		param.setCurrency("BRL");
		param.setOrder_id(orderNo);
		param.setAmount(new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN));
		param.setNotification_url(notifyUrl);
		param.setTimestamp(Instant.now().toEpochMilli());

		SpeedlyPayerOrPayeeParam payeeParam = new SpeedlyPayerOrPayeeParam();
		payeeParam.setName(cardName);
		payeeParam.setAccount(cardNo);
		payeeParam.setAccount_type(accountType.name());
		payeeParam.setPhone(cardNo);
		SpeedlyPayResult result = new SpeedlyPayApi().pay(mechCode, appId, mechKey, payGetway, param, payeeParam);

		logger.error("Speedlydaifu wrap, result:{}", JSON.toJSON(result));

		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getSettlement_id());
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

		SpeedlyBalanceResult result = new SpeedlyPayApi().queryBalance(replaceWithDraw.getMerchantCode(), replaceWithDraw.getAppid(),
				replaceWithDraw.getMerchantKey(), replaceWithDraw.getSearchGetway());

		logger.error("Speedlydaifu searchBalance, result:{}", JSON.toJSON(result));
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {

		SpeedlyBaseParam param = new SpeedlyBaseParam();
		param.setOrder_id(mnyDrawRecord.getOrderId());
		SpeedlyOrderResult result = new SpeedlyPayApi().queryPayOrder(replaceWithDraw.getMerchantCode(), replaceWithDraw.getAppid(), replaceWithDraw.getSearchGetway(), param);
		logger.error("Speedlydaifu search, result:{}", JSON.toJSON(result));

		if (100 == result.getStatus_code()) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Speedly代付成功");
			}
		}else if (4 == result.getStatus_code() || 5 == result.getStatus_code()) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_FAIL, "Speedly代付失败");
			}
		}else {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Speedly未处理");
			}
		}
	}
}