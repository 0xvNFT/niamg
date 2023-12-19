package com.play.dao;

import com.play.model.StationDepositStrategy;
import com.play.model.StationRegisterStrategy;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StationRegisterStrategyDao extends JdbcRepository<StationRegisterStrategy> {

    public Page<StationRegisterStrategy> getPage(Integer giftType, Integer valueType, Date begin,
                                                 Date end, Long stationId) {
        StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_register_strategy WHERE 1=1");
        Map<String, Object> paramMap = new HashMap<>();
        if (stationId != null && stationId > 0) {
            sql_sb.append(" AND station_id = :stationId");
            paramMap.put("stationId", stationId);
        }
        if (giftType != null && giftType > 0) {
            sql_sb.append(" AND gift_type = :giftType");
            paramMap.put("giftType", giftType);
        }
        if (valueType != null && valueType > 0) {
            sql_sb.append(" AND value_type = :valueType");
            paramMap.put("valueType", valueType);
        }
        if (begin != null) {
            sql_sb.append(" AND start_datetime >= :begin");
            paramMap.put("begin", begin);
        }
        if (end != null) {
            sql_sb.append(" AND start_datetime < :end");
            paramMap.put("end", end);
        }
        sql_sb.append(" ORDER BY start_datetime DESC");
        return super.queryByPage(sql_sb.toString(), paramMap);
    }

    public StationRegisterStrategy getOne(Long id, Long stationId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("stationId", stationId);
        return findOne("SELECT * FROM station_register_strategy WHERE id=:id and station_id=:stationId", paramMap);
    }

    public void delete(Long id, Long stationId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("stationId", stationId);
        update("delete from station_register_strategy WHERE id=:id and station_id=:stationId", paramMap);
    }

    public void updStatus(Long id, Integer status) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        paramMap.put("status", status);
        update("update station_register_strategy set status=:status WHERE id=:id", paramMap);
    }


    public List<StationRegisterStrategy> find(Long stationId, Integer status,
                                             Integer valueType, Date date, BigDecimal money) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("stationId", stationId);
        StringBuilder sql = new StringBuilder("SELECT * FROM station_register_strategy WHERE station_id=:stationId");
        if (status != null && status > 0) {
            sql.append(" and status=:status");
            paramMap.put("status", status);
        }
        if (date != null) {
            sql.append(" and start_datetime<=:date and end_datetime>=:date");
            paramMap.put("date", date);
        }
        if (money != null) {
            sql.append(" and min_money<=:money and max_money>=:money");
            paramMap.put("money", money);
        }
        if (valueType != null) {
            sql.append(" and value_type=:valueType");
            paramMap.put("valueType", valueType);
        }
        return find(sql.toString(), paramMap);
    }
}
