package com.play.web.var;

import com.play.core.*;
import com.play.model.bo.LanguageBo;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;

import com.play.model.Station;
import com.play.model.StationDomain;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SystemUtil {

	public static UserTypeEnum getUserType() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getUserType();
		}
		return UserTypeEnum.DEFAULT_TYPE;
	}

	public static int getUserTypeVaule() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null && obj.getUserType() != null) {
			return obj.getUserType().getType();
		}
		return UserTypeEnum.DEFAULT_TYPE.getType();
	}

	/**
	 * 存储在order表中终端投注方式
	 */
	public static int getTerminalType() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj == null) {
			return TerminalType.PC.getType();
		}
		switch (obj.getStationType()) {
		case FRONT_MOBILE: // 手机web
			return TerminalType.WAP.getType();
		default:
			return TerminalType.PC.getType();
		}
	}

	public static Long getStationId() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null && obj.getStation() != null) {
			return obj.getStation().getId();
		}
		return null;
	}

	public static StationType getStationType() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getStationType();
		}
		return null;
	}

	public static Station getStation() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getStation();
		}
		return null;
	}

	public static String getLanguage() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			if (obj.getLanguage() != null) {
				return obj.getLanguage().name();
			}
			if (obj.getStation() != null) {
				String lang = obj.getStation().getLanguage();
				if (StringUtils.isNotEmpty(lang)) {
					return lang;
				}
			}
		}
		return LanguageEnum.cn.name();
	}

	public static String getLocaleCountry() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			if (obj.getLanguage() != null) {
				return obj.getLanguage().getLocale().getCountry();
			}
			if (obj.getStation() != null) {
				String lang = obj.getStation().getLanguage();
				LanguageEnum languageEnum = LanguageEnum.getLanguageEnum(lang);
				if (languageEnum != null) {
					return languageEnum.getLocale().getCountry();
				}
			}
		}
		return LanguageEnum.cn.getLocale().getCountry();
	}

	public static StationDomain getDomain() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getDomain();
		}
		return null;
	}

	public static Long getPartnerId() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null && obj.getDomain() != null) {
			return obj.getDomain().getPartnerId();
		}
		return null;
	}

	/**
	 * 获得币种
	 * 
	 * @return
	 */
	public static CurrencyEnum getCurrency() {
		SysThreadObject obj = SysThreadVariable.get();
		CurrencyEnum ce = null;
		if (obj != null) {
			if (obj.getStation() != null) {
				ce = CurrencyEnum.getCurrency(obj.getStation().getCurrency());
			}
		}
		if (ce == null) {
			throw new ParamException(BaseI18nCode.currencyUnExist);
		}
		return ce;
	}

	public static List<LanguageBo> getLanguages() {
		List<String> strings = Arrays.stream(LanguageEnum.values()).map(LanguageEnum::name).collect(Collectors.toList());
		List<LanguageBo> languages = new ArrayList<>();

		for (String str : strings) {
			LanguageBo languageBo = new LanguageBo();
			languageBo.setName(str);

			if (str.equals(BaseI18nCode.en.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.en));
			} else if (str.equals(BaseI18nCode.vi.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.vi));
			} else if (str.equals(BaseI18nCode.es.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.es));
			} else if (str.equals(BaseI18nCode.th.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.th));
			} else if (str.equals(BaseI18nCode.ms.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.ms));
			} else if (str.equals(BaseI18nCode.id.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.id));
			} else if (str.equals(BaseI18nCode.in.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.in));
			} else if (str.equals(BaseI18nCode.br.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.br));
			} else if (str.equals(BaseI18nCode.ja.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.ja));
			} else if (str.equals(BaseI18nCode.cn.name())) {
				languageBo.setDesc(I18nTool.getMessage(BaseI18nCode.cn));
			}
			// Add more languages using the same pattern
			languages.add(languageBo);
		}

		return languages;
	}

	/**
	 * 根据站点的币种，获取对应时区
	 * @return
	 */
	public static String getTimezone() {
		SysThreadObject obj = SysThreadVariable.get();
		CurrencyEnum ce = null;
		if (obj != null) {
			if (obj.getStation() != null) {
				ce = CurrencyEnum.getCurrency(obj.getStation().getCurrency());
			}
		}
		if (ce == null) {
			throw new ParamException(BaseI18nCode.currencyUnExist);
		}

		StationTimezoneEnum stationTimezone = StationTimezoneEnum.getByCurrCode(ce.name());
		if (stationTimezone == null) {
			throw new ParamException(BaseI18nCode.timezoneNotExist);
		}
		return stationTimezone.getTimezone1();
	}
}
