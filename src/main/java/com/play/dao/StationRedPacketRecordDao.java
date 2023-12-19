package com.play.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationRedPacketRecord;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class StationRedPacketRecordDao extends JdbcRepository<StationRedPacketRecord> {

	public Page<StationRedPacketRecord> getRecordPage(String username, Long stationId, Date begin, Date end) {
		StringBuilder sql = new StringBuilder("SELECT * FROM station_red_packet_record WHERE station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" AND username = :username");
			paramMap.put("username", username);
		}
		if (begin != null) {
			sql.append(" AND create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sql.append(" AND create_datetime < :end");
			paramMap.put("end", end);
		}
		sql.append(" ORDER BY id DESC");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "money", "totalMoney"));
		return super.queryByPage(sql.toString(), paramMap, aggs);
	}

	public int changeUntreatedToTreated(StationRedPacketRecord record) {
		StringBuilder sql = new StringBuilder("UPDATE station_red_packet_record");
		sql.append(" SET status = :status,remark = :remark");
		sql.append(" WHERE id = :id AND status = ").append(StationRedPacketRecord.STATUS_UNTREATED);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", record.getId());
		paramMap.put("status", record.getStatus());
		paramMap.put("remark", record.getRemark());
		return super.update(sql.toString(), paramMap);
	}

	public List<StationRedPacketRecord> getRecordList(Long stationId, Integer status, Long redPacketId, Integer limit) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_red_packet_record");
		sql_sb.append(" WHERE 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if(stationId!=null) {
			sql_sb.append(" and station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (redPacketId != null && redPacketId > 0) {
			sql_sb.append(" AND packet_id = :redPacketId");
			paramMap.put("redPacketId", redPacketId);
		}
		if (status != null && status > 0) {
			sql_sb.append(" AND status = :status");
			paramMap.put("status", status);
		}
		sql_sb.append(" order by create_datetime desc");
		if (limit != null && limit > 0) {
			sql_sb.append(" LIMIT ").append(limit);
		}
		return find(sql_sb.toString(), paramMap);
	}

	public List<StationRedPacketRecord> getListBysidAndRid(Long stationId, Long redPacketId, String username,
			Integer limit, Integer status) {
		StringBuilder sql_sb = new StringBuilder(
				"SELECT  username,money,create_datetime FROM station_red_packet_record WHERE 1=1");
		sql_sb.append(" AND station_id = :stationId AND packet_id = :redPacketId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("redPacketId", redPacketId);
		paramMap.put("stationId", stationId);
		if (status != null && status > 0) {
			sql_sb.append(" AND status = :status");
			paramMap.put("status", status);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql_sb.append(" AND username = :username");
			paramMap.put("username", username);
		}
		sql_sb.append(" order by create_datetime desc");
		if (limit != null && limit > 0) {
			sql_sb.append(" LIMIT ").append(limit);
		}
		return find(sql_sb.toString(), paramMap);
	}

	public int countByIp(Date date, String ip, Long stationId, Long redPacketId) {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM station_red_packet_record");
		sql.append(" where station_id=:stationId and ip=:ip");
		sql.append(" and create_datetime >= :date");
		sql.append(" and packet_id=:redPacketId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ip", ip);
		paramMap.put("stationId", stationId);
		paramMap.put("redPacketId", redPacketId);
		paramMap.put("date", date);
		return queryForInt(sql.toString(), paramMap);
	}

	public int countByUserId(Date date, Long userId, Long stationId, Long redPacketId) {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM station_red_packet_record");
		sql.append(" where station_id=:stationId and user_id=:userId and packet_id=:redPacketId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		paramMap.put("redPacketId", redPacketId);
		if (date != null) {
			sql.append(" and create_datetime >=:date");
			paramMap.put("date", date);
		}
		return queryForInt(sql.toString(), paramMap);
	}

	/**
	 * 查询红包剩余额度 和 剩余个数
	 * 
	 * @param rid
	 * @return
	 */
	public StationRedPacketRecord getMoneyAndCount(Long rid) {
		StringBuilder sql_sb = new StringBuilder(
				"SELECT sum(money) money,count(*) num from station_red_packet_record where packet_id = :rid");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("rid", rid);
		return super.findOne(sql_sb.toString(), paramMap);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder("delete from station_red_packet_record");
		sb.append(" where station_id =:stationId and create_datetime <= :createTime");
		Map<String, Object> map = new HashMap<>();
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

    public List<StationRedPacketRecord> getUserRedPacketRecordList(Long userId, Long stationId, Long packetId, Integer status, Date startDate, Date endDate) {
		StringBuilder sql = new StringBuilder("SELECT * FROM station_red_packet_record");
		sql.append(" WHERE station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if(userId != null) {
			sql.append(" AND user_id = :userId");
			paramMap.put("userId", userId);
		}
		if (packetId != null && packetId > 0) {
			sql.append(" AND packet_id = :packetId");
			paramMap.put("packetId", packetId);
		}
		if (status != null && status > 0) {
			sql.append(" AND status = :status");
			paramMap.put("status", status);
		}
		if (startDate != null) {
			sql.append(" AND create_datetime >= :startDate");
			paramMap.put("startDate", startDate);
		}
		if (endDate != null) {
			sql.append(" AND create_datetime < :endDate");
			paramMap.put("endDate", endDate);
		}
		sql.append(" order by create_datetime desc");
		return find(sql.toString(), paramMap);
    }
}
