package com.play.dao;

import com.play.common.Constants;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import com.play.model.StationActivity;
import com.play.orm.jdbc.JdbcRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  
 *
 * @author admin
 *
 */
@Repository
public class StationActivityDao extends JdbcRepository<StationActivity> {

    public Page<StationActivity> page(Long stationId) {
        StringBuilder sb = new StringBuilder("select * from station_activity where 1=1");
        Map<String, Object> map = new HashMap<>();
        sb.append(" and station_id = :stationId");
        map.put("stationId", stationId);
        sb.append(" order by sort_no asc");
        return super.queryByPage(sb.toString(), map);
    }

    public void delete(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from station_activity where id = :id and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public List<StationActivity> getActivity(Long stationId, Date overTime) {
        StringBuilder sb = new StringBuilder("select * from station_activity where 1=1");
        Map<String, Object> map = new HashMap<>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        sb.append(" and over_time >= :overTime");
        map.put("overTime", overTime);
        sb.append(" and model_status = 2");
        sb.append(" order by sort_no asc");
        return super.find(sb.toString(), map);
    }

    public List<StationActivity> listByStationId(Long stationId, Integer status, Date date) {
        StringBuilder sb = new StringBuilder("select * from station_activity where 1=1");
        Map<String, Object> map = new HashMap<>();
        sb.append(" and station_id = :stationId and status = :status");
        map.put("stationId", stationId);
        map.put("status", status);
        if (date != null) {
            sb.append(" and update_time <=:date and over_time >=:date");
            map.put("date",date);
        }
        sb.append(" order by sort_no asc");
        return super.find(sb.toString(), map);
    }

    public int getActivityCount(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", Constants.STATUS_ENABLE);
        map.put("station_id", stationId);
        return queryForInt("select count(*) from station_activity where status= :status and station_id=:station_id", map);
    }

    public List<StationActivity> listByStationIdAndLang(Long stationId, int status, Date date, String lang, Integer deviceType) {
        StringBuilder sb = new StringBuilder("select * from station_activity where 1=1");
        Map<String, Object> map = new HashMap<>();
        sb.append(" and station_id = :stationId and status = :status and language = :lang");
        map.put("lang", lang);
        map.put("stationId", stationId);
        map.put("status", status);
        map.put("deviceType", deviceType);

        if (null != deviceType) {
            sb.append(" and (device_type = :deviceType or device_type = 1)");
        } else {
            sb.append(" and device_type = 1");
        }

        if (date != null) {
            sb.append(" and update_time <=:date and over_time >=:date");
            map.put("date",date);
        }
        sb.append(" order by sort_no asc");
        return super.find(sb.toString(), map);
    }
}