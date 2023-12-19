package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.PartnerUser;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商后台管理员
 *
 * @author admin
 *
 */
@Repository
public class PartnerUserDao extends JdbcRepository<PartnerUser> {

	public Page<PartnerUser> page(Long partnerId, String username) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select id,username,status,");
		sql.append("create_datetime,type,partner_id,original from partner_user where 1=1");
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and username=:username");
			map.put("username", username);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public PartnerUser findOne(Long id, Long partnerId) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from partner_user where id=:id");
		map.put("id", id);
		if (partnerId != null) {
			sql.append(" and partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		return findOne(sql.toString(), map);
	}

	public void changeStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update partner_user set status=:status where id=:id", map);
	}

	public void updatePwd(Long id, String salt, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("salt", salt);
		map.put("password", password);
		update("update partner_user set salt=:salt,password=:password where id=:id", map);
	}

	public boolean exist(String username, Long partnerId) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("partnerId", partnerId);
		return queryForLong("select count(*) from partner_user where username=:username and partner_id=:partnerId",
				map) > 0;
	}

	public void updatePwd2(Long id, String pwd) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("pwd", pwd);
		update("update partner_user set password2=:pwd where id=:id", map);
	}
}
