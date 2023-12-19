package com.play.pay.baxiowenpay.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;

import java.util.List;

public class HttpClientForTopay extends HttpClientProxy {
	private String content;

	public static HttpClientForTopay newClient() {
		return new HttpClientForTopay();
	}

	@Override
	public String getPutContent() {
		return content;
	}

	public Response curl(String url) {
		return curl(url, HttpType.POST_JSON);
	}
	public Response curlPost(String url) {
		return curl(url, HttpType.POST);
	}
	public Response curlPostForWowPay(String url) {
		return curl(url, HttpType.POST_WOW);
	}

	public HttpClientForTopay addContent(String content) {
		this.content = content;
		this._Content = content;
		return this;
	}

	@Override
	public void addHeader(List<Header> headerList) {
		headerList.add(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
	}

	@Override
	public HttpClient getClient() {
		return TopayConnectionManagerForHttps.getHttpClient();
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
