package com.play.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.StationRegisterConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationRegisterConfigDao;
import com.play.model.StationRegisterConfig;
import com.play.service.StationRegisterConfigService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 站点注册页面有哪些字段
 *
 * @author admin
 *
 */
@Service
public class StationRegisterConfigServiceImpl implements StationRegisterConfigService {

	@Autowired
	private StationRegisterConfigDao stationRegisterConfigDao;

	@Override
	public void init(Long stationId) {
		StationRegisterConfig c = null;
		for (StationRegisterConfigEnum e : StationRegisterConfigEnum.values()) {
			c = new StationRegisterConfig();
			c.setEleName(e.name());
			c.setName(e.getCname());
			c.setEleType(e.getEleType());
			c.setPlatform(UserTypeEnum.PROXY.getType());
			if (e != StationRegisterConfigEnum.username && e != StationRegisterConfigEnum.pwd
					&& e != StationRegisterConfigEnum.rpwd && e != StationRegisterConfigEnum.captcha) {
				c.setShow(1);
				c.setRequired(1);
				c.setValidate(1);
			} else {
				c.setShow(2);
				c.setRequired(2);
				c.setValidate(2);
			}
			c.setUniqueness(e.getUniqueness());
			c.setSortNo(e.getSortNo());
			c.setStationId(stationId);
			c.setCode(e.getCode());
			stationRegisterConfigDao.save(c);

			c.setId(null);
			c.setPlatform(UserTypeEnum.MEMBER.getType());
			stationRegisterConfigDao.save(c);
		}
	}

	@Override
	public List<StationRegisterConfig> initAndGetList(Long stationId, Integer platform) {
		List<StationRegisterConfig> list = stationRegisterConfigDao.find(stationId, platform, null);
		if (list == null) {
			list = new ArrayList<>();
		}
		Set<String> set = new HashSet<>();
		for (StationRegisterConfig c : list) {
			c.setName(I18nTool.getMessage(c.getCode()));
			set.add(c.getEleName());
		}
		StationRegisterConfig c = null;
		for (StationRegisterConfigEnum e : StationRegisterConfigEnum.values()) {
			if (!set.contains(e.name())) {
				c = new StationRegisterConfig();
				c.setEleName(e.name());
				c.setName(e.getCname());
				c.setEleType(e.getEleType());
				c.setPlatform(platform);
				c.setShow(2);
				c.setRequired(2);
				c.setUniqueness(e.getUniqueness());
				c.setValidate(2);
				c.setSortNo(e.getSortNo());
				c.setStationId(stationId);
				c.setCode(e.getCode());
				stationRegisterConfigDao.save(c);
				list.add(c);
			}
		}
		return list;
	}

	@Override
	public void updateProp(Long id, Long stationId, String prop, Integer value) {
		if (!StringUtils.equals("show", prop) && !StringUtils.equals("validate", prop)
				&& !StringUtils.equals("required", prop) && !StringUtils.equals("uniqueness", prop)) {
			throw new ParamException(BaseI18nCode.propCanntUpdate);
		}
		StationRegisterConfig old = stationRegisterConfigDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.registerConfigUnExist);
		}
		String s = I18nTool.getMessage(BaseI18nCode.isyes);
		if (value == null || value != Constants.STATUS_ENABLE) {
			value = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.isNot);
		}
		stationRegisterConfigDao.updateProp(id, stationId, prop, value);
		LogUtils.modifyStatusLog(
				"修改" + (old.getPlatform() == 130 ? "会员" : "代理") + "的注册选项:" + old.getName() + " 为：" + s);
	}

	@Override
	public StationRegisterConfig findOne(Long id, Long stationId) {
		return stationRegisterConfigDao.findOne(id, stationId);
	}

	@Override
	public void updateSortNo(Long id, Long stationId, Long sortNo, String name, String tips) {
		stationRegisterConfigDao.updateSortNo(id, stationId, sortNo, name, tips);
	}

	@Override
	public List<StationRegisterConfig> find(Long stationId, Integer platform, Integer show) {
		return stationRegisterConfigDao.find(stationId, platform, show);
	}
}
