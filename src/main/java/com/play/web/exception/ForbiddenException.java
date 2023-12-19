package com.play.web.exception;

import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;

public class ForbiddenException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String ip) {
		super(BaseI18nCode.ipForbidden, new Object[] { ip });
	}

	public ForbiddenException(I18nCode code) {
		super(code);
	}

	public ForbiddenException(I18nCode code, String... args) {
		super(code, args);
	}

	public ForbiddenException(I18nCode code, Object[] args) {
		super(code, args);
	}
}
