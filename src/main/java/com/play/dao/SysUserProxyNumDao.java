package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.SysUserProxyNum;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 代理下级人数记录表
 *
 * @author admin
 *
 */
@Repository
public class SysUserProxyNumDao extends JdbcRepository<SysUserProxyNum> {

	public void updateProxyNum(boolean isProxy, Integer num, Long proxyId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" update sys_user_proxy_num set");
		if (isProxy) {
			sb.append(" proxy_num =(proxy_num+:num)");
		} else {
			sb.append(" member_num =(member_num+:num)");
		}
		sb.append("where user_id =:proxyId");
		if (isProxy) {
			sb.append(" and (proxy_num+:num) >=0");
		} else {
			sb.append(" and (member_num+:num) >=0");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("proxyId", proxyId);
		params.put("num", num);
		update(sb.toString(), params);
	}
}
