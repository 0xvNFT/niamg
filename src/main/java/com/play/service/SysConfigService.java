package com.play.service;

import java.util.Map;

import com.play.core.SysConfigEnum;
import com.play.model.SysConfig;

/**
 * 系统配置信息
 *
 * @author admin
 *
 */
public interface SysConfigService {

	SysConfig findOneById(Long id);

	SysConfig findOne(SysConfigEnum sysConfigEnum);

	String findValue(SysConfigEnum sysConfigEnum);

	void save(String key, String value);

	Map<String, String> getAllMap();
}