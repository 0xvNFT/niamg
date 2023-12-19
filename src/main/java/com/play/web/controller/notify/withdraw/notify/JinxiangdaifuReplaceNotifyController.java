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
public class JinxiangdaifuReplaceNotifyController extends FrontBaseController {
    @Autowired
    private MnyDrawRecordDao mnyDrawRecordDao;
    @Autowired
    private MnyDrawRecordService mnyDrawRecordService;
    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;

   // private static Logger logger = LoggerFactory.getLogger(JinxiangdaifuReplaceNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/jinxiangdaifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
     //   logger.error("回调金象代付数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("trade_no");
        // 用户提款记录
    //    logger.error("金象代付成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
    //        logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
     //       logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
      //      logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
     //       logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (!"sign".equals(k) && !"sign_type".equals(k) &&StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.substring(0,sb.length()-1) + replace.getMerchantKey();
     //   logger.error("回调金象代付签名数据:" + str);
        String sign = OnlinepayUtils.MD5(str,"utf-8").toLowerCase();
     //   logger.error("回调金象代付回调签名:" + map.get("sign"));
     //   logger.error("回调金象代付自加密sign:" + sign);
        if (map.get("sign").equalsIgnoreCase(sign)) {
      //      logger.error("回调金象代付验签成功，3、成功；4、失败");
            if ("3".equals(map.get("order_status"))) {
                synchronized (this) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "金象代付成功");
                }
                pWriter.print("success");
            } else if ("4".equals(map.get("order_status"))) {
                synchronized (this) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "金象代付失败");
                }
                pWriter.print("success");
            }
        }
    }
}