package com.play.dao;

import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.ThirdTransLog;
import com.play.orm.jdbc.JdbcRepository;

import java.util.*;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class ThirdTransLogDao extends JdbcRepository<ThirdTransLog> {

	public Page<ThirdTransLog> page(String username, Long userId, Integer platform, Long stationId, Integer status,
			Integer type, Date start, Date end) {
		StringBuilder sql = new StringBuilder("select * from third_trans_log where station_id=:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and username=:username");
			map.put("username", username);
		}
		if (userId != null) {
			sql.append(" and user_id=:userId");
			map.put("userId", userId);
		}
		if (platform != null) {
			sql.append(" and platform=:platform");
			map.put("platform", platform);
		}
		if (status != null) {
			sql.append(" and status=:status");
			map.put("status", status);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		if (start != null) {
			sql.append(" and create_datetime>=:start");
			map.put("start", start);
		}
		if (end != null) {
			sql.append(" and create_datetime<=:end");
			map.put("end", end);
		}
		sql.append(" order by id desc");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, " case when status = 3 then 1 else 0 end ", "unkonwCount"));
		aggs.add(new Aggregation(AggregationFunction.SUM, " case when status=2 then money else 0 end ", "transMoney"));
		return queryByPage(sql.toString(), map, aggs);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder("delete from third_trans_log");
		sb.append(" where station_id =:stationId and create_datetime <= :createTime");
		Map<String, Object> map = new HashMap<>();
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);

	}

	public boolean updateFailed(Long id, Long stationId, String msg) {
		StringBuilder sb = new StringBuilder("update third_trans_log set status=:failed,msg=:msg");
		sb.append(",update_datetime=:udt where id=:id and station_id=:stationId and status=:unkonw");
		Map<String, Object> map = new HashMap<>();
		map.put("failed", ThirdTransLog.TRANS_STATUS_FAIL);
		map.put("unkonw", ThirdTransLog.TRANS_STATUS_UNKNOW);
		map.put("id", id);
		map.put("stationId", stationId);
		if (StringUtils.length(msg) > 500) {
			map.put("msg", msg.substring(0, 500));
		} else {
			map.put("msg", msg);
		}
		map.put("udt", new Date());
		return update(sb.toString(), map) == 1;
	}

	public boolean updateSuccess(ThirdTransLog log) {
		StringBuilder sb = new StringBuilder("update third_trans_log set status=:success");
		sb.append(",after_money=:after,update_datetime=:udt");
		sb.append(" where id=:id and station_id=:stationId and status=:unkonw");
		Map<String, Object> map = new HashMap<>();
		map.put("success", ThirdTransLog.TRANS_STATUS_SUCCESS);
		map.put("unkonw", ThirdTransLog.TRANS_STATUS_UNKNOW);
		map.put("id", log.getId());
		map.put("stationId", log.getStationId());
		map.put("after", log.getAfterMoney());
		map.put("udt", log.getUpdateDatetime());
		return update(sb.toString(), map) == 1;
	}

	public List<ThirdTransLog> findNeedCheck(Date startTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("unkonw", ThirdTransLog.TRANS_STATUS_UNKNOW);
		map.put("start", startTime);
		return find("select * from third_trans_log where status=:unkonw and create_datetime>:start limit 20", map);
	}
}
