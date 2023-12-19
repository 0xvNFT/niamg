package com.play.dao;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.orm.jdbc.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationKickbackStrategy;
import com.play.orm.jdbc.JdbcRepository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员按日反水策略 
 *
 * @author admin
 *
 */
@Repository
public class StationKickbackStrategyDao extends JdbcRepository<StationKickbackStrategy> {

    /**
     * 后台管理列表
     */
    public Page<StationKickbackStrategy> adminPage(Long stationId, Integer type) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append(" select * from station_kickback_strategy");
        sb.append(" where station_id =:stationId");
        if(type!=null) {
            sb.append(" and type=:type");
            map.put("type", type);
        }
        sb.append(" order by create_datetime desc");
        map.put("stationId", stationId);
        return queryByPage(sb.toString(), map);
    }

    /**
     * 根据类型，最小投注额获取策略
     */
    public StationKickbackStrategy getByMinBetType(BigDecimal minBet, Long stationId, Integer type) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append(" select * from station_kickback_strategy");
        sb.append(" where station_id =:stationId");
        sb.append(" and min_bet =:minBet");
        sb.append(" and type =:type");
        sb.append(" order by create_datetime desc");
        map.put("stationId", stationId);
        map.put("minBet", minBet);
        map.put("type", type);
        return findOne(sb.toString(), map);
    }

    public void changeStatus(Long id, Long stationId, Integer status) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append(" update station_kickback_strategy set status =:status");
        sb.append(" where station_id = :stationId");
        sb.append(" and id =:id");
        map.put("status", status);
        map.put("stationId", stationId);
        map.put("id", id);
        update(sb.toString(), map);
        CacheUtil.delCacheByPrefix(CacheKey.KICKBACK_STRATEGY, "s_" + stationId);
    }

    @Override
    public int deleteById(Serializable id) {
        int i = super.deleteById(id);
        CacheUtil.delCacheByPrefix(CacheKey.KICKBACK_STRATEGY);
        return i;
    }

    @Override
    public int update(StationKickbackStrategy t) {
        int i = super.update(t);

        CacheUtil.delCacheByPrefix(CacheKey.KICKBACK_STRATEGY, "s_" + t.getStationId());

        return i;
    }

    public List<StationKickbackStrategy> findByTypeFromCache(Integer betType, Long stationId) {
        if (stationId == null || betType == null) {
            return null;
        }
        String key = "s_" + stationId + "_bt_" + betType;
        String json = CacheUtil.getCache(CacheKey.KICKBACK_STRATEGY, key, String.class);
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, StationKickbackStrategy.class);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("betType", betType);
        List<StationKickbackStrategy> list = find(
                "select * from station_kickback_strategy where station_id=:stationId and type=:betType and status=2 order by min_bet asc",
                map);
        if (list != null && !list.isEmpty()) {
            CacheUtil.addCache(CacheKey.KICKBACK_STRATEGY, key, list);
        }
        return list;
    }

}
