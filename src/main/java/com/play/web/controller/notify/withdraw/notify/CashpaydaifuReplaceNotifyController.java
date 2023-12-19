package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxicashpay.util.CashRSAEncrypt;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class CashpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
  //  private static Logger logger = LoggerFactory.getLogger(CashpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
      //  PrintWriter pWriter = response.getWriter();
        StringBuffer requstJsonData = new StringBuffer();
        BufferedReader reader = request.getReader();
        String line;
        while ((line= reader.readLine())!=null){
            requstJsonData.append(line);
        }
      //  logger.error("代付数据="+requstJsonData.toString());
        JSONObject object =  JSONObject.parseObject(requstJsonData.toString());
        String ordernumber = object.getString("merchantOrderId");
      //  logger.error("代付数据="+orderNo);
      //  logger.error("回调Cash代付数据:merchantOrderId" +ordernumber);
        // 用户提款记录
       // logger.error("tpay代付成功收到回调通知，订单号：" + ordernumber);
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
   //         logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
    //        logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        String returnSign = object.getString("sign");
        object.remove("sign");
        String customSign =  CashRSAEncrypt.sign(replace.getMerchantKey(),object);
        if (customSign.equals(returnSign)) {
         //   logger.error("回调cash代付验签成功，1、成功");
            if ("200".equals(object.getString("code"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "cash代付成功");
                }
                response.getWriter().print("success");
            } else if ("500".equals(object.getString("code"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "cash代付失败");
                }
                response.getWriter().print("success");
            }
        }
    }

}
