package com.play.dao;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.play.model.ManagerUserAuth;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class ManagerUserAuthDao extends JdbcRepository<ManagerUserAuth> {

	public void deleteByUserId(Long userId) {
		update("delete from manager_user_auth where user_id=" + userId);
	}

	public void deleteBatch(Long userId, Set<Long> set) {
		if (set != null && !set.isEmpty()) {
			StringBuilder sql = new StringBuilder("delete from manager_user_auth where user_id=");
			sql.append(userId);
			sql.append(" and menu_id in(");
			for (Long mId : set) {
				sql.append(mId).append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
			update(sql.toString());
		}
	}

}
