package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxisunpay.util.SunpayRSAEncrypt;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
/**
 * 回调代付controller类
 */
public class SunpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {

   // private static Logger logger = LoggerFactory.getLogger(SunpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws Exception {
        String SunPay_Timestamp =  request.getHeader("SunPay-Timestamp");
        String SunPay_Nonce =  request.getHeader("SunPay-Nonce");
        String SunPay_Sign =  request.getHeader("SunPay-Sign");
        StringBuffer requstJsonData = new StringBuffer();//
        BufferedReader reader = request.getReader();
        String line;
        while ((line= reader.readLine())!=null){
            requstJsonData.append(line);
        }
        JSONObject object =  JSONObject.parseObject(requstJsonData.toString());
        String orderNumber = object.getJSONObject("data").getString("out_order_no");
   //     logger.error("SunPAY支付成功收到回调通知，订单号：" + orderNumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(orderNumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
     //       logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
       //     logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
       //     logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
       //     logger.error("回调IP不在白名单内订单号:" + orderNumber);
            return;
        }
        String customSign = SunpayRSAEncrypt.HexHMac(Long.parseLong(SunPay_Timestamp),SunPay_Nonce,
                requstJsonData.toString(),replace.getAccount());
        //金额,三方以元为单位
        if (customSign.equals(SunPay_Sign)) {
       //     logger.error("回调SUNpay代付验签成功，2、成功");
            if (object.getString("biz_status").equals("SUCCESS")
                    &&object.getString("biz_type").equals("PAYOUT")) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Sunpay代付成功");

                }
                response.getWriter().print("{\"is_success\": true,\"message\": \"success\"}");
            } else if (object.getString("biz_status").equals("FAIL")
                    &&object.getString("biz_type").equals("PAYOUT")){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Sunpay代付失败");
                }
                response.getWriter().print("{\"is_success\": true,\"message\": \"success\"}");
            }
        }
    }
}