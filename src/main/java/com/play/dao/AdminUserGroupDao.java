package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.AdminUserGroup;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点管理员组别信息
 *
 * @author admin
 *
 */
@Repository
public class AdminUserGroupDao extends JdbcRepository<AdminUserGroup> {

	public List<AdminUserGroup> getAll(Long partnerId, Long stationId, Integer minType) {
		StringBuilder sql = new StringBuilder("SELECT * FROM admin_user_group WHERE 1=1");
		Map<String, Object> map = new HashMap<>();
		if (partnerId != null) {
			sql.append(" and partner_id = :partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" and station_id = :stationId");
			map.put("stationId", stationId);
		}
		if (minType != null) {
			sql.append(" and type>=:minType");
			map.put("minType", minType);
		}
		sql.append(" order by id asc");
		return super.find(sql.toString(), map);
	}

	public Page<AdminUserGroup> page(Long partnerId, Long stationId, Integer minType) {
		StringBuilder sql = new StringBuilder("SELECT * FROM admin_user_group WHERE 1=1");
		Map<String, Object> map = new HashMap<>();
		if (partnerId != null) {
			sql.append(" and partner_id = :partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" and station_id = :stationId");
			map.put("stationId", stationId);
		}
		if (minType != null && minType > 0) {
			sql.append(" and type>=:minType");
			map.put("minType", minType);
		}
		sql.append(" order by id asc");
		return queryByPage(sql.toString(), map);
	}

	public void updateName(Long id, String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		update("update admin_user_group set name=:name where id=:id", map);
	}

    public AdminUserGroup getAdminUsergroup(Long stationId, Long partnerId, String groupName, Integer groupType) {
		StringBuilder sql = new StringBuilder("SELECT * FROM admin_user_group WHERE station_id = :stationId AND partner_id = :partnerId AND name = :groupName AND type = :groupType");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("partnerId", partnerId);
		map.put("groupName", groupName);
		map.put("groupType", groupType);
		return super.findOne(sql.toString(), map);
    }
}
