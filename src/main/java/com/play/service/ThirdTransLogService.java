package com.play.service;

import com.play.model.ThirdTransLog;
import com.play.orm.jdbc.page.Page;

import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author admin
 *
 */
public interface ThirdTransLogService {

	ThirdTransLog findOne(Long id, Long stationId);

	Page<ThirdTransLog> page(String username, Long userId, Integer platform, Long stationId, Integer status,
			Integer type, Date start, Date end);

	void updateOrderStatus(Long id, Integer status, Long stationId);

	List<ThirdTransLog> findNeedCheck(Date startTime);

	void checkStatus(ThirdTransLog log);

}