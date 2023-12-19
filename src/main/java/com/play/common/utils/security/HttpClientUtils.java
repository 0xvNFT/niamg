package com.play.common.utils.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static int SOCKET_TIMEOUT = 20000;
    private static int CONNECT_TIMEOUT = 20000;
    private static int CONNECT_REQUEST_TIMEOUT = 20000;
    private static int MAX_TOTAL = 200;
    private static int REQUEST_RETRY = 3;
    private static RequestConfig requestConfig = null;
    private static PoolingHttpClientConnectionManager conMgr = null;
    private static DefaultHttpRequestRetryHandler retryHandler = null;
    private static LaxRedirectStrategy redirectStrategy = null;
    private static SSLConnectionSocketFactory sslsf = null;

    private static HttpClientUtils instance = null;

    private HttpClientUtils() {
    }

    public static HttpClientUtils getInstance() {
        if (instance == null) {
            synchronized (HttpClientUtils.class) {
                if (instance == null) {
                    /*
                     * setSocketTimeout setConnectTimeout
                     * setConnectionRequestTimeout
                     */
                    requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

                    conMgr = new PoolingHttpClientConnectionManager();
                    // 设置整个连接池最大连接数 根据自己的场景决定
                    conMgr.setMaxTotal(MAX_TOTAL);
                    // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
                    // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout
                    // waiting for connection from pool)，路由是对maxTotal的细分。
                    // （目前只有一个路由，因此让他等于最大值）
                    conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());

                    // 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
                    retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

                    // 设置重定向策略
                    redirectStrategy = new LaxRedirectStrategy();

                    // 自签名策略
                    try {
                        final SSLContextBuilder sSLContextBuilder = new SSLContextBuilder();
                        sSLContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                        sslsf = new SSLConnectionSocketFactory(sSLContextBuilder.build());
                    } catch (NoSuchAlgorithmException e) {
                        logger.info(e.getLocalizedMessage(), e);
                    } catch (KeyStoreException e) {
                        logger.info(e.getLocalizedMessage(), e);
                    } catch (KeyManagementException e) {
                        logger.info(e.getLocalizedMessage(), e);
                    }

                    instance = new HttpClientUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     */
    public String sendHttpPost(String httpUrl) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        return sendHttpPost(httpPost, "UTF-8");
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param params  参数(格式:key1=value1&key2=value2)
     */
    public String sendHttpPost(String httpUrl, String params) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        try {
            // 设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, "UTF-8");
    }

    public String sendHttpPostJson(String httpUrl, String jsonParams) {
        HttpPost post = new HttpPost(httpUrl);
        String result = "";
        InputStream inStream = null;
        try {
            StringEntity s = new StringEntity(jsonParams, "utf-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpClient client = new DefaultHttpClient();
            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            reader.close();
            result = strber.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 发送HTTPS POST请求
     *
     * @param url 要访问的HTTPS地址,POST访问的参数Map对象
     * @return 返回响应值
     */
    public String sendHttpsRequestByPostJson(String url, String jsonParams, Map<String, String> headers) {
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // 这个好像是HOST验证
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            public void verify(String arg0, SSLSocket arg1) throws IOException {
            }

            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
            }

            public void verify(String arg0, X509Certificate arg1) throws SSLException {
            }
        };
        try {
            // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{xtm}, null);
            // 创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            socketFactory.setHostnameVerifier(hostnameVerifier);
            // 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpPost httpPost = new HttpPost(url);

            StringEntity s = new StringEntity(jsonParams, "utf-8");
            s.setContentType("application/json");
            httpPost.setEntity(s);

            // set header
            if (null != headers) {
                Set<Entry<String, String>> headerSet = headers.entrySet();
                Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
                while (headerIterator.hasNext()) {
                    Entry<String, String> entry = headerIterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    httpPost.setHeader(name, value);
                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public String sendHttpPostJson(String httpUrl, String jsonParams, Map<String, String> headers) {
        HttpPost post = new HttpPost(httpUrl);
        String result = "";
        InputStream inStream = null;
        try {
            StringEntity s = new StringEntity(jsonParams, "utf-8");
            s.setContentType("application/json");
            // set header
            Set<Entry<String, String>> headerSet = headers.entrySet();
            Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
            while (headerIterator.hasNext()) {
                Entry<String, String> entry = headerIterator.next();
                String name = entry.getKey();
                String value = entry.getValue();
                post.setHeader(name, value);
            }
            post.setEntity(s);
            HttpClient client = new DefaultHttpClient();
            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            reader.close();
            result = strber.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }

        // set header
        Set<Entry<String, String>> headerSet = headers.entrySet();
        Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
        while (headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            httpPost.setHeader(name, value);
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, "UTF-8");

    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> headers, String reponseEncoding) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }

        // set header
        Set<Entry<String, String>> headerSet = headers.entrySet();
        Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
        while (headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            httpPost.setHeader(name, value);
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, reponseEncoding));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, reponseEncoding);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param body    参数
     */
    public String sendHttpPost(String httpUrl, String body, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost

        // set header
        Set<Entry<String, String>> headerSet = headers.entrySet();
        Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
        while (headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            httpPost.setHeader(name, value);
        }
        try {
            httpPost.setEntity(new StringEntity(body, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, "UTF-8");

    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        return sendHttpPost(httpUrl, maps, "UTF-8");
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpsPost(String httpUrl, Map<String, String> maps) {
        return sendHttpsPost(httpUrl, maps, "UTF-8");
    }

    public String sendHttpsPost(String httpUrl, Map<String, String> maps, String reponseEncoding) {

        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
//        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);//连接时间
//        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);//数据传输时间
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // 这个好像是HOST验证
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            public void verify(String arg0, SSLSocket arg1) throws IOException {
            }

            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
            }

            public void verify(String arg0, X509Certificate arg1) throws SSLException {
            }
        };
        try {
            // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{xtm}, null);
            // 创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            socketFactory.setHostnameVerifier(hostnameVerifier);
            // 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpPost httpPost = new HttpPost(httpUrl);

            // 创建参数队列
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            for (String key : maps.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, reponseEncoding));
            } catch (Exception e) {
                e.printStackTrace();
            }

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public String sendHttpPost(String httpUrl, Map<String, String> maps, String reponseEncoding) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost, reponseEncoding);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost, String responseEncoding) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, responseEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpGet(httpGet, "UTF-8");
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl, Map<String, String> headerMap, String reponseEncoding) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求

        Set<Entry<String, String>> headerSet = headerMap.entrySet();
        Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
        while (headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            httpGet.setHeader(name, value);
        }

        return sendHttpGet(httpGet, reponseEncoding);
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl, Map<String, String> headerMap) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求

        Set<Entry<String, String>> headerSet = headerMap.entrySet();
        Iterator<Entry<String, String>> headerIterator = headerSet.iterator();
        while (headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            String name = entry.getKey();
            String value = entry.getValue();
            httpGet.setHeader(name, value);
        }

        return sendHttpGet(httpGet, "UTF-8");
    }

    /**
     * 发送 get请求Https
     *
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param
     * @return
     */
    private String sendHttpGet(HttpGet httpGet, String reponseEncoding) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = HttpClients.createDefault();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, reponseEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     *
     * @param
     * @return
     */
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public CloseableHttpClient getHttpClient() {
        /*
         * setSocketTimeout setConnectTimeout setConnectionRequestTimeout
         */
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).build();

        PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
        // 设置整个连接池最大连接数 根据自己的场景决定
        conMgr.setMaxTotal(MAX_TOTAL);
        // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout
        // waiting for connection from pool)，路由是对maxTotal的细分。
        // （目前只有一个路由，因此让他等于最大值）
        conMgr.setDefaultMaxPerRoute(conMgr.getMaxTotal());

        // 另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(REQUEST_RETRY, true);

        // 设置重定向策略
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();

        // 自签名策略
        SSLConnectionSocketFactory sslsf = null;
        try {
            final SSLContextBuilder sSLContextBuilder = new SSLContextBuilder();
            sSLContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(sSLContextBuilder.build());
        } catch (NoSuchAlgorithmException e) {
            logger.info(e.getLocalizedMessage(), e);
        } catch (KeyStoreException e) {
            logger.info(e.getLocalizedMessage(), e);
        } catch (KeyManagementException e) {
            logger.info(e.getLocalizedMessage(), e);
        }

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(conMgr).setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).setSSLSocketFactory(sslsf).build();

        return httpClient;
    }

    public String sendHttpPostXml(String redirectUrl, String xml) {
        HttpPost post = new HttpPost(redirectUrl);
        StringEntity se = new StringEntity(xml, "utf-8");
        post.setEntity(se);
        HttpResponse response;
        try {
            response = getHttpClient().execute(post);
            String res = EntityUtils.toString(response.getEntity(), "utf-8");
            return res;
        } catch (ClientProtocolException e) {
            logger.info(e.getMessage(), e);
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        }
        return null;
    }

    public String sendHttpsGet(String httpUrl, String reponseEncoding) {

        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        // 这个好像是HOST验证
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

            public void verify(String arg0, SSLSocket arg1) throws IOException {
            }

            public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
            }

            public void verify(String arg0, X509Certificate arg1) throws SSLException {
            }
        };
        try {
            // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext ctx = SSLContext.getInstance("TLS");
            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            ctx.init(null, new TrustManager[]{xtm}, null);
            // 创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
            socketFactory.setHostnameVerifier(hostnameVerifier);
            // 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            HttpGet httpGet = new HttpGet(httpUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity(); // 获取响应实体
            if (entity != null) {
                responseContent = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

}
