package com.play.service;

import java.util.Date;
import java.util.List;

import com.play.model.ProxyDailyBetStat;
import com.play.orm.jdbc.page.Page;

/**
 * 
 *
 * @author admin
 *
 */
public interface ProxyDailyBetStatService {

	void saveOrUpdate(ProxyDailyBetStat pdb);

	Page<ProxyDailyBetStat> page(String username, Date start, Date end, Integer status, String operator,
			Long stationId);

	ProxyDailyBetStat findOne(Long id, Long stationId);

	void doRollback(ProxyDailyBetStat stat);

	void cancel(Long id, Long stationId);

	List<ProxyDailyBetStat> findNeedRollback(Date end, List<Long> stationIdList);

	void doRollback4Job(ProxyDailyBetStat s);

}