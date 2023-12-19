package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.AgentMenu;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 站点下代理商后台菜单 
 *
 * @author admin
 *
 */
@Repository
public class AgentMenuDao extends JdbcRepository<AgentMenu> {

}
