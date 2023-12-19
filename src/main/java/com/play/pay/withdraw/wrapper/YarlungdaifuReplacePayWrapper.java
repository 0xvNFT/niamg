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
import java.text.SimpleDateFormat;
import java.util.*;

public class YarlungdaifuReplacePayWrapper implements ReplacePayWrapper {
    private static Logger logger = LoggerFactory.getLogger(YarlungdaifuReplacePayWrapper.class);
    private static String NOTIFY_URL = "@{merchant_domain}/onlinePay/ReplaceNotify/yarlungdaifu.do";
    private static JSONObject bankcodes = JSONObject.parseObject("{'Bank BCA':'BCA','Bank BRI':'BRI','THE SIAM COMMERCIAL BANK PUBLIC COMPANY':'SCB','KASIKORNBANK PCL':'KBANK','KRUNG THAI BANK PUBLIC COMPANY LTD.':'KTB','BANGKOK BANK PUBLIC COMPANY LTD.':'BBL','TMB BANK PUBLIC COMPANY LTD.':'TMB','THE GOVERNMENT SAVING BANK':'GSB','BANK OF AYUDHAYA PUBLIC COMPANY LTD.':'BAY','CIMB THAI BANK PUBLIC COMPANY LTD.':'CIMB','Thanachart Bank Public Company Limited':'TBNK','BANK FOR AGRICULTURE AND AGRICULTURAL CO-OPERATIVES':'BAAC','Land and Houses Bank':'LHBANK','GOVERNMENT HOUSING BANK':'GHB','ABBANK':'ABBANK','ACB':'ACB','AGRIBANK':'AGRIBANK','BIDV':'BIDV','DongABank':'DongABank','EIB':'EIB','GPBank':'GPBank','HDB':'HDB','MBBANK':'MBBANK','NamABank':'NamABank','Oceanbank':'Oceanbank','PG Bank':'PG Bank','Saigonbank':'Saigonbank','SCB':'SCB','SHB':'SHB','STB':'STB','Techcom':'Techcom','TPB':'TPB','VAB':'VAB','VIB':'VIB','Vietcombank':'Vietcombank','VietinBank':'VietinBank','VPBank':'VPBank'}");

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
        map.put("mch_id", mechCode);
        map.put("mch_transferId", orderNo);
        map.put("currency", "THB");
        map.put("transfer_amount", new BigDecimal(orderAmount).setScale(2).toString());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("apply_date",sdf.format(new Date()));
        map.put("bank_code", bankcodes.getString(bankCode) == null ? "" : bankcodes.getString(bankCode));
        map.put("receive_name", cardName);
        map.put("receive_account", cardNo);
        map.put("back_url", OnlinepayUtils.getNotifyUrl(NOTIFY_URL, params[4]));
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.append("key=").append(mechKey).toString();
        logger.error("yarlung代付请求加密字符串:" + str);
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
        map.put("sign_type", "MD5");
        logger.error("yarlung代付发起请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGetway, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, content);
        }
        logger.error("金象代付同步返回:" + content);
        JSONObject obj = JSONObject.parseObject(content);
        if (!obj.getString("respCode").equalsIgnoreCase("SUCCESS")) {
            throw new ParamException(BaseI18nCode.operateErrorReson, obj.getString("errorMsg"));
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
        logger.error("金象代付余额查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("金象代付返回内容:" + content);
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
        map.put("appid", replaceWithDraw.getMerchantCode());
        map.put("trade_no", mnyDrawRecord.getOrderId());
        map.put("orderid", RedisAPI.getCache(mnyDrawRecord.getOrderId()));
        map.put("time", String.valueOf(System.currentTimeMillis()/1000));


        String merchantKey = replaceWithDraw.getMerchantKey();
        //对请求参数进行加签
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            sb.append(k).append("=").append(v).append("&");
        });
        String str = sb.substring(0,sb.length()-1) + merchantKey;
        logger.error("金象代付查询请求加密字符串:" + str);
        map.put("sign", OnlinepayUtils.MD5(str,"utf-8").toLowerCase());
        map.put("sign_type", "MD5");
        logger.error("金象代付查询请求:" + JSONObject.toJSONString(map));
        String content = HttpClientUtils.getInstance().sendHttpsPost(payGate, map);
        if (StringUtils.isEmpty(content)) {
            throw new ParamException(BaseI18nCode.operateErrorReson, "请求第三方查询接口失败");
        }
        logger.error("金象代付返回内容:" + content);
        JSONObject json = JSONObject.parseObject(content);
        if (Integer.parseInt(json.getString("error")) > 0) {
            throw new ParamException(BaseI18nCode.operateErrorReson, json.getString("msg"));
        }
        JSONObject data = JSONObject.parseObject(json.getString("data"));
        if ("3".equals(data.getString("payState"))) {
            synchronized (this) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "金象代付成功");
            }
        } else if ("4".equals(data.getString("payState"))) {
            synchronized (this) {
                mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "金象代付失败");
            }
        } else if("0".equals(data.getString("payState"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, " :代付待生成");
        }else if("1".equals(data.getString("payState"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, " :代付待处理");
        }else if("2".equals(data.getString("payState"))){
            throw new ParamException(BaseI18nCode.operateErrorReson, " :代付处理中");
        }
    }
}