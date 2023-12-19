package com.play.pay.speedlypay;

import com.alibaba.fastjson.JSON;
import com.play.pay.speedlypay.param.SpeedlyBaseParam;
import com.play.pay.speedlypay.param.SpeedlyCollectionParam;
import com.play.pay.speedlypay.param.SpeedlyPayParam;
import com.play.pay.speedlypay.param.SpeedlyPayerOrPayeeParam;
import com.play.pay.speedlypay.result.SpeedlyBalanceResult;
import com.play.pay.speedlypay.result.SpeedlyCollectionResult;
import com.play.pay.speedlypay.result.SpeedlyOrderResult;
import com.play.pay.speedlypay.result.SpeedlyPayResult;
import com.play.pay.speedlypay.util.SecretUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class SpeedlyPayApiTest {
    private static final Logger logger = LoggerFactory.getLogger(SpeedlyPayApiTest.class);
    private static final String mchId = "BC600069";
    private static final String appId = "sp1657803106840350720m";
    private static final String collKey = "XUWBR4QQ8D66OUFCQY37VMSHH8ZAOKIH";
    private static final String payKey = "UU7Q4DOS2LEEIVNGCE0PP92KWNACFJ9X";
    private static final String apiUrl = "https://api.sandbox.speedlyglobal.com/api";

    public static void collection() {
        SpeedlyCollectionParam param = new SpeedlyCollectionParam();
        param.setCountry("BR");
        param.setCurrency("BRL");
        param.setPayment_method_id("PIX");
        param.setPayment_method_flow("REDIRECT");
        param.setOrder_id("D23062822242000001");
        param.setAmount(new BigDecimal("10.0000").setScale(2, BigDecimal.ROUND_DOWN));
        param.setNotification_url("www.baidu.com");
        param.setSuccess_redirect_url("www.baidu.com");
        param.setExtend("");
        param.setTimestamp(Instant.now().toEpochMilli());

        SpeedlyCollectionResult result = new SpeedlyPayApi().collection(mchId, appId, collKey, apiUrl, param, null);
        logger.error("SpeedlyPayApiTest collection, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void pay() {
        SpeedlyPayParam param = new SpeedlyPayParam();
        param.setCountry("BR");
        param.setCurrency("BRL");
        param.setOrder_id("D23062822242000004");
        param.setAmount(new BigDecimal("10.0000").setScale(2, BigDecimal.ROUND_DOWN));
        param.setNotification_url("www.baidu.com");
        param.setTimestamp(Instant.now().toEpochMilli());

        SpeedlyPayerOrPayeeParam payeeParam = new SpeedlyPayerOrPayeeParam();
        payeeParam.setName("Miscro Duma");
        payeeParam.setAccount("+5521255527871");
        payeeParam.setAccount_type("PHONE");
        payeeParam.setPhone("+5521255527871");
        SpeedlyPayResult result = new SpeedlyPayApi().pay(mchId, appId, payKey, apiUrl, param, payeeParam);
        logger.error("SpeedlyPayApiTest pay, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryCollOrder() {
        SpeedlyBaseParam param = new SpeedlyBaseParam();
        param.setOrder_id("D23062822242000003");

        SpeedlyOrderResult result = new SpeedlyPayApi().queryCollOrder(mchId, appId, apiUrl, param);
        logger.error("SpeedlyPayApiTest queryCollOrder, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryPayOrder() {
        SpeedlyBaseParam param = new SpeedlyBaseParam();
        param.setOrder_id("D23062822242000004");

        SpeedlyOrderResult result = new SpeedlyPayApi().queryPayOrder(mchId, appId, apiUrl, param);
        logger.error("SpeedlyPayApiTest queryPayOrder, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void queryBalance() {
        SpeedlyBalanceResult result = new SpeedlyPayApi().queryBalance(mchId, appId, payKey, apiUrl);
        logger.error("SpeedlyPayApiTest queryBalance, result:{}", JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
    }

    public static void main(String[] args) {
        collection();
        //pay();
        //queryCollOrder();
        //queryPayOrder();
        //queryBalance();
    }
}
