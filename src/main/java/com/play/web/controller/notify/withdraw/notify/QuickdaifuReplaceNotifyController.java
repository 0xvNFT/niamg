package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
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
import java.util.TreeMap;

/**
 * 回调代付controller类
 */
@Controller
@RequestMapping("/onlinePay/ReplaceNotify")
public class QuickdaifuReplaceNotifyController extends FrontBaseController {
    @Autowired
    private MnyDrawRecordDao mnyDrawRecordDao;
    @Autowired
    private MnyDrawRecordService mnyDrawRecordService;
    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;

    //private static Logger logger = LoggerFactory.getLogger(QuickdaifuReplaceNotifyController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/quickdaifu")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
    //    logger.error("回调快捷代付数据:" + JSONObject.toJSONString(map));
        SortedMap<String, String> datamap = new TreeMap<>();
        datamap = JSONObject.parseObject(map.get("result"), datamap.getClass());
        String ordernumber = datamap.get("orderid");
        // 用户提款记录
     //   logger.error("快捷代付成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
      //      logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
       //     logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
     //       logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
     //       logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }

        StringBuilder sb = new StringBuilder();
        datamap.forEach((k, v) -> {
            if (!"sign".equals(k) && StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.append("key=") + replace.getMerchantKey();
        String sign = OnlinepayUtils.MD5(str,"utf-8").toUpperCase();
     //   logger.error("回调快捷代付回调签名:" + map.get("sign"));
     //   logger.error("回调快捷代付签名数据:" + str.toString());
     //   logger.error("回调快捷代付自加密sign:" + sign);
        if (map.get("sign").equalsIgnoreCase(sign)) {
     //       logger.error("回调快捷代付验签成功，0=下发失败,1=下发成功");
            if ("10000".equals(map.get("state"))) {
                synchronized (this) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "快捷代付成功");
                }
                pWriter.print("success");
            }
//            else if ("0".equals(map.get("state"))) {
//                synchronized (this) {
//                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "快捷代付失败");
//                }
//                pWriter.print("success");
//            }
        }
    }
}