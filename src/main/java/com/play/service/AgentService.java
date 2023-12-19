package com.play.service;

import java.util.List;

import com.play.model.Agent;
import com.play.orm.jdbc.page.Page;

/**
 * 站点代理商信息表
 *
 * @author admin
 *
 */
public interface AgentService {

	Page<Agent> page(Long stationId);

	void save(Agent agent);

	Agent findById(Long id, Long stationId);

	void update(Agent agent);

	void delete(Long id, Long stationId);

	void changeStatus(Long id, Integer status, Long stationId);

	List<Agent> getAll(Long stationId);

	Agent findOneByName(String name, Long stationId);
	
	Agent findOneByPromoCode(String code, Long stationId);

}