package com.play.service.impl;

import com.play.core.ThirdPlayerConfigEnum;
import com.play.dao.SysUserDao;
import com.play.model.SysUser;
import com.play.model.ThirdPlayerConfig;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.ThirdPlayerConfigDao;
import com.play.service.ThirdPlayerConfigService;

import java.util.List;

/**
 * 用户第三方配置表 
 *
 * @author admin
 *
 */
@Service
public class ThirdPlayerConfigServiceImpl implements ThirdPlayerConfigService {

	@Autowired
	private ThirdPlayerConfigDao thirdPlayerConfigDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Override
	public Page<ThirdPlayerConfig> getPage(String username) {
		Page<ThirdPlayerConfig> page = thirdPlayerConfigDao.getPage(SystemUtil.getStationId(), username);
		page.getRows().forEach(x -> x.setConfigName(ThirdPlayerConfigEnum.valueOf(x.getConfigName()).getLabel()));
		return page;
	}

	@Override
	public void save(ThirdPlayerConfig config) {
		Long stationId = SystemUtil.getStationId();
		if (StringUtils.isEmpty(config.getConfigName())) {
			throw new ParamException(BaseI18nCode.stationConfigMust);
		}
		if (StringUtils.isEmpty(config.getUsername())) {
			throw new ParamException(BaseI18nCode.usernameError);
		}
		if (StringUtils.isEmpty(config.getConfigValue())) {
			throw new ParamException(BaseI18nCode.stationConfigUnExist);
		}
		SysUser account = sysUserDao.findOneByUsername(config.getUsername(), stationId, null);
		if (account == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}

		config.setStationId(stationId);
		config.setUserId(account.getId());
		if (config.getId() == null) {
			if (thirdPlayerConfigDao.getOneByUserAndConfigName(account.getId(), config.getConfigName(),
					stationId) != null) {
			//	throw new ParamException(BaseI18nCode.ipHadAdd);
				throw new BaseException(BaseI18nCode.userConfigExist);
			}
			thirdPlayerConfigDao.save(config);
		} else {
			ThirdPlayerConfig old = thirdPlayerConfigDao.findOneById(config.getId());
			if (old == null) {
				throw new ParamException(BaseI18nCode.registerConfigUnExist);
			}
			if (!old.getConfigName().equals(config.getConfigName())) {
				if (thirdPlayerConfigDao.getOneByUserAndConfigName(account.getId(), config.getConfigName(),
						stationId) != null) {
					throw new ParamException(BaseI18nCode.ipHadAdd);
				}
			}
			old.setUserId(config.getUserId());
			old.setStationId(config.getStationId());
			old.setConfigName(config.getConfigName());
			old.setConfigValue(config.getConfigValue());
			old.setUsername(config.getUsername());
			thirdPlayerConfigDao.update(old);
		}


	}

	@Override
	public ThirdPlayerConfig getOneById(Long id) {
		return thirdPlayerConfigDao.findOneById(id);
	}

	@Override
	public void delete(Long id) {
		thirdPlayerConfigDao.deleteById(id);
	}

	@Override
	public List<ThirdPlayerConfig> findConfig(Long userId, Long stationId) {
		return thirdPlayerConfigDao.findConfig(userId, stationId);
	}
}
