package com.play.dao;

import com.play.common.Constants;
import com.play.model.StationActivity;
import com.play.model.StationDrawFeeStrategy;
import com.play.model.StationDrawRuleStrategy;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 *
 */
@Repository
public class StationDrawRuleStrategyDao extends JdbcRepository<StationDrawRuleStrategy> {
    public Page<StationDrawRuleStrategy> getPage(Long stationId, Integer status) {
        StringBuilder sb = new StringBuilder("select * from station_draw_rule_strategy");
        sb.append(" where station_id =:stationId");
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        if (status != null) {
            sb.append(" and status =:status");
            map.put("status", status);
        }
        sb.append(" order by id desc");
        return queryByPage(sb.toString(), map);
    }

    public int updStatus(Long id, Integer status, Long stationId) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append(" update station_draw_rule_strategy set status=:status");
        sb.append(" where id =:id and station_id = :stationId");
        map.put("status", status);
        map.put("stationId", stationId);
        map.put("id", id);
        return update(sb.toString(), map);
    }

    public List<StationDrawRuleStrategy> getList(Long stationId, Long degreeId, Long groupId,Integer status) {
        StringBuilder sb = new StringBuilder("select * from station_draw_rule_strategy");
        sb.append(" where station_id =:stationId and status=:status");
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("stationId", stationId);
        if (degreeId != null && degreeId > 0) {
            sb.append(" and degree_ids like :degreeIds");
            map.put("degreeIds", "%," + degreeId + ",%");
        }
        if (groupId != null && groupId > 0) {
            sb.append(" and group_ids like :groupIds");
            map.put("groupIds", "%," + groupId + ",%");
        }
        sb.append(" order by value desc");
        return find(sb.toString(), map);
    }
}

