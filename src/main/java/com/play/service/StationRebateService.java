package com.play.service;

import com.play.model.StationRebate;
import com.play.orm.jdbc.page.Page;

/**
 * 站点返点设置信息 
 *
 * @author admin
 *
 */
public interface StationRebateService {

	Page<StationRebate> getPage(Long stationId, Integer userType, Integer type);

	StationRebate findOneById(Long id);

	void modifyMember(StationRebate rebate);
	
	void modifyProxy(StationRebate rebate);

	void init(Long stationId);

	StationRebate findOne(Long stationId, Integer userType);


}