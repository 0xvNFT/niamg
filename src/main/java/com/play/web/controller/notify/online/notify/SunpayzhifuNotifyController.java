package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.baxisunpay.util.SunpayRSAEncrypt;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.web.controller.front.FrontBaseController;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import java.util.List;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;

public class SunpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
   // private static Logger logger = LoggerFactory.getLogger(SunpayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws Exception {

    //    logger.error("开始进入SunPAY支付回调");
        String SunPay_Timestamp =  request.getHeader("SunPay-Timestamp");
        String SunPay_Nonce =  request.getHeader("SunPay-Nonce");
        String SunPay_Sign =  request.getHeader("SunPay-Sign");
        StringBuffer requstJsonData = new StringBuffer();//
        BufferedReader reader = request.getReader();
        String line;
        while ((line= reader.readLine())!=null){
            requstJsonData.append(line);
        }
        JSONObject object =  JSONObject.parseObject(requstJsonData.toString());
        String orderNumber = object.getJSONObject("data").getString("out_order_no");
  //      logger.error("SunPAY支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
    //        logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
    //        logger.error("付款方式为空");
            return;
        }
    //    logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
       //     logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String customSign = SunpayRSAEncrypt.HexHMac(Long.parseLong(SunPay_Timestamp),SunPay_Nonce,
                requstJsonData.toString(),online.getAccount());
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(object.getJSONObject("data").getString("amount"));
        // 签名校验
        if (customSign.equals(SunPay_Sign)) {
            //状态判断
            if (object.getString("biz_status").equals("SUCCESS")
            &&object.getString("biz_type").equals("PAYIN")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
       //            logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                response.getWriter().print("{\"is_success\": true,\"message\": \"success\"}");
            }
        }
    }
}

