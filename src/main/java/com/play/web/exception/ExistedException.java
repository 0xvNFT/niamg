
package com.play.web.exception;

import com.play.web.i18n.I18nCode;

public class ExistedException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ExistedException(I18nCode code) {
		super(code);
	}

}
