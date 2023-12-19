package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxiowenpay.util.OwenRSAEncrypt;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 回调代付controller类
 */

public class OwenpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {

  //  private static Logger logger = LoggerFactory.getLogger(OwenpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws Exception {
        Map<String, String> map = getRequestDataForMap(request);
        String noticeParams = map.get("NoticeParams").split("&")[0];
        JSONObject jsonObject = JSONObject.parseObject(noticeParams);
        // logger.error("OKKPAY支付成功收到回调通知，通知内容：" + JSONObject.toJSONString(map));
        String orderNumber = jsonObject.getString("outTradeNo");
        // 用户提款记录
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(orderNumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
      //      logger.error("订单号不存在");
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
     //       logger.error("回调IP不在白名单内订单号:" + jsonObject.getString("outTradeNo"));
            return;
        }
        //金额,三方以元为单位
        String returnSign = jsonObject.getString("sign");
        Map<String,String> treeMap = new TreeMap<>();
        jsonObject.forEach((k,v)->{
            treeMap.put(k, (String) v);
        });
        treeMap.remove("sign");
        String customSign = OwenRSAEncrypt.encrypt(JSONObject.toJSONString(treeMap)+replace.getMerchantKey(),"utf-8");
        if (returnSign.equals(customSign)) {
            if ("00".equals(treeMap.get("remitResult"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "OwenPay代付成功");
                }
                response.getWriter().print("SUCCESS");
            } else if ("02".equals(treeMap.get("remitResult"))||"1000".equals(treeMap.get("remitResult"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "OwenPay代付失败");
                }
                response.getWriter().print("SUCCESS");
            }
        }
    }
}