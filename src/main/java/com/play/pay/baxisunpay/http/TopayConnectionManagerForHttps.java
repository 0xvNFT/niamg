package com.play.pay.baxisunpay.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TopayConnectionManagerForHttps {
	private static Logger logger = LoggerFactory.getLogger(TopayConnectionManagerForHttps.class);
	/**
	 * 读取超时时间
	 */
	private static int SOCKET_TIMEOUT = 3000;
	/**
	 * 连接超时时间
	 */
	private static int CONNECT_TIMEOUT = 3000;
	/**
	 * 从连接池获取连接的超时时间，如果连接池里连接都被用了，且超过这个connectionrequesttimeout，会抛出超时异常
	 */
	private static int CONNECT_REQUEST_TIMEOUT = 3000;
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
	public final static int WAIT_TIMEOUT = 3000;
	/**
	 * 每个路由最大连接数
	 */
	public final static int MAX_ROUTE_CONNECTIONS = 40;

	private static CloseableHttpClient httpClient = null;

	static {
		SSLContextBuilder builder = new SSLContextBuilder();
		// 全部信任 不做身份鉴定

		LayeredConnectionSocketFactory sslsf = null;
		try {
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] {"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
			// sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			logger.error("初始化ssl发生错误", e);
		} catch (KeyManagementException e) {
			logger.error("初始化ssl发生错误2", e);
		} catch (KeyStoreException e) {
			logger.error("初始化ssl发生错误3", e);
		}
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		// 设置整个连接池最大连接数 根据自己的场景决定
		conMgr.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		// 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for
		// connection from pool)，路由是对maxTotal的细分。
		// （目前只有一个路由，因此让他等于最大值）
		conMgr.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

		// 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

		// 设置重定向策略
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

		// 创建HttpClientBuilder
		// HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// httpClient =
		// httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig).build();
		httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(conMgr)
				.setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).build();

	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}
}
