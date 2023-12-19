package com.play.service;

import com.play.model.AgentUser;
import com.play.orm.jdbc.page.Page;

/**
 * 站点下代理商账号表
 *
 * @author admin
 *
 */
public interface AgentUserService {

	AgentUser findOneByUsername(String agentUsername, Long stationId);

	AgentUser findOneById(Long userId, Long stationId);

	Page<AgentUser> page(Long stationId, Long agentId, String username);

	void save(AgentUser user);

	void changeStatus(Long id, Integer status, Long stationId);

	void delete(Long id, Long stationId);

	void modifyRealName(Long id, String realName, Long stationId);

	void modifyRemark(Long id, String remark, Long stationId);

	void resetPwd(Long id, Long stationId);

}