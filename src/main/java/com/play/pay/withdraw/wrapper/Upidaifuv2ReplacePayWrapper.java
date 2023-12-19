package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplacePayWrapper;
import com.play.common.utils.security.HttpClientUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.service.MnyDrawRecordService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

public class Upidaifuv2ReplacePayWrapper implements ReplacePayWrapper {
    private static Logger logger = LoggerFactory.getLogger(Upidaifuv2ReplacePayWrapper.class);
    private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
    private static String iconCss = "upidaifuv2";
    private static JSONObject bankcodes = JSONObject.parseObject("{'Allahabad Bank':'ALHB','Andhra Bank':'ANDB','Axis Bank':'AXIS','Bank of India':'BOIN','Bank of Baroda':'BRDA','Canara Bank':'CANBK','Central Bank of India':'CBOI','Citi Bank':'CITI','Corporation Bank':'CORB','Catholic Syrian Bank':'CSYB','Dena Bank':'DENB','Dhanlaxmi Bank':'DNLM','Federal Bank':'FEDB','HDFC Bank':'HDFC','ICICI Bank':'ICICI','IDBI Bank':'IDBI','Indian Bank':'INDNB','IndusInd Bank':'INDU','Indian Overseas Bank':'INOB','Jammu and Kashmir Bank':'JAKB','Karnataka Bank':'KARBK','Kotak Mahindra Bank':'KOTBK','Bank of Maharashtra':'MHRT','Oriental Bank of Commerce':'OBOC','Punjab and Sind Bank':'PASB','Punjab National Bank':'PNJB','State Bank of Bikaner and Jaipur':'SBBJ','State Bank of India':'SBIN','State Bank of Hyderabad':'SBOH','State Bank of Travancore':'SBOT','Standard Chartered Bank':'SCTRD','South Indian Bank':'SIBK','Syndicate Bank':'SYNB','Tamilnadu Mercantile Bank':'TAMB','UCO Bank':'UCOB','Union Bank of India':'UNBOI','Vijaya Bank':'VJYB','Yes Bank':'YESB'}");
    @Override
    public JSONObject wrap(String... params) {
        String mechCode = params[0];
        String mechKey = params[1]; //此处表示接口密钥
        String orderNo = params[2];
        String orderAmount = params[3];
        String merchantDomain = params[4];
        String bankCode = params[5];
 //       String merchantAccount = params[6];  //此处表示支付密钥
        String payGetway = params[10];
        String cardName = params[15];
        String cardNo = params[18];
  //      String bankAddr = params[17];
        String notify_url=OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
        SortedMap<String, String> map = new TreeMap<>();
        map.put("merchant", mechCode); //商户号
        map.put("orderId", orderNo); //商户订单号
        map.put("amount", new BigDecimal(orderAmount).setScale(2).toString()); //单位卢币
        map.put("customName",params[13]);
        map.put("customMobile","123456789");
        map.put("customEmail","19937239312@gmail.com");
        map.put("mode","IMPS");
        map.put("bankCode", bankcodes.getString(bankCode) == null ? "" : bankcodes.getString(bankCode)); // 开户行编码
        map.put("bankAccount", cardNo); // 持卡人卡号
        map.put("ifscCode", cardName); // 持卡人姓名
        map.put("notifyUrl", notify_url);
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + mechKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
   //     logger.error("Upi代付v2发起请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGetway, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, content);
        }
    //    logger.error("Upi代付v2同步返回:" + content);
        JSONObject obj = JSONObject.parseObject(content);
        if (!obj.getString("code").equals("200")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, obj.getString("errorMessages"));
        }
        JSONObject object = new JSONObject();
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
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("mch_id", replaceWithDraw.getMerchantCode());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());
        logger.error("Upi代付v2余额查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("Upi代付v2返回内容:" + content);
        JSONObject json = JSONObject.parseObject(content);
        if (!"10000".equals(json.getString("status"))) {
            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("result"));
        }
        JSONObject result = JSONObject.parseObject(json.getString("result"));
        String points = result.getString("points");
    }

    @Override
    public void search(HttpServletRequest request, MnyDrawRecordService mnyDrawRecordService, StationReplaceWithDraw replaceWithDraw, MnyDrawRecord mnyDrawRecord) {
        String payGate = replaceWithDraw.getSearchGetway();
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("merchant", replaceWithDraw.getMerchantCode());
        map.put("orderId", mnyDrawRecord.getOrderId());

        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
        logger.error("Upi代付v2查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("Upi代付v2返回内容:" + content);
        JSONObject json = JSONObject.parseObject(content);
        if (!json.getString("code").equals("200")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("errorMessages"));
        }
        JSONObject data = JSONObject.parseObject(json.getString("data"));
        if ("PAY_SUCCESS".equals(data.getString("status"))) {
            if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Upi代付v2成功");
            }
        } else if ("PAY_FAIL".equals(data.getString("status"))){
            if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Upi代付v2失败");
            }
        } else if ("PAY_CREATE".equals(data.getString("status"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, ": 订单正在创建");
        } else if ("PAY_ING".equals(data.getString("status"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, "： 订单正在支付中");
        }
    }
}