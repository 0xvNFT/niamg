package com.play.web.controller;

import com.play.orm.jdbc.page.Page;
import com.play.web.utils.ControllerRender;

public class BaseController {
	protected void renderPage(Page<?> page) {
		ControllerRender.renderJSON(page);
	}

	protected void renderJSON(String json) {
		ControllerRender.renderJSON(json);
	}

	protected void renderJSON(Page<?> page) {
		ControllerRender.renderJSON(page);
	}

	protected void renderJSON(Object json) {
		ControllerRender.renderJSON(json);
	}

	protected void renderSuccess() {
		ControllerRender.renderSuccess();
	}

	protected void renderSuccess(String msg) {
		ControllerRender.renderSuccess(msg);
	}

	protected void renderError(String msg) {
		ControllerRender.renderError(msg);
	}

	protected void renderText(String text) {
		ControllerRender.renderText(text);
	}

	protected void renderHtml(String text) {
		ControllerRender.renderHtml(text);
	}

}
