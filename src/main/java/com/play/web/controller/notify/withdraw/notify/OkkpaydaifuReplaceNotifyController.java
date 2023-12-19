package com.play.web.controller.notify.withdraw.notify;


import com.play.cache.redis.DistributedLockUtil;
import com.play.common.ReplaceNotifyWrapper;
import com.play.dao.MnyDrawRecordDao;
import com.play.model.MnyDrawRecord;
import com.play.model.StationReplaceWithDraw;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.SystemUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 回调代付controller类
 */

public class OkkpaydaifuReplaceNotifyController extends FrontBaseController implements ReplaceNotifyWrapper {

   // private static Logger logger = LoggerFactory.getLogger(OkkpaydaifuReplaceNotifyController.class);

    public void replaceNotify(HttpServletRequest request, HttpServletResponse response, String iconCss,
                              MnyDrawRecordDao mnyDrawRecordDao, MnyDrawRecordService mnyDrawRecordService,
                              StationReplaceWithDrawService stationReplaceWithDrawService) throws Exception {
        Map<String, String> map = getRequestDataForMap(request);
        // 用户提款记录
        MnyDrawRecord mnyDrawRecord = mnyDrawRecordDao.getMnyRecordByOrderId(map.get("orderNo"), SystemUtil.getStationId());
        if (mnyDrawRecord == null) {
       //     logger.error("订单号不存在");
            return;
        }
        // 判断记录是否处理未处理状态
        if (!mnyDrawRecord.getStatus().equals(MnyDrawRecord.STATUS_UNDO) || mnyDrawRecord.getRemark().contains("代付失败")) {
    //        logger.error("此记录已经被处理过！");
            return;
        }
        StationReplaceWithDraw replace = stationReplaceWithDrawService.findOneNoHideById(mnyDrawRecord.getPayId(), SystemUtil.getStationId());
        if (replace == null) {
   //         logger.error("付款方式为空");
            return;
        }
        if (!authenticatedIP(request, replace.getWhiteListIp())) {
   //         logger.error("回调IP不在白名单内订单号:" + map.get("orderNo"));
            return;
        }
        if (rsaCheck(map,replace.getAccount())) {
          //  logger.error("回调Okkpay代付验签成功，2、成功");
            if ("2".equals(map.get("status"))) {
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_SUCCESS, "OkkPay代付成功");
                }
                response.getWriter().print("success");
            } else if ("7".equals(map.get("status"))||"0".equals(map.get("status"))){
                if (DistributedLockUtil.tryGetDistributedLock("onlinePay:ReplaceNotify:" + mnyDrawRecord.getUserId(), 5)) {
                    mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(mnyDrawRecord.getId(), MnyDrawRecord.STATUS_UNDO, "OkkPay代付失败");
                }
                response.getWriter().print("success");
            }
        }
    }


    public static boolean rsaCheck(Map<String, String> map, String publicKey) throws Exception {
        String charset = "utf-8";
        map.put("charset", charset);

        String content = getSignContent(map);

        String sign = map.get("sign");

        PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
        java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);

        if (StringUtils.isEmpty(charset)) {
            signature.update(content.getBytes());
        } else {
            signature.update(content.getBytes(charset));
        }

        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        String signParam = getSignParam(sortedParams);
        String[] sign_param = signParam.split(",");// ����ǩ������Ĳ���
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < sign_param.length; i++) {
            keys.add(sign_param[i].trim());
        }
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value) && !key.equals("sign")) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }
    private static String getSignParam(Map<String, String> sortedParams) {
        Set<String> setKeys = sortedParams.keySet();
        String signParam = setKeys.toString();
        signParam = signParam.substring(1, signParam.length() - 1);
        return signParam;

    }
    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        StringWriter writer = new StringWriter();
        io(new InputStreamReader(ins), writer, -1);

        byte[] encodedKey = writer.toString().getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }
    private static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 8192 >> 1;
        }

        char[] buffer = new char[bufferSize];
        int amount;

        while ((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }
    }


}