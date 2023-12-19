package com.play.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;

import com.play.orm.jdbc.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.ThirdAutoExchange;
import com.play.orm.jdbc.JdbcRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方游戏额度自动转换记录表 
 *
 * @author admin
 *
 */
@Repository
public class ThirdAutoExchangeDao extends JdbcRepository<ThirdAutoExchange> {


    public Page<ThirdAutoExchange> getPage(Long stationId, Integer platform) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append("select * from third_auto_exchange where station_id=:stationId");
        map.put("stationId", stationId);
        if (platform != null) {
            sb.append(" and platform = :platform");
            map.put("platform", platform);
        }
        return queryByPage(sb.toString(), map);

    }

    public ThirdAutoExchange findByPlatformStationId(Long stationId, Integer platform) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append("select * from third_auto_exchange where station_id=:stationId");
        map.put("stationId", stationId);
        if (platform != null) {
            sb.append(" and platform = :platform");
            map.put("platform", platform);
        }
        return findOne(sb.toString(), map);
    }


    public List<ThirdAutoExchange> findAccountExchange(Long stationId, Long userId) {
        String key = getCacheKey(stationId, userId);
        String json = CacheUtil.getCache(CacheKey.THIRD_AUTO_EXCHANGE, key);
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, ThirdAutoExchange.class);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("accountId", userId);
        List<ThirdAutoExchange> list = find(
                "select * from third_auto_exchange where station_id=:stationId and user_id=:accountId", map);
        if (list != null && !list.isEmpty()) {
            CacheUtil.addCache(CacheKey.THIRD_AUTO_EXCHANGE, key, list);
        }
        return list;
    }

    public List<ThirdAutoExchange> findLastestAccountExchange(Long stationId, Long userId,Integer platform) {
        String key = getCacheKey2(stationId, userId);
        String json = CacheUtil.getCache(CacheKey.THIRD_AUTO_EXCHANGE, key);
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, ThirdAutoExchange.class);
        }
//        List<ThirdAutoExchange> list = find(
//                "select * from third_auto_exchange where station_id=:stationId and user_id=:accountId", map);
        StringBuilder sb = new StringBuilder("select * from third_auto_exchange where 1=1");
        Map<String, Object> map = new HashMap<>();
        if (stationId != null) {
            sb.append(" and station_id = :stationId");
            map.put("stationId", stationId);
        }
        if (userId != null) {
            sb.append(" and user_id = :userId");
            map.put("userId", userId);
        }
        if (platform != null) {
            sb.append(" and platform = :platform");
            map.put("platform", platform);
        }
        sb.append(" order by update_time desc limit 2");
        List<ThirdAutoExchange> result = super.find(sb.toString(), map);
        if (result != null && !result.isEmpty()) {
            CacheUtil.addCache(CacheKey.THIRD_AUTO_EXCHANGE, key, result);
        }
        return result;
    }

    private String getCacheKey(Long stationId, Long accountId) {
        return new StringBuilder("s").append(stationId).append(":").append(accountId).toString();
    }

    private String getCacheKey2(Long stationId, Long accountId) {
        return new StringBuilder("s-lastest").append(stationId).append(":").append(accountId).toString();
    }

    public void saveOrUpdate(Integer platform, Long stationId, Long userId, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("accountId", userId);
        map.put("platform", platform);
        map.put("type", type);
        map.put("time", new Date());
        StringBuilder sql = new StringBuilder("INSERT INTO third_auto_exchange(platform,station_id,");
        sql.append("user_id,type,update_time) VALUES (:platform,:stationId,:accountId,:type,:time)");
        sql.append(" on CONFLICT(platform,station_id,user_id) DO UPDATE SET type=:type,update_time=:time");
        update(sql.toString(), map);
        CacheUtil.delCache(CacheKey.THIRD_AUTO_EXCHANGE, getCacheKey(stationId, userId));
        CacheUtil.delCache(CacheKey.THIRD_AUTO_EXCHANGE, getCacheKey2(stationId, userId));
    }

}
