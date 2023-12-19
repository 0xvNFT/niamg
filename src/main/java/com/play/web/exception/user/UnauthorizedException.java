package com.play.web.exception.user;

import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;

public class UnauthorizedException extends BaseException {
	public UnauthorizedException() {
		super(BaseI18nCode.unauthorized, new Object[] { "" });
	}

	public UnauthorizedException(String... perm) {
		super(BaseI18nCode.unauthorized, perm);
	}

	public UnauthorizedException(I18nCode code) {
		super(code);
	}

	public UnauthorizedException(I18nCode code, String perm) {
		super(code, new Object[] { perm });
	}

}
