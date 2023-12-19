package com.play.pay.speedlypay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.RandomStringUtils;
import com.play.pay.speedlypay.constant.SpeedlyConstant;
import com.play.pay.speedlypay.http.HttpClientForSpeedlyPay;
import com.play.pay.speedlypay.param.SpeedlyBaseParam;
import com.play.pay.speedlypay.param.SpeedlyPayerOrPayeeParam;
import com.play.pay.speedlypay.result.SpeedlyBalanceResult;
import com.play.pay.speedlypay.result.SpeedlyOrderResult;
import com.play.pay.speedlypay.result.SpeedlyCollectionResult;
import com.play.pay.speedlypay.result.SpeedlyPayResult;
import com.play.pay.speedlypay.util.AssertUtils;
import com.play.pay.speedlypay.util.SecretUtils;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeedlyPayApi {
    private static final  Logger logger = LoggerFactory.getLogger(SpeedlyPayApi.class);
    /**
     * 代收，发起充值，创建订单
     */
    private static final String COLLECTION_SUFFIX = "%s/pay/payment";
    /**
     * 代付，用户代付申请
     */
    private static final String PAY_SUFFIX = "%s/settle/settlement";
    /**
     * 代收订单查询
     */
    private static final String QUERY_COLL_ORDER_SUFFIX = "%s/pay/queryPaymentOrder";
    /**
     * 代付订单查询
     */
    private static final String QUERY_PAY_ORDER_SUFFIX = "%s/settle/querySettlementOrder";
    /**
     * 获取余额
     */
    private static final String QUERY_BALANCE_SUFFIX = "%s/merchant/balance";

    public SpeedlyCollectionResult collection(String mchId, String appId, String collKey, String apiUrl, SpeedlyBaseParam param, SpeedlyPayerOrPayeeParam accountParam) {
        String url = String.format(COLLECTION_SUFFIX, urlFormat(apiUrl));
        return getResult(mchId, appId, collKey, url, param, accountParam, SpeedlyConstant.PAYER, SpeedlyCollectionResult.class, HttpType.POST_JSON);
    }

    public SpeedlyPayResult pay(String mchId, String appId, String payKey, String apiUrl, SpeedlyBaseParam param, SpeedlyPayerOrPayeeParam accountParam) {
        String url = String.format(PAY_SUFFIX, urlFormat(apiUrl));
        return getResult(mchId, appId, payKey, url, param, accountParam, SpeedlyConstant.PAYEE, SpeedlyPayResult.class, HttpType.POST_JSON);
    }

    public SpeedlyOrderResult queryCollOrder(String mchId, String appId, String apiUrl, SpeedlyBaseParam param) {
        String url = String.format(QUERY_COLL_ORDER_SUFFIX, urlFormat(apiUrl));
        return getResult(mchId, appId, null, url, param, null, null, SpeedlyOrderResult.class, HttpType.POST_JSON);
    }

    public SpeedlyOrderResult queryPayOrder(String mchId, String appId, String apiUrl, SpeedlyBaseParam param) {
        String url = String.format(QUERY_PAY_ORDER_SUFFIX, urlFormat(apiUrl));
        return getResult(mchId, appId, null, url, param, null, null, SpeedlyOrderResult.class, HttpType.POST_JSON);
    }

    public SpeedlyBalanceResult queryBalance(String mchId, String appId, String payKey, String apiUrl) {
        String url = String.format(QUERY_BALANCE_SUFFIX, urlFormat(apiUrl));
        return getResult(mchId, appId, payKey, url, null, null,SpeedlyConstant.QUERY_BALANCE,  SpeedlyBalanceResult.class, HttpType.POST_JSON);
    }

    private <T> T getResult(String mchId, String appId, String key, String url, SpeedlyBaseParam param, SpeedlyPayerOrPayeeParam accountParam, Integer accountType,
                            Class<T> agentResultClass, HttpType httpType) {
        JSONObject baseParam = assembleParam(mchId, key, param, accountParam, accountType);
        logger.info("SpeedlyPayApi getResult, baseParam:{}", baseParam);

        String response = doRequest(baseParam, appId, url, httpType);
        JSONObject retMap = JSONObject.parseObject(response);
        logger.info("SpeedlyPayApi getResult, retMap:{}", retMap);

        if ("ok".equals(retMap.getString("state"))) {
            AssertUtils.assertNotNull(agentResultClass, "class must not be null");
            String resultStr = retMap.getString("data");
            return JSONObject.parseObject(resultStr, agentResultClass);
        }
        throw new RuntimeException(retMap.getString("errorMsg"));
    }

    /**
     * 生成签名，组装参数
     * @param mchId
     * @param key
     * @param param
     * @return
     */
    private JSONObject assembleParam(String mchId, String key, SpeedlyBaseParam param, SpeedlyPayerOrPayeeParam accountParam, Integer accountType) {
        JSONObject obj = new JSONObject();
        obj.put("merchant_no", mchId);

        if (SpeedlyConstant.QUERY_BALANCE == accountType) {
            Map<String, Object> balParam = new HashMap<>();
            balParam.put("merchant_no", mchId);
            String sign = SecretUtils.sign(balParam, key);
            obj.put("signature", sign);
            return obj;
        }

        String signedValue = SecretUtils.sign(param.toMap(), key);
        param.setSignature(signedValue);
        Map<String, Object> data = SecretUtils.getNotEmptyParam(param.toMap());
        if (null != accountParam) {
            Map<String, Object> payeeMap = SecretUtils.getNotEmptyParam(accountParam.toMap());
            if (SpeedlyConstant.PAYER == accountType) {
                data.put("payer", payeeMap);
            } else if (SpeedlyConstant.PAYEE == accountType) {
                data.put("payee", payeeMap);
            }
        }
        obj.put("data", data);
        return obj;
    }

    private String doRequest(JSONObject param, String appId, String url, HttpType httpType) {
        logger.info("SpeedlyPayApi doRequest, param:{}, url:{}, httpType:{}", param, url, httpType);
        //String context = HttpClientUtils.getInstance().sendHttpsRequestByPostJson(url, param.toJsonString(), new HashMap<>());
        HttpClientForSpeedlyPay httpClientForSpeedlyPay = HttpClientForSpeedlyPay.newClient().addContent(param.toJSONString());

        List<Header> headerList = new ArrayList<Header>();
        headerList.add(new BasicHeader("ApiVersion", "1.0"));
        headerList.add(new BasicHeader("AppId", appId));
        headerList.add(new BasicHeader("Noncestr", RandomStringUtils.randomInt(8)));
        headerList.add(new BasicHeader("Timestamp", String.valueOf(Instant.now().toEpochMilli())));
        httpClientForSpeedlyPay.getHeaderList().addAll(headerList);

        Response response = httpClientForSpeedlyPay.curl(url, httpType);

        logger.info("SpeedlyPayApi doRequest, response:{}", JSON.toJSONString(response));

        if (response.getCode() != 200)
            throw new RuntimeException("Unexpected code: " + response);
        String body = response.getContent();

        logger.info("SpeedlyPayApi doRequest, body:{}", body);
        return body;
    }

    private String urlFormat(String url) {
        if (StringUtils.isBlank(url))
            return url;
        url = url.trim();
        if (url.endsWith("/"))
            return url.substring(0, url.length() - 1);
        return url;
    }
}
