package com.play.web.controller.notify.withdraw.notify;


import com.play.common.utils.security.OnlinepayUtils;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;


/**
 * 回调代付controller类
 */
@Controller
@RequestMapping("/onlinePay/ReplaceNotify")
public class YarlungdaifuReplaceNotifyController extends FrontBaseController {
    @Autowired
    private MnyDrawRecordDao mnyDrawRecordDao;
    @Autowired
    private MnyDrawRecordService mnyDrawRecordService;
    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;

   // private static Logger logger = LoggerFactory.getLogger(YarlungdaifuReplaceNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/yarlungdaifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
        //  logger.error("回调yarlung代付数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("merTransferId");
        // 用户提款记录
     //   logger.error("yarlung代付成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
      //      logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
     //       logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
        //    logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
        //    logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        StringBuilder sb=new StringBuilder();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && !"signType".equals(k) &&StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.append("key=").append(replace.getMerchantKey()).toString();
       // logger.error("回调yarlung代付签名数据:" + str);
        String sign = OnlinepayUtils.MD5(str,"utf-8").toLowerCase();
      //  logger.error("回调yarlung代付自加密sign:" + sign);
        if (map.get("sign").equalsIgnoreCase(sign)) {
       //     logger.error("回调yarlung代付验签成功，1、成功 ,2.失败");
            if ("1".equals(map.get("tradeResult"))) {
                synchronized (this) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "yarlung代付成功");
                }
                pWriter.print("success");
            } else if ("2".equals(map.get("tradeResult"))) {
                synchronized (this) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "yarlung代付失败");
                }
                pWriter.print("fail");
            }
        }
    }
}