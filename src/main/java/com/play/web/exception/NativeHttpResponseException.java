package com.play.web.exception;

import com.play.web.utils.app.NativeJsonUtil;

import java.util.HashMap;
import java.util.Map;

public class NativeHttpResponseException extends Exception {

	private int code;

	private String content;

	public String toJsonMsg() {
		Map<String, Object> result = new HashMap<>();
		result.put("msg", "status[" + this.code + "],content:" + content);
		result.put("success", false);
		return NativeJsonUtil.toJson(result);
	}

	public NativeHttpResponseException(int code) {
		this.code = code;
	}

	public NativeHttpResponseException(int code, String content) {
		this.code = code;
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
