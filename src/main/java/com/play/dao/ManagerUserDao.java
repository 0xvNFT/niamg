package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.ManagerUser;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 总控管理员
 *
 * @author admin
 *
 */
@Repository
public class ManagerUserDao extends JdbcRepository<ManagerUser> {

	public ManagerUser findByUsername(String username) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username.trim().toLowerCase());
		return findOne("select * from manager_user where username=:username", map);
	}

	public void updatePwd(ManagerUser u) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", u.getId());
		map.put("salt", u.getSalt());
		map.put("password", u.getPassword());
		update("update manager_user set salt=:salt,password=:password where id=:id", map);
	}

	public Page<ManagerUser> query() {
		return queryByPage("select * from manager_user order by id desc");
	}

	public void changeStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update manager_user set status=:status where id=:id", map);
	}

	public boolean exist(String username) {
		return queryForLong("select count(*) from manager_user where username=:username", "username", username) > 0;
	}

	public void updatePwd2(Long id, String pwd) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("pwd", pwd);
		update("update manager_user set password2=:pwd where id=:id", map);
	}

}
