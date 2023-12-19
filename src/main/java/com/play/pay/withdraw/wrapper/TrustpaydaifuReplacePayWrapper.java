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
import com.play.pay.baxitrustpay.TrustpayPayApi;
import com.play.pay.baxitrustpay.para.TrustpayParamPay;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryBalance;
import com.play.pay.baxitrustpay.para.TrustpayParamQueryOrder;
import com.play.pay.baxitrustpay.result.TrustpayResultBalance;
import com.play.pay.baxitrustpay.result.TrustpayResultOrderPay;
import com.play.pay.baxitrustpay.result.TrustpayResultPay;
import com.play.service.MnyDrawRecordService;

/** 出款 */
public class TrustpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(TrustpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "trustpaydaifu";
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
	//	String account = params[13];
	//	String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];
	//	String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		TrustpayParamPay param = new TrustpayParamPay();
		param.setOrderNo(orderNo);
		param.setAmount(orderAmount);
		param.setMerchantId(mechCode);
		// PIX账号类型 1-CPF 2-CNPJ 3-EMAIL 4-PHONE(需要带上国际区号 例如+55XXXXXXX) 5-EVP
		BrazilPixAccountTypeEnum accountType = PayUtils.getBrazilPixAccountType(cardNo);
		if (accountType == null)
			throw new RuntimeException("accoutType invalid...");
		param.setPixType(accountType.name());
		cardNo = cardNo.replace(".", "").replace("-", "").replace("+55", "").replace("/", "");
		param.setCert(cardNo);
		param.setAccountNum(cardNo);
		param.setNotifyUrl(notify_url);
		TrustpayResultPay result = new TrustpayPayApi().agentpay(mechKey, payGetway, param);
	//	log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getData().getPayNo());
		object.put("payNo", result.getData().getPayNo());
	//	log.error("thirdOrderId = {}", result.getData().getPayNo());
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
		TrustpayParamQueryOrder param = new TrustpayParamQueryOrder();
		param.setMerchantId(replaceWithDraw.getMerchantCode());//商户号
		log.error("查询代付==商户号：" + replaceWithDraw.getMerchantCode());
		param.setPayNo(mnyDrawRecord.getPayPlatformNo());//三方平台订单号
		param.setCountry("2");
		log.error("查询代付==订单编号：" + mnyDrawRecord.getPayPlatformNo());
		TrustpayResultOrderPay result = new TrustpayPayApi().queryPayStatus(replaceWithDraw.getMerchantKey(), payGate, param);
		log.error("查询代付==三方查询结果"+JSON.toJSONString(result));
		if (result.getCode().compareTo("200") == 0) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "trustpay代付成功");
			}
		} else {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "trustpay代付失败");
			}
		}
	}
}