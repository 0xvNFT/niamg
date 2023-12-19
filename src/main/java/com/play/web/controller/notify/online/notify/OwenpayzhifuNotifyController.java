package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;
import com.play.common.ip.IpUtils;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

public class OwenpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
    private static Logger logger = LoggerFactory.getLogger(OwenpayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws Exception {
      //  logger.error("开始进入OWENPAY支付回调");
        Map<String, String> map = getRequestDataForMap(request);
        String noticeParams = map.get("NoticeParams");
        JSONObject jsonObject = JSONObject.parseObject(noticeParams);
       // logger.error("OKKPAY支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = jsonObject.getString("outTradeNo");
     //   logger.error("OKKPAY支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
   //         logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
      //      logger.error("付款方式为空");
            return;
        }
     //   logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
     //       logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(jsonObject.getString("totalAmount")).divide(new BigDecimal(100));

        String returnSign = jsonObject.getString("sign");
        Map<String,String> treeMap = new TreeMap<>();
        jsonObject.forEach((k,v)->{
            treeMap.put(k, (String) v);
        });
        treeMap.remove("sign");
        String customSign = OwenRSAEncrypt.encrypt(JSONObject.toJSONString(treeMap)+online.getMerchantKey(),"utf-8");
        // 签名校验
        if (returnSign.equals(customSign)) {
            //状态判断
            if (treeMap.get("payCode").equals("0000")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
                   // logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                response.getWriter().print("SUCCESS");
            }
        }
    }


}

