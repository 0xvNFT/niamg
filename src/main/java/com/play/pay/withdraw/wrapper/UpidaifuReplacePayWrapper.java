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
import java.util.*;

public class UpidaifuReplacePayWrapper implements ReplacePayWrapper {
    private static Logger logger = LoggerFactory.getLogger(UpidaifuReplacePayWrapper.class);
    private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/v2/";
    private static String iconCss = "upidaifu";
    private static JSONObject bankcodes = JSONObject.parseObject("{'AIRTEL PAYMENTS BANK':'AIRP','ALLAHABAD BANK':'ALH','AXIS BANK':'AXIS','ANDHRA BANK':'ANDB','BANK OF INDIA':'BOI','BHARAT BANK':'BHARAT','BANK OF BAHRAIN AND KUWAIT':'BBK','BANK OF BARODA':'BOB','BANK OF MAHARASHTRA':'BOM','CENTRAL BANK OF INDIA':'CBI','CATHOLIC SYRIAN BANK':'CSB','CANARA BANK':'CANARA','CITIBANK':'CITI','CITY UNION BANK':'CITIUB','CORPORATION BANK':'CORP','COSMOS BANK':'COSMOS','DEVELOPMENT BANK OF SINGAPORE':'DBS','DCB BANK LIMITED':'DCB','DHANLAXMI BANK':'DHAN','DENA BANK':'DENA','DEUTSCHE BANK':'DEUTS'}");
    @Override
    public JSONObject wrap(String... params) {
        String mechCode = params[0];
        String mechKey = params[1]; //此处表示接口密钥
        String orderNo = params[2];
        String orderAmount = params[3];
        String merchantDomain = params[4];
      //  String bankCode = params[5];
     //   String merchantAccount = params[6];  //此处表示支付密钥
        String payGetway = params[10];
        String cardName = params[15];
        String cardNo = params[18];
        String bankAddr = params[17];
        String notify_url=OnlinepayUtils.getNotifyUrl(NOTIFY_URL, merchantDomain) + iconCss + ".do";
        SortedMap<String, String> map = new TreeMap<>();
        map.put("type","3");
        map.put("mch_id", mechCode); //商户号
        map.put("order_sn", orderNo); //商户订单号
        map.put("money", orderAmount); //以元为单位
        map.put("goods_desc","goodsDesc");
        map.put("client_ip",params[7]);
        map.put("notify_url", notify_url);
        map.put("bank_name", cardName); // 持卡人姓名
        map.put("bank_card",cardNo); //银行卡号
        map.put("time",String.valueOf(System.currentTimeMillis()));
        map.put("ifsc",bankAddr);
        map.put("bank_tel","19937239312");
        map.put("bank_email","19937239312@gmail.com");
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + mechKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
//        map.put("bank_type_name", bankcodes.getString(bankCode) == null ? "" : bankcodes.getString(bankCode));// 开户行编码
     //   logger.error("upi代付发起请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGetway, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, content);
        }
   //     logger.error("upi代付同步返回:" + content);
        JSONObject obj = JSONObject.parseObject(content);
        if (!obj.getString("code").equals("1")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, obj.getString("msg"));
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
        logger.error("upi代付余额查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("upi代付返回内容:" + content);
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
        map.put("mch_id", replaceWithDraw.getMerchantCode());
        map.put("out_order_sn", mnyDrawRecord.getOrderId());
        map.put("time", String.valueOf(System.currentTimeMillis()));


        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=") + merchantKey;
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
        logger.error("upi代付查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("upi代付返回内容:" + content);
        JSONObject json = JSONObject.parseObject(content);
        if (!json.getString("code").equals("1")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("msg"));
        }
        JSONObject data = JSONObject.parseObject(json.getString("data"));
        if ("4".equals(data.getString("status"))) {
            if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "upi代付成功");
            }
        } else{
            if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "upi代付失败");
            }
        }
    }
}