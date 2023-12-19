package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.AdminLoginGoogleAuth;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 租户登录动态口令
 *
 * @author admin
 */
@Repository
public class AdminLoginGoogleAuthDao extends JdbcRepository<AdminLoginGoogleAuth> {

	public Page<AdminLoginGoogleAuth> page(Long stationId, String username) {
		Map<String, Object> paramMap = new HashMap<>();
		StringBuilder sql = new StringBuilder("select id,station_id,admin_username");
		sql.append(",remark,status from admin_login_google_auth where 1=1");
		if (stationId != null && stationId > 0) {
			sql.append(" and station_id=:stationId");
			paramMap.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and (admin_username='' or admin_username like :username)");
			paramMap.put("username", "%," + username + ",%");
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), paramMap);
	}

	public boolean existKey(String key) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("key", key);
		return queryForLong("select count(*) from admin_login_google_auth where key=:key", paramMap) > 0;
	}

	public boolean existUsername(String username, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", "%," + username + ",%");
		paramMap.put("stationId", stationId);
		return queryForLong(
				"select count(*) from admin_login_google_auth where admin_username like :username and station_id=:stationId",
				paramMap) > 0;
	}

	public AdminLoginGoogleAuth findPublic(Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		return findOne(
				"select * from admin_login_google_auth where (admin_username is null or admin_username='') and station_id=:stationId",
				paramMap);
	}

	public AdminLoginGoogleAuth findOne(Long stationId, String username) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("username", "%," + username + ",%");
		return findOne(
				"select * from admin_login_google_auth where admin_username like :username and station_id=:stationId",
				paramMap);
	}

	public List<String> findAllUsername(Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		return find("select admin_username from admin_login_google_auth where station_id=:stationId", paramMap,
				String.class);
	}

}
