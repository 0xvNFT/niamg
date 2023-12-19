package com.play.dao;

import com.play.orm.jdbc.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.ThirdPlayerConfig;
import com.play.orm.jdbc.JdbcRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户第三方配置表 
 *
 * @author admin
 *
 */
@Repository
public class ThirdPlayerConfigDao extends JdbcRepository<ThirdPlayerConfig> {


    public Page<ThirdPlayerConfig> getPage(Long stationId, String username) {
        StringBuilder sql = new StringBuilder("select d.*");
        sql.append(" from third_player_config d where d.station_id =:stationId");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(username)) {
            sql.append(" and d.username=:username");
            map.put("username", username);
        }
        map.put("stationId", stationId);
        return queryByPage(sql.toString(), map);
    }


    public ThirdPlayerConfig getOneByUserAndConfigName(Long userId, String configName, Long stationId) {
        StringBuilder sql = new StringBuilder("select d.*");
        Map<String, Object> map = new HashMap<>();
        sql.append(" from third_player_config d where d.station_id =:stationId");
        sql.append(" and d.config_name=:configName");
        sql.append(" and d.user_id =:userId");
        map.put("userId", userId);
        map.put("configName", configName);
        map.put("stationId", stationId);
        return findOne(sql.toString(), map);
    }

    public List<ThirdPlayerConfig> findConfig(Long userId, Long stationId) {
        StringBuilder sql = new StringBuilder("select * from third_player_config ");
        Map<String, Object> map = new HashMap<>();
        sql.append("where station_id =:stationId and user_id=:userId");
        map.put("userId", userId);
        map.put("stationId", stationId);
        return find(sql.toString(), map);
    }


}
