package com.play.tronscan.allclient.alltool;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.tronscan.allclient.conf.TronConfig;
import com.play.web.utils.http.HttpClientProxy;
import com.play.web.utils.http.HttpConnectionManagerForHttps;
import com.play.web.utils.http.HttpType;
import com.play.web.utils.http.Response;

public class HttpClientForTron extends HttpClientProxy {
	private List<NameValuePair> parameterList = new ArrayList<>();

	public static HttpClientForTron newClient() {
		return new HttpClientForTron();
	}

	public HttpClientForTron addParameter(String name, Object value) {
		if (name == null || value == null)
			return this;
		parameterList.add(new BasicNameValuePair(name, String.valueOf(value)));
		return this;
	}

	public Response curl(String url) {
		return curl(url, HttpType.GET);
	}

	@Override
	public void addHeader(List<Header> headerList) {
		String apiKey = TronConfig.getAPIKey();
		if (StringUtils.isBlank(apiKey))
			return;
		apiKey = apiKey.trim();
		headerList.add(new BasicHeader("TRON-PRO-API-KEY", apiKey));
	}

	@Override
	public List<NameValuePair> getParameters() {
		return parameterList;
	}

	@Override
	public HttpClient getClient() {
		return HttpConnectionManagerForHttps.getHttpClient();
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
