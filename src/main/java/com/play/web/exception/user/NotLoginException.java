package com.play.web.exception.user;

import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;

public class NotLoginException extends BaseException {
	public NotLoginException(I18nCode code) {
		super(code);
	}

	public NotLoginException() {
        throw new ParamException(BaseI18nCode.notLogin,"5810");
	}

}
