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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class OkkpayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {
 //   private static Logger logger = LoggerFactory.getLogger(OkkpayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws Exception {
       // logger.error("开始进入OKKPAY支付回调");
        PrintWriter pWriter = response.getWriter();
        Map<String, String> map = getRequestDataForMap(request);
       // logger.error("OKKPAY支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("orderNo");
     //   logger.error("OKKPAY支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
    //        logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
   //         logger.error("付款方式为空");
            return;
        }
 //       logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
   //         logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        String key = online.getAccount();
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(map.get("price"));
        // 签名校验
        if (rsaCheck(map,key)) {
            //状态判断
            if (map.get("status").equalsIgnoreCase("2")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
                   // logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                pWriter.print("success");
            }
        }
    }

    public static boolean rsaCheck(Map<String, String> map, String publicKey) throws Exception {
        String charset = "utf-8";
        map.put("charset", charset);
        String content = getSignContent(map);
        String sign = map.get("sign");
        PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
        java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);
        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        String signParam = getSignParam(sortedParams);
        String[] sign_param = signParam.split(",");// ����ǩ������Ĳ���
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < sign_param.length; i++) {
            keys.add(sign_param[i].trim());
        }
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value) && !key.equals("sign")) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }
    private static String getSignParam(Map<String, String> sortedParams) {
        Set<String> setKeys = sortedParams.keySet();
        String signParam = setKeys.toString();
        signParam = signParam.substring(1, signParam.length() - 1);
        return signParam;
    }
    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);
        byte[] encodedKey = writer.toString().getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 8192 >> 1;
        }

        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }
}

