package com.play.pay.baxisunpay;


import com.alibaba.fastjson.JSONObject;
import com.play.pay.baxisunpay.http.HttpClientForTopay;
import com.play.pay.baxisunpay.params.ITopayParamBase;
import com.play.pay.baxisunpay.params.SunPayParamQueryOrder;
import com.play.pay.baxisunpay.params.SunpayParamCollect;
import com.play.pay.baxisunpay.params.SunpayParamPay;
import com.play.pay.baxisunpay.result.SunPayqueryOrderResultStatus;
import com.play.pay.baxisunpay.result.SunpayResultOrder;
import com.play.pay.baxisunpay.util.SunpayRSAEncrypt;
import com.play.pay.goopago.util.AssertUtils;
import com.play.web.utils.http.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class SunpayApi {
	private final static Logger log = LoggerFactory.getLogger(SunpayApi.class);
	private final static String COLLECTION_SUFFIX = "%s/api/v3/Crypto/PayIn";
	private final static String AGENTPAY_SUFFIX = "%s/api/v3/Crypto/PayOut";
	private final static String QUERY_ORDER_STATUS_SUFFIX = "%s/api/v3/Crypto/PayOut";
	private final static String QUERY_BALANCE_SUFFIX = "%s/api/v3/Crypto/PayOut";

	public SunpayResultOrder collection(String publicKey, String privateKey, String url, SunpayParamCollect param) {
		String formattedURL = String.format(COLLECTION_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, SunpayResultOrder.class);
	}

	public SunpayResultOrder agentpay(String publicKey, String privateKey, String url, SunpayParamPay param) {
		String formattedURL = String.format(AGENTPAY_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, SunpayResultOrder.class);
	}

	public SunPayqueryOrderResultStatus queryOrderStatus(String publicKey, String privateKey, String url, SunPayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_ORDER_STATUS_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, SunPayqueryOrderResultStatus.class);
	}

	public SunPayqueryOrderResultStatus queryBalance(String publicKey, String privateKey, String url, SunPayParamQueryOrder param) {
		String formattedURL = String.format(QUERY_BALANCE_SUFFIX, urlFormat(url));
		return getResult(publicKey, privateKey, param, formattedURL, SunPayqueryOrderResultStatus.class);
	}

	private <T> T getResult(String publicKey, String privateKey, ITopayParamBase param, String url, Class<T> agentResultClass) {
		try {
			String response = null;
			if(param.toMap().getString("orderNo")!=null){


				response = get(param,url,privateKey,publicKey);
			}else {
				response = post(param,url,privateKey,publicKey);
			}

			JSONObject retMap = JSONObject.parseObject(response);
		//	log.info("response:{}", JSONObject.toJSONString(retMap, true));
			// 记录已存在
			if ("400034".equals(retMap.getString("code"))) {
				throw new RuntimeException(retMap.getString("msg"));
			}
			// 成功
			if ("200".equals(retMap.getString("code"))&&retMap.getBoolean("is_success")==true) {
				AssertUtils.assertNotNull(agentResultClass, "class must not be null");
				return JSONObject.parseObject(response, agentResultClass);
			}
			throw new RuntimeException(retMap.getString("msg"));
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	private String post(ITopayParamBase param, String url,String privateKey,String apisercet)  {
	//	log.info("url:{}", url);
		HttpClientForTopay httpClientForTopay = HttpClientForTopay.newClient();
		List<Header> list = new ArrayList<>();
		Header header = new BasicHeader("SunPay-Key",privateKey);
		list.add(header);
		Long timestamp=System.currentTimeMillis();
		Header header2 = new BasicHeader("SunPay-Timestamp",timestamp+"");
		list.add(header2);
		String nonce=UUID.randomUUID().toString().replace("-", "");
		Header header3 = new BasicHeader("SunPay-Nonce",nonce);
		list.add(header3);
		try {
			Header header4 = new BasicHeader("SunPay-Sign", SunpayRSAEncrypt.HexHMac(timestamp,nonce,
					JSONObject.toJSONString(param),apisercet));
			list.add(header4);
		}catch (Exception e){
			e.printStackTrace();
		}

		httpClientForTopay.setHeaderList(list);
		httpClientForTopay.addContent(param.toJsonString());
		Response response = httpClientForTopay.curl(url);
	//	log.info(JSON.toJSONString(response));
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
	//	log.info("response:{}", body);
		return body;
	}



	private String get(ITopayParamBase param, String url,String privateKey,String apisercet)  {
	//	log.info("url:{}", url);
		HttpClientForTopay httpClientForTopay = HttpClientForTopay.newClient();
		List<Header> list = new ArrayList<>();
		Header header = new BasicHeader("SunPay-Key",privateKey);
		list.add(header);
		Long timestamp=System.currentTimeMillis();
		Header header2 = new BasicHeader("SunPay-Timestamp",timestamp+"");
		list.add(header2);
		String nonce=UUID.randomUUID().toString().replace("-", "");
		Header header3 = new BasicHeader("SunPay-Nonce",nonce);
		list.add(header3);
		try {
			Header header4 = new BasicHeader("SunPay-Sign", SunpayRSAEncrypt.HexHMac(timestamp,nonce,"",apisercet));
			list.add(header4);
		}catch (Exception e){
			e.printStackTrace();
		}

		httpClientForTopay.setHeaderList(list);
		//	httpClientForTopay.addContent(param.toJsonString());
		Response response = httpClientForTopay.curlSunGet(url+"/"+param.toMap().getString("orderNo"));
	//	log.info(JSON.toJSONString(response));
		if (response.getCode() != 200)
			throw new RuntimeException("Unexpected code " + response);
		String body = response.getContent();
	//	log.info("response:{}", body);
		return body;
	}
	private static String urlFormat(String url) {
		if (StringUtils.isBlank(url))
			return url;
		url = url.trim();
		if (url.endsWith("/"))
			return url.substring(0, url.length() - 1);
		return url;
	}
}
