package com.play.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.core.UserTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.StationConfigEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.MnyDrawRecord;
import com.play.model.vo.DailyMoneyVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import com.play.web.utils.StationConfigUtil;

/**
 * 用户提款记录
 *
 * @author admin
 *
 */
@Repository
public class MnyDrawRecordDao extends JdbcRepository<MnyDrawRecord> {
	private static final String CACHE_PREF = "admin:untreated:draw:";

	public Integer getCountOfUntreated(Long stationId) {
		String key = CACHE_PREF + stationId;
		Integer i = CacheUtil.getCache(CacheKey.STATISTIC, key, Integer.class);
		if (i != null) {
			return i;
		}
		StringBuilder sql_sb = new StringBuilder("SELECT count(1) FROM mny_draw_record");
		sql_sb.append(" WHERE lock_flag=:lock AND station_id=:stationId AND status =:status");
		sql_sb.append("  and create_datetime>=:begin and create_datetime<=:end ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin", DateUtils.addDays(new Date(), -1));
		paramMap.put("end", new Date());
		paramMap.put("stationId", stationId);
		paramMap.put("status", MnyDepositRecord.STATUS_UNDO);
		paramMap.put("lock", MnyDepositRecord.LOCK_FLAG_UNLOCK);
		i = super.queryForInt(sql_sb.toString(), paramMap);
		CacheUtil.addCache(CacheKey.STATISTIC, key, i, 60);
		return i;
	}

	public long countWithdrawCount(Long userId, Date time, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("time", time);
		paramMap.put("stationId", stationId);
		return queryForLong(
				"select count(*) from mny_draw_record where station_id=:stationId and create_datetime>=:time and user_id=:userId and user_type != 150",
				paramMap);
	}

	public Page<MnyDrawRecord> page(Long stationId, Date begin, Date end, Long userId, Integer status, int lock,
			Integer type, Long proxyId, String degreeIds, boolean depositSort, String pay, BigDecimal minMoney,
			BigDecimal maxMoney, String opUsername, Integer checkStatus, String orderId, Long payId,
			Integer checkWithDrawOrderStatus, String remark, String agentUser, String bankName, Integer withdrawNum,
			Long recommendId, Integer drawType,Integer secondReview) {
		StringBuilder sb = new StringBuilder("select a.*,degree.name as degree_name,b.proxy_name, ");
		sb.append(" case when (draw.first_time notnull and a.create_datetime!=draw.first_time)");
		sb.append(" then false else true end as is_first,");
		sb.append(" case when (b.online_warn=1 or b.online_warn isnull) then false else true end as is_warning");
		sb.append(" from mny_draw_record a ");
		Map<String, Object> params = new HashMap<>();
		sb.append(" left join sys_user b on a.user_id = b.id");
		sb.append(" left join sys_user_degree degree on b.degree_id = degree.id");
		sb.append(" left join sys_user_withdraw draw on a.user_id = draw.user_id");
		if (withdrawNum != null) {
			sb.append(" left join sys_user_withdraw d on a.user_id = d.user_id");
		}
		sb.append(" where a.station_id=:stationId and user_type != 150");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", begin);

		}
		if (drawType != null) {
			sb.append(" and a.type=:drawType");
			params.put("drawType", drawType);
		}
		if (secondReview != null) {
			sb.append(" and a.second_review=:secondReview");
			params.put("secondReview", secondReview);
		}
		if (end != null) {
			sb.append(" and a.create_datetime < :end");
			params.put("end", DateUtils.addSeconds(end, 1));
		}
		if (userId != null) {
			sb.append(" and a.user_id=:userId");
			params.put("userId", userId);
		}
		if (recommendId != null) {
			sb.append(" and a.recommend_id =:recommendId");
			params.put("recommendId", recommendId);
		}
		if (withdrawNum != null && withdrawNum == 1 && begin != null && end != null) {
			sb.append(" and d.first_time = a.create_datetime");
		} else if (withdrawNum != null && withdrawNum == 2 && begin != null && end != null) {
			sb.append(" and d.sec_time = a.create_datetime");
		}
		if (checkWithDrawOrderStatus != null) {
			sb.append(" and a.status in (1,10)");
		} else {
			if (status != null) {
				sb.append(" and a.status =:status");
				params.put("status", status);
			}
			if (lock != 0) {
				sb.append(" and a.lock_flag =:lock");
				params.put("lock", lock);
			}
		}
		if (proxyId != null) {
			sb.append(" AND (a.parent_ids LIKE :proxyIdCn ");
			sb.append(" or a.user_id = :proxyId)");
			params.put("proxyId", proxyId);
			params.put("proxyIdCn", "%," + proxyId + ",%");
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}

		if (minMoney != null) {
			sb.append(" and a.draw_money >= :minMoney");
			params.put("minMoney", minMoney);

		}
		if (maxMoney != null) {
			sb.append(" and a.draw_money <=:maxMoney");
			params.put("maxMoney", maxMoney);
		}
		if (StringUtils.isNotEmpty(opUsername)) {
			sb.append(" and a.op_username=:opUsername ");
			params.put("opUsername", opUsername);
		}
		// if (checkStatus != null) {
		// if (checkStatus == MnyDrawRecord.UN_CHECK) {
		// sb.append(" and a.check_status = :checkStatus");
		// params.put("checkStatus", checkStatus);
		// } else {
		// sb.append(" and (a.check_status is null or a.check_status !=:checkStatus)");
		// params.put("checkStatus", MnyDrawRecord.UN_CHECK);
		// }
		// }
		if (StringUtils.isNotEmpty(orderId)) {
			sb.append(" and a.order_id=:orderId ");
			params.put("orderId", orderId);
		}
		if (StringUtils.isNotEmpty(remark)) {
			sb.append(" AND a.remark LIKE :remark ");
			params.put("remark", "%" + remark + "%");
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and a.agent_name =:agentUser");
			params.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(bankName)) {
			sb.append(" and a.bank_name =:bankName");
			params.put("bankName", bankName);
		}
		sb.append(depositSort ? " order by status Asc,create_datetime desc" : " order by create_datetime desc");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN a.status in (1,2) THEN a.draw_money ELSE 0 END ",
				"totalMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN a.status in (1,2) THEN a.fee_money ELSE 0 END ",
				"feeMoney"));
		Page<MnyDrawRecord> page = super.queryByPage(sb.toString(), params, aggs);
		if (page.hasRows()) {
			for (MnyDrawRecord r : page.getRows()) {
				r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return page;
	}

	public int countDrawTimes(Long stationId, Date begin, Date end, Long userId, Integer status, int lock, Integer type,
			Long proxyId, String degreeIds, boolean depositSort, String pay, BigDecimal minMoney, BigDecimal maxMoney,
			String opUsername, Integer checkStatus, String orderId, Long payId, Integer checkWithDrawOrderStatus,
			String remark, String agentUser, String bankName, Integer withdrawNum, Long recommendId, Integer drawType) {
		StringBuilder sb = new StringBuilder("select count(distinct a.user_id) from mny_draw_record a");
		Map<String, Object> params = new HashMap<>();
		sb.append(" left join sys_user b on a.user_id = b.id");
		sb.append(" left join sys_user_degree degree on b.degree_id = degree.id");
		if (withdrawNum != null) {
			sb.append(" left join sys_user_withdraw d on a.user_id = d.user_id");
		}
		sb.append(" where a.station_id=:stationId and user_type != 150");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", begin);

		}
		if (drawType != null) {
			sb.append(" and a.type=:drawType");
			params.put("drawType", drawType);
		}
		if (end != null) {
			sb.append(" and a.create_datetime < :end");
			params.put("end", DateUtils.addSeconds(end, 1));
		}
		if (recommendId != null) {
			sb.append(" and a.recommend_id =:recommendId");
			params.put("recommendId", recommendId);
		}
		if (userId != null) {
			sb.append(" and a.user_id=:userId");
			params.put("userId", userId);
		}
		if (withdrawNum != null && withdrawNum == 1 && begin != null && end != null) {
			sb.append(" and d.first_time = a.create_datetime");
		} else if (withdrawNum != null && withdrawNum == 2 && begin != null && end != null) {
			sb.append(" and d.sec_time = a.create_datetime");
		}
		if (checkWithDrawOrderStatus != null) {
			sb.append(" and a.status in (1,10)");
		} else {
			if (status != null) {
				sb.append(" and a.status =:status");
				params.put("status", status);
			}
			if (lock != 0) {
				sb.append(" and a.lock_flag =:lock");
				params.put("lock", lock);
			}
		}
		if (proxyId != null) {
			sb.append(" AND (a.parent_ids LIKE :proxyIdCn ");
			sb.append(" or a.user_id = :proxyId)");
			params.put("proxyId", proxyId);
			params.put("proxyIdCn", "%," + proxyId + ",%");
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (minMoney != null) {
			sb.append(" and a.draw_money >= :minMoney");
			params.put("minMoney", minMoney);

		}
		if (maxMoney != null) {
			sb.append(" and a.draw_money <=:maxMoney");
			params.put("maxMoney", maxMoney);
		}
		if (StringUtils.isNotEmpty(opUsername)) {
			sb.append(" and a.op_username=:opUsername ");
			params.put("opUsername", opUsername);
		}
		// if (checkStatus != null) {
		// if (checkStatus == MnyDrawRecord.UN_CHECK) {
		// sb.append(" and a.check_status = :checkStatus");
		// params.put("checkStatus", checkStatus);
		// } else {
		// sb.append(" and (a.check_status is null or a.check_status !=:checkStatus)");
		// params.put("checkStatus", MnyDrawRecord.UN_CHECK);
		// }
		// }
		if (StringUtils.isNotEmpty(orderId)) {
			sb.append(" and a.order_id=:orderId ");
			params.put("orderId", orderId);
		}
		if (StringUtils.isNotEmpty(remark)) {
			sb.append(" AND a.remark LIKE :remark ");
			params.put("remark", "%" + remark + "%");
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and a.agent_name =:agentUser");
			params.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(bankName)) {
			sb.append(" and a.bank_name =:bankName");
			params.put("bankName", bankName);
		}
		return super.queryForInt(sb.toString(), params);
	}

	public MnyDrawRecord getLastDraw(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" select a.* from mny_draw_record a");
		sb.append(" where a.station_id =:stationId and user_type != 150");
		sb.append(" and a.user_id =:userId");
		params.put("stationId", stationId);
		params.put("userId", userId);
		sb.append(" order by create_datetime desc limit 1 OFFSET 1");
		MnyDrawRecord r = findOne(sb.toString(), params);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}

	public Page<MnyDrawRecord> userCenterPage(Long stationId, Long userId, Date begin, Date end, String orderId,
			Boolean include, boolean isMember, Integer status) {
		StringBuilder sb = new StringBuilder("select username,order_id,create_datetime,draw_money");
		sb.append(",true_money,fee_money,bank_name,card_no,status,remark,lock_flag from mny_draw_record ");
		sb.append(" where station_id =:stationId and create_datetime>=:begin and create_datetime<=:end");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("begin", begin);
		params.put("end", end);
		if (include != null && include) {
			if (isMember) {
				sb.append(" and (user_id =:userId OR recommend_id=:userId)");
			} else {
				sb.append(" and (user_id =:userId OR parent_ids like :userIdCn )");
				params.put("userIdCn", "%," + userId + ",%");
			}
		} else {
			sb.append(" and user_id =:userId");
		}
		if (status != null) {
			sb.append(" and status=:status");
			params.put("status", status);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			sb.append(" and order_id = :orderId");
			params.put("orderId", orderId);
		}
		sb.append(" order by id DESC");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM,
				"CASE WHEN status =" + MnyDrawRecord.STATUS_SUCCESS + " THEN draw_money ELSE 0 END ", "totalMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM,
				"CASE WHEN status =" + MnyDrawRecord.STATUS_SUCCESS + " THEN true_money ELSE 0 END ", "trueMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM,
				"CASE WHEN status =" + MnyDrawRecord.STATUS_SUCCESS + " THEN fee_money ELSE 0 END ", "feeMoney"));

		Page<MnyDrawRecord> page = super.queryByPage(sb.toString(), params, aggs);
		if (page.hasRows()) {
			for (MnyDrawRecord r : page.getRows()) {
				r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			}
		}
		return page;
	}

	@Override
	public MnyDrawRecord findOne(Long id, Long stationId) {
		MnyDrawRecord r = super.findOne(id, stationId);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}

	@Override
	public MnyDrawRecord save(MnyDrawRecord t) {
		t.setCardNo(EncryptDataUtil.encryptData(t.getCardNo()));
		t.setRealName(EncryptDataUtil.encryptData(t.getRealName()));
		t = super.save(t);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + t.getStationId() + ":check:" + "null");
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + t.getStationId() + ":check:" + "true");
		return t;
	}

	@Override
	public int update(MnyDrawRecord t) {
		t.setCardNo(EncryptDataUtil.encryptData(t.getCardNo()));
		t.setRealName(EncryptDataUtil.encryptData(t.getRealName()));
		int i = super.update(t);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + t.getStationId());
		return i;
	}

	public boolean updateDrawLockFlag(Long id, Integer lockFlag, Long opUserId, String opUsername, Date opDatetime,
			String remark, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" update mny_draw_record set op_user_id = :opUserId");
		map.put("opUserId", opUserId);
		sb.append(" ,op_username = :opUsername");
		map.put("opUsername", opUsername);
		sb.append(" ,op_datetime = :opDatetime");
		map.put("opDatetime", opDatetime);
		sb.append(" ,remark = :remark");
		map.put("remark", remark);
		sb.append(" ,lock_flag = :lockFlag");
		map.put("lockFlag", lockFlag);
		sb.append(" where id = :id");
		map.put("id", id);
		if (lockFlag == MnyDrawRecord.LOCK_FLAG_LOCK) {
			sb.append(" and lock_flag = :unLock");
			map.put("unLock", MnyDrawRecord.LOCK_FLAG_UNLOCK);
		} else {
			sb.append(" and lock_flag =:lock");
			map.put("lock", MnyDrawRecord.LOCK_FLAG_LOCK);
		}
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + stationId + ":check:" + "null");
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + stationId + ":check:" + "false");
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + stationId + ":check:" + "true");
		return update(sb.toString(), map) > 0;
	}

	public Integer getCountOfUntreated(Long stationId, Boolean isCheck) {
		String key = CACHE_PREF + stationId + ":check:" + isCheck;
		Integer i = CacheUtil.getCache(CacheKey.STATISTIC, key, Integer.class);
		if (i != null) {
			return i;
		}
		StringBuilder sql_sb = new StringBuilder("SELECT count(1) FROM mny_draw_record");
		sql_sb.append(" WHERE lock_flag=:lock AND station_id=:stationId AND status =:status");
		sql_sb.append("  and create_datetime>=:begin and create_datetime<=:end ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// if (isCheck != null) {
		// if (isCheck) {
		// sql_sb.append(" and check_status =:checkStatus");
		// paramMap.put("checkStatus", MnyDrawRecord.UN_CHECK);
		// } else {
		// sql_sb.append(" and check_status !=:checkStatus");
		// paramMap.put("checkStatus", MnyDrawRecord.UN_CHECK);
		// }
		// }
		paramMap.put("begin", DateUtils.addDays(new Date(), -1));
		paramMap.put("end", new Date());
		paramMap.put("stationId", stationId);
		paramMap.put("status", MnyDepositRecord.STATUS_UNDO);
		paramMap.put("lock", MnyDepositRecord.LOCK_FLAG_UNLOCK);
		i = super.queryForInt(sql_sb.toString(), paramMap);
		CacheUtil.addCache(CacheKey.STATISTIC, key, i, 60);
		return i;
	}

	/**
	 * 获取最后一条提现信息
	 */
	public MnyDrawRecord getNewestRecord(Long stationId, Long userId, Date end, Integer status) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select a.* from mny_draw_record a");
		sb.append(" where a.station_id =:stationId");
		sb.append(" and a.user_id =:userId");
		map.put("stationId", stationId);
		map.put("userId", userId);
		if (end != null) {
			sb.append(" and a.create_datetime <= :end");
			map.put("end", end);
		}
		if (status != null) {
			sb.append(" and a.status = :status");
			map.put("status", status);
		}
		sb.append(" order by a.create_datetime desc limit 1");
		MnyDrawRecord r = findOne(sb.toString(), map);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}

	public Integer getCount4Day(Long userId, Long stationId, Date start, Date end) {
		StringBuilder sql_sb = new StringBuilder("SELECT count(1) FROM mny_draw_record");
		sql_sb.append(" WHERE user_id=:userId AND station_id=:stationId");
		sql_sb.append(" AND create_datetime>=:start and create_datetime<:end and user_type != 150");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		paramMap.put("start", start);
		paramMap.put("end", end);

		if (StationConfigUtil.isOn(stationId, StationConfigEnum.withdraw_times_only_success)) {
			paramMap.put("status", MnyDrawRecord.STATUS_SUCCESS);
			sql_sb.append(" AND status =:status");
		}
		int i = super.queryForInt(sql_sb.toString(), paramMap);
		if (i < 0) {
			return 0;
		}
		return i;
	}

	/**
	 * 获取充值记录
	 *
	 * @param status
	 * @param lockFlag
	 * @param limit
	 * @return
	 */
	public List<MnyDrawRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
			Date end,String ip, String os) {
		StringBuilder sb = new StringBuilder("select * from mny_draw_record where 1=1 and user_type != 150");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null && stationId > 0) {
			sb.append(" AND station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (status != null && status > 0) {
			sb.append(" and status = :status");
			paramMap.put("status", status);
		}
		if (lockFlag != null && lockFlag > 0) {
			sb.append(" and lock_flag =:lockFlag");
			paramMap.put("lockFlag", lockFlag);
		}
		if (begin != null) {
			sb.append(" and create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			paramMap.put("end", end);
		}
		if (StringUtils.isNotEmpty(os)) {
			sb.append(" and record_os = :recordOs");
			paramMap.put("recordOs", os);
		}
		if (StringUtils.isNotEmpty(ip)) {
			sb.append(" and record_ip = :recordIp");
			paramMap.put("recordIp", ip);
		}
		sb.append(" order by create_datetime ASC");
		if (limit != null && limit > 0) {
			sb.append(" LIMIT :count");
			paramMap.put("count", limit);
		}
		List<MnyDrawRecord> list = find(sb.toString(), paramMap);
		if (list != null && !list.isEmpty()) {
			for (MnyDrawRecord r : list) {
				r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return list;
	}

	public long countOfRecord(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
											 Date end,String ip, String os,Long excludeUserId) {
		StringBuilder sb = new StringBuilder("select count(*) from mny_draw_record where 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" and (user_type <> :guest and user_type <> :guestBack)");
		paramMap.put("guest", UserTypeEnum.GUEST.getType());
		paramMap.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		if (stationId != null && stationId > 0) {
			sb.append(" AND station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (status != null && status > 0) {
			sb.append(" and status = :status");
			paramMap.put("status", status);
		}
		if (excludeUserId != null && excludeUserId > 0) {
			sb.append(" and user_id <>:userId");
			paramMap.put("userId", excludeUserId);
		}
		if (lockFlag != null && lockFlag > 0) {
			sb.append(" and lock_flag =:lockFlag");
			paramMap.put("lockFlag", lockFlag);
		}
		if (begin != null) {
			sb.append(" and create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			paramMap.put("end", end);
		}
		if (StringUtils.isNotEmpty(os)) {
			sb.append(" and record_os = :recordOs and record_os is not null");
			paramMap.put("recordOs", os);
		}
		if (StringUtils.isNotEmpty(ip)) {
			sb.append(" and record_ip = :recordIp and record_ip is not null");
			paramMap.put("recordIp", ip);
		}
		if (limit != null && limit > 0) {
			sb.append(" LIMIT :count");
			paramMap.put("count", limit);
		}
		return queryForLong(sb.toString(),paramMap);
	}

	public BigDecimal getTotalRecordMoney(Long stationId, Integer status, Integer lockFlag, Long limit,
										  Date begin, Date end,String userIds){
		StringBuilder sb = new StringBuilder("select sum(draw_money) from mny_draw_record where 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null && stationId > 0) {
			sb.append(" AND station_id = :stationId");
			paramMap.put("stationId", stationId);
		}
		if (status != null && status > 0) {
			sb.append(" and status = :status");
			paramMap.put("status", status);
		}
		if (lockFlag != null && lockFlag > 0) {
			sb.append(" and lock_flag =:lockFlag");
			paramMap.put("lockFlag", lockFlag);
		}
		if (begin != null) {
			sb.append(" and create_datetime >= :begin");
			paramMap.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			paramMap.put("end", end);
		}
		if (StringUtils.isNotEmpty(userIds)) {
			sb.append(" AND user_id in (").append(userIds).append(")");
		}
//		sb.append(" order by create_datetime ASC");
		if (limit != null && limit > 0) {
			sb.append(" LIMIT :count");
			paramMap.put("count", limit);
		}
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	public int handleTimeoutRecord(Integer status, String remark, Long stationId, Long id, Integer checkStatus) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set status =:status,remark=:remark");
		sb.append(" where id=:id and station_id =:stationId and user_type != 150");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("status", status);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}

	public List<MnyDrawRecord> export(Long stationId, Date begin, Date end, String username, Integer status, int lock,
			Integer type, Long proxyId, String opUsername, String orderId, String degreeIds, String pay, Long payId,
			String agentUser, String bankName, Integer withdrawNum) {
		StringBuilder sb = new StringBuilder("select a.*,b.name as degree_name from mny_draw_record a");
		Map<String, Object> params = new HashMap<>();
		sb.append(" left join sys_user_degree b on a.degree_id = b.id");
		if (withdrawNum != null) {
			sb.append(" left join sys_user_withdraw d on a.user_id = d.user_id");
		}
		sb.append(" where a.station_id=:stationId and user_type != 150");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", begin);

		}
		if (end != null) {
			sb.append(" and a.create_datetime <=:end");
			params.put("end", end);
		}
		if (withdrawNum != null && withdrawNum == 1 && begin != null && end != null) {
			sb.append(" and d.first_time = a.create_datetime");
		} else if (withdrawNum != null && withdrawNum == 2 && begin != null && end != null) {
			sb.append(" and d.sec_time = a.create_datetime");
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and a.username=:username");
			params.put("username", username);
		}
		if (status != null) {
			sb.append(" and a.status =:status");
			params.put("status", status);
		}
		if (lock != 0) {
			sb.append(" and a.lock_flag =:lock");
			params.put("lock", lock);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (proxyId != null) {
			sb.append(" AND (parent_ids LIKE :proxyIdCn ");
			sb.append(" or a.user_id = :proxyId)");
			params.put("proxyId", proxyId);
			params.put("proxyIdCn", "%," + proxyId + ",%");
		}
		if (StringUtils.isNotEmpty(opUsername)) {
			sb.append(" and a.op_username=:opUsername ");
			params.put("opUsername", opUsername);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			sb.append(" and a.order_id=:orderId");
			params.put("orderId", orderId);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and a.agent_name=:agentUser");
			params.put("agentUser", agentUser);
		}
		if (StringUtils.isNotEmpty(bankName)) {
			sb.append(" and a.bank_name=:bankName");
			params.put("bankName", bankName);
		}
		sb.append(" order by a.status Asc,a.create_datetime desc");
		List<MnyDrawRecord> list = find(sb.toString(), params);
		if (list != null && !list.isEmpty()) {
			for (MnyDrawRecord r : list) {
				r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return list;
	}

	public boolean updateStatus(Long id, Long stationId, Integer status, String remark, Date date, Long payId,
			String panName, Integer type, Integer lockFlag) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append("update mny_draw_record set status =:status,remark=:remark,op_datetime=:date");
		if (payId != null) {
			sb.append(",pay_id=:payId");
			paramMap.put("payId", payId);
		}
		if (panName != null) {
			sb.append(",pay_name=:panName");
			paramMap.put("panName", panName);
		}
		if (type != null) {
			sb.append(",type=:type");
			paramMap.put("type", type);
		}
		if (lockFlag != null) {
			sb.append(",lock_flag=:lockFlag");
			paramMap.put("lockFlag", lockFlag);
		}
		sb.append(" where id=:id and station_id =:stationId and status=:oldStatus");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("status", status);
		paramMap.put("remark", remark);
		paramMap.put("date", date);
		paramMap.put("oldStatus", MnyDrawRecord.STATUS_UNDO);
		return update(sb.toString(), paramMap) > 0;
	}
	
	public boolean updateStatusAppendRemark(Long id, Long stationId, Integer status, String remark, Date date, Long payId,
			String panName, Integer type, Integer lockFlag) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append("update mny_draw_record set status =:status,remark=remark || :remark,op_datetime=:date");
		if (payId != null) {
			sb.append(",pay_id=:payId");
			paramMap.put("payId", payId);
		}
		if (panName != null) {
			sb.append(",pay_name=:panName");
			paramMap.put("panName", panName);
		}
		if (type != null) {
			sb.append(",type=:type");
			paramMap.put("type", type);
		}
		if (lockFlag != null) {
			sb.append(",lock_flag=:lockFlag");
			paramMap.put("lockFlag", lockFlag);
		}
		sb.append(" where id=:id and station_id =:stationId and status=:oldStatus");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("status", status);
		paramMap.put("remark", remark);
		paramMap.put("date", date);
		paramMap.put("oldStatus", MnyDrawRecord.STATUS_UNDO);
		return update(sb.toString(), paramMap) > 0;
	}

	public MnyDrawRecord getMnyRecordByOrderId(String order, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" select a.* from mny_draw_record a");
		sb.append(" where a.station_id =:stationId ");
		sb.append(" and a.order_id =:order_id");
		params.put("stationId", stationId);
		params.put("order_id", order);
		MnyDrawRecord r = findOne(sb.toString(), params);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}

	//
	// /**
	// * 统计提款金额
	// *
	// * @param stationId
	// * @param userId
	// * @param beginTime
	// * @param endTime
	// * @return
	// */
	// public List<SysUserDailyMoney> findSysUserDailyMoney(Long stationId, Long
	// userId, Date beginTime,
	// Date endTime) {
	// Map<String, Object> params = new HashMap<>();
	// params.put("stationId", stationId);
	// params.put("userId", userId);
	// params.put("beginTime", beginTime);
	// params.put("endTime", endTime);
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append("select sum(draw_money) as money, to_char(create_datetime,
	// 'YYYY-MM-DD')");
	// sb.append(" as statDateStr from mny_draw_record where station_id =:stationId
	// and");
	// sb.append(" create_datetime >=:beginTime and create_datetime <=:endTime and
	// user_id =:userId");
	// sb.append(" GROUP BY to_char(create_datetime, 'YYYY-MM-DD')");
	// return find(sb.toString(), new
	// BeanPropertyRowMapper<>(SysUserDailyMoney.class), params);
	// }

	public MnyDrawRecord findOneByOrderId(Long stationId, String orderId) {
		StringBuilder sb = new StringBuilder(" select * from mny_draw_record");
		sb.append(" where station_id =:stationId and order_id =:order_id");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("order_id", orderId);
		MnyDrawRecord r = findOne(sb.toString(), params);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}

	public MnyDrawRecord getHighestMoneyDrawRecord(Long stationId, Long userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		StringBuilder sb = new StringBuilder("select * from mny_draw_record");
		sb.append(" where station_id =:stationId and user_id=:userId");
		sb.append(" and status = :status");
		params.put("status", MnyDrawRecord.STATUS_SUCCESS);
		sb.append(" order by draw_money desc limit 1");
		MnyDrawRecord r = findOne(sb.toString(), params);
		if (r != null) {
			r.setCardNo(EncryptDataUtil.decryptData(r.getCardNo()));
			r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
		}
		return r;
	}



	/**
	 * 修改订单审核状态
	 */
	public int updateDrawCheckStatus(Long id, Long stationId, Integer status, String msg, String checkUser) {
		StringBuilder sb = new StringBuilder("update mny_draw_record set check_status = :checkStatus,remark=:remark");
		sb.append(",check_user=:checkUser,op_username=''");
		Map<String, Object> params = new HashMap<>();
		params.put("checkUser", checkUser);
		if (status == MnyDrawRecord.CHECK_FAIL) {
			sb.append(",status=:status");
			params.put("status", MnyDrawRecord.STATUS_FAIL);
		} else {
			sb.append(",lock_flag=:lockFlag");
			params.put("lockFlag", MnyDrawRecord.LOCK_FLAG_UNLOCK);
		}
		sb.append(" where id=:id and check_status=:oldStatus");
		params.put("id", id);
		params.put("checkStatus", status);
		params.put("remark", StringUtils.isEmpty(msg) ? "" : msg);
		params.put("oldStatus", MnyDrawRecord.UN_CHECK);
		return update(sb.toString(), params);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" delete from mny_draw_record");
		sb.append(" where station_id =:stationId ");
		Map<String, Object> map = new HashMap<>();
		sb.append(" and create_datetime <= :createTime");
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

	public int changeRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set remark=:remark");
		sb.append(" where id=:id and station_id =:stationId ");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}
	
	public int appenRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set remark=remark||:remark");
		sb.append(" where id=:id and station_id =:stationId ");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}

	public int appenRemarkThrid(Long id, Long stationId, String remark,String payPlatformNo) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set remark=remark||:remark,pay_platform_no=:payPlatformNo");
		sb.append(" where id=:id and station_id =:stationId");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("remark", remark);
		paramMap.put("payPlatformNo",payPlatformNo);
		return update(sb.toString(), paramMap);
	}


	public int changeBgRemark(Long id, Long stationId, String bgRemark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set bg_remark=:bgRemark");
		sb.append(" where id=:id and station_id =:stationId ");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("bgRemark", bgRemark);
		return update(sb.toString(), paramMap);
	}

	public int changeRemark(Long id, Long stationId, String remark, String bgRemark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(" update mny_draw_record set bg_remark=:bgRemark, remark=:remark");
		sb.append(" where id=:id and station_id =:stationId ");
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("bgRemark", bgRemark);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}

	public DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		StringBuilder sb = new StringBuilder();
		sb.append("select COALESCE(sum(draw_money),0) as money,count(*) as times");
		sb.append(" from mny_draw_record where station_id=:stationId and user_id=:userId and status=2");
		sb.append(" and create_datetime>=:start and create_datetime < :end");
		paramMap.put("start", start);
		paramMap.put("end", end);
		return findOne(sb.toString(), paramMap, DailyMoneyVo.class);
	}

}
