package com.play.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.common.utils.DateUtil;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.model.StationKickbackRecord;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;

/**
 * 会员反水记录表，按日投注和游戏类型来反水
 *
 * @author admin
 *
 */
@Repository
public class StationKickbackRecordDao extends JdbcRepository<StationKickbackRecord> {

	public Page<StationKickbackRecord> page(Long stationId, String username, Date start, Date end, Integer betType,
			Integer status, String realName) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (start == null) {
			start = new Date();
		}
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 1);
		}
		map.put("startDate", start);
		map.put("endDate", end);
		map.put("username", username);
		map.put("betType", betType);
		map.put("stationId", stationId);
		map.put("status", status);
		StringBuilder sb = new StringBuilder("select a.*,b.real_name from station_kickback_record a");
		sb.append(" left join sys_user_info b on a.user_id = b.user_id");
		sb.append(" where a.station_id=:stationId and a.bet_date>=:startDate and a.bet_date<:endDate");
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and a.username=:username");
		}
		if (StringUtils.isNotEmpty(realName)) {
			sb.append(" and b.real_name=:realName");
			map.put("realName", EncryptDataUtil.encryptData(realName));
		}
		if (betType != null && betType > 0) {
			sb.append(" and a.bet_type=:betType");
		}
		if (status != null && status > 0) {
			sb.append(" and a.status=:status");
		}
		sb.append(" order by a.bet_date desc,bet_money desc");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		String condition = "case when a.status = " + StationKickbackRecord.STATUS_ROLL
				+ " then a.kickback_money else 0 end";
		aggs.add(new Aggregation(AggregationFunction.SUM, condition, "kickbackMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "a.bet_money", "betMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "a.win_money", "winMoney"));
		Page<StationKickbackRecord> page = super.queryByPage(sb.toString(), map, aggs);
		if (page.hasRows()) {
			for (StationKickbackRecord r : page.getRows()) {
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return page;
	}

	public StationKickbackRecord findOne(Date betDate, String username, Integer betType, Long stationId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("select * from station_kickback_record");
		sb.append(" where station_id=:stationId and bet_date=:betDate and username=:username");
		sb.append(" and bet_type=:betType");
		map.put("betDate", betDate);
		map.put("username", username);
		map.put("betType", betType);
		map.put("stationId", stationId);
		return findOne(sb.toString(), map);
	}

	public int updateStatus(Date betDate, String username, Integer betType, Long stationId, int srcStatus,
			int newStatus, String operator, Long operatorId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("update station_kickback_record");
		sb.append(" set status=:newStatus,operator=:operator,operator_id=:operatorId");
		sb.append(" where bet_date=:betDate and username=:username and bet_type=:betType");
		sb.append(" and station_id=:stationId and status=:srcStatus ");
		map.put("betDate", betDate);
		map.put("username", username);
		map.put("betType", betType);
		map.put("srcStatus", srcStatus);
		map.put("newStatus", newStatus);
		map.put("stationId", stationId);
		map.put("operator", operator);
		map.put("operatorId", operatorId);
		return update(sb.toString(), map);
	}

	public int updateBackInfo(StationKickbackRecord r, int srcStatus) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("betDate", r.getBetDate());
		map.put("username", r.getUsername());
		map.put("betType", r.getBetType());
		map.put("kickbackMoney", r.getKickbackMoney());
		map.put("kickbackRate", r.getKickbackRate());
		map.put("kickbackDesc", r.getKickbackDesc());
		map.put("drawNeed", r.getDrawNeed());
		map.put("newStatus", r.getStatus());
		map.put("srcStatus", srcStatus);
		map.put("stationId", r.getStationId());
		map.put("operator", r.getOperator());
		map.put("operatorId", r.getOperatorId());
		StringBuilder sb = new StringBuilder("update station_kickback_record");
		sb.append(" set status=:newStatus,operator=:operator,operator_id=:operatorId,kickback_desc=:kickbackDesc");
		sb.append(",kickback_money=:kickbackMoney,kickback_rate=:kickbackRate,draw_need=:drawNeed");
		sb.append(" where bet_date=:betDate and username=:username and bet_type=:betType");
		sb.append(" and station_id=:stationId and status=:srcStatus ");
		return update(sb.toString(), map);
	}

	public List<StationKickbackRecord> findByIds(List<Long> ids, Date startDate, Date endDate, Long stationId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select * from station_kickback_record");
		sb.append(" where bet_date>=:startDate and bet_date<:endDate and id in(");
		for (Long id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return find(sb.toString(), map);
	}

	public void updateBetMoney(StationKickbackRecord r) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("betDate", r.getBetDate());
		map.put("username", r.getUsername());
		map.put("betType", r.getBetType());
		map.put("betMoney", r.getBetMoney());
		map.put("realBetNum", r.getRealBetNum());
		map.put("winMoney", r.getWinMoney());
		map.put("stationId", r.getStationId());
		StringBuilder sql = new StringBuilder("update station_kickback_record");
		sql.append(" set bet_money=:betMoney,win_money=:winMoney,real_bet_num=:realBetNum");
		sql.append(" where station_id=:stationId and bet_date=:betDate and username=:username");
		sql.append(" and bet_type=:betType");
		update(sql.toString(), map);
	}

	public List<StationKickbackRecord> findNeedKickback(Date start, Date end, Long stationId) {
		StringBuilder sb = new StringBuilder("select * from station_kickback_record");
		sb.append(" where station_id=:stationId  and bet_date>=:startDate");
		sb.append(" and bet_date<:endDate and status=:status");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("stationId", stationId);
		map.put("startDate", start);
		map.put("endDate", end);
		map.put("status", StationKickbackRecord.STATUS_NOT_ROLL);
		return find(sb.toString(), map);
	}

	public List<StationKickbackRecord> exportList(String username, Date start, Date end, Integer betType,
			Integer status, Long stationId, String realName) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (start == null) {
			start = new Date();
		}
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 1);
		}
		map.put("startDate", start);
		map.put("endDate", end);
		map.put("username", username);
		map.put("betType", betType);
		map.put("stationId", stationId);
		map.put("status", status);
		StringBuilder sb = new StringBuilder("select a.*,b.real_name from station_kickback_record a");
		sb.append(" left join sys_user_info b on  a.user_id = b.user_id");
		sb.append(" where a.station_id=:stationId and a.bet_date>=:startDate and a.bet_date<:endDate");
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and a.username=:username");
		}
		if (StringUtils.isNotEmpty(realName)) {
			sb.append(" and b.real_name=:realName");
			map.put("realName", EncryptDataUtil.encryptData(realName));
		}
		if (betType != null && betType > 0) {
			sb.append(" and a.bet_type=:betType");
		}
		if (status != null && status > 0) {
			sb.append(" and a.status=:status");
		}
		sb.append(" order by a.bet_date desc,bet_money desc");
		List<StationKickbackRecord> list = super.find(sb.toString(), map);
		if (!list.isEmpty()) {
			for (StationKickbackRecord r : list) {
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return list;
	}
}
