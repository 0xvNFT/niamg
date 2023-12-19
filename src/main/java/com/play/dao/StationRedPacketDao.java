package com.play.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.common.Constants;
import org.springframework.stereotype.Repository;

import com.play.model.StationRedPacket;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class StationRedPacketDao extends JdbcRepository<StationRedPacket> {

	public Page<StationRedPacket> getPage(Long stationId) {
		return super.queryByPage(
				"SELECT * FROM station_red_packet WHERE station_id =:stationId ORDER BY begin_datetime DESC",
				MapUtil.newHashMap("stationId", stationId));
	}

	public int updStatus(Long id, Integer status, Long stationId) {
		return super.update("UPDATE station_red_packet SET status = :status WHERE id =:id and station_id=:stationId",
				MapUtil.newHashMap("status", status, "id", id, "stationId", stationId));
	}

	public List<StationRedPacket> getRedPacketList(Long stationId, Integer status, Date time) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_red_packet WHERE station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (status != null && status > 0) {
			sql_sb.append(" AND status = :status");
			paramMap.put("status", status);
		}
		if (time != null) {
			sql_sb.append(" AND end_datetime>:date");
			paramMap.put("date", time);
		}
		return super.find(sql_sb.toString(), paramMap);
	}

	public StationRedPacket getOne(Long stationId, Long rpId) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM station_red_packet WHERE station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		sql_sb.append(" AND status = :status");
		paramMap.put("status", Constants.STATUS_ENABLE);
		sql_sb.append(" AND id = :id");
		paramMap.put("id", rpId);
		return super.findOne(sql_sb.toString(), paramMap);
	}

	public int grabRedPacket(Long id, BigDecimal money, BigDecimal remainMoney) {
		StringBuilder sql_sb = new StringBuilder("UPDATE station_red_packet SET ");
		sql_sb.append(" remain_money = remain_money - :money,");
		sql_sb.append(" remain_number = remain_number - 1");
		sql_sb.append(" WHERE id =:id AND remain_money=:remainMoney AND remain_number > 1");
		int i = super.update(sql_sb.toString(),
				MapUtil.newHashMap("money", money, "id", id, "remainMoney", remainMoney));
		return i;
	}
}
