package com.play.web.exception;

import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;

public class HttpResponseException extends BaseException {
	public HttpResponseException(I18nCode code) {
		super(code);
	}

	public HttpResponseException(int code, String msg) {
		super(BaseI18nCode.callError, new Object[] { code, msg });
	}
}
