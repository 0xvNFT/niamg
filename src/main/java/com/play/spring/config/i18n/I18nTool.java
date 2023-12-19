package com.play.spring.config.i18n;

import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.play.common.Constants;
import com.play.core.LanguageEnum;
import com.play.model.Station;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.i18n.I18nCode;
import com.play.web.var.SysThreadObject;
import com.play.web.var.SysThreadVariable;
import com.play.web.var.SystemUtil;

public class I18nTool {
	private static ApplicationContext i18n;

	private static MessageSource getI18n() {
		if ((i18n == null)) {
			i18n = ContextLoader.getCurrentWebApplicationContext();
		}
		return i18n;
	}

	public static String getMessage(I18nCode code) {
		return getI18n().getMessage(code.getCode(), null, code.getCode(), getLocale());
	}

	public static String getMessage(I18nCode code,Locale locale) {
		return getI18n().getMessage(code.getCode(), null, code.getCode(), locale!=null?locale:getLocale());
	}

	public static String getMessage(I18nCode code, String... args) {
		return getI18n().getMessage(code.getCode(), args, code.getCode(), getLocale());
	}

	public static String getMessage(I18nCode code, Object[] args) {
		return getI18n().getMessage(code.getCode(), args, code.getCode(), getLocale());
	}

	public static String getMessage(String code) {
		return getI18n().getMessage(code, null, code, getLocale());
	}

	public static String getMessage(String code,Locale locale) {
		return getI18n().getMessage(code, null, code, locale);
	}

	public static String getMessage(String code, String defaultMessage) {
		return getI18n().getMessage(code, null, defaultMessage, getLocale());
	}

	public static String getMessage(String code, Object[] args) {
		return getI18n().getMessage(code, args, code, getLocale());
	}

	public static String getMessage(String code, Object[] args, String defaultMessage) {
		return getI18n().getMessage(code, args, defaultMessage, getLocale());
	}


	public static Locale getLocale() {
		SysThreadObject obj = SysThreadVariable.get();
		Locale locale = null;
		if (obj != null && obj.getLanguage() != null) {
			locale = obj.getLanguage().getLocale();
		}
		if (locale == null) {
			return Locale.ENGLISH;
		}
		return locale;
	}

	public static void changeLocale(String lang, HttpServletRequest request, HttpServletResponse response) {
		boolean isFront = SysThreadVariable.get().isFrontStation();
		String sessionKey = isFront ? Constants.SESSION_KEY_LANGUAGE : Constants.SESSION_KEY_ADMIN_LANGUAGE;
		if (StringUtils.isEmpty(lang)) {
			Station station = SystemUtil.getStation();
			if (station != null) {
				lang = station.getLanguage();
			}
		}
//		LanguageEnum le = LanguageEnum.getLanguageEnum(lang);
		LanguageEnum le = LanguageEnum.getLanguageEnum2(lang);
		RequestContextUtils.getLocaleResolver(request).setLocale(request, response, le.getLocale());
//        request.getSession().setAttribute(sessionKey, le.getLocale().getLanguage());
        request.getSession().setAttribute(sessionKey, le.name());
        SysThreadObject threadObj = SysThreadVariable.get();
		if (threadObj != null) {
			threadObj.setLanguage(le);
		}
	}

	public static Locale getStationLocale() {
		Station station = SystemUtil.getStation();
		String lang = "";
		if (station != null) {
			lang = station.getLanguage();
		}
		return LanguageEnum.getLanguageEnum2(lang).getLocale();
	}

	//从消息解析出 I18nCode
	public static BaseI18nCode getCode(String msg, Locale locale) {
		BaseI18nCode i18nCode = null;
		Optional<BaseI18nCode> optional = Arrays.stream(BaseI18nCode.values()).filter(code->{
			return I18nTool.getMessage(code,locale).equals(msg);
		}).findFirst();
		if (optional.isPresent()){
			i18nCode = optional.get();
		}
		return i18nCode;
	}

	/**
	 * 将多语言code转换为List
	 * @param codes
	 * @return
	 */
	public static List<String> convertCodeToList(String... codes) {
		if (codes == null || codes.length < 1) {
			return new ArrayList<>();
		}
		return Arrays.stream(codes).collect(Collectors.toList());
	}

	/**
	 * 在原有多语言code列表，新增需要国际化的元素
	 * @param codeList
	 * @param addElement
	 * @return
	 */
	public static List<String> convertCodeToList(List<String> codeList, String... addElement) {
		if(codeList == null) {
			codeList = new ArrayList<>();
		}
		List<String> addList = convertCodeToList(addElement);
		codeList.addAll(addList);
		return codeList;
	}

	/**
	 * 将多语言Code的数组字符串转换为List
	 * @param arrStr
	 * @return
	 */
	public static List<String> convertArrStrToList(String arrStr) {
		if (arrStr == null || "".equals(arrStr)) {
			return new ArrayList<>();
		}
		List<String> codeList = JSONArray.parseArray(arrStr, String.class);
		return codeList;
	}

	/**
	 * 将多语言code列表转换为数组字符串
	 * @param codeList
	 * @return
	 */
	public static String convertCodeToArrStr(List<String> codeList) {
		return JSONArray.toJSONString(codeList);
	}

	/**
	 * 将多语言code转换为数组，以字符串方式返回
	 * @param codes
	 * @return
	 */
	public static String convertCodeToArrStr(String... codes) {
		List<String> strList = convertCodeToList(codes);
		return JSONArray.toJSONString(strList);
	}

	/**
	 * 在原有多语言code列表，新增需要国际化的元素
	 * @param codeList
	 * @param addElement
	 * @return
	 */
	public static String convertCodeToArrStr(List<String> codeList, String... addElement) {
		return JSONArray.toJSONString(convertCodeToList(codeList, addElement));
	}

	/**
	 * 将多语言Code的数组转换消息字符串
	 * @param arrStr
	 * @return
	 */
	public static String convertArrStrToMessage(String arrStr) {
		return convertArrStrToMessage(arrStr, null);
	}

	/**
	 * 根据所需语言，将多语言Code的数组转换消息字符串
	 * @param arrStr
	 * @return
	 */
	public static String convertArrStrToMessage(String arrStr, Locale locale) {
		// 兼容未用code方式存放的老数据，若数据不是以数组的"[" 打头，即视为老数据
		if (StringUtils.isEmpty(arrStr) || !arrStr.startsWith("[") || !arrStr.endsWith("]")) {
			return arrStr;
		}

		List<String> list = JSONArray.parseArray(arrStr, String.class);
		StringBuffer stringBuffer = new StringBuffer();
		if (null != locale) {
			list.forEach(e -> {
				stringBuffer.append(getMessage(e, locale));
			});
		} else {
			list.forEach(e -> {
				stringBuffer.append(getMessage(e));
			});
		}

		return stringBuffer.toString();
	}
}
