package com.play.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationRedPacketDegree;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class StationRedPacketDegreeDao extends JdbcRepository<StationRedPacketDegree> {
	private static final String REDIS_KEY_DEGREE = "degree:";

	public void deleteByRedPacketId(Long redPacketId) {
		if (redPacketId == null || redPacketId <= 0) {
			return;
		}
		update("delete from station_red_packet_degree where packet_id=:id", MapUtil.newHashMap("id", redPacketId));
		CacheUtil.delCache(CacheKey.RED_PACKET, REDIS_KEY_DEGREE + redPacketId);
	}

	public List<Long> findByRedPacketId(Long redPacketId) {
		if (redPacketId == null || redPacketId <= 0) {
			return null;
		}
		String key = REDIS_KEY_DEGREE + redPacketId;
		String json = CacheUtil.getCache(CacheKey.RED_PACKET, key);
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, Long.class);
		}
		List<Long> list = find("select degree_id from station_red_packet_degree where packet_id=:id",
				MapUtil.newHashMap("id", redPacketId), Long.class);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.RED_PACKET, key, list);
		}
		return list;
	}

	public List<Long> findRedPacketNumById(Long redPacketId, Long degreeId) {
		if (redPacketId == null || redPacketId <= 0) {
			return null;
		}
		String key = REDIS_KEY_DEGREE + redPacketId + ":" + degreeId;
		String json = CacheUtil.getCache(CacheKey.RED_PACKET, key);
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, Long.class);
		}
		List<Long> list = find(
				"select packet_number_got from station_red_packet_degree where packet_id=:redPacketId and degree_id=:degreeId",
				MapUtil.newHashMap("redPacketId", redPacketId, "degreeId", degreeId), Long.class);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.RED_PACKET, key, list);
		}
		return list;
	}

	/**
	 * 计算2个红包是否包含同一个会员层级
	 * 
	 * @param id
	 * @param otherId
	 * @return
	 */
	public boolean containLevel(Long id, Long otherId) {
		StringBuilder sql = new StringBuilder("select count(*) from station_red_packet_degree");
		sql.append(" where packet_id=:id and degree_id in(");
		sql.append("select degree_id from station_red_packet_degree");
		sql.append(" where packet_id=:otherId)");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("otherId", otherId);
		return super.count(sql.toString(), map) > 0;
	}

	public boolean existByDegreeId(Long redPacketId, Long degreeId) {
		Map<String, Object> map = new HashMap<>();
		map.put("redPacketId", redPacketId);
		map.put("degreeId", degreeId);
		return count(
				"select count(*) from station_red_packet_degree where packet_id=:redPacketId and degree_id=:degreeId",
				map) > 0;
	}

	/**
	 * redBagNum 是否为0
	 * 
	 * @return
	 */
	public List<StationRedPacketDegree> listBySidAndRid(Long stationId, Long redBagId, boolean redBagNum) {
		StringBuilder sql_sb = new StringBuilder("select * from station_red_packet_degree");
		sql_sb.append(" where station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (redBagId != null) {
			sql_sb.append(" AND packet_id = :redBagId");
			paramMap.put("redBagId", redBagId);
		}
		if (redBagNum) {
			sql_sb.append(" and packet_number > 0");
		}
		sql_sb.append(" order by red_bag_recharge_min asc");
		return super.find(sql_sb.toString(), paramMap);
	}

	public StationRedPacketDegree getBySidAndRid(Long stationId, Long redBagId, Long degreeId,
			BigDecimal rechargeMoney) {
		StringBuilder sql = new StringBuilder("select * from station_red_packet_degree where 1=1");
		sql.append(" and station_id = :stationId");
		sql.append(" and packet_id = :redBagId");
		sql.append(" and degree_id=:degreeId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("redBagId", redBagId);
		paramMap.put("degreeId", degreeId);
		if (rechargeMoney != null && rechargeMoney.compareTo(BigDecimal.ZERO) > 0) {
			sql.append(" and :rechargeMoney >= red_bag_recharge_min and :rechargeMoney <= red_bag_recharge_max");
			paramMap.put("rechargeMoney", rechargeMoney);
		}
		return findOne(sql.toString(), paramMap);
	}

	public void batchInsert(List<StationRedPacketDegree> list, Long redPacketId) {
		StringBuilder sql = new StringBuilder("INSERT INTO station_red_packet_degree");
		sql.append("(station_id,packet_id,degree_id,packet_number,packet_number_got,");
		sql.append("red_bag_min,red_bag_max,red_bag_recharge_min,red_bag_recharge_max)");
		sql.append(" VALUES (?, ?, ?, ?, ?, ?,?,?,?)");
		getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				StationRedPacketDegree rd = list.get(i);
				ps.setLong(1, rd.getStationId());
				ps.setLong(2, redPacketId);
				ps.setLong(3, rd.getDegreeId());
				ps.setInt(4, rd.getPacketNumber());
				ps.setInt(5, rd.getPacketNumberGot());
				ps.setBigDecimal(6, rd.getRedBagMin());
				ps.setBigDecimal(7, rd.getRedBagMax());
				ps.setBigDecimal(8, rd.getRedBagRechargeMin());
				ps.setBigDecimal(9, rd.getRedBagRechargeMax());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		CacheUtil.delCache(CacheKey.RED_PACKET, REDIS_KEY_DEGREE + redPacketId);
	}

}
