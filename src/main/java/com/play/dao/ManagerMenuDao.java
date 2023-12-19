package com.play.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.play.model.ManagerMenu;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 总控菜单
 *
 * @author admin
 *
 */
@Repository
public class ManagerMenuDao extends JdbcRepository<ManagerMenu> {

	public List<ManagerMenu> findAllActive() {
		return find("select * from manager_menu");
	}

	public List<ManagerMenu> findByUserId(Long id) {
		return find(
				"select m.* from manager_menu m left join manager_user_auth a on m.id=a.menu_id where a.user_id=" + id);
	}

}
