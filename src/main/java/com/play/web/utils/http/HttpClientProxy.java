package com.play.web.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.web.exception.HttpResponseException;
import com.play.web.exception.NetworkException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class HttpClientProxy {

    public String _Content;

    public String _Sunpay;

    List<NameValuePair> parametersCash = this.getParameters();
    List<Header> headerList = new ArrayList<Header>();


    public String get_Sunpay() {
        return _Sunpay;
    }

    public void set_Sunpay(String _Sunpay) {
        this._Sunpay = _Sunpay;
    }

    /**
     * 浏览器
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";

    private Logger logger = LoggerFactory.getLogger(HttpClientProxy.class);

    public String doGet(String url) {
        return doRequest(url, HttpType.GET);
    }

    public String doPost(String url) {
        return doRequest(url, HttpType.POST);
    }

    public String doRequest(String url, HttpType type) {
        Response res = curl(url, type);
        int statusCode = res.getCode();
        String content = res.getContent();
        if (statusCode == 200) {
            return content;
        }
        throw new HttpResponseException(statusCode, content);
    }

    public Response curl(String url, HttpType type) {
        HttpEntity httpEntity = null;
        HttpRequestBase requestBase = null;
        try {
            requestBase = getHttpReq(url, type);
            HttpResponse httpResponse = getClient().execute(requestBase);
            Response res = new Response();
            res.setCode(httpResponse.getStatusLine().getStatusCode());
            httpEntity = httpResponse.getEntity();
            res.setContent(EntityUtils.toString(httpEntity, "UTF-8"));
            res.setHeaders(httpResponse.getAllHeaders());
            return res;
        } catch (URISyntaxException e) {
            throw new HttpResponseException(404, "URL[" + url + "]异常");
        } catch (HttpHostConnectException | ConnectTimeoutException | UnknownHostException e) {
            throw new HttpResponseException(404, e.getClass().getName() + ":" + e.getMessage());
        } catch (IOException e) {
            logger.error("链接异常 url=" + url, e);
            throw new NetworkException(e);
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
    }

    private HttpRequestBase getHttpReq(String url, HttpType type) throws IOException, URISyntaxException {
        HttpRequestBase r = null;
        switch (type) {
            case GET:
                r = getHttpGet(url);
                break;
            case GET_SUN:
                r = getHttpSunGet(url);
                break;
            case GET_CASH:
                r = getHttpGetCash(url);
                break;
            case PUT:
                r = getHttpPut(url);
                break;
            case DELETE:
                r = getHttpDelete(url);
                break;
            case POST:
                r = getHttpPost(url);
                break;
            case POST_XFROM:
                r = getHttpPostXform(url);
                break;
            case POST_JSON:
                r = getHttpPostJSON(url);
                break;
            case POST_WOW:
                r = getHttpPostForWowPay(url);
                break;
            default:
                return r;
        }
        // 请求头部信息
        List<Header> headerList = this.getHeaders();
        if (headerList != null && headerList.size() > 0) {
            r.setHeaders(headerList.toArray(new Header[]{}));
        }
        return r;
    }

    private HttpRequestBase getHttpGet(String url) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(url);
        List<NameValuePair> parameters = this.getParameters();
        if (parameters != null && parameters.size() > 0) {
            String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(parameters, "UTF-8"));
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));
        }
       // logger.error("url=" + httpGet.getURI());
        return httpGet;
    }
    private HttpRequestBase getHttpSunGet(String url) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(url);
        List<NameValuePair> parameters = this.getParameters();
        if (parameters != null && parameters.size() > 0) {
            String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(parameters, "UTF-8"));
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));
        }
      //  logger.error("url=" + httpGet.getURI());
        return httpGet;
    }



    private HttpRequestBase getHttpGetCash(String url) throws IOException, URISyntaxException {
        HttpGet httpGet = new HttpGet(url);
        List<NameValuePair> parameters = this.getParametersCash();
        if (parameters != null && parameters.size() > 0) {
            String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(parameters, "UTF-8"));
            httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramsStr));
        }
      //  logger.error("url=" + httpGet.getURI());
        return httpGet;
    }



    private HttpRequestBase getHttpPut(String url) throws ClientProtocolException, IOException {
        HttpPut httpPut = new HttpPut(url);
        String content = this.getPutContent();
      //  logger.error("url =" + url + " put content=" + content);
        httpPut.setEntity(new StringEntity(content, "UTF-8"));
        return httpPut;
    }

    private HttpRequestBase getHttpDelete(String url) throws ClientProtocolException, IOException, URISyntaxException {
        HttpDelete httpDelete = new HttpDelete(url);
        // 请求参数
        List<NameValuePair> parameters = this.getParameters();
        if (parameters != null && parameters.size() > 0) {
            String str = EntityUtils.toString(new UrlEncodedFormEntity(parameters, "UTF-8"));
            httpDelete.setURI(new URI(httpDelete.getURI().toString() + "?" + str));
        }
        return httpDelete;
    }

    private HttpRequestBase getHttpPost(String url) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        // 请求参数
        List<NameValuePair> parameters = this.getParameters();
        if (parameters != null && parameters.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        }
       // logger.error("url=" + httpPost.getURI() + " parameters=" + JSON.toJSONString(parameters));
        return httpPost;
    }


    private HttpRequestBase getHttpPostForWowPay(String url) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> parameters = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(this._Content);
        Map<String,String> map = jsonObject.toJavaObject(Map.class);
        map.forEach((x,y)->{
            NameValuePair nameValuePair= new BasicNameValuePair(x,y);
            parameters.add(nameValuePair);
        });
        // 请求参数
        if (parameters != null && parameters.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        }
       // logger.error("url=" + httpPost.getURI() + " parameters=" + JSON.toJSONString(parameters));
        return httpPost;
    }

    private HttpRequestBase getHttpPostXform(String url) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        // 请求参数
        List<NameValuePair> parameters = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(this._Content);
        Map<String,String> map = jsonObject.toJavaObject(Map.class);
        map.forEach((x,y)->{
            NameValuePair nameValuePair= new BasicNameValuePair(x,y);
            parameters.add(nameValuePair);
        });
        if (parameters != null && parameters.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
        }
     //   logger.error("url=" + httpPost.getURI() + " parameters=" + JSON.toJSONString
        //   (parameters));
        return httpPost;
    }

    private HttpRequestBase getHttpPostJSON(String url) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        String json = this.getPutContent();
        if (json != null) {
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }
       // logger.error("url=" + httpPost.getURI() + " json=" + json);
        return httpPost;
    }
    private HttpRequestBase getHttpPostJSONXform(String url) throws ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(url);
        String json = this.getPutContent();
        if (json != null) {
            StringEntity entity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(entity);
        }
      //  logger.error("url=" + httpPost.getURI() + " json=" + json);
        return httpPost;
    }

    private List<Header> getHeaders() {
        headerList.add(new BasicHeader("User-Agent", USER_AGENT));
        headerList.add(new BasicHeader("Connection", "close")); // 短链接
        headerList.add(new BasicHeader("Accept-Encoding", "identity")); // 短链接
        headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
        if(_Content!=null && !_Content.equals("")){
            headerList.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        }
        addHeader(headerList);
        return headerList;
    }

    public void addHeader(List<Header> headerList) {
        headerList.addAll(headerList);
    }

    /**
     * 请求参数
     *
     * @return
     */
    public List<NameValuePair> getParameters() {
        return null;
    }


    public List<Header> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<Header> headerList) {
        this.headerList = headerList;
    }

    public String getPutContent() {
        if(_Sunpay!=null && !_Sunpay.equals(""))
            return _Sunpay;
        return null;
    }

    public HttpClient getClient() {
        return HttpConnectionManager.getHttpClient();
    }

    public String get_Content() {
        return _Content;
    }

    public void set_Content(String _Content) {
        this._Content = _Content;
    }

    public List<NameValuePair> getParametersCash() {
        return parametersCash;
    }

    public void setParametersCash(List<NameValuePair> parametersCash) {
        this.parametersCash = parametersCash;
    }
}
