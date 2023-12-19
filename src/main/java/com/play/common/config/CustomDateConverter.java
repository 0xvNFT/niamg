package com.play.common.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;

public class CustomDateConverter implements Converter<String, Date> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Date convert(String s) {
		SimpleDateFormat dateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (s.length() == 10) {
				return dateFormat.parse(s);
			} else {
				return dateFormatTime.parse(s);
			}

		} catch (ParseException e) {
			logger.error("convertError", e);
			LogUtils.log("convertError", LogTypeEnum.DEFAULT_TYPE);
		}

		return null;
	}

}