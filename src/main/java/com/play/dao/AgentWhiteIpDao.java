package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.AgentWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点前台IP白名单列表
 *
 * @author admin
 *
 */
@Repository
public class AgentWhiteIpDao extends JdbcRepository<AgentWhiteIp> {

	public List<WhiteIpVo> getIps(Long agentId, Long stationId) {
		String key = new StringBuilder("p:").append(agentId).append(":s:").append(stationId).toString();
		String str = CacheUtil.getCache(CacheKey.AGENT_IPS, key);
		if (str != null) {
			return JSON.parseArray(str, WhiteIpVo.class);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("agentId", agentId);
		List<WhiteIpVo> ips = find("select ip from agent_white_ip where station_id=:stationId and agent_id=:agentId",
				new BeanPropertyRowMapper<WhiteIpVo>(WhiteIpVo.class), map);
		if (ips != null) {
			CacheUtil.addCache(CacheKey.AGENT_IPS, key, JSON.toJSON(ips));
		} else {
			CacheUtil.addCache(CacheKey.AGENT_IPS, key, "[]");
		}
		return ips;
	}

	public Page<AgentWhiteIp> page(Long agentId, Long stationId, String ip, Integer type) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from agent_white_ip where 1=1");
		if (stationId != null) {
			sql.append(" and station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (agentId != null) {
			sql.append(" and agent_id=:agentId");
			map.put("agentId", agentId);
		}
		if (StringUtils.isNotEmpty(ip)) {
			sql.append(" and ip=:ip");
			map.put("ip", ip);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public boolean exist(String ip, Long agentId) {
		Map<String, Object> map = new HashMap<>();
		map.put("ip", ip);
		map.put("agentId", agentId);
		return count("select count(*) from agent_white_ip where ip=:ip and agent_id=:agentId", map) > 0;
	}

}
