package com.play.pay.yespay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.RandomStringUtils;
import com.play.pay.yespay.param.*;
import com.play.pay.yespay.result.YesBalanceResult;
import com.play.pay.yespay.result.YesCollectionResult;
import com.play.pay.yespay.result.YesOrderResult;
import com.play.pay.yespay.result.YesPayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class YesPayApiTest {
    private static final Logger logger = LoggerFactory.getLogger(YesPayApiTest.class);
    private static final String mchCode = "c2caa92b67cc46c2806836e07d1c6917";
    private static final String apiKey = "hb73oxsu6zqity0x883yth0w6pxmgf49";
    private static final String apiUrl = "https://jsue13qsoi.yespay.bet/api";

    public static void collection() {
        YesCollectionParam param = new YesCollectionParam();
        param.setUserid(mchCode);
        param.setOrderid("D23062822242000003");
        param.setAmount(new BigDecimal(100000).setScale(4, BigDecimal.ROUND_HALF_DOWN));
        param.setType("momo");
        param.setNotifyurl("");
        param.setReturnurl("");
        param.setNote("YesPay Test");

        YesCollectionResult result = new YesPayApi().collection(apiKey, apiUrl, param);
        logger.error("YesPayApiTest collection, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void agentPay() {
        YesPayParam param = new YesPayParam();
        param.setUserid(mchCode);
        param.setOrderid("D23062822242000002");
        param.setAmount(new BigDecimal(1000).setScale(4, BigDecimal.ROUND_HALF_DOWN));
        param.setNotifyurl("");
        param.setReturnurl("");
        param.setNote("YesPay Test");

        YesPayLoadParam loadParam = new YesPayLoadParam();
        loadParam.setCardname("zhangsan");
        loadParam.setCardno("123456");
        loadParam.setBankid("111");
        loadParam.setBankname("aaabank");

        param.setPayload(loadParam.toJsonString());

        YesPayResult result = new YesPayApi().agentPay(apiKey, apiUrl, param);
        logger.error("YesPayApiTest agentPay, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryInfo() {
        YesBaseParam param = new YesBaseParam();
        param.setTicket("b0e39ed8e701a36d975954b9453dd7fe");

        YesOrderResult result = new YesPayApi().queryInfo(apiUrl, param);
        logger.error("YesPayApiTest queryInfo, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryIsCancel() {
        YesBaseParam param = new YesBaseParam();
        param.setTicket("b0e39ed8e701a36d975954b9453dd7fe");

        YesOrderResult result = new YesPayApi().queryIsCancel(apiUrl, param);
        logger.error("YesPayApiTest queryIsCancel, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryBalance() {
        YesBaseParam param = new YesBaseParam();
        param.setUserid(mchCode);
        param.setNoncestr(RandomStringUtils.randomAll(16));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));

        YesBalanceResult result = new YesPayApi().queryBalance(apiKey, apiUrl, param);
        logger.error("YesPayApiTest queryBalance, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryInfoByOrderId() {
        YesBaseParam param = new YesBaseParam();
        param.setUserid(mchCode);
        param.setNoncestr(RandomStringUtils.randomAll(16));
        param.setTimestamp(String.valueOf(System.currentTimeMillis()));
        param.setOrderid("D23062822242000001");

        YesOrderResult result = new YesPayApi().queryInfoByOrderId(apiKey, apiUrl, param);
        logger.error("YesPayApiTest queryInfoByOrderId, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void main(String[] args) {
//        collection();
//        agentPay();
        queryInfo();
//        queryIsCancel();
//        queryBalance();
//        queryInfoByOrderId();
    }
}
