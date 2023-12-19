package com.play.web.controller.notify.online.notify;


import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.PayNotifyWrapper;

import com.play.common.utils.security.HttpClientUtils;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class TopayzhifuNotifyController extends FrontBaseController implements PayNotifyWrapper {

    //private static Logger logger = LoggerFactory.getLogger(TopayzhifuNotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response, String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, Object> map = getRequestJsonData(request);
      //  logger.error("Topay支付v2成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("out_trade_no").toString();
        //logger.error("Topay支付v2成功收到回调通知，订单号：" + orderNumber);
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
         //   logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
    //    logger.info("回调验证1");
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        String key = online.getAccount();
    //    logger.info(payName + "回调验证2");
        //金额,三方以元为单位
        BigDecimal amount = new BigDecimal(map.get("pay_amount").toString());
  //      logger.info(payName + "三方回调金额：" + amount);
        String returnsign = map.get("sign") + "";
        map.remove("sign");
        String prepareParam = getSignedValue(map);
        String data = "";
        try {
             data = decrypt(key,returnsign);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 签名校验
        if (data.equals(prepareParam)) {
            //状态判断
            if (map.get("status").toString().equals("1")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                            amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
     //               logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                }
                pWriter.print("SUCCESS");
            }
        }
    }


    public static String decrypt(String key,String data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException{
        byte[] decode = Base64.getDecoder().decode(key);
       X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey generatePublic = kf.generatePublic(x509EncodedKeySpec);
        Cipher ci = Cipher.getInstance("RSA");
        ci.init(Cipher.DECRYPT_MODE,generatePublic);
        byte[] bytes = Base64.getDecoder().decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new String(byteArray);
    }

    private static String getSignedValue(Map<String, Object> reqMap) {
        Map<String, String> copy = new TreeMap<>();
        reqMap.forEach((k, v) -> {
            if (v != null && !"".equals(v)) {
                copy.put(k, v.toString());
            }
        });
        StringBuilder sb = new StringBuilder();
        copy.forEach((k, v) -> {
            if (v != null) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        return sb.toString();
    }





    private boolean queryStatus(String orderNumber, StationDepositOnline online,String payName){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("merchant",online.getMerchantCode());//商户号
        map.put("orderId",orderNumber);//订单号
        StringBuilder sb = new StringBuilder();
        map.forEach((k,v)->sb.append(k).append("=").append(v).append("&"));
        String str = sb.append("key=").append(online.getMerchantKey()).toString();
    //    logger.info(payName+":"+"三方查询签名字符串："+str);
        String querySign= OnlinepayUtils.MD5(str,"utf-8").toLowerCase();
    //    logger.info(payName+":"+"三方查询签名："+querySign);
        map.put("sign",querySign);//查询签名
    //    logger.info(payName+":"+"三方查询请求参数："+ JSON.toJSONString(map));
        String content;
        try{
            content = HttpClientUtils.getInstance().sendHttpsPost(online.getQueryGateway(), map);
            JSONObject obj = JSONObject.parseObject(content);
            JSONObject data = JSONObject.parseObject(obj.getString("data"));
            String status=data.getString("status");
            if(status.equalsIgnoreCase("PAY_SUCCESS")){
        //        logger.info(payName+":"+"查询成功，返回状态:"+status);
                return true;
            }else {
      //          logger.info(payName+":"+"查询失败，返回状态:"+status);
                return  false;
            }
        }catch (Exception e){
       //     logger.error(payName+":"+"三方查询异常："+"查询网关："+online.getQueryGateway(),e.getMessage());
            return false;
        }
    }
}