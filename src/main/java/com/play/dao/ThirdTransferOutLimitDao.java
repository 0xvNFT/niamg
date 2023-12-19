package com.play.dao;

import com.play.model.ThirdTransferOutLimit;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.JdbcRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 站点转出限制
 *
 * @author admin
 */
@Repository
public class ThirdTransferOutLimitDao extends JdbcRepository<ThirdTransferOutLimit> {

    public Page<ThirdTransferOutLimit> getPage(Long stationId, Integer platform) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append("select * from third_transfer_out_limit where station_id=:stationId");
        map.put("stationId", stationId);
        if (platform != null) {
            sb.append(" and platform = :platform");
            map.put("platform", platform);
        }
        return queryByPage(sb.toString(), map);

    }

    public ThirdTransferOutLimit findByPlatformStationId(Long stationId, Integer platform) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sb.append("select * from third_transfer_out_limit where station_id=:stationId");
        map.put("stationId", stationId);
        if (platform != null) {
            sb.append(" and platform = :platform");
            map.put("platform", platform);
        }
        return findOne(sb.toString(), map);
    }

}
