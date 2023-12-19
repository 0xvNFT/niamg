package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.AdminUserGroupAuth;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.mapper.LongRowMapper;

/**
 * 站点管理员组别权限信息 
 *
 * @author admin
 *
 */
@Repository
public class AdminUserGroupAuthDao extends JdbcRepository<AdminUserGroupAuth> {

	public List<Long> getMenuIds(Long stationId, Long groupId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("groupId", groupId);
		return find("select menu_id from admin_user_group_auth where station_id=:stationId and group_id=:groupId",
				new LongRowMapper(), map);
	}

	public void deleteByGroupId(Long stationId, Long groupId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("groupId", groupId);
		update("delete from admin_user_group_auth where station_id=:stationId and group_id=:groupId", map);
	}

	public boolean exist(Long stationId, Long groupId, Long menuId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("groupId", groupId);
		map.put("menuId", menuId);
		return queryForLong(
				"select count(*) from admin_user_group_auth where station_id=:stationId and group_id=:groupId and menu_id=:menuId",
				map) > 0;
	}

	public void deleteForBatch(Long stationId, Long groupId, List<Long> menuIds) {
		if (menuIds == null || menuIds.isEmpty()) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("delete from admin_user_group_auth where station_id=");
		sql.append(stationId).append(" and group_id=").append(groupId).append(" and menu_id in(");
		for (Long menuId : menuIds) {
			sql.append(menuId).append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		update(sql.toString());
	}
}
