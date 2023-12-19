package com.play.web.exception;

import com.play.web.i18n.I18nCode;

public class PageNotFoundException extends BaseException {
	public PageNotFoundException() {
		super();
	}

	public PageNotFoundException(I18nCode code) {
		super(code);
	}

	public PageNotFoundException(I18nCode code, Throwable cause) {
		super(code, cause);
	}
}
