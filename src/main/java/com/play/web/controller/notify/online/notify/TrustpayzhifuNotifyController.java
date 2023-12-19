package com.play.web.controller.notify.online.notify;


import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.baxitrustpay.util.TrustpayShaEncrypt;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.math.BigDecimal;

import java.util.List;
import java.util.SortedMap;


public class TrustpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
//    private static Logger logger = LoggerFactory.getLogger(TrustpayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws IOException {
      //  logger.error("开始进入TrustPay支付回调");
        SortedMap<String, String> map = getRequestData(request);
     //   logger.error("TrustPay支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("orderNo");
    //    logger.error("TrustPay支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
         //   logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
     //       logger.error("付款方式为空");
            return;
        }
     //   logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
     //       logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
       // String key = online.getMerchantKey();
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(map.get("amount"));
     //   logger.info(payName + "三方回调金额：" + amount);
        // 签名校验
        if (checkSign(map, online.getMerchantKey(), payName)) {
            //状态判断
            if (map.get("status").equalsIgnoreCase("2")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
       //             logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                response.getWriter().print("success");
            }
        }
    }

    private boolean checkSign(SortedMap<String, String> map, String key, String payName) {
        String sign = map.get("sign");
    //    logger.error(payName + ":" + "三方回调签名前字符串：" + sign);
        map.remove("sign");
        String signStr = TrustpayShaEncrypt.genSign(map,key);
     //   logger.error(payName + ":" + "自己生成加密字符串：" + signStr);
        if (sign.equalsIgnoreCase(signStr)) {
    //        logger.info(payName + ":" + "三方签名认证通过：" + sign);
            return true;
        } else {
      //      logger.error(payName + ":" + "三方签名认证失败：" + sign);
            return false;
        }
    }

}

