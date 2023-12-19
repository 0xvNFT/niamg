package com.play.pay.baxitrustpay.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;

public class HttpClientForTrustpay extends HttpClientProxy {
	private String content;
	private List<NameValuePair> parameter = new ArrayList<>();

	public static HttpClientForTrustpay newClient() {
		return new HttpClientForTrustpay();
	}

	@Override
	public String getPutContent() {
		return content;
	}

	public Response curl(String url) {
		return curl(url, HttpType.POST);
	}

	public HttpClientForTrustpay addContent(String content) {
		this.content = content;
		return this;
	}

	public HttpClientForTrustpay addParameter(Map<String, String> para) {
		para.forEach((k, v) -> parameter.add(new BasicNameValuePair(k, v)));
		return this;
	}

	public List<NameValuePair> getParameters() {
		return parameter;
	}

	@Override
	public void addHeader(List<Header> headerList) {
		headerList.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
	}

	@Override
	public HttpClient getClient() {
		return TrustpayConnectionManagerForHttps.getHttpClient();
	}

	public static Object getJSON(Response response) {
		String content = response.getContent();
		if (content.startsWith("{")) {
			JSONObject jsonObject = JSON.parseObject(content);
			return jsonObject;
		} else if (content.startsWith("[")) {
			JSONArray jsonArray = JSON.parseArray(content);
			return jsonArray;
		} else
			return null;
	}

	public static JSONObject getJSONObject(Response response) {
		String content = response.getContent();
		if (content.startsWith("{") == false)
			return null;
		return JSON.parseObject(content);
	}

	public static JSONArray getJSONArray(Response response) {
		String content = response.getContent();
		if (content.startsWith("[") == false)
			return null;
		return JSON.parseArray(content);
	}
}
