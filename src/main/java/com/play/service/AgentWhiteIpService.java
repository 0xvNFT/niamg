package com.play.service;

import java.util.List;

import com.play.model.AgentWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;

/**
 * 站点前台IP白名单列表
 *
 * @author admin
 *
 */
public interface AgentWhiteIpService {

	List<WhiteIpVo> getIps(Long agentId, Long stationId);

	Page<AgentWhiteIp> page(Long agentId, Long stationId, String ip, Integer type);

	void delete(Long id, Long stationId);

	String save(AgentWhiteIp ip);

}