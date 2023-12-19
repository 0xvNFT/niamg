package com.play.dao;

import com.play.model.SysUserThirdAuth;
import com.play.model.dto.SysUserThirdAuthDto;
import com.play.orm.jdbc.JdbcRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SysUserThirdAuthDao extends JdbcRepository<SysUserThirdAuth> {

    public SysUserThirdAuth getOne(Long stationId, String source, String thirdId) {

        Map<String, Object> map = new HashMap<>();
        StringBuilder sql = new StringBuilder("select * from sys_user_third_auth where station_id = :stationId and source = :source and third_id = :thirdId ");
        map.put("stationId", stationId);
        map.put("source", source);
        map.put("thirdId", thirdId);
        return super.findOne(sql.toString(), map);
    }
    public boolean isExist(Long stationId, String source, String thirdId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("source", source);
        map.put("thirdId", thirdId);
        return count("select count(1) from sys_user_third_auth where station_id = :stationId and source = :source and third_id = :thirdId",
                map) > 0;
    }



}
