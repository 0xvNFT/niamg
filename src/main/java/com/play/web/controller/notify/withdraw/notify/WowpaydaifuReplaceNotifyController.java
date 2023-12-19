package com.play.web.controller.notify.withdraw.notify;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
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
import java.io.BufferedReader;
import java.io.IOException;

import java.security.MessageDigest;
import java.util.*;

/**
 * 回调代付controller类
 */

public class WowpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {

   // private static Logger logger = LoggerFactory.getLogger(WowpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws IOException {
       // PrintWriter pWriter = response.getWriter();
        SortedMap<String, String> map = getRequestData(request);
        boolean flag = false;
        JSONObject object = null;
        StringBuilder requstJsonData = null;
        if(map.isEmpty()){
            flag = true;
            requstJsonData = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line= reader.readLine())!=null){
                requstJsonData.append(line);
            }
            object = JSONObject.parseObject(requstJsonData.toString());
      //      logger.error("WOWpay接收回调的数据：" + requstJsonData.toString());
      //      logger.error("WOWpay代付成功收到回调通知，订单号：" + object.getString("merTransferId"));
        }
     //   logger.error("回调WOWpAY代付数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("merTransferId")==null?object.getString("merTransferId"):map.get("merTransferId");
        // 用户提款记录
      //  logger.error("tpay代付成功收到回调通知，订单号：" + ordernumber);
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(ordernumber, SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
            //       logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
      //      logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
      //      logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
       //     logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        String returnSign = null;
        if(flag){
            map.put("applyDate",object.getString("applyDate"));
            map.put("merNo",object.getString("merNo"));
            map.put("merTransferId",object.getString("merTransferId"));
            map.put("respCode",object.getString("respCode"));
            map.put("tradeNo",object.getString("tradeNo"));
            map.put("tradeResult",object.getString("tradeResult"));
            map.put("transferAmount",object.getString("transferAmount"));
            map.put("version",object.getString("version"));
            returnSign = object.getString("sign");
        }else {
            returnSign = map.get("sign");
            map.remove("sign");
            map.remove("signType");
        }
      //  logger.error("sginStr="+this.getSign(map));
      //  logger.error("key="+replace.getAccount());
       // logger.error(validateSignByKey(this.getSign(map),replace.getAccount(),returnSign)+"");
        if (validateSignByKey(this.getSign(map),replace.getAccount(),returnSign)) {
        //    logger.error("回调uzpay代付验签成功，1、成功");
            if ("1".equals(map.get("tradeResult"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "Wowpay代付成功");
                }
                response.getWriter().print("success");
            } else if ("2".equals(map.get("tradeResult"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "Wowpay代付失败");
                }
                response.getWriter().print("success");
            }
        }
    }
    public static boolean validateSignByKey(String signSource, String key, String retsign) {
        if (StringUtils.isNotBlank(key)) {
            signSource = signSource + "&key=" + key;
        }
        return calculate(signSource).equals(retsign);
    }
    private static String calculate(String signSource) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signSource.getBytes("utf-8"));
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 生成签名
     * @param map
     * @return
     */
    public  String getSign(Map<String, String> map) {
        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            result = sb.toString().substring(0,sb.length()-1);
            //进行MD5加密
        } catch (Exception e) {
            return null;
        }
        return result;
    }

}