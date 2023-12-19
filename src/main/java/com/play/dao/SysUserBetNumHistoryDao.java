package com.play.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.core.BetNumTypeEnum;
import com.play.model.SysUserBetNumHistory;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;

/**
 * 会员打码量变动记录
 *
 * @author admin
 *
 */
@Repository
public class SysUserBetNumHistoryDao extends JdbcRepository<SysUserBetNumHistory> {
	public Page<SysUserBetNumHistory> adminPage(Long stationId, Date begin, Date end, Integer type, Long userId,
			Long proxyId, String agentUser) {
		StringBuilder sb = new StringBuilder("select username,type,before_num,bet_num,after_num,create_datetime,");
		sb.append("before_draw_need,after_draw_need,draw_need,operator_name,remark,order_id");
		sb.append(" from sys_user_bet_num_history where station_id=:stationId");
		sb.append(" and create_datetime >= :begin and create_datetime <:end");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("begin", begin);
		Calendar now = Calendar.getInstance();
		now.setTime(end);
		now.add(Calendar.SECOND, 1);
		params.put("end", now.getTime());
		if (type != null && type != 0) {
			sb.append(" and type =:type");
			params.put("type", type);
		}
		if (userId != null) {
			sb.append(" and user_id =:userId");
			params.put("userId", userId);
		}
		if (proxyId != null) {
			sb.append(" and (parent_ids like :parentIds or user_id=:proxyId)");
			params.put("parentIds", "%," + proxyId + ",%");
			params.put("proxyId", proxyId);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and agent_username=:agentUser");
			params.put("agentUser", agentUser);
		}
		sb.append(" order by id DESC");
		List<Aggregation> aggregations = new ArrayList<>();
		StringBuilder as = new StringBuilder("case when type in(");
		for (BetNumTypeEnum t : BetNumTypeEnum.getBetGameType()) {
			as.append(t.getType()).append(",");
		}
		as.deleteCharAt(as.length() - 1);
		as.append(") then bet_num else 0 end");
		aggregations.add(new Aggregation(AggregationFunction.SUM, as.toString(), "betNumCount"));
		return queryByPage(sb.toString(), params, aggregations);
	}

	public BigDecimal getBetNumForUser(Date begin, Date end, Long userId, Long stationId, Integer... type) {
		if (userId == null) {
			return BigDecimal.ZERO;
		}
		StringBuilder sb = new StringBuilder("select sum(bet_num) bet_num FROM sys_user_bet_num_history");
		sb.append(" where station_id=:stationId and user_id = :userId");
		sb.append(" and create_datetime >= :begin and create_datetime <=:end");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("begin", begin);
		params.put("end", end);
		if (type != null && type.length > 0) {
			sb.append(" and type in(");
			for (Integer t : type) {
				sb.append(t).append(",");
			}
			sb.append("9999)");
		}
		BigDecimal betNum = super.findObject(sb.toString(), params, BigDecimal.class);
		if (betNum == null) {
			return BigDecimal.ZERO;
		} else {
			return betNum;
		}
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" delete from sys_user_bet_num_history");
		sb.append(" where station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		sb.append(" and create_datetime <= :createTime");
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

	public void saveHistory(SysUserBetNumHistory his) {
		StringBuilder sql = new StringBuilder("INSERT INTO sys_user_bet_num_history");
		sql.append("(partner_id,station_id,agent_name,proxy_id,proxy_name,parent_ids,");
		sql.append("user_id,username,bet_num,before_num,after_num,type,remark,operator_id");
		sql.append(",operator_name,create_datetime,order_id,draw_need,before_draw_need,");
		sql.append("after_draw_need,biz_datetime) values(:partnerId,:stationid,:agentName,:proxyId,");
		sql.append(":proxyName,:parentIds,:userId,:username,:betNum,:beforeNum,:afterNum,:type,:remark,");
		sql.append(":operatorId,:operatorName,:createDatetime,:orderId,:drawNeed,:beforeDrawNeed,");
		sql.append(":afterDrawNeed,:bizDatetime)");
		Map<String, Object> map = new HashMap<>();
		map.put("partnerId", his.getPartnerId());
		map.put("stationid", his.getStationId());
		map.put("agentName", his.getAgentName());
		map.put("proxyId", his.getProxyId());
		map.put("proxyName", his.getProxyName());
		map.put("parentIds", his.getParentIds());
		map.put("userId", his.getUserId());
		map.put("username", his.getUsername());
		map.put("betNum", his.getBetNum());
		map.put("beforeNum", his.getBeforeNum());
		map.put("afterNum", his.getAfterNum());
		map.put("type", his.getType());
		map.put("remark", his.getRemark());
		map.put("operatorId", his.getOperatorId());
		map.put("operatorName", his.getOperatorName());
		map.put("createDatetime", his.getCreateDatetime());
		map.put("orderId", his.getOrderId());
		map.put("drawNeed", his.getDrawNeed());
		map.put("beforeDrawNeed", his.getBeforeDrawNeed());
		map.put("afterDrawNeed", his.getAfterDrawNeed());
		map.put("bizDatetime", his.getBizDatetime());
		update(sql.toString(), map);
	}

}
