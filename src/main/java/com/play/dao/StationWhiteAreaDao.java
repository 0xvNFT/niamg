package com.play.dao;


import com.play.common.Constants;

import com.play.orm.jdbc.page.Page;

import org.springframework.stereotype.Repository;

import com.play.model.StationWhiteArea;
import com.play.orm.jdbc.JdbcRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 国家/地区限制 
 *
 * @author admin
 *
 */
@Repository
public class StationWhiteAreaDao extends JdbcRepository<StationWhiteArea> {

    public Page<StationWhiteArea> page(Long partnerId, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_area where 1=1");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        if (Objects.nonNull(partnerId)) {
            sql.append(" and partner_id=:partnerId");
            map.put("partnerId", partnerId);
        }
        sql.append(" order by id desc");
        return queryByPage(sql.toString(), map);
    }

    public void update(Long id, int status) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("update station_white_area set status= :status where id=:id");
        map.put("id", id);
        map.put("status", status);
        update(sql.toString(), map);
    }

    public StationWhiteArea findOneById(Long id, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_area where id=:id");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        map.put("id", id);
        return findOne(sql.toString(), map);
    }

    public List<StationWhiteArea> getAreas(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_area where 1=1");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        sql.append(" order by id desc");
        return find(sql.toString(), map);
    }

    public List<StationWhiteArea> getUserfulAreas(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from station_white_area where 1=1");
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        sql.append(" and status=:status");
        map.put("status", Constants.STATUS_ENABLE);
        sql.append(" order by id desc");
        return find(sql.toString(), map);
    }
}
