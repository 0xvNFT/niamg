package com.play.web.exception;

import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;

public class ParamException extends BaseException {

	public ParamException(I18nCode code) {
		super(code);
	}

	public ParamException() {
		super(BaseI18nCode.parameterEmpty);
	}

	public ParamException(I18nCode code, String... param) {
		super(code, param);
	}
}