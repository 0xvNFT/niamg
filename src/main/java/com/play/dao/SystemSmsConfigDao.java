package com.play.dao;

import com.play.common.Constants;
import com.play.model.SystemSmsConfig;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SystemSmsConfigDao extends JdbcRepository<SystemSmsConfig> {

    public Page<SystemSmsConfig> adminPage() {
        Map<String, Object> map = new HashMap<>();
        return queryByPage("select * from system_sms_config", map);
    }

    public SystemSmsConfig findOne(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_ENABLE);
        return findOne("select * from system_sms_config where status=:status order by id desc limit 1", map);
    }

    public SystemSmsConfig findByCountry(String countryCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_ENABLE);
        map.put("countryCode", countryCode);
        return findOne("select * from system_sms_config where status=:status and country_code=:countryCode order by id desc limit 1", map);
    }

    public void changeStatus(Long id, Long stationId, Integer status) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append(" update system_sms_config set status =:status");
        sb.append(" where id =:id");
        map.put("status", status);
        map.put("id", id);
        update(sb.toString(), map);
    }

}
