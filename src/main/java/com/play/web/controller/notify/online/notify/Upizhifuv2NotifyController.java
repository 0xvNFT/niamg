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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Upizhifuv2NotifyController extends FrontBaseController implements PayNotifyWrapper {


    private static Logger logger = LoggerFactory.getLogger(Upizhifuv2NotifyController.class);

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response,String name,
                       MnyDepositRecordService mnyDepositRecordService,
                       StationDepositOnlineService stationDepositOnlineService) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, Object> map = getRequestJsonData(request);
     //   logger.error("Upi支付v2成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("orderId").toString();
     //   logger.error("Upi支付v2成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
      //      logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
     //       logger.error("付款方式为空");
            return;
        }
      //  logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
      //      logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
      //  logger.info("回调验证1");
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        String key=online.getMerchantKey();
      //  logger.info(payName + "回调验证2");
        //金额,三方以元为单位
        BigDecimal amount=new BigDecimal(map.get("amount").toString());
     //   logger.info(payName+"三方回调金额："+amount);
        // 签名校验
        if (checkSign(map,key,payName)) {
            //状态判断
            if (map.get("status").toString().equals("PAY_SUCCESS") && queryStatus(orderNumber,online,payName)) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                        mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                                amount, String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                            amount, I18nTool.convertCodeToArrStr(remarkList));
       //             logger.info(payName + "三方支付回调完成：orderId" + orderNumber);
                    }
                    pWriter.print("ok");
            }
        }
    }
    private boolean checkSign(SortedMap<String,Object> map,String key,String payName){
        String sign=map.get("sign").toString();
        StringBuffer stringBuffer = new StringBuffer();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && StringUtils.isNotEmpty(v.toString())) {
                stringBuffer.append(k).append("=").append(v).append("&");
            }
        });
        String signStr = stringBuffer.append("key=").append(key).toString();
      //  logger.info(payName + ":" + "三方回调签名前字符串：" + signStr);
        String checkSign = OnlinepayUtils.MD5(signStr, "utf-8").toLowerCase();
     //   logger.info(payName+":"+"三方回调签名："+checkSign);
        if(sign.equalsIgnoreCase(checkSign)){
      //      logger.info(payName+":"+"三方签名认证通过："+sign);
            return true;
        }else {
     //       logger.error(payName+":"+"三方签名认证失败："+sign);
            return false;
        }
    }



    private boolean queryStatus(String orderNumber, StationDepositOnline online,String payName){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("merchant",online.getMerchantCode());//商户号
        map.put("orderId",orderNumber);//订单号
        StringBuilder sb = new StringBuilder();
        map.forEach((k,v)->sb.append(k).append("=").append(v).append("&"));
        String str = sb.append("key=").append(online.getMerchantKey()).toString();
     //   logger.info(payName+":"+"三方查询签名字符串："+str);
        String querySign= OnlinepayUtils.MD5(str,"utf-8").toLowerCase();
     //   logger.info(payName+":"+"三方查询签名："+querySign);
        map.put("sign",querySign);//查询签名
    //    logger.info(payName+":"+"三方查询请求参数："+ JSON.toJSONString(map));
        String content;
        try{
            content = HttpClientUtils.getInstance().sendHttpsPost(online.getQueryGateway(), map);
            JSONObject obj = JSONObject.parseObject(content);
            JSONObject data = JSONObject.parseObject(obj.getString("data"));
            String status=data.getString("status");
            if(status.equalsIgnoreCase("PAY_SUCCESS")){
      //          logger.info(payName+":"+"查询成功，返回状态:"+status);
                return true;
            }else {
      //          logger.info(payName+":"+"查询失败，返回状态:"+status);
                return  false;
            }
        }catch (Exception e){
            logger.error(payName+":"+"三方查询异常："+"查询网关："+online.getQueryGateway(),e.getMessage());
            return false;
        }
    }
}
