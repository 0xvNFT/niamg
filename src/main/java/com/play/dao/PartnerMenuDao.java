package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.PartnerMenu;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 合作商后台菜单 
 *
 * @author admin
 *
 */
@Repository
public class PartnerMenuDao extends JdbcRepository<PartnerMenu> {
	public List<PartnerMenu> getAll() {
		return find("select * from partner_menu");
	}

	public List<PartnerMenu> findByUserId(Long id) {
		return find(
				"select m.* from partner_menu m left join partner_user_auth a on m.id=a.menu_id where a.user_id="
						+ id);
	}

	public List<Long> findMenuIdByUserId(Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return find(
				"select m.id from partner_menu m left join partner_user_auth a on m.id=a.menu_id where a.user_id=:id",
				map, Long.class);
	}

	public List<String> findPermByUserId(Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return find(
				"select m.perm_name from partner_menu m left join partner_user_auth a on m.id=a.menu_id where a.user_id=:id",
				map, String.class);
	}
}
