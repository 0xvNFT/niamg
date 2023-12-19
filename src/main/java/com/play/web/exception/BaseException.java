package com.play.web.exception;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.I18nCode;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private I18nCode code;
	private Object[] args;

	public BaseException() {
		super();
	}

	public BaseException(Throwable cause) {
		super(cause);
	}
	public BaseException(String msg) {
		super(msg);
	}

	public BaseException(I18nCode code) {
		super(code.getMessage());
		this.code = code;
	}

	public BaseException(I18nCode code, Object[] args) {
		super(MessageFormat.format(code.getMessage(), args));
		this.code = code;
		this.args = args;
	}

	public BaseException(I18nCode code, Throwable cause) {
		super(code.getMessage(), cause);
		this.code = code;
	}

	public BaseException(I18nCode code, Object[] args, Throwable cause) {
		super(MessageFormat.format(code.getMessage(), args), cause);
		this.code = code;
		this.args = args;
	}

	public BaseException(I18nCode code, String message) {
		super(message);
		this.code = code;
	}

	public BaseException(I18nCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public BaseException(I18nCode code, Object[] args, String message) {
		super(message);
		this.code = code;
		this.args = args;
	}

	public BaseException(I18nCode code, Object[] args, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public I18nCode getCode() {
		return code;
	}

	public void setCode(I18nCode code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		if (this.code == null) {
			return super.getMessage();
		} else {
			try {
				return I18nTool.getMessage(code.getCode(), args);
			} catch (Exception e) {
				return MessageFormat.format("错误代码: {0}, 错误参数: {1}, 国际化消息读取失败!",
						new Object[] { code.getCode(), StringUtils.join(args, "|") });
			}
		}
	}

}
