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
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 回调controller类
 */
@Controller
@RequestMapping("/onlinePay/notify")
public class QuickpayPayNotifyController extends FrontBaseController {


    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;

    @Autowired
    private StationDepositOnlineService stationDepositOnlineService;

    private static Logger logger = LoggerFactory.getLogger(QuickpayPayNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/quickpay")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();

        SortedMap<String, String> map = getRequestData(request);

     //   logger.error("快捷支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        SortedMap<String, String> datamap = new TreeMap<>();
        datamap = JSONObject.parseObject(map.get("result"), datamap.getClass());
        String ordernumber = datamap.get("orderid");

    //    logger.error("快捷支付支付成功收到回调通知，订单号：" + ordernumber);

        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(ordernumber);
        if (record == null) {
    //        logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
    //        logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, online.getWhiteListIp())) {
      //      logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }

        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());

        StringBuilder sb = new StringBuilder();
        datamap.forEach((k, v) -> {
            if (!"sign".equals(k) && StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.append("key=") + online.getMerchantKey();
    //    logger.error(String.format("%s待签名数据:%s",payName , str));
        String sign = OnlinepayUtils.MD5(str,"utf-8").toUpperCase();
    //    logger.error(String.format("%s签名数据sign：%s", payName, sign));
    //    logger.error(String.format("%s回调签名:%s", payName, map.get("sign")));
        if (sign.equals(map.get("sign"))) {
            if ("10000".equals(map.get("status"))&&returnStatus(online,record,payName)) {
                synchronized (this) {
                    //mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS, new BigDecimal(map.get("real_amount")), String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS, new BigDecimal(map.get("real_amount")), I18nTool.convertCodeToArrStr(remarkList));
                }
                pWriter.print("success");
            }
        }
    }
    private boolean returnStatus(StationDepositOnline online, MnyDepositRecord record, String payName) {
        if(StringUtils.equals(online.getAccount(),"123456")){
       //     logger.error("该支付没有查询接口!");
            return true;
        }
        SortedMap<String, String> searchMap = new TreeMap<String, String>();
        searchMap.put("MerchantId", online.getMerchantCode());
        searchMap.put("out_trade_no", record.getOrderId());
        StringBuilder sb = new StringBuilder();
        searchMap.forEach((k, v) -> {
            if (!"sign".equals(k) && StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String signStr = sb.substring(0, sb.length() - 1) + online.getMerchantKey();
        searchMap.put("sign",OnlinepayUtils.MD5(signStr,"utf-8").toUpperCase());
     //   logger.error(String.format("%s查询待签名数据:%s", payName, signStr));
     //   logger.error(String.format("%s查询签名数据:%s", payName, OnlinepayUtils.MD5(signStr,"utf-8").toUpperCase()));
        String content = HttpClientUtils.getInstance().sendHttpsPost(online.getQueryGateway(), searchMap);
      //  logger.error(String.format("%s查询返回数据:%s", payName, content));
        JSONObject resultJsonObj = JSONObject.parseObject(content);
        if ("SUCCESS".equals(resultJsonObj.getString("trade_status"))) {
            return true;
        }
       // logger.error(String.format("%s查询失败:%s", payName, resultJsonObj.getString("msg")));
        return false;
    }}
