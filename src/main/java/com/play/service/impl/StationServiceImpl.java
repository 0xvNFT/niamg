package com.play.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.play.core.CurrencyEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.LanguageEnum;
import com.play.dao.StationDao;
import com.play.model.Station;
import com.play.model.vo.StationComboVo;
import com.play.model.vo.StationOnlineNumVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private StationDao stationDao;

	@Override
	public List<StationComboVo> getCombo(Long partnerId) {
		return stationDao.getCombo(partnerId);
	}

	@Override
	public List<Station> getAll() {
		return stationDao.findAll();
	}

	@Override
	public List<Long> getActiveIds() {
		List<Station> list = stationDao.findAll();
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<Long> alist = new ArrayList<>();
		for (Station s : list) {
			if (s.getStatus() == Constants.STATUS_ENABLE) {
				alist.add(s.getId());
			}
		}
		return alist;
	}

	@Override
	public Page<Station> getPage(Long partnerId, String code, String name) {
		return stationDao.getPage(partnerId, code, name);
	}

	@Override
	public void changeStatus(Long id, Long partnerId, Integer status) {
		Station station = stationDao.findOneById(id, partnerId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, station.getStatus())) {
			stationDao.changeStatus(id, partnerId, status);
			LogUtils.modifyLog("修改站点" + station.getName() + "[" + station.getCode() + "]" + " 状态为：" + str);
		}
	}

	@Override
	public void changeBgStatus(Long id, Long partnerId, Integer status) {
		Station station = stationDao.findOneById(id, partnerId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, station.getBgStatus())) {
			stationDao.changeBgStatus(id, partnerId, status);
			LogUtils.modifyStatusLog("修改站点" + station.getName() + "[" + station.getCode() + "]" + " 后台状态为：" + str);
		}
	}

	@Override
	public Station findOneByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		return stationDao.findOneByCode(code);
	}

	@Override
	public Station findOneById(Long id, Long partnerId) {
		return stationDao.findOneById(id, partnerId);
	}

	@Override
	public Station findOneById(Long id) {
		return stationDao.findOneById(id);
	}

	@Override
	public void modify(Long id, Long partnerId, String name, String language, String currency) {
		name = StringUtils.trim(name);
		if (StringUtils.isEmpty(name)) {
			throw new ParamException(BaseI18nCode.stationNameNotEmpty);
		}
		LanguageEnum le = null;
		try {
			le = LanguageEnum.valueOf(language);
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.stationSelectLanguage);
		}
		if (le == null) {
			throw new ParamException(BaseI18nCode.stationSelectLanguage);
		}
		CurrencyEnum cur = null;
		try {
			cur = CurrencyEnum.valueOf(currency);
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.stationSelectCurrency);
		}
		if (cur == null) {
			throw new ParamException(BaseI18nCode.stationSelectCurrency);
		}
		Station station = stationDao.findOneById(id, partnerId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		stationDao.modify(id, name, language, currency);
		LogUtils.modifyLog("修改站点" + station.getName() + "[" + station.getCode() + "]" + " 为：" + name);
	}

	@Override
	public void modifyCode(Long id, String code) {
		code = StringUtils.trim(code);
		if (StringUtils.isEmpty(code)) {
			throw new BaseException("站点编号不能为空");
		}
		Station station = stationDao.findOneById(id);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		stationDao.modifyCode(id, code);
		LogUtils.modifyLog("修改站点" + station.getName() + "[" + station.getCode() + "]" + "的编号为：" + code);
	}

	@Override
	public List<StationOnlineNumVo> getManagerOnlineNum() {
		return stationDao.getManagerOnlineNum();
	}

	@Override
	public Station findByDomain(String domain) {
		if (StringUtils.isEmpty(domain)) {
			return null;
		}
		if (domain.startsWith("www.")) {
			domain = domain.substring(4);
		}
		return stationDao.findByDomain(domain);
	}
}
