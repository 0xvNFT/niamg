package com.play.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.play.model.StationDummyData;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 虚拟数据
 *
 * @author admin
 *
 */
@Repository
public class StationDummyDataDao extends JdbcRepository<StationDummyData> {

	public List<StationDummyData> find(Long stationId, int type, Date date) {
		StringBuilder sql = new StringBuilder("select * from station_dummy_data");
		sql.append(" where station_id=:stationId and type=:type");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("type", type);
		if (date != null) {
			sql.append(" and win_time<=:date");
			map.put("date", date);
		}
		sql.append(" order by win_time desc");
		return find(sql.toString(), map);
	}

	public Page<StationDummyData> getPage(Long stationId, Integer type, Date begin, Date end) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from station_dummy_data");
		sql.append(" where station_id=:stationId");
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		map.put("stationId", stationId);
		if (begin != null) {
			sql.append(" and win_time >= :begin");
			map.put("begin", begin);
		}
		if (end != null) {
			sql.append(" and win_time <= :end");
			map.put("end", end);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public void deleteById(Long id, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("delete from station_dummy_data");
		sql.append(" where station_id=:stationId and id =:id");
		map.put("stationId", stationId);
		map.put("id", id);
		update(sql.toString(), map);
	}

	public void batchInsert(List<StationDummyData> sfdList) {
		StringBuilder sql = new StringBuilder("INSERT INTO station_dummy_data");
		sql.append("(username, win_money, win_time,type,station_id,item_name)");
		sql.append(" VALUES (?, ?, ?, ?, ?, ?)");
		getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				StationDummyData sfd = sfdList.get(i);
				ps.setString(1, sfd.getUsername());
				ps.setBigDecimal(2, sfd.getWinMoney());
				ps.setTimestamp(3, new java.sql.Timestamp(sfd.getWinTime().getTime()));
				ps.setInt(4, sfd.getType());
				ps.setLong(5, sfd.getStationId());
				ps.setString(6, sfd.getItemName());
			}

			@Override
			public int getBatchSize() {
				return sfdList.size();
			}
		});
	}
}
