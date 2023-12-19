package com.play.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.AdminMenu;
import com.play.orm.jdbc.JdbcRepository;

/**
 * admin user 权限表
 *
 * @author admin
 *
 */
@Repository
public class AdminMenuDao extends JdbcRepository<AdminMenu> {

	public List<AdminMenu> findByGroupId(Long groupId, Long stationId) {
		StringBuilder sql = new StringBuilder("select m.* from admin_user_group_auth a left join admin_menu m");
		sql.append(" on a.menu_id=m.id where a.group_id=:groupId and a.station_id=:stationId and m.id is not null");
		Map<String, Object> map = new HashMap<>();
		map.put("groupId", groupId);
		map.put("stationId", stationId);
		sql.append(" order by m.sort_no desc");
		return find(sql.toString(), map);
	}

	public List<AdminMenu> getAll() {
		return find("select * from admin_menu order by sort_no desc");
	}

	public void initStation(Long stationId, Long partnerId, Long groupId, List<Long> excludeMenuIds) {
		StringBuilder sql = new StringBuilder("insert into admin_user_group_auth");
		sql.append(" (group_id,menu_id,station_id,partner_id)");
		sql.append("SELECT ").append(groupId).append(",id,").append(stationId);
		sql.append(",").append(partnerId).append(" FROM admin_menu");
		if (excludeMenuIds != null && !excludeMenuIds.isEmpty()) {
			sql.append(" where id not in(");
			for (Long id : excludeMenuIds) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		update(sql.toString());
	}

	public boolean hasPermission(Long menuId, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("menuId", menuId);
		map.put("stationId", stationId);
		return count("select count(1) from admin_user_group_auth where station_id=:stationId and menu_id=:menuId",
				map) > 0;
	}

	public List<Long> getExcludeMenuIdForZg() {// 总控主管 需要剔除的的菜单id
		List<Long> list = new ArrayList<>();
		return list;
	}

	public List<Long> getExcludeMenuIdForKf() {// 客服管理员 需要剔除的的菜单id
		List<Long> list = new ArrayList<>();
		return list;
	}

	public List<Long> getExcludeMenuIdForJs() {// 技术管理员 需要剔除的的菜单id
		List<Long> list = new ArrayList<>();
		return list;
	}

	/**
	 * 站点需要扣除的菜单
	 *
	 * @return
	 */
	public List<Long> getExcludeMenuIdForStation() {
		List<Long> list = new ArrayList<>();
		list.add(254L);//游戏开关
		list.add(255L);//修改游戏开关状态
		return list;
	}

	public List<Long> getExcludeMenuIdForPartner() {// 合作商管理员 需要剔除的菜单
		List<Long> list = new ArrayList<>();
		return list;
	}

	public void adminMenuRefresh(Long stationId, Long partnerId, Long groupId, Long[] adminMenuIds) {
		StringBuilder sql = new StringBuilder("insert into admin_user_group_auth");
		sql.append(" (group_id,menu_id,station_id,partner_id)");
		sql.append("SELECT ").append(groupId).append(",id,").append(stationId);
		sql.append(",").append(partnerId).append(" FROM admin_menu");
		if (adminMenuIds != null && adminMenuIds.length > 0) {
			sql.append(" where id in(");
			for (Long id : adminMenuIds) {
				sql.append(id).append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		update(sql.toString());
	}
}
