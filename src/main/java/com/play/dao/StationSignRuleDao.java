package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationSignRule;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 会员签到赠送积分、金额规则表
 *
 * @author admin
 *
 */
@Repository
public class StationSignRuleDao extends JdbcRepository<StationSignRule> {
	public Page<StationSignRule> getRulePage(Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		String sql = "select * from station_sign_rule where station_id = :stationId order by days ";
		return super.queryByPage(sql, paramMap);
	}

	public List<StationSignRule> findRuleList(Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		String sql = "select * from station_sign_rule where station_id = :stationId order by days ";
		return super.find(sql, paramMap);
	}

	public List<StationSignRule> findRules(String degreeIds, String groupIds, Long stationId) {
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "degreeIds", degreeIds, "groupsIds", groupIds);
		String sql = "select * from station_sign_rule where ";
		sql += "string_to_array(substring(degree_ids, 2, length(degree_ids)- 2),',')::int[] && string_to_array(substring(:degreeIds, 2, length(:degreeIds)- 2),',')::int[] ";
		sql += "and ";
		sql += "string_to_array(substring(group_ids, 2, length(group_ids)- 2),',')::int[] && string_to_array(substring(:groupsIds, 2, length(:groupsIds)- 2),',')::int[]";
		return super.find(sql, paramMap);
	}

	public List<StationSignRule> findOneRule(Long degreeIds, Long groupIds, Long stationId) {
	    Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "degreeIds", degreeIds, "groupIds", groupIds);
        String sql = "select * from station_sign_rule where station_id = :stationId and (degree_ids like concat('%',:degreeIds,'%') or degree_ids is null)" +
				" and (group_ids like concat('%',:groupIds,'%') or group_ids is null)";
        return super.find(sql, paramMap);
    }

	public StationSignRule getSuitableSign(Long stationId, Integer isReset, Long degreeId, Long groupId,
			Integer todayDeposit, Long days) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from station_sign_rule where station_id = :stationId ");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (isReset != null) {
			sql.append(" and is_reset=:isReset");
			params.put("isReset", isReset);
		}
		if (degreeId != null && degreeId > 0) {
			sql.append(" and degree_ids like :degreeIds");
			params.put("degreeIds", "%," + degreeId + ",%");
		}
		if (groupId != null && groupId > 0) {
			sql.append(" and group_ids like :groupIds");
			params.put("groupIds", "%," + groupId + ",%");
		}
		if (todayDeposit != null) {
			sql.append(" and today_deposit=:todayDeposit");
			params.put("todayDeposit", todayDeposit);
		}
		if (days != null) {
			sql.append(" and days =:days");
			params.put("days", days);
		}
		sql.append(" order By days DESC limit 1");
		return findOne(sql.toString(), params);
	}
}
