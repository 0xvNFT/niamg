package com.play.pay.withdraw.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

public class QuickdaifuReplacePayWrapper implements ReplacePayWrapper {
    private static Logger logger = LoggerFactory.getLogger(QuickdaifuReplacePayWrapper.class);
    private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/quickdaifu.do";
    private static JSONObject bankcodes = JSONObject.parseObject("{'选择此银行就行':'ABC'}");

    @Override
    public JSONObject wrap(String... params) {
        String mechCode = params[0];
        String mechKey = params[1]; //此处表示接口密钥
        String orderNo = params[2];
        String orderAmount = params[3];
        String bankCode = params[5];
        String merchantAccount = params[6];  //此处表示支付密钥
        String payGetway = params[10];
        String cardName = params[15];
        String cardNo = params[18];
        String bankAddr = params[17];
        SortedMap<String, String> map = new TreeMap<>();
        map.put("uid", mechCode);
        map.put("orderid", orderNo);
        map.put("channel", "712");
        map.put("notify_url", OnlinepayUtils.getNotifyUrl(NOTIFY_URL, params[4]));
        map.put("amount", new BigDecimal(orderAmount).setScale(2).toString());
        map.put("userip", params[7]);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        map.put("custom", "custom");
        map.put("bank_account", cardName);
        map.put("bank_no", cardNo);
        map.put("bank_id", bankcodes.getString(bankCode) == null ? "" : bankcodes.getString(bankCode));
        map.put("bank_province", "北京");
        map.put("bank_city", "北京");
        map.put("bank_sub", "北京");


        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + mechKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());

        logger.error("快捷代付发起请求:" + JSONObject.toJSONString(map));
        JSONObject object = new JSONObject();
		return object;
//        String content = HttpClientUtils.getInstance().sendHttpPost(payGetway, map);
//        if (StringUtils.isEmpty(content)) {
//            throw new ParamException(BaseI18nCode.operateErrorReson, content);
//        }
//        logger.error("快捷代付同步返回:" + content);
//        JSONObject obj = JSON.parseObject(content);
//        if (!"10000".equals(obj.getString("status"))) {
//            throw new ParamException(BaseI18nCode.operateErrorReson, obj.getString("result"));
//        }
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
        map.put("uid", replaceWithDraw.getMerchantCode());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());
        logger.error("快捷代付余额查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("快捷代付返回内容:" + content);
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
        map.put("uid", replaceWithDraw.getMerchantCode());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        map.put("orderid", mnyDrawRecord.getOrderId());

        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toUpperCase());

        logger.error("快捷代付查询请求:" + JSONObject.toJSONString(map));
//        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
//        if (StringUtils.isEmpty(content)) {
//            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
//        }
//        logger.error("快捷代付返回内容:" + content);
//        JSONObject json = JSONObject.parseObject(content);
//        if (!"10000".equals(json.getString("status"))) {
//            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("result"));
//        }
//        JSONObject result = JSONObject.parseObject(json.getString("result"));
//        JSONObject data = JSONObject.parseObject(result.getString("data"));
//        if ("1".equals(data.getString("status"))) {
            synchronized (this) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "快捷代付成功");
            }
//        } else if ("3".equals(data.getString("status")) || "4".equals(data.getString("status")) || "5".equals(data.getString("status"))) {
//            synchronized (this) {
//                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "快捷代付失败");
//            }
//        } else if("0".equals(data.getString("status"))){
//            throw new ParamException(BaseI18nCode.operateErrorReson, "代付未处理");
//        }else if("2".equals(data.getString("status"))){
//            throw new ParamException(BaseI18nCode.operateErrorReson, "代付处理中");
//        }
    }
}