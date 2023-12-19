package com.play.web.exception;

import com.play.web.i18n.I18nCode;

public class NetworkException extends BaseException {
	private static final long serialVersionUID = 1L;

	public NetworkException() {
		super();
	}

	public NetworkException(Throwable cause) {
		super(cause);
	}

	public NetworkException(I18nCode code) {
		super(code);
	}

	public NetworkException(I18nCode code, Throwable cause) {
		super(code, cause);
	}
}
