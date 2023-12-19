package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.PayUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.core.BrazilCashAccountTypeEnum;
import com.play.core.BrazilPixAccountTypeEnum;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxicashpay.CashpayApi;
import com.play.pay.baxicashpay.params.CashpayParamPay;
import com.play.pay.baxicashpay.params.CashpayParamQueryOrder;
import com.play.pay.baxicashpay.result.CashResultOrder;
import com.play.pay.baxicashpay.util.CashRSAEncrypt;
import com.play.pay.baxitopay.TopayPayApi;
import com.play.pay.baxitopay.params.TopayParamQueryBalance;
import com.play.pay.baxitopay.result.TopayResultBalance;
import com.play.service.MnyDrawRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** 出款 */
public class CashpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(CashpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "cashpaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'PIX':'PIX'}");

	@Override
	public JSONObject wrap(String... params) {
		log.error("==========>>>>");
		for (int i = 0; i < params.length; i++) {
			log.error("item[{}]= {}", i, params[i]);
		}
	//	String mechCode = params[0];
		String mechKey = params[1]; //此处表示接口密钥
		String orderNo = params[2];
		String orderAmount = params[3];
		// 保留两位小数 不足补0
		orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).toPlainString();
		String merchantDomain = params[4];
	//	String bankCode = params[5];
	//	String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String appId = params[11];
		String account = params[13];
		//String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];
		//String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		//同样pix 键就是上面5种格式，CPF:11位数；PHONE:11位数(可加前缀'+55')；EMAIL:邮箱格式；CNPJ:14位数；RANDOM: uuid格式
		//RANDOM不支持
		BrazilCashAccountTypeEnum accountType = PayUtils.getBrazilCashAccountType(cardNo);
		if (accountType == null)
			throw new RuntimeException("accoutType invalid...");
		CashpayParamPay param = new CashpayParamPay();
		param.setAccountType(accountType.name());
		param.setAccountNum(cardNo);
		param.setAmount(orderAmount);
		param.setMerchantOrderId(orderNo);
		param.setNotifyUrl(notify_url);
	//	param.setCustomerCert("32562523890");
		String headerAuthorization = CashRSAEncrypt.build(appId,mechKey);
		CashResultOrder result = new CashpayApi().agentpay(account,mechKey,payGetway,param,headerAuthorization);
	//	log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		object.put("thirdOrderId", result.getMerchantOrderId());
		/*log.error("thirdOrderId = {}", result.getData().getTrade_no());*/
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
		String headerAuthorization = CashRSAEncrypt.build(replaceWithDraw.getAppid(),replaceWithDraw.getMerchantKey());
		CashpayParamQueryOrder param = new CashpayParamQueryOrder();
		param.setMerchantOrderId(mnyDrawRecord.getOrderId());
		CashResultOrder result = new CashpayApi().queryOrderStatus("", "", payGate, param,headerAuthorization);
		log.error(JSON.toJSONString(result));
		if (result.getCode().compareTo("200") == 0) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "cash代付成功");
			}
		} else if (result.getCode().compareTo("404") == 0) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "cash代付失败");
			}
		}
	}
}