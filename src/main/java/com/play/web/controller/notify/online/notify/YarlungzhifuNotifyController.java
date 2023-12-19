package com.play.web.controller.notify.online.notify;


import com.alibaba.fastjson.JSONObject;

import com.play.common.utils.security.HttpClientUtils;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.core.PayPlatformEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositOnline;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositOnlineService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;

import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.math.BigDecimal;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 回调controller类
 */
@Controller
@RequestMapping("/onlinePay/notify")
public class YarlungzhifuNotifyController extends FrontBaseController {
    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;
    @Autowired
    private StationDepositOnlineService stationDepositOnlineService;
    private static Logger logger = LoggerFactory.getLogger(YarlungzhifuNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/yarlungzhifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //  PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
    //    logger.error("yarlung支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = map.get("mchOrderNo");
    //    logger.error("yarlung支付成功收到回调通知，订单号：" + orderNumber);
        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(orderNumber);
        if (record == null) {
       //     logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
        //    logger.error("付款方式为空");
            return;
        }
    //    logger.error("request.getRemoteAddr()：" + request.getRemoteAddr());
    //    logger.error("SystemConfig.ALL_CDN_IP_LIST：" + SystemConfig.ALL_CDN_IP_LIST);
   //     logger.error("request.getHeader：" + request.getHeader("X-Forwarded-For"));
        if (!authenticatedIP(request, online.getWhiteListIp())) {
       //     logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        //String key=online.getMerchantKey();
        // 签名校验
        if (checkSign(map,online.getMerchantKey(),payName)) {
            //状态判断
            if (map.get("tradeResult").equalsIgnoreCase("1")) {
                // 查询返回状态判断
                if(queryStatus(orderNumber,online,payName)){
                    synchronized (this) {
//                        mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
//                                new BigDecimal(map.get("oriAmount")), String.format("%s在线充值成功", payName));
                        List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                        mnyDepositRecordService.onlineDepositNotifyOpe(orderNumber, MnyDepositRecord.STATUS_SUCCESS,
                                new BigDecimal(map.get("oriAmount")), I18nTool.convertCodeToArrStr(remarkList));
                    }
                    response.getWriter().print("success");
                }else {
                    response.getWriter().print("fail");
                }

            }
        }
    }
    private boolean checkSign(SortedMap<String,String> map,String key,String payName){
        String sign=map.get("sign");
        String signStr = signStr(map, key);
    //    logger.info(payName+":"+"三方回调签名前字符串："+signStr);
        String checkSign= OnlinepayUtils.MD5(signStr,"utf-8").toLowerCase();
      //  logger.info(payName+":"+"三方回调签名："+checkSign);
        if(sign.equalsIgnoreCase(checkSign)){
       //     logger.info(payName+":"+"三方签名认证通过："+sign);
            return true;
        }else {
        //    logger.error(payName+":"+"三方签名认证失败："+sign);
            return false;
        }
    }

    public String signStr(SortedMap<String,String> map,String key){
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && !"sign_type".equals(k)&& !"signType".equals(k) && StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
         String signStr=sb.append("key=").append(key).toString();
        return signStr;
    }

    private boolean queryStatus(String orderNumber, StationDepositOnline online,String payName){
        SortedMap<String, String> map = new TreeMap<String, String>();
        map.put("mch_id",online.getMerchantCode());//商户号
        map.put("mch_order_no",orderNumber);//订单号
        String queryStr = signStr(map, online.getMerchantKey());
     //   logger.info(payName+":"+"三方查询签名字符串："+queryStr);
        String querySign= OnlinepayUtils.MD5(queryStr,"utf-8").toLowerCase();
      //  logger.info(payName+":"+"三方查询签名："+querySign);
        map.put("sign",querySign);//查询签名
        map.put("sign_type","MD5");//商户号
    //    logger.info(payName+":"+"三方查询请求参数："+ JSON.toJSONString(map));
        String content;
        try{
            content = HttpClientUtils.getInstance().sendHttpsPost(online.getQueryGateway(), map);
            JSONObject obj = JSONObject.parseObject(content);
            String tradeResult=obj.getString("tradeResult");
            if(tradeResult.equalsIgnoreCase("1")){
       //         logger.info(payName+":"+"查询成功，返回状态:"+tradeResult);
                return true;
            }else {
       //         logger.info(payName+":"+"查询失败，返回状态:"+tradeResult);
                return  false;
            }
        }catch (Exception e){
            logger.error(payName+":"+"三方查询异常："+"查询网关："+online.getQueryGateway(),e.getMessage());
            return false;
        }
    }

  }
