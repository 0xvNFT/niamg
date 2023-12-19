package com.play.tronscan.allclient.conf;

import com.play.core.SysConfigEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.play.service.SysConfigService;

@Component
public class TronConfig {
	/** log */
	static Logger log = LoggerFactory.getLogger(TronConfig.class);
	/** 静态句柄 */
	static TronConfig handle;
	@Autowired
	protected SysConfigService sysConfigService;

	public static String getAPIKey() {
		if (handle == null)
			return null;
		return handle.sysConfigService.findValue(SysConfigEnum.sys_tron_pro_api_key);
	}

	public TronConfig() {
		handle = this;
	}
}
