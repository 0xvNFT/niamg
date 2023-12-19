package com.play.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.play.web.annotation.NeedLogView;


import java.util.Arrays;

import java.util.Map;

public class LogViewInterceptor extends HandlerInterceptorAdapter {

	public static final Logger LOGGER = LoggerFactory.getLogger(LogViewInterceptor.class);
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView view)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			// 处理Permission Annotation，实现方法级权限控制
			NeedLogView view = ((HandlerMethod) handler).getMethodAnnotation(NeedLogView.class);
//			if (view != null) {
//				LogUtils.log(view.value(), view.type(), true);
//			}
//			StringBuilder sb = new StringBuilder(1000);
//			sb.append("-----------------------开始计时:").append(new SimpleDateFormat("hh:mm:ss.SSS").format(new Date())).append("-------------------------------------\n");
//			HandlerMethod h = (HandlerMethod) handler;
//			sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
//			sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
//			sb.append("Params    : ").append(getParamString(request.getParameterMap())).append("\n");
//			sb.append("URI       : ").append(request.getRequestURI()).append("\n");
//			LogUtils.addLog(sb.toString());
//			LOGGER.info(sb.toString());
		}
		return true;
	}

	private String getParamString(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,String[]> e:map.entrySet()){
			sb.append(e.getKey()).append("=");
			String[] value = e.getValue();
			if(value != null && value.length == 1){
				sb.append(value[0]).append("\t");
			}else{
				sb.append(Arrays.toString(value)).append("\t");
			}
		}
		return sb.toString();
	}

}
