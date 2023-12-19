package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.yespay.YesPayApi;
import com.play.pay.yespay.param.YesBaseParam;
import com.play.pay.yespay.param.YesPayLoadParam;
import com.play.pay.yespay.param.YesPayParam;
import com.play.pay.yespay.result.YesBalanceResult;
import com.play.pay.yespay.result.YesOrderResult;
import com.play.pay.yespay.result.YesPayResult;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YespaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger logger = LoggerFactory.getLogger(YespaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "yespaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'TCB':'TCB', 'SACOM':'SACOM', 'VCB':'VCB', 'ACB':'ACB', 'DAB':'DAB', 'VTB':'VTB'}");

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

		String notifyUrl = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		YesPayParam param = new YesPayParam();
		param.setUserid(mechCode);
		param.setOrderid(orderNo);
		param.setAmount(new BigDecimal(orderAmount).setScale(4, BigDecimal.ROUND_HALF_DOWN));
		param.setNotifyurl(notifyUrl);
		param.setReturnurl(merchantDomain);
		param.setNote("");

		YesPayLoadParam loadParam = new YesPayLoadParam();
		loadParam.setCardname(cardName);
		loadParam.setCardno(cardNo);
		loadParam.setBankid(cardId);
		loadParam.setBankname(account);

		YesPayResult result = new YesPayApi().agentPay(mechKey, payGetway, param);
		logger.error("YesPaydaifu wrap, result:{}", JSON.toJSON(result));

		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getTicket());
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
		YesBaseParam param = new YesBaseParam();
		param.setUserid(replaceWithDraw.getMerchantCode());
		param.setNoncestr(RandomStringUtils.randomAll(16));
		param.setTimestamp(String.valueOf(System.currentTimeMillis()));

		YesBalanceResult result = new YesPayApi().queryBalance(replaceWithDraw.getMerchantKey(), replaceWithDraw.getSearchGetway(), param);
		logger.error("YesPaydaifu searchBalance, result:{}", JSON.toJSON(result));
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
		YesBaseParam param = new YesBaseParam();
		param.setUserid(replaceWithDraw.getMerchantCode());
		param.setNoncestr(RandomStringUtils.randomAll(16));
		param.setTimestamp(String.valueOf(System.currentTimeMillis()));
		param.setOrderid(mnyDrawRecord.getOrderId());

		YesOrderResult result = new YesPayApi().queryInfoByOrderId(replaceWithDraw.getMerchantKey(), replaceWithDraw.getSearchGetway(), param);
		logger.error("YesPaydaifu search, result:{}", JSON.toJSON(result));

		if (1 == result.getIspay()) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "YesPay代付成功");
			}
		}else if (0 == result.getIspay()) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_FAIL, "YesPay代付失败");
			}
		}else {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "YesPay未处理");
			}
		}
	}
}