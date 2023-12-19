package com.play.pay.yespay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.pay.yespay.http.HttpClientForYesPay;
import com.play.pay.yespay.param.YesBaseParam;
import com.play.pay.yespay.result.YesBalanceResult;
import com.play.pay.yespay.result.YesCollectionResult;
import com.play.pay.yespay.result.YesOrderResult;
import com.play.pay.yespay.result.YesPayResult;
import com.play.pay.yespay.util.AssertUtils;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class YesPayApi {
    private static final  Logger logger = LoggerFactory.getLogger(YesPayApi.class);
    /**
     * 代收，发起充值，创建订单
     */
    private static final String COLLECTION_SUFFIX = "%s/create";
    /**
     * 代付，用户代付申请
     */
    private static final String AGENTPAY_SUFFIX = "%s/wd";
    /**
     * 获取订单支付信息
     */
    private static final String QUERY_ORDER_SUFFIX = "%s/getpayinfo";
    /**
     * 商户查询订单是否被取消
     */
    private static final String QUERY_CANCEL_SUFFIX = "%s/getcancel";
    /**
     * 获取余额
     */
    private static final String QUERY_BALANCE_SUFFIX = "%s/getbalance";
    /**
     * 根据orderid获取订单支付信息
     */
    private static final String QUERY_ORDER_BY_ID_SUFFIX = "%s/getpayinfobyorderid";

    public YesCollectionResult collection(String apiKey, String apiUrl, YesBaseParam param) {
        String url = String.format(COLLECTION_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesCollectionResult.class, HttpType.POST_JSON, apiKey, param.getOrderid(),
                param.getAmount().toString());
    }

    public YesPayResult agentPay(String apiKey, String apiUrl, YesBaseParam param) {
        String url = String.format(AGENTPAY_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesPayResult.class, HttpType.POST_JSON, apiKey, param.getOrderid(),
                param.getAmount().toString());
    }

    public YesOrderResult queryInfo(String apiUrl, YesBaseParam param) {
        String url = String.format(QUERY_ORDER_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesOrderResult.class, HttpType.GET);
    }

    public YesOrderResult queryIsCancel(String apiUrl, YesBaseParam param) {
        String url = String.format(QUERY_CANCEL_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesOrderResult.class, HttpType.GET);
    }

    public YesBalanceResult queryBalance(String apiKey, String apiUrl, YesBaseParam param) {
        String url = String.format(QUERY_BALANCE_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesBalanceResult.class, HttpType.POST_JSON, apiKey, param.getNoncestr(), param.getTimestamp());
    }

    public YesOrderResult queryInfoByOrderId(String apiKey, String apiUrl, YesBaseParam param) {
        String url = String.format(QUERY_ORDER_BY_ID_SUFFIX, urlFormat(apiUrl));
        return getResult(param, url, YesOrderResult.class, HttpType.POST_JSON, apiKey, param.getNoncestr(), param.getTimestamp(), param.getOrderid());
    }

    private <T> T getResult(YesBaseParam param, String url, Class<T> agentResultClass, HttpType httpType, String... signValues) {
        YesBaseParam yesBaseParam = assembleParam(param, signValues);
        logger.info("YesPayApi getResult, yesBaseParam:{}", JSONObject.toJSON(yesBaseParam));

        String response = doRequest(yesBaseParam, url, httpType);
        JSONObject retMap = JSONObject.parseObject(response);
        logger.info("YesPayApi getResult, retMap:{}", retMap);

        if ("1".equals(retMap.getString("code"))) {
            AssertUtils.assertNotNull(agentResultClass, "class must not be null");
            String resultStr = retMap.getString("data");
            return JSONObject.parseObject(resultStr, agentResultClass);
        }
        throw new RuntimeException(retMap.getString("msg"));
    }

    /**
     * 生成签名
     * @param param
     * @param signValues 需要签名的参数，需按要求顺序传参
     * @return
     */
    private YesBaseParam assembleParam(YesBaseParam param, String... signValues) {
        if(signValues == null || signValues.length < 1) {
            return param;
        }
        StringBuffer sb = new StringBuffer();
        for (String str : signValues) {
            sb.append(str);
        }
        String signedValue = MD5Util.md5(sb.toString().toLowerCase()).toLowerCase();
        param.setSign(signedValue);
        return param;
    }

    private String doRequest(YesBaseParam param, String url, HttpType httpType) {
        logger.info("YesPayApi doRequest, param:{}, url:{}, httpType:{}", param, url, httpType);

        Response response = null;
        if (httpType == HttpType.GET) {
            response = HttpClientForYesPay.newClient().addParameter("ticket", param.getTicket()).curl(url, httpType);
        } else  {
            response = HttpClientForYesPay.newClient().addContent(param.toJsonString()).curl(url, httpType);
        }

        logger.info("YesPayApi doRequest, response:{}", JSON.toJSONString(response));

        if (response.getCode() != 200)
            throw new RuntimeException("Unexpected code: " + response);
        String body = response.getContent();

        logger.info("YesPayApi doRequest, body:{}", body);
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
