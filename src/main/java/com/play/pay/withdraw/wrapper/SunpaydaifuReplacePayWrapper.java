package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxisunpay.SunpayApi;
import com.play.pay.baxisunpay.params.SunPayParamQueryOrder;
import com.play.pay.baxisunpay.params.SunpayParamPay;
import com.play.pay.baxisunpay.result.SunPayqueryOrderResultStatus;
import com.play.pay.baxisunpay.result.SunpayResultOrder;
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
public class SunpaydaifuReplacePayWrapper implements ReplacePayWrapper {
	private static Logger log = LoggerFactory.getLogger(SunpaydaifuReplacePayWrapper.class);
	private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
	private static String iconCss = "sunpaydaifu";
	private static JSONObject bankcodes = JSONObject.parseObject("{'USDT':'USDT'}");

	@Override
	public JSONObject wrap(String... params) {
		log.error("==========>>>>");
		/*for (int i = 0; i < params.length; i++) {
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
		String merchantAccount = params[6]; // 此处表示支付密钥
		String payGetway = params[10];
		String appId = params[11];
		String account = params[13];
	//	String cardName = params[15];
	//	String cardId = params[16];
		String cardNo = params[18];//DSTD是区块链地址
	//	String bankAddr = params[17];
		String notify_url = OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
		SunpayParamPay paramPay = new SunpayParamPay();
		paramPay.setOut_user_id(account);
		paramPay.setAmount(orderAmount);
		paramPay.setOut_order_no(orderNo);
		paramPay.setWebhook_url(notify_url);
		paramPay.setAddress(cardNo);
		// 这个没有就不要传了
		// param.setCpf(cardId);
		SunpayResultOrder result = new SunpayApi().agentpay(merchantAccount, mechKey, payGetway, paramPay);
		//log.error(JSON.toJSONString(result));
		JSONObject object = new JSONObject();
		if(result.getCode()==200 && result.isIs_success()==true){
			object.put("thirdOrderId", result.getData().getOrder_no());//我们的订单号
			object.put("payNo", result.getData().getOrder_no());//平台订单号
		}
	//	log.error("thirdOrderId = {}",orderNo);
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
		SunPayParamQueryOrder param = new SunPayParamQueryOrder();
		param.setOrderNo(mnyDrawRecord.getPayPlatformNo());
		SunPayqueryOrderResultStatus result = new SunpayApi().queryBalance(replaceWithDraw.getAccount(), replaceWithDraw.getMerchantKey(), replaceWithDraw.getSearchGetway(), param);
		    //2表示支付成功
		if (result.getData().equals("200")&&result.isIs_success()==true
		&&result.getData().getOrder_status().equals("SUCCESS")) {
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Sunpay代付成功");
			}
			//没有失败状态,占时不处理
		} else if (result.getData().getStatus().equals("0")||result.getData().getStatus().equals("7")){
			if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
				mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Sunpay代付失败");
			}
		}
	}
}