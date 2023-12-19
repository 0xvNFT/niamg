package com.play.pay.withdraw.wrapper;

import java.math.BigDecimal;
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
import com.play.pay.baxitopay.TopayPayApi;
import com.play.pay.baxitopay.params.TopayParamPay;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.params.TopayParamQueryOrder;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.pay.baxitopay.result.TopayResultOrder;
import com.play.service.MnyDrawRecordService;

/** 出款 */
public class TopaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(TopaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "topaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		/*log.error("==========>>>>");
		for (int i = 0; i < params.length; i++) {
			log.error("item[{}]= {}", i, params[i]);
		}*/
		String mechCode = params[0];
		String mechKey = params[1]; // 此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		// 保留两位小数 不足补0
		orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
		String merchantDomain = params[4];
	//	String bankCode = params[5];
	//	String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
	//	String appId = params[11];
		String account = params[13];
	//	String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];
	//	String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		TopayParamPay param = new TopayParamPay();
		param.setPay_amount(orderAmount);
		param.setMerchant_no(mechCode);
		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		BrazilPixAccountTypeEnum accountType = PayUtils.getBrazilPixAccountType(cardNo);
		if (accountType == null)
			throw new RuntimeException("accoutType invalid...");
		param.setPix_type(accountType.name());
		param.setDict_key(cardNo.replace(".", "").replace("-", "").replace("+55", "").replace("/", ""));
		param.setDescription("withdraw");
		param.setOut_trade_no(orderNo);
		param.setNotify_url(notify_url);
		param.setName(account);
		// 这个没有就不要传了
		// param.setCpf(cardId);
		TopayResultOrder result = new TopayPayApi().agentpay(account, mechKey, payGetway, param);
		//log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getData().getTrade_no());
	//	log.error("thirdOrderId = {}", result.getData().getTrade_no());
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
		TopayParamQueryOrder param = new TopayParamQueryOrder();
		param.setMerchant_no(replaceWithDraw.getMerchantCode());
		param.setOut_trade_no(mnyDrawRecord.getOrderId());
		TopayResultOrder result = new TopayPayApi().queryOrderStatus(replaceWithDraw.getAccount(), replaceWithDraw.getMerchantKey(), payGate, param);
		log.error(JSON.toJSONString(result));
		if (result.getCode().compareTo("0") == 0) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "topay代付成功");
			}
		} else {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "topay代付失败");
			}
		}
	}
}