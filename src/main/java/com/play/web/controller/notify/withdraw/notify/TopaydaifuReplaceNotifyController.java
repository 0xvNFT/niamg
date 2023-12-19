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


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TopaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {
   // private static Logger logger = LoggerFactory.getLogger(TopaydaifuReplaceNotifyController.class);

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
      //  logger.error("回调tpay代付数据:out_trade_no" + request.getParameter("out_trade_no"));
        Map<String, String> map = new HashMap<>();
        map.put("merchant_no",object.getString("merchant_no"));
        map.put("trade_no",object.getString("trade_no"));
        map.put("out_trade_no",object.getString("out_trade_no"));
        map.put("pay_amount",object.getString("pay_amount"));
        map.put("status",object.getInteger("status")+"");
        map.put("fee_amount",object.getString("fee_amount"));
        map.put("failure_reason",object.getString("failure_reason"));
        map.put("sign",object.getString("sign"));
    //    logger.error("回调tpay代付数据:" + JSONObject.toJSONString(map));
        String ordernumber = map.get("out_trade_no");
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
     //       logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
      //      logger.error("回调IP不在白名单内订单号:" + ordernumber);
            return;
        }
        String returnsign = map.get("sign");
        String key = replace.getAccount();
        String data = "";
        try {
            data = decrypt(key,returnsign);
        }catch (Exception e){
            e.printStackTrace();
        }
        StringBuilder sbCustom = new StringBuilder();
        sbCustom.append("fee_amount=").append(map.get("fee_amount")).append("&");
        sbCustom.append("merchant_no=").append(map.get("merchant_no")).append("&");
        sbCustom.append("out_trade_no=").append(map.get("out_trade_no")).append("&");
        sbCustom.append("pay_amount=").append(map.get("pay_amount")).append("&");
        sbCustom.append("status=").append(map.get("status")).append("&");
        sbCustom.append("trade_no=").append(map.get("trade_no")).append("&");
     //   logger.error("自己拼装的数据" + sb.toString());
      //  logger.error("验签后的数据局:" + data);
     //   logger.error("自己拼装的数据:" + sbCustom.toString());
        if (data.equals(sbCustom.toString())
        ) {
          //  logger.error("回调topay代付验签成功，1、成功");
            if ("1".equals(map.get("status"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "topay代付成功");
                }
                response.getWriter().print("success");
            } else if ("2".equals(map.get("status"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "topay代付失败");
                }
                response.getWriter().print("success");
            }
        }
    }

    public static String decrypt(String key,String data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException{
        byte[] decode = Base64.getDecoder().decode(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey generatePublic = kf.generatePublic(x509EncodedKeySpec);
        Cipher ci = Cipher.getInstance("RSA");
        ci.init(Cipher.DECRYPT_MODE,generatePublic);
        byte[] bytes = Base64.getDecoder().decode(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new String(byteArray);
    }
}
