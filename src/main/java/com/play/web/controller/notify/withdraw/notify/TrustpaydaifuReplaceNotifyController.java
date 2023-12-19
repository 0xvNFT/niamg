package com.play.web.controller.notify.withdraw.notify;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.pay.baxitrustpay.util.TrustpayShaEncrypt;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class TrustpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
    //private static Logger logger = LoggerFactory.getLogger(TrustpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
       // logger.error("进入回调》》》》》》》》》"+request.getParameter("orderNo"));
      //  PrintWriter pWriter = response.getWriter();
     //   logger.error("回调TrustPay代付v2数据:pWriter" + pWriter);
        //SortedMap<String, Object> map = getRequestJsonData(request);
        Map<String,String> map = new HashMap<>();
        map.put("merchantId",request.getParameter("merchantId"));
        map.put("orderNo",request.getParameter("orderNo"));
        map.put("payNo",request.getParameter("payNo"));
        map.put("channel",request.getParameter("channel"));
        map.put("amount",request.getParameter("amount"));
        map.put("amountReal",request.getParameter("amountReal"));
        map.put("status",request.getParameter("status"));
        map.put("failMsg",request.getParameter("failMsg"));
        map.put("sign",request.getParameter("sign"));
     //   logger.error("回调TrustPay代付v2数据:map" +map);
     //   logger.error("回调TrustPay代付v2数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("orderNo").toString();
        // 用户提款记录
    //    logger.error("TrustPay代付v2成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
      //      logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
      //      logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
    //        logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
        //    logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
    //    logger.error("回调TrustPay代付v2自加密sign:" + map.get("sign"));
        //String signStr = TrustpayShaEncrypt.genSignObject(map,replace.getMerchantKey());
        if (checkSign(map,replace.getMerchantKey(),null)) {
            if ("2".equals(map.get("status"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "TrustPay代付v2成功");
                }
                response.getWriter().print("success");
            } else if ("3".equals(map.get("status"))||"5".equals(map.get("status"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "TrustPay代付v2失败");
                }
                response.getWriter().print("success");
            }
        }
    }
    private boolean checkSign(Map<String, String> map, String key, String payName) {
        String sign = map.get("sign");
     //   logger.error(payName + ":" + "三方回调签名前字符串：" + sign);
        map.remove("sign");
        String signStr = TrustpayShaEncrypt.genSign(map,key);
     //   logger.error(payName + ":" + "自己生成加密字符串：" + signStr);
     //   logger.error("开始比较"+sign+"和"+signStr);
        if (sign.equals(signStr)) {
      //      logger.info(payName + ":" + "三方签名认证通过：" + sign);
            return true;
        } else {
    //        logger.error(payName + ":" + "三方签名认证失败：" + sign);
            return false;
        }
    }


}
