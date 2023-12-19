package com.play.web.controller.exception.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ForbiddenException;
import com.play.web.exception.NotPageException;
import com.play.web.exception.user.NotLoginException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ControllerRender;
import com.play.web.utils.ServletUtils;
import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler implements HandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (ex.getClass() == NotPageException.class) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return new ModelAndView();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (ex.getClass() == UnauthorizedException.class) {// 没权限
			if (ServletUtils.isAjaxInvoie(request)) {
				ControllerRender.renderNotAuthorized(ex.getMessage());
				return null;
			}
			map.put("unauth", "true");
			map.put("msg", ex.getMessage());
			map.put("base", request.getContextPath());
			return new ModelAndView("common/error/guestUnPerm", map);
		} else if (ex.getClass() == NotLoginException.class) {// 没登录，登录超时
			if (ServletUtils.isAjaxInvoie(request)) {
				ControllerRender.renderUnLogin(I18nTool.getMessage(ex.getMessage(), I18nTool.getLocale()));
//				ControllerRender.renderUnLogin(ex.getMessage());
				return null;
			}
			switch (SystemUtil.getStationType()) {
			case MANAGER:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_MANAGER + "/login.do");
			case PARTNER:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_PARTNER + "/login.do");
			case ADMIN:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_ADMIN + "/login.do");
			case AGENT:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_AGENT + "/login.do");
			case PROXY:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_PROXY + "/login.do");
			case FRONT_MOBILE:
				return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_MOBILE + "/index.do#/login");
			default:
				if (MobileUtil.isMoblie(request)) {
					return new ModelAndView("redirect:" + SystemConfig.CONTROL_PATH_MOBILE + "/index.do#/login");
				}
				return new ModelAndView("redirect:/login.do");
			}
		} else if (ex.getClass() == ForbiddenException.class) {// 拒绝访问
			map.put("msg", ex.getMessage());
			return new ModelAndView("common/error/forbidden", map);
		} else {
			if (ServletUtils.isAjaxInvoie(request)) {
				if (ex instanceof NullPointerException) {
					ControllerRender.renderError(I18nTool.getMessage(BaseI18nCode.systemError));
				}
				if (ex instanceof BaseException) {
					ControllerRender.renderError(ex.getMessage());
				}
				return null;
			}
			if (ex instanceof BaseException) {
				map.put("msg", ex.getMessage());
			}
		}
		map.put("base", request.getContextPath());
		return new ModelAndView("common/error/500", map);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		List<ObjectError> errorList = ex.getAllErrors();
		StringBuilder msg = new StringBuilder();
		for (ObjectError error : errorList) {
			if (error.getDefaultMessage() != null) {
				msg.append(error.getDefaultMessage());
			} else {
				if (error instanceof FieldError) {
					FieldError fieldError = (FieldError) error;
					msg.append(I18nTool.getMessage(BaseI18nCode.fieldBindError, fieldError.getField()));
				} else {
					msg.append(I18nTool.getMessage(BaseI18nCode.objectBindError, error.getObjectName()));
				}
			}
		//	LOGGER.error(error.toString(), ex);
		}

		JSONObject json = new JSONObject();
		json.put("msg", msg.toString());
		json.put("success", false);
		return new ResponseEntity<Object>(json.toString(), HttpStatus.OK);
	}
}
