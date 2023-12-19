package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.AgentUser;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点下代理商账号表
 *
 * @author admin
 *
 */
@Repository
public class AgentUserDao extends JdbcRepository<AgentUser> {

	public AgentUser findOneByUsername(String username, Long stationId) {
		if (StringUtils.isEmpty(username) || stationId == null) {
			return null;
		}
		username = StringUtils.lowerCase(StringUtils.trim(username));
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		Long id = CacheUtil.getCache(CacheKey.AGENT, getCacheKey(username, stationId), Long.class);
		if (id != null) {
			return findOne(id, stationId);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from agent_user");
		sql.append(" where username=:username and station_id=:stationId");
		AgentUser u = findOne(sql.toString(), map);
		addCache(u);
		return u;
	}

	@Override
	public AgentUser findOne(Long id, Long stationId) {
		AgentUser u = CacheUtil.getCache(CacheKey.AGENT, getCacheKey(id, stationId), AgentUser.class);
		if (u == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("stationId", stationId);
			u = findOne("select * from agent_user where id=:id and station_id=:stationId", map);
			addCache(u);
		}
		return u;
	}

	private String getCacheKey(Long id, Long stationId) {
		return new StringBuffer("uid:").append(id).append(":").append(stationId).toString();
	}

	private String getCacheKey(String username, Long stationId) {
		return new StringBuffer("u:username:").append(username).append(":").append(stationId).toString();
	}

	private void addCache(AgentUser u) {
		if (u != null) {
			CacheUtil.addCache(CacheKey.AGENT, getCacheKey(u.getId(), u.getStationId()), u);
			CacheUtil.addCache(CacheKey.AGENT, getCacheKey(u.getUsername(), u.getStationId()), u.getId());
		}
	}

	@Override
	public AgentUser save(AgentUser t) {
		t = super.save(t);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(t.getId(), t.getStationId()));
		return t;
	}

	public Page<AgentUser> page(Long stationId, Long agentId, String username) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from agent_user where station_id=:stationId");
		if (agentId != null) {
			sql.append(" and agent_id=:agentId");
			map.put("agentId", agentId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and username=:username");
			map.put("username", username);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public boolean exist(String username, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("stationId", stationId);
		return queryForInt("select count(*) from agent_user where username=:username and station_id=:stationId",
				map) > 0;
	}

	public void changeStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		map.put("stationId", stationId);
		update("update agent_user set status=:status where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(id, stationId));
	}

	public void delete(Long id, Long stationId) {
		deleteById(id);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(id, stationId));
	}

	public void modifyRealName(Long id, String realName, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("realName", realName);
		map.put("stationId", stationId);
		update("update agent_user set real_name=:realName where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(id, stationId));
	}

	public void modifyRemark(Long id, String remark, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("remark", remark);
		map.put("stationId", stationId);
		update("update agent_user set remark=:remark where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(id, stationId));
	}

	public void updatePwd(Long id, String salt, String password, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("salt", salt);
		map.put("password", password);
		map.put("stationId", stationId);
		update("update agent_user set salt=:salt,password=:password where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.AGENT, getCacheKey(id, stationId));
	}
}
