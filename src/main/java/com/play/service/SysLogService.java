package com.play.service;

import com.play.model.SysLog;
import com.play.orm.jdbc.page.Page;

import java.util.Date;

public interface SysLogService {

	void addLog(SysLog log);

	Page<SysLog> page(Long partnerId, Long stationId, String username, String ip, Integer type, Integer userType,
			String begin, String end, String content);

	/**
	 * 日志改版，需要传入时间查询
	 *
	 * @param id
	 * @param createTime
	 * @return
	 */
	SysLog findOne(Long id, Date createTime);

}
