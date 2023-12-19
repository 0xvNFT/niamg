package com.play.web.controller.notify.online.notify;


import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

import com.play.common.utils.security.OnlinepayUtils;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import java.util.List;
import java.util.SortedMap;

public class UpizhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {


    //private static Logger logger = LoggerFactory.getLogger(UpizhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws IOException {
   //     logger.error("开始进入upi支付回调");
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
   //     logger.error("upi支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("sh_order");
    //    logger.error("upi支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
     //       logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
     //       logger.error("付款方式为空");
            return;
        }
   //     logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
   //         logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        String key = online.getMerchantKey();
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(map.get("money"));
    //    logger.info(payName + "三方回调金额：" + amount);
        // 签名校验
        if (checkSign(map, key, payName)) {
            //状态判断
            if (map.get("state").equalsIgnoreCase("4")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
      //              logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                pWriter.print("success");
            }
        }
    }

    private boolean checkSign(SortedMap<String, String> map, String key, String payName) {
        String sign = map.get("sign");
        StringBuffer stringBuffer = new StringBuffer();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && StringUtils.isNotEmpty(v)) {
                stringBuffer.append(k).append("=").append(v).append("&");
            }
        });
        String signStr = stringBuffer.append("key=").append(key).toString();
   //     logger.info(payName + ":" + "三方回调签名前字符串：" + signStr);
        String checkSign = OnlinepayUtils.MD5(signStr, "utf-8").toLowerCase();
   //     logger.info(payName + ":" + "三方回调签名：" + checkSign);
        if (sign.equalsIgnoreCase(checkSign)) {
      //      logger.info(payName + ":" + "三方签名认证通过：" + sign);
            return true;
        } else {
     //       logger.error(payName + ":" + "三方签名认证失败：" + sign);
            return false;
        }
    }

}
