package com.play.web.exception;

import com.play.web.i18n.I18nCode;

public class UnStationException extends BaseException {
	public UnStationException(String msg) {
		super(msg);
	}
	public UnStationException(I18nCode code) {
		super(code);
	}
}
