package com.play.pay.yespay.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.utils.http.HttpConnectionManagerForHttps;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientForYesPay extends HttpClientProxy {
	private String content;
	private List<NameValuePair> parameterList = new ArrayList<>();

	public static HttpClientForYesPay newClient() {
		return new HttpClientForYesPay();
	}

	public HttpClientForYesPay addContent(String content) {
		this.content = content;
		return this;
	}

	@Override
	public String getPutContent() {
		return content;
	}

	@Override
	public void addHeader(List<Header> headerList) {
		headerList.add(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
	}

	@Override
	public HttpClient getClient() {
		return HttpConnectionManagerForHttps.getHttpClient();
	}

	@Override
	public List<NameValuePair> getParameters() {
		return parameterList;
	}

	public HttpClientForYesPay addParameter(String key, Object value) {
		if (key == null || value == null)
			return this;
		parameterList.add(new BasicNameValuePair(key, String.valueOf(value)));
		return this;
	}

	public HttpClientForYesPay addParameter(Map<String, String> param) {
		param.forEach((k, v) -> parameterList.add(new BasicNameValuePair(k, v)));
		return this;
	}

	public Response curl(String url) {
		return curl(url, HttpType.GET);
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
