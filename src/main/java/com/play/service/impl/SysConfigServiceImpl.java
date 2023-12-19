package com.play.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.SysConfigEnum;
import com.play.dao.SysConfigDao;
import com.play.model.SysConfig;
import com.play.service.SysConfigService;


/**
 * 系统配置信息
 *
 * @author admin
 *
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

	@Autowired
	private SysConfigDao sysConfigDao;

	@Override
	public SysConfig findOneById(Long id) {
		return sysConfigDao.findOneById(id);
	}

	@Override
	public SysConfig findOne(SysConfigEnum configEnum) {
		return sysConfigDao.findOne(configEnum.name());
	}

	@Override
	public String findValue(SysConfigEnum configEnum) {
		String result = null;
		SysConfig c = sysConfigDao.findOne(configEnum.name());
		if (c != null) {
			result = c.getValue();
		} else {
			result = configEnum.getInitValue();
		}
		return result == null ? result : result.trim();
	}

	@Override
	public Map<String, String> getAllMap() {
		List<SysConfig> list = sysConfigDao.getAll();
		Map<String, String> map = new HashMap<>();
		if (list != null && !list.isEmpty()) {
			for (SysConfig c : list) {
				map.put(c.getKey(), c.getValue());
			}
		}
		return map;
	}

	@Override
	public void save(String key, String value) {
		SysConfigEnum e = SysConfigEnum.valueOf(key);
		if (e == null) {
//			throw new ParamException("此配置项不存在");
		}
		SysConfig c = sysConfigDao.findOne(key);
		if (c != null) {
			String oldValue = c.getValue();
			c.setValue(value);
			sysConfigDao.update(c);
			LogUtils.modifyLog("修改配置项:" + e.getCname() + " 原始值：" + oldValue + " 修改成：" + value);
		} else {
			c = new SysConfig();
			c.setKey(key);
			c.setValue(value);
			sysConfigDao.save(c);
			LogUtils.addLog("添加配置项:" + e.getCname() + " 值：" + value);
		}
	}
}
