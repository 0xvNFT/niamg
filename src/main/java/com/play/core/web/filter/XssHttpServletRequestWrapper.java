package com.play.core.web.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * 请求包装对象 处理Xss
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static HttpServletRequest orgRequest = null;
	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(XssShieldUtil.xssEncode(name));
		if (value != null) {
			value = XssShieldUtil.xssEncode(value);
		}
		return value;
	}

	/**
	 * 覆盖getParameterValues方法，将参数名和参数值都做xss过滤
	 */
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = XssShieldUtil.xssEncode(values[i]);
		}
		return encodedValues;
	}

	/**
	 * 获取request的属性时，做xss过滤
	 */
	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (null != value && value instanceof String) {
			value = XssShieldUtil.xssEncode((String) value);
		}
		return value;
	};

	/**
	 * 获取最原始的request
	 * 
	 * @return
	 */
	public static HttpServletRequest getOrgRequest() {
		return orgRequest;
	}
	
	/**
	 * 获取最原始的request的静态方法
	 * 
	 * @return
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		HttpServletRequestWrapper reqw = (HttpServletRequestWrapper) req;
		while (!(reqw.getClass().getName().equals(XssHttpServletRequestWrapper.class.getName()))) {
			if(reqw.getRequest() instanceof HttpServletRequestWrapper){
				reqw = (HttpServletRequestWrapper) (reqw.getRequest());
			}else{
				return reqw;
			}
		}
		if( reqw instanceof XssHttpServletRequestWrapper){
			return ((XssHttpServletRequestWrapper) reqw).getOrgRequest();
		}
		return reqw;
	}
	
	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 */
//	@Override
//	public String getHeader(String name) {
//		String value = super.getHeader(xssEncode(name));
//		if (value != null) {
//			value = xssEncode(value);
//		}
//		return value;
//	}

	/**
	 * 重写并过滤getParameterMap方法
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paraMap = super.getParameterMap();
		// 对于paraMap为空的直接return
		if (null == paraMap || paraMap.isEmpty()) {
			return paraMap;
		}
		Map<String, String[]> temp = new HashMap<String, String[]>();
		Iterator<Entry<String, String[]>> iter = paraMap.entrySet().iterator();
		Entry<String, String[]> entry = null;
		String key = null;
		String[] values = null;
		while (iter.hasNext()) {
			entry = iter.next();
			values = entry.getValue();
			if (null == values) {
				continue;
			}
			key = entry.getKey();
			String[] newValues = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				newValues[i] = XssShieldUtil.xssEncode(values[i]);
			}
			temp.put(key, newValues);
		}
		return temp;
	}

}