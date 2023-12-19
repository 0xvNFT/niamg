package com.play.web.controller.notify.online.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ip.IpUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 回调controller类
 */
@Controller
@RequestMapping("/onlinePay/notify")
public class JinxiangzhifuNotifyController extends FrontBaseController {


    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;

    @Autowired
    private StationDepositOnlineService stationDepositOnlineService;

    private static Logger logger = LoggerFactory.getLogger(JinxiangzhifuNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/jinxiangzhifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();

        SortedMap<String, String> map = getRequestData(request);

  //      logger.error("金象支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String ordernumber = map.get("trade_no");

 //       logger.error("金象支付支付成功收到回调通知，订单号：" + ordernumber);

        MnyDepositRecord record = mnyDepositRecordService.findOneByOrderId(ordernumber);
        if (record == null) {
   //         logger.error("订单号不存在");
            return;
        }
        StationDepositOnline online = stationDepositOnlineService.getOneNoHide(record.getPayId(), SystemUtil.getStationId());
   //     logger.error("第三方服务器IP：" + IpUtils.getSafeIpAdrress(request) + ": 配置服务器ip：" + Arrays.asList(online.getWhiteListIp().split("/")));
        if (online == null) {
   //         logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, online.getWhiteListIp())) {
  //          logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }

        String payName = PayPlatformEnum.valueOfPayName(online.getPayPlatformCode());
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && !"sign_type".equals(k) &&StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.substring(0,sb.length()-1) + online.getMerchantKey();
   //     logger.info("金象支付回调签名字符串："+str);
        String sign = OnlinepayUtils.MD5(str,"utf-8").toLowerCase();
   //     logger.info("金象支付回调签名："+sign);
        if (sign.equals(map.get("sign"))) {
            if ("2".equals(map.get("pay_status"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:notify:" + record.getUserId(), 5)) {
//                    mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS,
//                            new BigDecimal(map.get("money")), String.format("%s在线充值成功", payName));
                    List<String> remarkList = I18nTool.convertCodeToList(payName, BaseI18nCode.onlineRechargeSuccessful.getCode());
                    mnyDepositRecordService.onlineDepositNotifyOpe(ordernumber, MnyDepositRecord.STATUS_SUCCESS,
                            new BigDecimal(map.get("money")), I18nTool.convertCodeToArrStr(remarkList));
                }
                pWriter.print("success");
            }
        }
    }
}
