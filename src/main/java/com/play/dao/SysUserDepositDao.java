package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.core.UserTypeEnum;
import com.play.model.SysUserDeposit;
import com.play.model.vo.StationDailyRegisterVo;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员存款总计
 *
 * @author admin
 *
 */
@Repository
public class SysUserDepositDao extends JdbcRepository<SysUserDeposit> {

	/**
	 * 统计有效会员人数
	 */
	public int countDepositNum(Long stationId, boolean isDeposit) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_user_deposit");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where station_id =:stationId ");
		params.put("stationId", stationId);
		if (isDeposit) {
			sql.append(" and times > 0");
		}
		return queryForInt(sql.toString(), params);
	}

	/**
	 * 获取站点代理每日首充二充数据
	 */
	public List<StationDailyRegisterVo> getDailyDepositByProxyName(Long stationId, Date begin, Date end,
			String proxyName, boolean isFirst, String agentUser) {
		String queryData = isFirst ? "first_time" : "sec_time";
		StringBuilder sql = new StringBuilder("select");
		sql.append(" to_char(ad.").append(queryData).append(", 'yyyy-MM-dd') as stat_date,");
		sql.append(" COUNT(DISTINCT ad.user_id) as first_deposit_num");
		sql.append(" from sys_user_deposit ad left join sys_user a on a.id = ad.user_id");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where a.station_id=:stationId");
		if (StringUtils.isNotEmpty(proxyName)) {
			sql.append(" and a.parent_names like :proxyName");
			params.put("proxyName", "%," + proxyName + ",%");
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sql.append(" and a.agent_username = :agentUser");
			params.put("agentUser", "agentUser");
		}
		sql.append(" and ad.").append(queryData).append(" notnull");
		params.put("stationId", stationId);
		sql.append(" and ad.").append(queryData).append(" >=:begin and ad.").append(queryData).append(" <=:end");
		params.put("begin", begin);
		params.put("end", end);
		sql.append(" GROUP BY to_char(ad.").append(queryData).append(", 'yyyy-MM-dd') ");
		return find(sql.toString(), new BeanPropertyRowMapper<StationDailyRegisterVo>(StationDailyRegisterVo.class),
				params);
	}

	/**
	 * 获取代理首充二充数据
	 */
	public int countDepositByProxyName(Long stationId, Date begin, Date end, String proxyName, boolean isFirst) {
		String queryData = isFirst ? "first_time" : "sec_time";
		StringBuilder sql = new StringBuilder("select");
		sql.append(" COUNT(DISTINCT ad.user_id)");
		sql.append(" from sys_user_deposit ad left join sys_user a on a.id = ad.user_id");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where a.station_id=:stationId");
		sql.append(" and a.parent_names like :proxyName");
		sql.append(" and ad.").append(queryData).append(" notnull");
		params.put("stationId", stationId);
		params.put("proxyName", "%," + proxyName + ",%");
		sql.append(" and ad.").append(queryData).append(" >=:begin and ad.").append(queryData).append(" <=:end");
		params.put("begin", begin);
		params.put("end", end);
		return queryForInt(sql.toString(), params);
	}

	/**
	 * 获取指定代理或会员的首充二充数据
	 * @param stationId
	 * @param begin
	 * @param end
	 * @param userId
	 * @param isFirst
	 * @return
	 */
	public int countDepositByUser(Long stationId, Date begin, Date end, Long userId, boolean isFirst) {
		String queryData = isFirst ? "first_time" : "sec_time";
		StringBuilder sql = new StringBuilder("select");
		sql.append(" COUNT(DISTINCT ad.user_id)");
		sql.append(" from sys_user_deposit ad left join sys_user a on a.id = ad.user_id");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where a.station_id=:stationId");
		sql.append(" and a.id =:userId");
		sql.append(" and ad.").append(queryData).append(" notnull");
		params.put("stationId", stationId);
		params.put("userId", userId);
		sql.append(" and ad.").append(queryData).append(" >=:begin and ad.").append(queryData).append(" <=:end");
		params.put("begin", begin);
		params.put("end", end);
		return queryForInt(sql.toString(), params);
	}

	/**
	 * 统计最近十分钟之内首充人数
	 */
	public Integer getCountOfFirstDeposit(Long stationId, Date begin) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_user_deposit");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		sql.append(" and first_time notnull  and first_time >=:begin");
		params.put("begin", begin);
		return queryForInt(sql.toString(), params);
	}


	/**
	 * 统计最近一段时间内会员首充次数
	 * @param stationId
	 * @param begin
	 * @return
	 */
	public Integer getLastCountOfDeposit(Long stationId, Date begin,Long userId) {
		StringBuilder sql = new StringBuilder("select count(*) from sys_user_deposit");
		Map<String, Object> params = new HashMap<>();
		sql.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		sql.append(" and first_time notnull and first_time >=:begin and user_id =:userId");
		params.put("begin", begin);
		params.put("userId", userId);
		return queryForInt(sql.toString(), params);
	}

	@Override
	public SysUserDeposit findOne(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("select * from sys_user_deposit");
		sb.append(" where station_id =:stationId and user_id =:userId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		return findOne(sb.toString(), map);
	}

	public SysUserDeposit findOne2(Long userId, Long stationId,boolean isFirst,Date begin,Date end) {
		String queryData = isFirst ? "first_time" : "sec_time";
		StringBuilder sb = new StringBuilder("select * from sys_user_deposit");
		sb.append(" where station_id =:stationId and user_id =:userId");
		sb.append(" and ").append(queryData).append(" notnull");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		sb.append(" and ").append(queryData).append(" >=:begin and ").append(queryData).append(" <=:end");
		map.put("begin", begin);
		map.put("end", end);
		return findOne(sb.toString(), map);
	}

	public SysUserDeposit findOneByUsername(String username, Long stationId,Date begin,Date end,boolean isFirst) {
		String queryData = isFirst ? "first_time" : "sec_time";
		StringBuilder sb = new StringBuilder("select * from sys_user_deposit");
		sb.append(" ad left join sys_user a on a.id = ad.user_id");
		sb.append(" and ").append(queryData).append(" notnull");
		Map<String, Object> map = new HashMap<>();
		sb.append(" where a.station_id=:stationId");
		map.put("stationId", stationId);
		sb.append(" and a.username =:username");
		map.put("username", username);
		sb.append(" and ").append(queryData).append(" >=:begin and ").append(queryData).append(" <=:end");
		map.put("begin", begin);
		map.put("end", end);
		return findOne(sb.toString(), map);
	}

	/**
	 * 统计首充人数
	 */
	public Integer countTeamFirstDepositTodayNum(Long stationId, Long userId, Date begin, Date end, Integer type,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select count(*) from sys_user a");
		sb.append(" left join sys_user_deposit b on a.id = b.user_id ");
		sb.append(" where a.station_id =:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (userId != null) {
			params.put("userId", userId);
			if (isRecommend) {
				sb.append(" and(a.id =:userId OR a.recommend_id=:userId)");
			} else {
				sb.append(" and (a.id=:userId or a.parent_ids like :userIdCn)");
				params.put("userIdCn", "%," + userId + ",%");
			}
		}
		if (type != null) {
			sb.append(" and a.type =:type");
			params.put("type", type);
		} else {
			// 试玩不统计
			sb.append(" and a.type !=:type");
			params.put("type", UserTypeEnum.PROXY.getType());
		}
		if (begin != null) {
			sb.append(" and b.first_time >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and b.first_time <= :end");
			params.put("end", end);
		}
		return queryForInt(sb.toString(), params);
	}

	/**
	 * 获取站点代理每日首充二充数据
	 */
	public List<StationDailyRegisterVo> getDailyDepositByProxy(Long stationId, Date begin, Date end, Long proxyId,
			Integer depositNum, String agentName, boolean isRecommend) {
		String queryData = depositNum == 1 ? "first_time" : (depositNum == 2 ? "sec_time" : "third_time");
		StringBuilder sql = new StringBuilder("select");
		sql.append(" to_char(ad.").append(queryData).append(", 'yyyy-MM-dd') as stat_date,");
		sql.append(" COUNT(DISTINCT ad.user_id) as first_deposit");
		sql.append(" from sys_user_deposit ad left join sys_user a on a.id = ad.user_id");
		sql.append(" where a.station_id=:stationId");
		Map<String, Object> params = new HashMap<>();
		if (proxyId != null) {
			if (isRecommend) {
				sql.append(" and a.recommend_id=:proxyId");
				params.put("proxyId", proxyId);
			} else {
				sql.append(" and a.parent_ids like :proxyIds");
				params.put("proxyIds", "%," + proxyId + ",%");
			}
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sql.append(" and a.agent_name = :agentName");
			params.put("agentName", "agentName");
		}
		sql.append(" and ad.").append(queryData).append(" notnull");
		params.put("stationId", stationId);
		sql.append(" and ad.").append(queryData).append(" >=:begin and ad.").append(queryData).append(" <=:end");
		params.put("begin", begin);
		params.put("end", end);
		sql.append(" GROUP BY stat_date");
		return find(sql.toString(), new BeanPropertyRowMapper<StationDailyRegisterVo>(StationDailyRegisterVo.class),
				params);
	}

}
