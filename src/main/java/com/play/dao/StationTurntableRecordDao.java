package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationTurntableAward;
import com.play.model.StationTurntableRecord;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 用户大转盘中奖记录
 *
 * @author admin
 */
@Repository
public class StationTurntableRecordDao extends JdbcRepository<StationTurntableRecord> {

    public Page<StationTurntableRecord> gtPage(Long stationId, Long turntableId, Long userId, String username, Date begin, Date end, Integer awardType, Integer status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM station_turntable_record");
        sql.append(" WHERE station_id=:stationId");
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        if (turntableId != null) {
            sql.append(" AND turntable_id = :turntableId");
            map.put("turntableId", turntableId);
        }
        if (awardType != null && awardType > 0) {
            sql.append(" AND award_type = :awardType");
            map.put("awardType", awardType);
        }
        if (status != null) {
            sql.append(" AND status = :status");
            map.put("status", status);
        }
        if (userId != null) {
            sql.append(" AND user_id = :userId");
            map.put("userId", userId);
        }
        if (StringUtils.isNotEmpty(username)) {
            sql.append(" AND username = :username");
            map.put("username", username);
        }
        if (begin != null) {
            sql.append(" AND create_datetime >= :begin");
            map.put("begin", begin);
        }
        if (end != null) {
            sql.append(" AND create_datetime < :end");
            map.put("end", end);
        }
        sql.append(" ORDER BY id DESC");
        return super.queryByPage(sql.toString(), map);
    }

    public void updateStatus(Long id, Integer status, String remark) {
        StringBuilder sql_sb = new StringBuilder("UPDATE station_turntable_record");
        sql_sb.append(" SET status = :newStatus,remark=:remark");
        sql_sb.append(" WHERE id = :id AND status=:oldStatus");
        Map<String, Object> map = new HashMap<>();
        map.put("newStatus", status);
        map.put("id", id);
        map.put("remark", remark);
        map.put("oldStatus", StationTurntableRecord.STATUS_UNTREATED);
        super.update(sql_sb.toString(), map);
    }

    public Integer countUserPlayNum(Long userId, Date begin, Date end, Long turntableId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("select count(*) from station_turntable_record");
        sb.append(" where user_id=:userId and turntable_id=:turntableId");
        params.put("userId", userId);
        params.put("turntableId", turntableId);
        if (begin != null) {
            sb.append(" and create_datetime >=:begin");
            params.put("begin", begin);
        }
        if (end != null) {
            sb.append(" and create_datetime <=:end");
            params.put("end", end);
        }
        return queryForInt(sb.toString(), params);
    }

    public List<StationTurntableRecord> getRecordList(Long stationId, Date begin, Date end, String username, Integer awardType, Long turntableId, Integer limit, Integer status, Long userId,String productName) {
        StringBuilder sb = new StringBuilder("SELECT turntable_id, username,gift_name,create_datetime,remark,status,");
        sb.append("award_value,award_type FROM station_turntable_record WHERE station_id=:stationId");
        Map<String, Object> params = new HashMap<>();
        params.put("stationId", stationId);
        if (turntableId != null) {
            sb.append(" AND turntable_id = :turntableId");
            params.put("turntableId", turntableId);
        }
        if (begin != null) {
            sb.append(" and create_datetime >=:begin");
            params.put("begin", begin);
        }
        if (end != null) {
            sb.append(" and create_datetime <=:end");
            params.put("end", end);
        }
        if (awardType != null) {
            sb.append(" AND award_type = :awardType");
            params.put("awardType", awardType);
        }
        if (status != null) {
            sb.append(" AND status = :status");
            params.put("status", status);
        }
        if (StringUtils.isNotEmpty(username)) {
            sb.append(" AND username = :username");
            params.put("username", username);
        }
        if (null != userId) {
            sb.append(" AND user_id = :userId");
            params.put("userId", userId);
        }
        if(StringUtils.isNotEmpty(productName)){
            sb.append(" AND gift_name = :productName ");
            params.put("productName",productName);
        }
        sb.append(" ORDER BY id desc");
        if (limit != null && limit > 0) {
            sb.append(" LIMIT ").append(limit);
        }
        logger.error(sb.toString());
        return super.find(sb.toString(), params);
    }

    public List<StationTurntableRecord> getUntreatedRecords() {
        StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_turntable_record");
        sql_sb.append(" WHERE award_type <> :type AND status = :status");
        sql_sb.append(" order by create_datetime desc");
        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("type", StationTurntableAward.AWARD_TYPE_GIFT);
        paramMap.put("status", StationTurntableRecord.STATUS_UNTREATED);
        sql_sb.append(" LIMIT ").append(1000);
        return super.find(sql_sb.toString(), paramMap);
    }

    public int hanlderUntreated(StationTurntableRecord record) {
        StringBuilder sql_sb = new StringBuilder("UPDATE station_turntable_record");
        sql_sb.append(" SET status=:status,remark=:remark WHERE id=:id");
        sql_sb.append(" AND status=").append(StationTurntableRecord.STATUS_UNTREATED);
        Map<String, Object> paramMap = MapUtil.newHashMap();
        paramMap.put("status", record.getStatus());
        paramMap.put("id", record.getId());
        paramMap.put("remark", record.getRemark());
        return super.update(sql_sb.toString(), paramMap);
    }

}
