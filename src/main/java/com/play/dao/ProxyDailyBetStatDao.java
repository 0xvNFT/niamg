package com.play.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.ProxyDailyBetStat;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class ProxyDailyBetStatDao extends JdbcRepository<ProxyDailyBetStat> {
	private static final String insertSql = "INSERT INTO proxy_daily_bet_stat (station_id,user_id,username,stat_date,status,"
			+ "lot_bet,lot_rollback,live_bet,live_rollback,egame_bet,egame_rollback,sport_bet,sport_rollback,chess_bet,"
			+ "chess_rollback,esport_bet,esport_rollback,fishing_bet,fishing_rollback,draw_num)"
			+ " VALUES (:stationId,:userId,:username,:statDate,:status,:lotBet,:lotRollback,:liveBet,:liveRollback,"
			+ ":egameBet,:egameRollback,:sportBet,:sportRollback,:chessBet,:chessRollback,:esportBet,:esportRollback,"
			+ ":fishingBet,:fishingRollback,:drawNum)";

	public void saveOrUpdate(ProxyDailyBetStat pdb) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", pdb.getStationId());
		map.put("userId", pdb.getUserId());
		map.put("username", pdb.getUsername());
		map.put("statDate", pdb.getStatDate());
		map.put("status", pdb.getStatus());
		map.put("lotBet", getValue(pdb.getLotBet()));
		map.put("lotRollback", getValue(pdb.getLotRollback()));
		map.put("liveBet", getValue(pdb.getLiveBet()));
		map.put("liveRollback", getValue(pdb.getLiveRollback()));
		map.put("egameBet", getValue(pdb.getEgameBet()));
		map.put("egameRollback", getValue(pdb.getEgameRollback()));
		map.put("sportBet", getValue(pdb.getSportBet()));
		map.put("sportRollback", getValue(pdb.getSportRollback()));
		map.put("chessBet", getValue(pdb.getChessBet()));
		map.put("chessRollback", getValue(pdb.getChessRollback()));
		map.put("esportBet", getValue(pdb.getEsportBet()));
		map.put("esportRollback", getValue(pdb.getEsportRollback()));
		map.put("fishingBet", getValue(pdb.getFishingBet()));
		map.put("fishingRollback", getValue(pdb.getFishingRollback()));
		map.put("drawNum", getValue(pdb.getDrawNum()));
		update(insertSql, map);
	}

	private BigDecimal getValue(BigDecimal val) {
		if (val == null || val.compareTo(BigDecimal.ZERO) < 0) {
			return BigDecimal.ZERO;
		}
		return val;
	}

	public Page<ProxyDailyBetStat> page(String username, Date start, Date end, Integer status, String operator,
			Long stationId) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from proxy_daily_bet_stat where station_id=:stationId");
		map.put("stationId", stationId);
		if (StringUtils.isNotEmpty(username)) {
			map.put("username", username);
			sql.append(" and username=:username");
		}
		if (start != null) {
			map.put("start", start);
			sql.append(" and stat_date>=:start");
		}
		if (end != null) {
			map.put("end", end);
			sql.append(" and stat_date<=:end");
		}
		if (status != null) {
			map.put("status", status);
			sql.append(" and status=:status");
		}
		if (StringUtils.isNotEmpty(operator)) {
			map.put("operator", operator);
			sql.append(" and operator=:operator");
		}
		sql.append(" order by id desc");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "lot_bet", "lotBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "live_bet", "liveBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "egame_bet", "egameBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "chess_bet", "chessBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "sport_bet", "sportBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "esport_bet", "esportBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "fishing_bet", "fishingBetAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then lot_rollback else 0 end",
				"lotRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then live_rollback else 0 end",
				"liveRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then egame_rollback else 0 end",
				"egameRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then chess_rollback else 0 end",
				"chessRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then sport_rollback else 0 end",
				"sportRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then esport_rollback else 0 end",
				"esportRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "case when status=2 then fishing_rollback else 0 end",
				"fishingRollbackAll"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "draw_num", "drawNumAll"));
		return super.queryByPage(sql.toString(), map, aggs);
	}

	public boolean updateSuccess(Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", 2);
		map.put("id", id);
		map.put("oldStatus", 1);
		return update("update proxy_daily_bet_stat set status=:status where id=:id and status=:oldStatus", map) == 1;
	}

	public boolean updateSuccess4Manual(ProxyDailyBetStat pdb) {
		StringBuilder sb = new StringBuilder("update proxy_daily_bet_stat set");
		sb.append(" lot_rollback=:lotRollback,live_rollback=:liveRollback,egame_rollback=:egameRollback");
		sb.append(",esport_rollback=:esportRollback,fishing_rollback=:fishingRollback");
		sb.append(",chess_rollback=:chessRollback,sport_rollback=:sportRollback,draw_num=:drawNum,status=:status");
		sb.append(",operator=:operator,operator_id=:opId,update_time=:utime where id=:id and status=:unback");
		Map<String, Object> map = new HashMap<>();
		map.put("status", ProxyDailyBetStat.status_do);
		map.put("unback", ProxyDailyBetStat.status_unback);
		map.put("id", pdb.getId());
		map.put("lotRollback", getValue(pdb.getLotRollback()));
		map.put("liveRollback", getValue(pdb.getLiveRollback()));
		map.put("egameRollback", getValue(pdb.getEgameRollback()));
		map.put("chessRollback", getValue(pdb.getChessRollback()));
		map.put("sportRollback", getValue(pdb.getSportRollback()));
		map.put("esportRollback", getValue(pdb.getEsportRollback()));
		map.put("fishingRollback", getValue(pdb.getFishingRollback()));
		map.put("drawNum", getValue(pdb.getDrawNum()));
		map.put("operator", pdb.getOperator());
		map.put("opId", pdb.getOperatorId());
		map.put("utime", new Date());
		return update(sb.toString(), map) == 1;
	}

	public boolean updateFailed(Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", 1);
		map.put("id", id);
		map.put("oldStatus", 2);
		return update("update proxy_daily_bet_stat set status=:status where id=:id and status=:oldStatus", map) == 1;
	}

	public List<ProxyDailyBetStat> findNeedRollback(Date end, List<Long> stationIdList) {
		Map<String, Object> map = new HashMap<>();
		map.put("end", end);
		StringBuilder sb = new StringBuilder("select * from proxy_daily_bet_stat");
		sb.append(" where status=1 and stat_date<:end and station_id in(");
		for (Long id : stationIdList) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return find(sb.toString(), map);
	}
}
