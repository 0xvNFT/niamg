package com.play.web.controller.notify.online.notify;


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

/**
 * 回调controller类
 */
@Controller
@RequestMapping("/onlinePay/notify")
public class CrownzhifuNotifyController extends FrontBaseController {


    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;

    @Autowired
    private StationDepositOnlineService stationDepositOnlineService;

    private static Logger logger = LoggerFactory.getLogger(CrownzhifuNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/crownzhifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();

        SortedMap<String, String> map = getRequestData(request);

       // logger.error("crown支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String ordernumber = map.get("order");

    //    logger.error("crown支付支付成功收到回调通知，订单号：" + ordernumber);

        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(ordernumber);
        if (record == null) {
      //      logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
        if (online == null) {
       //     logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, online.getWhiteListIp())) {
        //    logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }

        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (!"sn".equals(k)) {
                sb.append(k).append("=").append(v);
            }
        });
        String str = sb.append("secret=") + online.getMerchantKey();
     //   logger.info("crown支付回调签名字符串：" + str);
        String sign = OnlinepayUtils.MD5(str, "utf-8").toLowerCase();
    //    logger.info("crown支付回调签名：" + sign);
        BigDecimal realMoney = new BigDecimal(map.get("amount"));
    //    logger.info("crown支付回调金额：" + realMoney);
        if (sign.equals(map.get("sn"))) {
            synchronized (this) {
                //mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS, realMoney, String.format("%s在线充值成功", payName));
                List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS, realMoney, I18nTool.convertCodeToArrStr(remarkList));
            }
            pWriter.print("success");
        }
    }
}
