package com.play.dao;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.play.model.PartnerUserAuth;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class PartnerUserAuthDao extends JdbcRepository<PartnerUserAuth> {

	public void deleteByUserId(Long userId) {
		update("delete from partner_user_auth where user_id=" + userId);
	}

	public void deleteBatch(Long userId, Set<Long> set) {
		if (set != null && !set.isEmpty()) {
			StringBuilder sql = new StringBuilder("delete from partner_user_auth where user_id=");
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

	public void initAuth(Long userId, Long partnerId) {
		StringBuilder sql = new StringBuilder("insert into partner_user_auth");
		sql.append(" (user_id,menu_id,partner_id) SELECT ").append(userId).append(",id,");
		sql.append(partnerId).append(" FROM partner_menu");
		update(sql.toString());
	}
}
