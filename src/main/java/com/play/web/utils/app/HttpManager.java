package com.play.web.utils.app;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpManager {
	
	private static int SOCKET_TIMEOUT = 35000;
	private static int CONNECT_TIMEOUT = 35000;
	private static int CONNECT_REQUEST_TIMEOUT = 35000;
	private static int MAX_TOTAL = 200;
	private static int REQUEST_RETRY = 3;

	private static CloseableHttpClient httpClient = null;

	/***
	public String hgHost;

	private String hgUsername;
	private String hgPasswd;

	private String uid = "";
	private String mtype = "4";
	private String langx = "zh-cn";
	**/
	
	static{
	
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

		PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
		// 设置整个连接池最大连接数 根据自己的场景决定
		conMgr.setMaxTotal(MAX_TOTAL);
		// 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
		//（目前只有一个路由，因此让他等于最大值）
		conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());

		//另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

		//设置重定向策略  
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		// 创建HttpClientBuilder
		// HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// httpClient = httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(conMgr).setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).build();

	}
	
	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
}
