package com.play.web.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.orm.jdbc.page.Page;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

public class ControllerRender {
	private static final String HEADER_CEIPSTATE_FREFIX = "ceipstate";
	private static final String CEIPSTATE_DEFAULT = "1";

	/**
	 * response响应头部 后台执行出现异常
	 */
	public static final String HEADER_CEIPSTATE_ERROR = "2";
	/**
	 * response响应头部 未登录异常
	 */
	public static final String HEADER_CEIPSTATE_NO_LOGIN = "4";

	/**
	 * response响应头部 没有权限
	 */
	public static final String HEADER_CEIPSTATE_NO_PERMISSION = "5";

	public static void renderError() {
		renderError(I18nTool.getMessage(BaseI18nCode.operateError));
	}

	public static void renderError(String errorMsg) {
		render(errorMsg, HEADER_CEIPSTATE_ERROR);
	}

	public static void unLogin() {
		renderUnLogin(I18nTool.getMessage(BaseI18nCode.notLogin));
	}

	public static void renderUnLogin(String errorMsg) {
		JSONObject json = new JSONObject();
		json.put("msg", errorMsg);
		json.put("success", false);
		json.put("login", false);
		render("application/json", json.toJSONString(), HEADER_CEIPSTATE_NO_LOGIN);
		// render(errorMsg, HEADER_CEIPSTATE_NO_LOGIN);
	}

	public static void renderNotAuthorized() {
		renderNotAuthorized(I18nTool.getMessage(BaseI18nCode.unauthorized, ""));
	}

	public static void renderNotAuthorized(String errorMsg) {
		render(errorMsg, HEADER_CEIPSTATE_NO_PERMISSION);
	}

	public static void renderSuccess() {
		renderSuccess(I18nTool.getMessage(BaseI18nCode.operateSuccess));
	}

	public static void renderSuccess(String successMsg) {
		render(successMsg, CEIPSTATE_DEFAULT);
	}

	public static void render(String message, String ceipstate1) {
		JSONObject json = new JSONObject();
		json.put("msg", message);
		json.put("success", ceipstate1 == CEIPSTATE_DEFAULT);
		render("application/json", json.toJSONString(), ceipstate1);
	}

	public static void render(String contentType, String content, String ceipstate1) {
		PrintWriter writer = null;
		try {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getResponse();
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader(HEADER_CEIPSTATE_FREFIX, ceipstate1);
			response.setContentType(contentType + "; charset=UTF-8");
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			throw new BaseException(e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 直接输出文本.
	 * 
	 * @see #render(String, String, String...)
	 */
	static public void renderText(String text) {
		render("text/plain", text, CEIPSTATE_DEFAULT);
	}

	/**
	 * 直接输出HTML.
	 * 
	 * @see #render(String, String, String...)
	 */
	static public void renderHtml(String html) {
		render("text/html", html, CEIPSTATE_DEFAULT);
	}

	/**
	 * 直接输出XML.
	 * 
	 * @see #render(String, String, String...)
	 */
	static public void renderXml(String xml) {
		render("text/xml", xml, CEIPSTATE_DEFAULT);
	}

	static public void renderJSON(String string) {
		render("application/json", string, CEIPSTATE_DEFAULT);
	}

	static public void renderJSON(Object object) {
		renderJSON(JSON.toJSONString(object));
	}

	static public void renderJSON(Page<?> page) {
		JSONObject json = new JSONObject();
		json.put("rows", page.getRows());
		json.put("total", page.getTotal());
		if (page.getAggsData() != null && page.getAggsData().size() > 0) {
			json.put("aggsData", page.getAggsData());
		}
		renderJSON(json.toJSONString());
	}
}
