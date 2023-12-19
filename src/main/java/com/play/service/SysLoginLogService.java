package com.play.service;

import com.play.model.SysLoginLog;
import com.play.orm.jdbc.page.Page;

public interface SysLoginLogService {

	void save(SysLoginLog log);

	Page<SysLoginLog> page(Long partnerId, Long stationId, String account, String loginIp, String begin, String end,
			Integer userType, Integer status);

}
