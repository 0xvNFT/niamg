package com.play.dao;

import com.play.common.utils.DateUtil;
import com.play.core.MoneyRecordType;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.SysUserMoneyHistory;
import com.play.orm.jdbc.JdbcRepository;

import java.math.BigDecimal;
import java.util.*;

/**
 * 会员金额变动表
 *
 * @author admin
 *
 */
@Repository
public class SysUserMoneyHistoryDao extends JdbcRepository<SysUserMoneyHistory> {
	public Page<SysUserMoneyHistory> orderReport(Long stationId, Long userId, Integer[] type, Date begin, Date end,
			Boolean include, boolean isMember) {
		if (begin == null || end == null) {
			return new Page<>();
		}
		StringBuilder sqlSelect = new StringBuilder("select username,create_datetime,type,ABS(money) as money");
		sqlSelect.append(",after_money,before_money,remark from sys_user_money_history where station_id=:stationId");
		sqlSelect.append(" and create_datetime >=:begin and create_datetime <:end");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("begin", begin);
		Calendar now = Calendar.getInstance();
		now.setTime(end);
		now.add(Calendar.SECOND, 1);
		map.put("end", now.getTime());
		if (include != null && include) {
			logger.info("帐变报表是否是会员:isMember:{}",isMember);
			if (isMember) {
				sqlSelect.append(" and (user_id =:userId OR recommend_id=:userId)");
			} else {
				sqlSelect.append(" and (user_id =:userId OR parent_ids like :userIdCn )");
				map.put("userIdCn", "%," + userId + ",%");
			}
		} else {
			sqlSelect.append(" and user_id =:userId");
		}
		if (type != null) {
			sqlSelect.append(" and type IN(-1,");
			Arrays.stream(type).forEach(x -> {
				sqlSelect.append(x).append(",");
			});
			sqlSelect.append("-2)");
		}
		sqlSelect.append(" order by id DESC");

		// 收入
		StringBuilder totalSMoney = new StringBuilder();
		totalSMoney.append("CASE WHEN type IN(0,");
		Arrays.stream(MoneyRecordType.values()).forEach(x -> {
			totalSMoney.append(x.isAdd() ? x.getType() + "," : "");
		});
		totalSMoney.append("999999)");
		totalSMoney.append(" THEN ABS(money) ELSE 0 END");
		// 支出
		StringBuilder totalZMoney = new StringBuilder();
		totalZMoney.append("CASE WHEN type IN(0,");
		Arrays.stream(MoneyRecordType.values()).forEach(x -> {
			totalZMoney.append(!x.isAdd() ? x.getType() + "," : "");
		});
		totalZMoney.append("999999)");
		totalZMoney.append(" THEN ABS(money) ELSE 0 END");
		List<Aggregation> aggregations = new ArrayList<>();
		aggregations.add(new Aggregation(AggregationFunction.SUM, totalSMoney.toString(), "totalSMoney"));
		aggregations.add(new Aggregation(AggregationFunction.SUM, totalZMoney.toString(), "totalZMoney"));
		logger.info("帐变报表执行的sqlSelect:{}",sqlSelect.toString());
		return queryByPage(sqlSelect.toString(), map, aggregations);
	}

	public BigDecimal findMoneyByTypes(Long stationId, Date begin, Date end, Long userId, String types) {
		StringBuilder sqlSelect = new StringBuilder("select sum(money) ");
		Map<String, Object> map = new HashMap<>();
		sqlSelect.append(" from sys_user_money_history where station_id=:stationId");
		map.put("stationId", stationId);
		sqlSelect.append(" and create_datetime>=:begin and create_datetime<:end");
		map.put("begin", begin);
		map.put("end", end);
		if (StringUtils.isNotEmpty(types)) {
			sqlSelect.append(" and type in (-999,");
			Arrays.stream(types.split(",")).forEach(x -> {
				sqlSelect.append(Integer.parseInt(x)).append(",");
			});
			sqlSelect.append("-888)");
		}
		if (userId != null) {
			sqlSelect.append(" and user_id =:userId");
			map.put("userId", userId);
		}
		return findObject(sqlSelect.toString(), map, BigDecimal.class);
	}

	public Page<SysUserMoneyHistory> adminPage(Long stationId, Long userId, Long proxyId, String types, Date begin,
			Date end, String orderId, BigDecimal minMoney, BigDecimal maxMoney, String operatorName, String agentUser,
			String remark, String bgRemark,Long recommendId) {
		StringBuilder sqlSelect = new StringBuilder("select id,username,type,before_money,ABS(money) as money,");
		Map<String, Object> map = new HashMap<>();
		sqlSelect.append("after_money,create_datetime,order_id,operator_name,remark,bg_remark");
		sqlSelect.append(" from sys_user_money_history where station_id=:stationId");
		map.put("stationId", stationId);
		sqlSelect.append(" and create_datetime>=:begin and create_datetime<:end");
		map.put("begin", begin);
		Calendar now = Calendar.getInstance();
		now.setTime(end);
		now.add(Calendar.SECOND, 1);
		map.put("end", now.getTime());
		if (minMoney != null) {
			sqlSelect.append(" and ABS(money) >= :minMoney");
			map.put("minMoney", minMoney);
		}
		if (maxMoney != null) {
			sqlSelect.append(" and ABS(money) <= :maxMoney");
			map.put("maxMoney", maxMoney);
		}
		if (StringUtils.isNotEmpty(types)) {
			sqlSelect.append(" and type in (-999,");
			Arrays.stream(types.split(",")).forEach(x -> {
				sqlSelect.append(Integer.parseInt(x)).append(",");
			});
			sqlSelect.append("-888)");
		}
		if (userId != null) {
			sqlSelect.append(" and user_id =:userId");
			map.put("userId", userId);
		}
		if (proxyId != null) {
			map.put("proxyId", "%," + proxyId + ",%");
			map.put("curUserId", proxyId);
			sqlSelect.append(" AND (parent_ids LIKE :proxyId OR user_id = :curUserId)");
		}
		if (StringUtils.isNotEmpty(orderId)) {
			sqlSelect.append(" and order_id =:orderId");
			map.put("orderId", orderId);
		}
		if (StringUtils.isNotEmpty(operatorName)) {
			sqlSelect.append(" and operator_name =:operatorName");
			map.put("operatorName", operatorName);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sqlSelect.append(" and agent_username =:agentUser");
			map.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(remark)) {
			sqlSelect.append(" and remark LIKE :remark");
			map.put("remark", "%" + remark + "%");
		}
		if (StringUtils.isNotEmpty(bgRemark)) {
			sqlSelect.append(" and bg_remark LIKE :bgRemark");
			map.put("bgRemark", "%" + bgRemark + "%");
		}
		if (recommendId != null) {
			sqlSelect.append(" and recommend_id =:recommendId");
			map.put("recommendId", recommendId);
		}
		sqlSelect.append(" order by create_datetime DESC");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "ABS(money)", "money"));
		return queryByPage(sqlSelect.toString(), map, aggs);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" delete from sys_user_money_history");
		sb.append(" where station_id=:stationId");
		Map<String, Object> map = new HashMap<>();
		sb.append(" and create_datetime <= :createTime");
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

	public SysUserMoneyHistory saveRecord(SysUserMoneyHistory record) {
		StringBuilder sql = new StringBuilder("INSERT INTO sys_user_money_history");
		sql.append("(id,partner_id,station_id,agent_name,parent_ids,recommend_id,user_id,username,");
		sql.append("money,before_money,after_money,type,create_datetime,order_id,");
		sql.append("biz_datetime,remark,bg_remark,operator_id,operator_name) VALUES ");
		sql.append("(:id,:partnerId,:stationId,:agentName,:parentIds,:recommendId,:userId,:username,");
		sql.append(":money,:beforeMoney,:afterMoney,:type,:createDatetime,:orderId,");
		sql.append(":bizDatetime,:remark,:bgRemark,:operatorId,:operatorName)");
		record.setId(queryForLong("select nextval('sys_user_money_history_id_seq')"));
		Map<String, Object> map = new HashMap<>();
		map.put("id", record.getId());
		map.put("partnerId", record.getPartnerId());
		map.put("stationId", record.getStationId());
		map.put("agentName", record.getAgentName());
		map.put("parentIds", record.getParentIds());
		map.put("recommendId", record.getRecommendId());
		map.put("userId", record.getUserId());
		map.put("username", record.getUsername());
		map.put("money", record.getMoney());
		map.put("beforeMoney", record.getBeforeMoney());
		map.put("afterMoney", record.getAfterMoney());
		map.put("type", record.getType());
		map.put("createDatetime", record.getCreateDatetime());
		map.put("orderId", record.getOrderId());
		map.put("bizDatetime", record.getBizDatetime());
		map.put("remark", record.getRemark());
		map.put("bgRemark", record.getBgRemark());
		map.put("operatorId", record.getOperatorId());
		map.put("operatorName", record.getOperatorName());
		update(sql.toString(), map);
		return record;
	}

	public BigDecimal sumDayPay(Long userId, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select sum(money)  from sys_user_money_history");
		sb.append(" where station_id=:stationId and user_id=:userId");
		sb.append(" and create_datetime>=:start and create_datetime < :end");
		Date today = new Date();
		paramMap.put("start", DateUtil.dayFirstTime(today, 0));
		paramMap.put("end", DateUtil.dayFirstTime(today, 1));
		// 对应的存款赠送
		sb.append(" and type = ").append(MoneyRecordType.DEPOSIT_GIFT_ACTIVITY.getType());
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	public int changeRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update sys_user_money_history set bg_remark=:remark");
		sb.append(" where id=:id and station_id =:stationId ");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}
}
