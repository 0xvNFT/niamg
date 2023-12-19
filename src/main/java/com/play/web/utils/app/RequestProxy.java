package com.play.web.utils.app;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.play.web.exception.NativeHttpResponseException;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public abstract class RequestProxy {

	/**
	 * 浏览器
	 */
	public static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows XP)";

	// private Logger logger = Logger.getLogger(RequestProxy.class.getSimpleName());

	private HttpRequestBase requestBase = null;

	private PostType postType = null;

	public String doRequest(String url, PostType type) {
		return doRequest(url, type, false);
	}

	public String doRequest(String url, PostType type, boolean checkSysError) {
		postType = type;
		HttpEntity httpEntity = null;
		try {
			HttpResponse httpResponse = null;
			if (type == PostType.GET) {
				try {
					httpResponse = this.doGet(url);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} else {
				httpResponse = this.doPost(url);
			}
			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			httpEntity = httpResponse.getEntity();
			if (statusCode == 200) {
				String content = getContent(httpEntity);
				if (!checkSysError) {
					return content;
				}
				Header[] hs = httpResponse.getHeaders("ceipstate");
				if (hs == null || hs.length == 0) {
					return content;
				}
				String responseStatus = hs[0].getValue();
				String defErrorMsg = "数据中心异常[ceipstate:" + responseStatus + "]";
				if (!"1".equals(responseStatus)) {
					try {
						JSONObject json = JSONObject.parseObject(content);
						String msg = json.getString("msg");
						if (msg == null) {
							msg = defErrorMsg;
						}
						throw new IllegalStateException(msg);
					} catch (JSONException e) {
						throw new IllegalStateException(defErrorMsg);
					}
				}
				return content;
			} else if (statusCode > 500) {
				String content = getContent(httpEntity);
				throw new NativeHttpResponseException(statusCode, content);
			}
			throw new NativeHttpResponseException(statusCode);
		} catch (NativeHttpResponseException e) {
			try {
				throw new NativeHttpResponseException(404, "URL[" + url + "]异常");
			} catch (NativeHttpResponseException e1) {
				e1.printStackTrace();
			}
		} catch (HttpHostConnectException | ConnectTimeoutException | UnknownHostException e) {
			try {
				throw new NativeHttpResponseException(404, e.getClass().getName() + ":" + e.getMessage());
			} catch (NativeHttpResponseException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				throw new NativeHttpResponseException(404);
			} catch (NativeHttpResponseException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (requestBase != null) {
				requestBase.abort();
			}
			if (httpEntity != null) {
				try {
					EntityUtils.consume(httpEntity);
				} catch (IOException e) {

				}
			}
		}
		return "";
	}

	private String getContent(HttpEntity httpEntity) throws ParseException, IOException {
		if (httpEntity == null) {
			return "";
		}
		String content = EntityUtils.toString(httpEntity, "UTF-8");
		return content;
	}

	public String doRequest(String url) {
		return doRequest(url, PostType.GET);
	}

	private HttpResponse doGet(String url) throws ClientProtocolException, IOException, URISyntaxException {
		HttpGet httpGet = new HttpGet(url);

		// 请求头部信息
		List<Header> headerList = this.getHeaders();
		if (headerList != null && headerList.size() > 0) {
			httpGet.setHeaders(headerList.toArray(new Header[] {}));
		}
		// 请求参数
		List<NameValuePair> parameters = this.getParameters();
		if (parameters != null && parameters.size() > 0) {
			String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(parameters));
			httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));
		}
		requestBase = httpGet;
		HttpClient httpClient = HttpManager.getHttpClient();

		return httpClient.execute(httpGet);
	}

	private HttpResponse doPost(String url) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		// 请求头部信息
		List<Header> headerList = this.getHeaders();
		if (headerList != null && headerList.size() > 0) {
			httpPost.setHeaders(headerList.toArray(new Header[] {}));
		}
		if (this.postType == PostType.POST_XML) {
			String xml = this.getXml();
			if (xml != null) {
				StringEntity entity = new StringEntity(xml, "UTF-8");
				httpPost.setEntity(entity);
			}
		} else {
			// 请求参数
			List<NameValuePair> parameters = this.getParameters();
			if (parameters != null && parameters.size() > 0) {
				HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
				httpPost.setEntity(entity);
			}
		}
		requestBase = httpPost;
		HttpClient httpClient = HttpManager.getHttpClient();
		return httpClient.execute(httpPost);
	}

	public List<Header> getHeaders() {
		List<Header> headerList = new ArrayList<Header>();
		headerList.add(new BasicHeader("User-Agent", USER_AGENT));
		headerList.add(new BasicHeader("Connection", "close")); // 短链接
		headerList.add(new BasicHeader("Accept-Encoding", "identity")); // 短链接
		return headerList;
	}

	/**
	 * 请求参数
	 *
	 * @return
	 */
	public List<NameValuePair> getParameters() {
		return null;
	}

	public String getXml() {
		return null;
	}
}
