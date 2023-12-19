package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.SysUserAvatarRecord;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员头像记录
 *
 * @author admin
 *
 */
@Repository
public class SysUserAvatarRecordDao extends JdbcRepository<SysUserAvatarRecord> {

	public SysUserAvatarRecord findOneByUserId(Long userId, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		return findOne(
				"select * from sys_user_avatar_record where user_id = :userId and station_id=:stationId and type =1  order by create_datetime desc limit 1",
				map);
	}

	public List<SysUserAvatarRecord> getAvatarList(Long userId, Long stationId, Integer type) {
		StringBuilder sql_sb = new StringBuilder("SELECT * FROM sys_user_avatar_record");
		sql_sb.append(" WHERE station_id = :stationId and user_id = :userId and type = :type");
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		map.put("type", type);
		return super.find(sql_sb.toString(), map);
	}

}
