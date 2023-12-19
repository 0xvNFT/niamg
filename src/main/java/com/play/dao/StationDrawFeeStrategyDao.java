package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.common.Constants;
import com.play.model.StationDrawFeeStrategy;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 */
@Repository
public class StationDrawFeeStrategyDao extends JdbcRepository<StationDrawFeeStrategy> {
    public Page<StationDrawFeeStrategy> getPage(Long stationId, Integer status) {
        StringBuilder sb = new StringBuilder("select * from station_draw_fee_strategy");
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
        sb.append(" update station_draw_fee_strategy set status=:status");
        sb.append(" where id =:id and station_id = :stationId");
        map.put("status", status);
        map.put("stationId", stationId);
        map.put("id", id);
        return update(sb.toString(), map);
    }

    public List<StationDrawFeeStrategy> getList(Long stationId, Long degreeId, Long groupId) {
        StringBuilder sb = new StringBuilder("select * from station_draw_fee_strategy");
        sb.append(" where station_id =:stationId and status=:status");
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_ENABLE);
        map.put("stationId", stationId);
        if (degreeId != null && degreeId > 0) {
            sb.append(" and degree_ids like :degreeIds");
            map.put("degreeIds", "%," + degreeId + ",%");
        }
        if (groupId != null && groupId > 0) {
            sb.append(" and group_ids like :groupIds");
            map.put("groupIds", "%," + groupId + ",%");
        }
        sb.append(" order by fee_value desc");
        return find(sb.toString(), map);
    }

    public StationDrawFeeStrategy findOne(Long id, Long stationId, String degreeIds, String groupIds) {
        StringBuilder sb = new StringBuilder("select * from station_draw_fee_strategy");
        sb.append(" where station_id =:stationId and status=:status");
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_ENABLE);
        map.put("stationId", stationId);
        if (null!=id) {
            sb.append(" and id !=:id");
            map.put("id", id);
        }
        if (StringUtils.isNotEmpty(degreeIds)) {
            sb.append(" and degree_ids =:degreeIds");
            map.put("degreeIds", degreeIds);
        }
        if (StringUtils.isNotEmpty(groupIds)) {
            sb.append(" and group_ids = :groupIds");
            map.put("groupIds", groupIds);
        }
        return findOne(sb.toString(), map);
    }
}
