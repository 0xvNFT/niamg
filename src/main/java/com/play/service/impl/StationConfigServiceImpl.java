package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.dto.StationFakeData;
import com.play.web.utils.StationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationConfigDao;
import com.play.dao.StationDao;
import com.play.model.Station;
import com.play.model.StationConfig;
import com.play.service.StationConfigService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForManagerUser;

/**
 * 站点配置信息
 *
 * @author admin
 *
 */
@Service
public class StationConfigServiceImpl implements StationConfigService {

	@Autowired
	private StationConfigDao stationConfigDao;
	@Autowired
	private StationDao stationDao;

	/**
	 * stationType=2 娱乐站 1彩票站
	 */
	@Override
	public void initStation(Long stationId, Long partnerId) {
		StationConfig c = null;
		for (StationConfigEnum ce : StationConfigEnum.values()) {
			c = new StationConfig();
			c.setKey(ce.name());
			c.setValue(ce.getInitValue());
			c.setStationId(stationId);
			c.setPartnerId(partnerId);
			c.setEleType(ce.getEleType());
			c.setTitle(ce.getCname());
			c.setGroupName(ce.getGroupName());
			c.setVisible(ce.getVisible());
			c.setSortNo(ce.getSortNo());
			stationConfigDao.save(c);
		}
	}

	@Override
	public Set<String> getAllSet(Long stationId) {
		List<StationConfig> list = stationConfigDao.getAll(stationId, null);
		Set<String> set = new HashSet<>();
		if (list != null && !list.isEmpty()) {
			for (StationConfig c : list) {
				set.add(c.getKey());
			}
		}
		return set;
	}

	@Override
	public List<StationConfig> getAll(Long stationId, Integer visible) {
		List<StationConfig> list = stationConfigDao.getAll(stationId, visible);
		if (list != null && !list.isEmpty()) {
			for (StationConfig c : list) {
				c.setGroupName(getI18nGroupName(c.getGroupName()));
				c.setTitle(I18nTool.getMessage("StationConfigEnum." + c.getKey(), c.getTitle()));
			}
		}
		return list;
	}

	private String getI18nGroupName(String groupName) {
		switch (groupName) {
		case "系统配置":
			return I18nTool.getMessage("StationConfigGroup.system");
		case "站点配置":
			return I18nTool.getMessage("StationConfigGroup.station");
		case "存取款配置":
			return I18nTool.getMessage("StationConfigGroup.money");
		case "手机配置":
			return I18nTool.getMessage("StationConfigGroup.phone");
		case "反水返点配置":
			return I18nTool.getMessage("StationConfigGroup.commission");
		case "APP配置":
		case "原生配置":
			return I18nTool.getMessage("StationConfigGroup.app");
		case "活动配置":
			return I18nTool.getMessage("StationConfigGroup.activity");
		case "二级密码配置":
		case "二级密码验证设置":
			return I18nTool.getMessage("StationConfigGroup.secondPwd");
		case "第三方配置":
		case "真人配置":
			return I18nTool.getMessage("StationConfigGroup.third");
		case "三方授权登录":
			return I18nTool.getMessage("StationConfigGroup.thirdAuthLogin");
		}
		return groupName;
	}

	@Override
	public void saveSettings(Long stationId, String keys) {
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		if (StringUtils.isEmpty(keys)) {
			throw new ParamException(BaseI18nCode.stationConfigMust);
		}
		StationConfigEnum ce = null;
		StationConfig c = null;
		Set<String> set = getAllSet(stationId);
		for (String k : keys.split(",")) {
			if (StringUtils.isEmpty(k)) {
				continue;
			}
			ce = StationConfigEnum.valueOf(k);
			c = stationConfigDao.findOne(stationId, k);
			if (c == null) {
				c = new StationConfig();
				c.setKey(ce.name());
				c.setValue(ce.getInitValue());
				c.setStationId(stationId);
				c.setEleType(ce.getEleType());
				c.setPartnerId(station.getPartnerId());
				c.setTitle(ce.getCname());
				c.setGroupName(ce.getGroupName());
				c.setVisible(ce.getVisible());
				c.setSortNo(ce.getSortNo());
				stationConfigDao.save(c);
			} else {
				c.setEleType(ce.getEleType());
				c.setTitle(ce.getCname());
				c.setGroupName(ce.getGroupName());
				c.setVisible(ce.getVisible());
				c.setSortNo(ce.getSortNo());
				stationConfigDao.updateForBitch(c);
			}
			set.remove(k);
		}
		stationConfigDao.deleteForBatch(stationId, set);
		LogUtils.addLog("保存站点" + station.getName() + "[" + station.getCode() + "]的配置信息");
	}

	@Override
	public void saveConfig(Long stationId, String key, String value) {
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		StationConfigEnum ce = StationConfigEnum.valueOf(key);
		if (ce == null) {
			throw new ParamException(BaseI18nCode.stationConfigUnExist);
		}
		if (StringUtils.isNotEmpty(ce.getNeedPerm()) && !PermissionForManagerUser.hadPermission(ce.getNeedPerm())) {
			throw new UnauthorizedException();
		}
		StationConfig sc = stationConfigDao.findOne(stationId, key);
		if (sc == null) {
			sc = new StationConfig();
			sc.setValue(value);
			sc.setEleType(ce.getEleType());
			sc.setGroupName(ce.getGroupName());
			sc.setSortNo(ce.getSortNo());
			sc.setTitle(ce.getCname());
			sc.setPartnerId(station.getPartnerId());
			sc.setStationId(stationId);
			sc.setKey(key);
			sc.setVisible(ce.getVisible());
			stationConfigDao.save(sc);
			LogUtils.modifyLog("保存站点配置：" + ce.getCname() + " 值：" + value);
		} else {
			String oldValue = sc.getValue();
			stationConfigDao.update(stationId, key, value);
			LogUtils.modifyLog("修改站点配置：" + ce.getCname() + " 旧值：" + oldValue + " 值：" + value);
		}
	}

	@Override
	public StationFakeData getStationFakeData(Long stationId) {
		String key = new StringBuilder("fakeData:").append(stationId).toString();
		StationFakeData cacheData = CacheUtil.getCache(CacheKey.STATION_FAKE_DATA, key, StationFakeData.class);
		if (cacheData != null) {
			return cacheData;
		}

		Double m = -10.0;
		Double n = 10.0;
		Double i = ((Double) (Math.random() * (n - m + 1)) + m)/100;

		String numberOfPeopleOnline = StationConfigUtil.get(stationId, StationConfigEnum.number_of_people_online);
		String numberOfWinners = StationConfigUtil.get(stationId, StationConfigEnum.number_of_winners);

		StationFakeData stationFakeData = new StationFakeData();
		stationFakeData.setNumberOfPeopleOnline(new BigDecimal(numberOfPeopleOnline).add((new BigDecimal(numberOfPeopleOnline)).multiply(new BigDecimal(i)))
				.setScale(0, BigDecimal.ROUND_HALF_DOWN));
		stationFakeData.setNumberOfWinners(new BigDecimal(numberOfWinners).add((new BigDecimal(numberOfWinners)).multiply(new BigDecimal(i)))
				.setScale(0, BigDecimal.ROUND_HALF_DOWN));

		CacheUtil.addCache(CacheKey.STATION_FAKE_DATA, key, stationFakeData);
		return stationFakeData;
	}

}
