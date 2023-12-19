package com.play.pay.withdraw.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.PayUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.core.BrazilPixAccountTypeEnum;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.damson.DamsonPayApi;
import com.play.pay.damson.params.BaseParam;
import com.play.pay.damson.params.PayParams;
import com.play.pay.damson.params.query.QueryParam;
import com.play.pay.damson.result.PayResult;
import com.play.pay.damson.result.query.QueryBalanceResult;
import com.play.pay.damson.result.query.QueryOrderResult;
import com.play.service.MnyDrawRecordService;

public class DamsondaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(DamsondaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "damsondaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		log.error("==========>>>>");
		for (int i = 0; i < params.length; i++) {
			log.error("item[{}]= {}", i, params[i]);
		}
		String mechCode = params[0];
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		String merchantDomain = params[4];
		String bankCode = params[5];
		String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String appId = params[11];
		String account = params[13];
		String cardName = params[15];
		String cardNo = params[18];
		String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		PayParams param = new PayParams();
		// 以分为单位
		param.setAmount(Long.valueOf(orderAmount) * 100);
		param.setAppId(appId);
		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		BrazilPixAccountTypeEnum accountType = PayUtils.getBrazilPixAccountType(cardNo);
		if (accountType == null)
			throw new RuntimeException("accoutType invalid...");
		param.setAccountType(accountType.getType());
		param.setAccountNo(cardNo);
		param.setRemark("withdraw");
		param.setMchOrderNo(orderNo);
		param.setNotifyUrl(notify_url);
		// tetst param
		param.setAccountName(account);
		param.setEmail("");
		param.setPhone("");
		PayResult result = new DamsonPayApi().agentpay(Long.valueOf(mechCode), mechKey, payGetway, param);
		log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getOrderNo());
		log.error("thirdOrderId = {}", result.getOrderNo());
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
		BaseParam param = new BaseParam();
		param.setAppId(replaceWithDraw.getAppid());
		QueryBalanceResult result = new DamsonPayApi().queryBalance(Long.valueOf(replaceWithDraw.getMerchantCode()), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
	}

	@Override
	public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
		String payGate = replaceWithDraw.getSearchGetway();
		QueryParam param = new QueryParam();
		param.setMchOrderNo(mnyDrawRecord.getOrderId());
		QueryOrderResult result = new DamsonPayApi().queryAgentpay(Long.valueOf(replaceWithDraw.getMerchantCode()), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
		if (result.getResCode().compareTo("SUCCESS") == 0) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "damson代付成功");
			}
		} else {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "damson代付失败");
			}
		}
	}
}