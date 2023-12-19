package com.play.web.controller.notify.withdraw.notify;


import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.common.utils.security.OnlinepayUtils;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.SortedMap;

/**
 * 回调代付controller类
 */

public class BossdaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {

  //  private static Logger logger = LoggerFactory.getLogger(BossdaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
        PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
     //   logger.error("回调boss代付数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("merissuingcode");
        // 用户提款记录
     //   logger.error("boss代付成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
    //        logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
    //        logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
     //       logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
      //      logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> {
            if (!"signs".equals(k)&& StringUtils.isNotEmpty(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        });
        String str = sb.append("key=") + replace.getMerchantKey();
     //   logger.error("回调boss代付签名数据:" + str);
        String sign = OnlinepayUtils.MD5(str, "utf-8").toUpperCase();
     //   logger.error("回调boss代付自加密sign:" + sign);
        if (map.get("signs").equalsIgnoreCase(sign)) {
     //       logger.error("回调boss代付验签成功");
            if ("SUCCESS".equals(map.get("returncode"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "boss代付成功");
                }
                pWriter.print("OK");
            } else {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "boss代付失败");
                }
                pWriter.print("fail");
            }
        }
    }
}