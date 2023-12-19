package com.play.web.controller.notify.online.notify;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.MessageDigest;

import java.util.List;
import java.util.Map;

public class WowpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
    private static Logger logger = LoggerFactory.getLogger(WowpayzhifuNotifyController.class);
    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws IOException {
        PrintWriter pWriter = response.getWriter();
        Map<String, String> map = getRequestDataForMap(request);
      //  logger.error("Topay支付v2成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("mchOrderNo").toString();
      //  logger.error("Wowpay支付v2成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
         //   logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
         //   logger.error("付款方式为空");
            return;
        }
       // logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
        //    logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
    //    logger.info("回调验证1");
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
   //     String key = online.getMerchantKey();
   //     logger.info(payName + "回调验证2");
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(map.get("amount").toString());
     //   logger.info(payName + "三方回调金额：" + amount);
        StringBuilder signStr = new StringBuilder();

        signStr.append("amount=").append(map.get("amount")).append("&");
        signStr.append("mchId=").append(map.get("mchId")).append("&");
        signStr.append("mchOrderNo=").append(map.get("mchOrderNo")).append("&");
        signStr.append("orderDate=").append(map.get("orderDate")).append("&");
        signStr.append("orderNo=").append(map.get("orderNo")).append("&");
        signStr.append("oriAmount=").append(map.get("oriAmount")).append("&");
        signStr.append("tradeResult=").append(map.get("tradeResult"));
       // logger.error("需要验签的字符串"+signStr.toString());
        //logger.error("需要验签的秘钥"+key);
        //logger.error("返回的sign"+map.get("sign"));
        //logger.error(validateSignByKey(signStr.toString(),key,map.get("sign"))+"");
        // 签名校验
        if (validateSignByKey(signStr.toString(),online.getMerchantKey(),map.get("sign"))) {
            //状态判断
            if (map.get("tradeResult").toString().equals("1")) {//1：支付成功
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
      //              logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                response.getWriter().print("success");
            }
        }
    }
    public static boolean validateSignByKey(String signSource, String key, String retsign) {
        if (StringUtils.isNotBlank(key)) {
            signSource = signSource + "&key=" + key;
        }
        String signkey = calculate(signSource);
     //   System.out.println("signkey======" + signkey);
        if (signkey.equals(retsign)) {
            return true;
        }
        return false;
    }
    private static String calculate(String signSource) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signSource.getBytes("utf-8"));
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}