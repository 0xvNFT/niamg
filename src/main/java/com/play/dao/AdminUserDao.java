package com.play.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.UserType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.model.vo.AdminUserVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点管理员信息
 *
 * @author admin
 *
 */
@Repository
public class AdminUserDao extends JdbcRepository<AdminUser> {
	private static final String CACHE_KEY_USERNAME = "admin:username:";
	private static final String CACHE_KEY_ID = "admin:id:";

	public AdminUser findByUsername(String username, Long stationId) {
		AdminUser u = null;
		Long id = CacheUtil.getCache(CacheKey.ADMIN, CACHE_KEY_USERNAME + username + ":s:" + stationId, Long.class);
		if (id != null) {
			u = CacheUtil.getCache(CacheKey.ADMIN, CACHE_KEY_ID + id, AdminUser.class);
		}
		if (u == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("username", username.trim().toLowerCase());
			map.put("stationId", stationId);
			u = findOne("select * from admin_user where username=:username and station_id=:stationId", map);
			addCache(u);
		}
		return u;
	}

	public AdminUser findOneById(Long id, Long stationId) {
		if (id == null || id <= 0) {
			return null;
		}
		AdminUser u = CacheUtil.getCache(CacheKey.ADMIN, CACHE_KEY_ID + id, AdminUser.class);
		if (u == null || !u.getStationId().equals(stationId)) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("stationId", stationId);
			u = findOne("select * from admin_user where id=:id and station_id=:stationId", map);
			addCache(u);
		}
		return u;
	}

	public List<AdminUser> findByStationId(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return find("select * from admin_user where station_id=:stationId", map);
	}

	private void addCache(AdminUser u) {
		if (u != null) {
			StringBuilder nameKey = new StringBuilder(CACHE_KEY_USERNAME);
			nameKey.append(u.getUsername()).append(":s:").append(u.getStationId());
			CacheUtil.addCache(CacheKey.ADMIN, nameKey.toString(), u.getId());
			CacheUtil.addCache(CacheKey.ADMIN, CACHE_KEY_ID + u.getId(), u);
		}
	}

	public boolean exist(String username, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", username.trim().toLowerCase());
		map.put("stationId", stationId);
		return queryForInt("select count(*) from admin_user where username=:username and station_id=:stationId",
				map) > 0;
	}

	public Page<AdminUserVo> pageForManager(Long stationId, Long groupId, String username, Integer type,
			Integer minType) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select u.*,g.name as groupName from admin_user u ");
		sql.append("left join admin_user_group g on u.group_id=g.id where 1=1");
		if (stationId != null) {
			sql.append(" and u.station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (groupId != null && groupId > 0) {
			sql.append(" and u.group_id=:groupId");
			map.put("groupId", groupId);
		}
		if (type != null) {
			map.put("type", type);
			sql.append(" and u.type=:type");
		}
		if (minType != null) {
			map.put("minType", minType);
			sql.append(" and u.type>=:minType");
		}
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and u.username=:username");
			map.put("username", username.trim().toLowerCase());
		}
		sql.append(" order by u.id desc");
		return queryByPage(sql.toString(), new BeanPropertyRowMapper<>(AdminUserVo.class), map);
	}

	public Page<AdminUserVo> page(Long partnerId, Long stationId, Long groupId, String username, Integer type,
			Integer minType, Set<Long> ids) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select u.id,u.username,u.group_id,u.status,u.create_datetime,");
		sql.append("u.type,u.create_ip,u.last_login_ip,u.last_login_time,u.station_id,u.remark,u.add_money_limit");
		sql.append(",u.deposit_limit,u.withdraw_limit,u.original,g.name as groupName from admin_user u ");
		sql.append("left join admin_user_group g on u.group_id=g.id where 1=1");
		if (partnerId != null && partnerId > 0) {
			sql.append(" and u.partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" and u.station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (groupId != null && groupId > 0) {
			sql.append(" and u.group_id=:groupId");
			map.put("groupId", groupId);
		}
		if (type != null) {
			map.put("type", type);
			sql.append(" and u.type=:type");
		}
		if (minType != null) {
			map.put("minType", minType);
			sql.append(" and u.type>=:minType");
		}
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and u.username=:username");
			map.put("username", username.trim().toLowerCase());
		}
		if (ids != null && !ids.isEmpty()) {
			sql.append(" and u.id in(");
			ids.forEach(x -> {
				sql.append(x).append(",");
			});
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" )");
		}
		sql.append(" order by u.id desc");
		return queryByPage(sql.toString(), new BeanPropertyRowMapper<>(AdminUserVo.class), map);
	}

	@Override
	public int deleteById(Serializable id) {
		int i = super.deleteById(id);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
		return i;
	}

	public void changeStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		map.put("stationId", stationId);
		update("update admin_user set status=:status where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
	}

	public void updateInfo(Long id, Long groupId, Integer type, Long stationId, String remark) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("groupId", groupId);
		map.put("type", type);
		map.put("stationId", stationId);
		map.put("remark", remark);
		update("update admin_user set remark=:remark,type=:type,group_id=:groupId where id=:id and station_id=:stationId",
				map);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
	}

	public void updatePwd(AdminUser u) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", u.getId());
		map.put("salt", u.getSalt());
		map.put("password", u.getPassword());
		map.put("stationId", u.getStationId());
		update("update admin_user set salt=:salt,password=:password where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + u.getId());
	}

	public void changeAddMoneyLimit(Long id, Long stationId, BigDecimal moneyLimit) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update admin_user set add_money_limit =:moneyLimit");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("moneyLimit", moneyLimit);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
	}

	public void updateReceiptPwd(AdminUser u) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", u.getId());
		map.put("password", u.getReceiptPassword());
		map.put("stationId", u.getStationId());
		update("update admin_user set receipt_password=:password where id=:id and station_id=:stationId", map);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + u.getId());
	}

	public void changeDepositLimit(Long id, Long stationId, BigDecimal depositLimit) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update admin_user set deposit_limit=:depositLimit");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("depositLimit", depositLimit);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
	}

	public void changeWithdrawLimit(Long id, Long stationId, BigDecimal withdrawLimit) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update admin_user set withdraw_limit=:withdrawLimit");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("withdrawLimit", withdrawLimit);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.ADMIN, CACHE_KEY_ID + id);
	}

	public List<AdminUserVo> getNormalUsernameList(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("type", UserTypeEnum.ADMIN.getType());
		StringBuilder sb = new StringBuilder("select a.username,b.name as groupName,a.id from admin_user a");
		sb.append(" left join admin_user_group b on a.group_id=b.id where a.type=:type");
		sb.append(" and a.station_id=:stationId order by b.id desc,a.id desc");
		return find(sb.toString(), new BeanPropertyRowMapper<>(AdminUserVo.class), map);
	}
}
