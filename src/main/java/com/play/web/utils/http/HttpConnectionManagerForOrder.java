package com.play.web.utils.http;

import java.nio.charset.CodingErrorAction;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectionManagerForOrder {
	private static Logger logger = LoggerFactory.getLogger(HttpConnectionManagerForOrder.class);
	/**
	 * 读取超时时间
	 */
	private static int SOCKET_TIMEOUT = 500000;
	/**
	 * 连接超时时间
	 */
	private static int CONNECT_TIMEOUT = 500000;
	/**
	 * 从连接池获取连接的超时时间，如果连接池里连接都被用了，且超过这个connectionrequesttimeout，会抛出超时异常
	 */
	private static int CONNECT_REQUEST_TIMEOUT = 5000;
	/**
	 * 链接失败重试次数
	 */
	private static int REQUEST_RETRY = 3;
	/**
	 * 最大连接数
	 */
	public final static int MAX_TOTAL_CONNECTIONS = 1000;
	/**
	 * 获取连接的最大等待时间
	 */
	public final static int WAIT_TIMEOUT = 60000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 40;

	private static CloseableHttpClient httpClient = null;

	static {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			logger.error("初始化ssl发生错误", e);
		}
		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
				.setExpectContinueEnabled(true).setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		// Create socket configuration
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		// Configure the connection manager to use socket configuration either
		// by default or for a specific host.
		conMgr.setDefaultSocketConfig(socketConfig);
		conMgr.setValidateAfterInactivity(30000);
		// Create message constraints
//		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(40).setMaxLineLength(400)
//				.build();

		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
//				.setMessageConstraints(messageConstraints)
				.build();
		conMgr.setDefaultConnectionConfig(connectionConfig);
		// 设置整个连接池最大连接数 根据自己的场景决定
		conMgr.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		// 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for
		// connection from pool)，路由是对maxTotal的细分。
		// （目前只有一个路由，因此让他等于最大值）
		conMgr.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		// Use custom cookie store if necessary.
		CookieStore cookieStore = new BasicCookieStore();
		// Use custom credentials provider if necessary.
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

		// 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

		// 设置重定向策略
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

			@Override
			public long getKeepAliveDuration(HttpResponse res, HttpContext cxt) {

				long keepAlive = super.getKeepAliveDuration(res, cxt);
				if (keepAlive == -1) {
					// Keep connections alive 10 seconds if a keep-alive value
					// has not be explicitly set by the server
					keepAlive = 100000;
				}
				return keepAlive;
			}
		};

		// 创建HttpClientBuilder
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(conMgr)
				.setRetryHandler(retryHandler).setDefaultCredentialsProvider(credentialsProvider)
				.setRedirectStrategy(redirectStrategy).setDefaultCookieStore(cookieStore)
				.setKeepAliveStrategy(keepAliveStrat).build();

	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
}
