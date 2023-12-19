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
import com.play.model.PartnerWhiteIp;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商后台ip白名单
 *
 * @author admin
 *
 */
@Repository
public class PartnerWhiteIpDao extends JdbcRepository<PartnerWhiteIp> {

	public Page<PartnerWhiteIp> page(Long partnerId, String ip, Integer type) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from partner_white_ip where 1=1");
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		if (StringUtils.isNotEmpty(ip)) {
			sql.append(" and ip=:ip");
			map.put("ip", ip);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public PartnerWhiteIp findOne(Long id, Long partnerId) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from partner_white_ip where id=:id");
		map.put("id", id);
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		return findOne(sql.toString(), map);
	}

	public List<WhiteIpVo> getIps(Long partnerId) {
		String key = new StringBuilder("p:").append(partnerId).toString();
		String str = CacheUtil.getCache(CacheKey.PARTNER_IPS, key);
		if (str != null) {
			return JSON.parseArray(str, WhiteIpVo.class);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("partnerId", partnerId);
		List<WhiteIpVo> ips = find("select ip from partner_white_ip where partner_id=:partnerId",
				new BeanPropertyRowMapper<WhiteIpVo>(WhiteIpVo.class), map);
		if (ips != null) {
			CacheUtil.addCache(CacheKey.PARTNER_IPS, key, JSON.toJSON(ips));
		} else {
			CacheUtil.addCache(CacheKey.PARTNER_IPS, key, "[]");
		}
		return ips;
	}

	public boolean exist(String ip, Long partnerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("ip", ip);
		map.put("partnerId", partnerId);
		return queryForLong("select count(*) from partner_white_ip where ip=:ip and partner_id=:partnerId", map) > 0;
	}
}
