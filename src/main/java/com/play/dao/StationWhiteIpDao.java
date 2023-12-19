package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationWhiteIp;
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
public class StationWhiteIpDao extends JdbcRepository<StationWhiteIp> {

    public Page<StationWhiteIp> page(Long partnerId, Long stationId, String ip, Integer type) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_ip where 1=1");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        if (type != null) {
            sql.append(" and type=:type");
            map.put("type", type);
        }
        if (Objects.nonNull(partnerId)) {
            sql.append(" and partner_id=:partnerId");
            map.put("partnerId", partnerId);
        }
        if (StringUtils.isNotEmpty(ip)) {
            sql.append(" and ip=:ip");
            map.put("ip", ip);
        }
        sql.append(" order by id desc");
        return queryByPage(sql.toString(), map);
    }

    public boolean exist(String ip, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("stationId", stationId);
        return queryForLong("select count(*) from station_white_ip where ip=:ip and station_id=:stationId", map) > 0;
    }

    public List<WhiteIpVo> getIps(Long stationId) {
    	String str = CacheUtil.getCache(CacheKey.FRONT_STATIOIN_IPS, "s:" + stationId);
		if (str != null) {
			return JSON.parseArray(str, WhiteIpVo.class);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		List<WhiteIpVo> ips = find("select ip,type from station_white_ip where station_id=:stationId",
				new BeanPropertyRowMapper<WhiteIpVo>(WhiteIpVo.class), map);
		if (ips != null) {
			CacheUtil.addCache(CacheKey.FRONT_STATIOIN_IPS, "s:" + stationId, JSON.toJSON(ips));
		} else {
			CacheUtil.addCache(CacheKey.FRONT_STATIOIN_IPS, "s:" + stationId, "[]");
		}
		return ips;
    }

    public StationWhiteIp findOneById(Long id, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_ip where id=:id");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        map.put("id", id);
        return findOne(sql.toString(), map);
    }


    public List<StationWhiteIp> findList(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_ip where ");
        if (stationId != null) {
            sql.append(" station_id=:stationId");
            map.put("stationId", stationId);
        }
        return find(sql.toString(), map);
    }
}
