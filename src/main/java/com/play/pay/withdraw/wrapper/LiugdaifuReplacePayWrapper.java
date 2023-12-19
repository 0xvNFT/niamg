package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.RedisAPI;
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

public class LiugdaifuReplacePayWrapper implements ReplacePayWrapper {
    private static Logger logger = LoggerFactory.getLogger(LiugdaifuReplacePayWrapper.class);
    private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/liugdaifu.do";
    private static JSONObject bankcodes = JSONObject.parseObject("{'SCB':'SCB','BAAC':'BAAC','BAY':'BAY','BBL':'BBL','CIMB':'CIMB','CITI':'CITI','DB':'DB','GHB':'GHB','GSB':'GSB','HSBC':'HSBC','ICBC':'ICBC','ISBT':'ISBT','KBANK':'KBANK','KK':'KK','KTB':'KTB','LHBANK':'LHBANK','MHCB':'MHCB','SCBT':'SCBT','SMBC':'SMBC','TBANK':'TBANK','TCRB':'TCRB','TMB':'TMB','TSCO':'TSCO','UOB':'UOB'}");

    @Override
    public JSONObject wrap(String... params) {
        String mechCode = params[0];
        String mechKey = params[1]; //此处表示接口密钥
        String orderNo = params[2];
        String orderAmount = new BigDecimal(params[3]).multiply(new BigDecimal("1000")).setScale(0).toString();
        String bankCode = params[5];
        String merchantAccount = params[6];  //此处表示支付密钥
        String payGetway = params[10];
        String cardName = params[15];
        String cardNo = params[18];
        String bankAddr = params[17];
        SortedMap<String, String> map = new TreeMap<>();
        map.put("amount", orderAmount);
        map.put("applyDate", String.valueOf(System.currentTimeMillis()/1000));
        map.put("channelCode", "YHK");
        map.put("merId", mechCode);
        map.put("notifyUrl", OnlinepayUtils.getNotifyUrl(NOTIFY_URL, params[4]));
        map.put("orderId", orderNo);
        map.put("currency", "BT");
        map.put("ip", params[7]);
        map.put("bankName", bankCode);
        map.put("bankNumber", cardNo);
        map.put("address", bankCode);
        map.put("name", cardName);
        map.put("phone", "555555");
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + mechKey;
        logger.error("6G代付请求加密字符串:" + str);
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());
        logger.error("6G代付发起请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGetway, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, content);
        }
        logger.error("6G代付同步返回:" + content);
        JSONObject obj = JSONObject.parseObject(content);
        if (!obj.getString("code").equals("200")) {
            JSONObject result = JSONObject.parseObject(obj.getString("result"));
            throw new ParamException(BaseI18nCode.operateErrorReson, result.getString("msg"));
        }
        RedisAPI.addCache(orderNo,params[7],60*60*60);
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
        map.put("appid", replaceWithDraw.getMerchantCode());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());
        logger.error("6G代付余额查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("6G代付返回内容:" + content);
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
        map.put("merId", replaceWithDraw.getMerchantCode());
        map.put("outTradeId", mnyDrawRecord.getOrderId());
        map.put("ip", RedisAPI.getCache(mnyDrawRecord.getOrderId()));
        map.put("applyDate", String.valueOf(System.currentTimeMillis()/1000));


        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        logger.error("6G代付查询请求加密字符串:" + str);
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());
        logger.error("6G代付查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("6G代付返回内容:" + content);
        JSONObject json = JSONObject.parseObject(content);
        if (!json.getString("code").equals("200")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("msg"));
        }
        JSONObject data = JSONObject.parseObject(json.getString("data"));
        if ("200".equals(data.getString("returnCode"))) {
            synchronized (this) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "6G代付成功");
            }
        } else if ("500".equals(data.getString("returnCode"))) {
            synchronized (this) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "6G代付失败");
            }
        } else if("400".equals(data.getString("returnCode"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, " :6G代付待支付");
        }
    }
}