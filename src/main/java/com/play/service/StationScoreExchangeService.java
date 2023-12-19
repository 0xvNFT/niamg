package com.play.service;

import java.math.BigDecimal;
import java.util.List;

import com.play.model.StationScoreExchange;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

/**
 *
 *
 * @author admin
 *
 */
public interface StationScoreExchangeService {

	Page<StationScoreExchange> getPage(Integer type, String name, Long stationId);

	void save(StationScoreExchange se);

	StationScoreExchange findOne(Long id, Long stationId);

	void modify(StationScoreExchange se);

	void delete(Long id, Long stationId);

	void updStatus(Long id, Long stationId, Integer status);

	List<StationScoreExchange> findByStationId(Long stationId);

	void confirmExchange(SysUser sysUser, BigDecimal amount, Long configId);

	StationScoreExchange findOneConfig(Integer type, Long stationId, Integer status);

}
