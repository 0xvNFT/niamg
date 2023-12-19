package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.pay.baxicashpay.util.CashRSAEncrypt;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;

public class CashpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
   // private static Logger logger = LoggerFactory.getLogger(CashpayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws Exception {
     //   logger.error("开始进入cash支付回调");
      //  PrintWriter pWriter = response.getWriter();
        StringBuffer requstJsonData = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while ((line= reader.readLine())!=null){
            requstJsonData.append(line);
        }
        //  logger.error("代付数据="+requstJsonData.toString());
        JSONObject object =  JSONObject.parseObject(requstJsonData.toString());
        String orderNo = object.get("merchantOrderId").toString();

        //  logger.error("代付数据="+orderNo);
      //  logger.error("回调cash代付数据:merchantOrderId" + orderNo);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNo);
        if (record == null) {
       //     logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
        //    logger.error("付款方式为空");
            return;
        }
      //  logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
        //    logger.error("回调IP不在白名单内订单号:" + orderNo);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());

        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(object.getString("amount"));
        String returnSign = object.getString("sign");
        object.remove("sign");
        String customSign =  CashRSAEncrypt.sign(online.getMerchantKey(),object);
        // 签名校验
        if (returnSign.equals(customSign)) {
            //状态判断
            if (object.getString("code").equals("200")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNo, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNo, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
           //         logger.info(payName + "三方支付回调完成：orderId" + orderNo);
                }
                response.getWriter().print("success");
            }
        }
    }




}

