package com.play.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import com.play.common.utils.security.EncryptDataUtil;
import com.play.model.SysUserLogin;
import com.play.model.vo.SysUserOnlineVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import com.play.web.utils.ServletUtils;

/**
 * 存储会员最后登录信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserLoginDao extends JdbcRepository<SysUserLogin> {

	public void update(Long id, Long stationId, String ip, int terminal, String domain) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		map.put("ip", ip);
		map.put("terminal", terminal);
		map.put("domain", domain);
		map.put("status", SysUserLogin.STATUS_ONLINE_ON);
		map.put("date", new Date());
		update("update sys_user_login set last_login_datetime=:date,last_login_ip=:ip,online_status=:status,terminal=:terminal,domain=:domain where user_id=:id and station_id=:stationId",
				map);
	}

	public void udateUserOffline(Long userId, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		map.put("status", SysUserLogin.STATUS_ONLINE_OFF);
		update("update sys_user_login set online_status=:status where user_id=:userId and station_id=:stationId", map);
	}

	public List<Long> findAllOnlineIds() {
		return find("select user_id from sys_user_login where online_status=" + SysUserLogin.STATUS_ONLINE_ON,
				new SingleColumnRowMapper<>(Long.class));
	}

	public void updateOnlineStatus(List<Long> ids, int status) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		StringBuilder sql = new StringBuilder("UPDATE sys_user_login SET online_status=");
		sql.append(status);
		sql.append(" WHERE user_id=?");
		getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, ids.get(i));
			}

			@Override
			public int getBatchSize() {
				return ids.size();
			}
		});
	}

	/**
	 * 统计所属代理下会员离线信息
	 */
	public Integer countOffLineNumForProxy(Long stationId, Long proxyId, Date lastLoginTime, boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select count(*) from sys_user_login a");
		sb.append(" left join sys_user b on a.user_id=b.id");
		sb.append(" where a.station_id=:stationId");
		if (isRecommend) {// 会员推荐
			sb.append(" and (b.id=:proxyId or b.recommend_id=:proxyId)");
		} else {
			sb.append(" and (b.parent_ids like :proxyIdCn or b.id =:proxyId)");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("proxyIdCn", "%," + proxyId + ",%");
		map.put("proxyId", proxyId);
		if (lastLoginTime != null) {
			sb.append(" and (a.last_login_datetime is null or a.last_login_datetime <= :lastLoginTime)");
			map.put("lastLoginTime", lastLoginTime);
		}
		sb.append(" and a.online_status = :onlineStatus");
		map.put("onlineStatus", SysUserLogin.STATUS_ONLINE_OFF);
		return queryForInt(sb.toString(), map);
	}

	public Page<SysUserOnlineVo> onlinePage(Long stationId, String username, String realName, String lastLoginIp,
			BigDecimal minMoney, BigDecimal maxMoney, Long proxyId, Integer warn, Integer terminal, String agentUser,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select a.last_login_datetime,a.online_status,a.domain,");
		sb.append("a.last_login_ip,a.terminal,b.money,c.real_name,d.id,d.remark,d.proxy_name,");
		sb.append("d.username,d.recommend_username,d.create_datetime,d.online_warn,d.status");
		sb.append(" from sys_user_login a");
		sb.append(" left join sys_user_money b on a.user_id = b.user_id");
		sb.append(" left join sys_user_info c on a.user_id = c.user_id");
		sb.append(" left join sys_user d on a.user_id = d.id");
		sb.append(" where a.station_id =:stationId and a.online_status =:onlineStatus");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("onlineStatus", SysUserLogin.STATUS_ONLINE_ON);
		if (proxyId != null) {
			map.put("proxyId", proxyId);
			if (isRecommend) {
				if (StringUtils.isEmpty(username)) {// 包含下级，会员推荐，不能查看下下级的数据
					sb.append(" and (a.user_id =:proxyId OR d.recommend_id=:proxyId)");
				} else {
					sb.append(" and d.recommend_id =:proxyId");
				}
			} else {
				if (StringUtils.isEmpty(username)) {
					sb.append(" and (a.user_id =:proxyId OR d.parent_ids like :proxyIdCn)");
				} else {
					sb.append(" and d.parent_ids like :proxyIdCn");
				}
				map.put("proxyIdCn", "%," + proxyId + ",%");
			}
		}
		if (warn != null) {
			sb.append(" and d.online_warn =:warn");
			map.put("warn", warn);
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and d.username =:username");
			map.put("username", username);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and d.agent_user =:agentUser");
			map.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(realName)) {
			sb.append(" and c.real_name=:realName");
			map.put("realName", EncryptDataUtil.encryptData(realName));
		}
		if (minMoney != null) {
			sb.append(" and b.money >=:minMoney");
			map.put("minMoney", minMoney);
		}
		if (maxMoney != null) {
			sb.append(" and b.money <=:maxMoney");
			map.put("maxMoney", maxMoney);
		}
		if (StringUtils.isNotEmpty(lastLoginIp)) {
			sb.append(" and a.last_login_ip =:lastLoginIp");
			map.put("lastLoginIp", lastLoginIp);
		}
		if (terminal != null) {
			sb.append(" and a.terminal =:terminal");
			map.put("terminal", terminal);
		}
		// 排序
		HttpServletRequest request = ServletUtils.getRequest();
		String order = " order by d.create_datetime desc";
		if (request != null) {
			String sortName = request.getParameter("sortName");
			String sortOrder = request.getParameter("sortOrder");
			if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortOrder)) {
				if (!StringUtils.equalsAnyIgnoreCase("asc", sortOrder)) {
					sortOrder = "desc";
				}
				switch (sortName) {
				case "money":
					order = " order by b.money " + sortOrder;
					break;
				case "registerTime":
					order = " order by d.create_datetime " + sortOrder;
					break;
				case "status":
					order = " order by d.status " + sortOrder;
					break;
				case "terminal":
					order = " order by a.terminal " + sortOrder;
					break;
				default:
					break;
				}
			}
		}
		sb.append(order);
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "b.money", "totalMoney"));
		Page<SysUserOnlineVo> page = queryByPage(sb.toString(),
				new BeanPropertyRowMapper<SysUserOnlineVo>(SysUserOnlineVo.class), map, aggs);
		if (page.hasRows()) {
			for (SysUserOnlineVo l : page.getRows()) {
				l.setRealName(EncryptDataUtil.decryptData(l.getRealName()));
			}
		}
		return page;
	}

	public List<Long> getOnlineUserIdList(Long stationId, String username, String realName, String lastLoginIp,
			BigDecimal minMoney, BigDecimal maxMoney, Long proxyId, Integer warn, Integer terminal, String agentUser,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select a.user_id from sys_user_login a");
		sb.append(" left join sys_user_info c on a.user_id = c.user_id");
		sb.append(" left join sys_user d on a.user_id = d.id");
		sb.append(" where a.station_id =:stationId and a.online_status =:onlineStatus");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("onlineStatus", SysUserLogin.STATUS_ONLINE_ON);
		if (proxyId != null) {
			map.put("proxyId", proxyId);
			if (isRecommend) {
				if (StringUtils.isEmpty(username)) {// 包含下级，会员推荐，不能查看下下级的数据
					sb.append(" and (a.user_id =:proxyId OR d.recommend_id=:proxyId)");
				} else {
					sb.append(" and d.recommend_id =:proxyId");
				}
			} else {
				if (StringUtils.isEmpty(username)) {
					sb.append(" and (a.user_id =:proxyId OR d.parent_ids like :proxyIdCn)");
				} else {
					sb.append(" and d.parent_ids like :proxyIdCn");
				}
				map.put("proxyIdCn", "%," + proxyId + ",%");
			}
		}
		if (warn != null) {
			sb.append(" and d.online_warn =:warn");
			map.put("warn", warn);
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and d.username =:username");
			map.put("username", username);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and d.agent_user =:agentUser");
			map.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(realName)) {
			sb.append(" and c.real_name=:realName");
			map.put("realName", EncryptDataUtil.encryptData(realName));
		}
		if (minMoney != null) {
			sb.append(" and b.money >=:minMoney");
			map.put("minMoney", minMoney);
		}
		if (maxMoney != null) {
			sb.append(" and b.money <=:maxMoney");
			map.put("maxMoney", maxMoney);
		}
		if (StringUtils.isNotEmpty(lastLoginIp)) {
			sb.append(" and a.last_login_ip =:lastLoginIp");
			map.put("lastLoginIp", lastLoginIp);
		}
		if (terminal != null) {
			sb.append(" and a.terminal =:terminal");
			map.put("terminal", terminal);
		}
		return find(sb.toString(), map, Long.class);
	}

}
