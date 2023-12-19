package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AgentMenuDao;
import com.play.service.AgentMenuService;

/**
 * 站点下代理商后台菜单 
 *
 * @author admin
 *
 */
@Service
public class AgentMenuServiceImpl implements AgentMenuService {

	@Autowired
	private AgentMenuDao agentMenuDao;
}
