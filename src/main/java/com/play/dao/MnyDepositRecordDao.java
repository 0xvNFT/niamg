package com.play.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.UserTypeEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDailyDeposit;
import com.play.model.vo.DailyMoneyVo;
import com.play.model.vo.DepositVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import com.play.web.var.LoginAdminUtil;

/**
 * 用户充值记录
 *
 * @author admin
 *
 */
@Repository
public class MnyDepositRecordDao extends JdbcRepository<MnyDepositRecord> {
	private static final String CACHE_PREF = "admin:untreated:depoist:";

	public Integer getCountOfUntreated(Long stationId) {
		Integer i = CacheUtil.getCache(CacheKey.STATISTIC, CACHE_PREF + stationId, Integer.class);
		if (i != null) {
			return i;
		}
		StringBuilder sql_sb = new StringBuilder("SELECT count(1) FROM mny_deposit_record");
		sql_sb.append(" WHERE deposit_type IN(1,2,3) AND lock_flag=:lock AND station_id=:stationId");
		sql_sb.append(" AND status =:status and create_datetime>:begin and create_datetime<=:end");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("stationId", stationId);
		paramMap.put("status", MnyDepositRecord.STATUS_UNDO);
		paramMap.put("lock", MnyDepositRecord.LOCK_FLAG_UNLOCK);
		paramMap.put("begin", DateUtils.addDays(new Date(), -1));
		paramMap.put("end", new Date());
		i = super.queryForInt(sql_sb.toString(), paramMap);
		CacheUtil.addCache(CacheKey.STATISTIC, CACHE_PREF + stationId, i, 60);
		return i;
	}

	public Page<MnyDepositRecord> page(DepositVo vo) {
		StringBuilder sb = new StringBuilder("select *, di.real_name as realName,");
		sb.append("case when (draw.first_time notnull and a.create_datetime!=");
		sb.append("draw.first_time) then false else true end as is_first");
		sb.append(" from mny_deposit_record a");
		sb.append(" left join sys_user_info di on a.user_id = di.user_id");
		sb.append(" left join sys_user_deposit draw on a.user_id = draw.user_id");
		if (vo.getRegisterTimeD() != null || vo.getRegisterTimeEndD() != null
				|| StringUtils.isNotEmpty(vo.getDegreeIds())) {
			sb.append(" left join sys_user dd on a.user_id = dd.id");
		}
		Map<String, Object> params = new HashMap<>();
		sb.append(" where a.station_id=:stationId");
		params.put("stationId", vo.getStationId());
		if (vo.getBegin() != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", vo.getBegin());
		}
		if (vo.getEnd() != null) {
			sb.append(" and a.create_datetime <=:end");
			params.put("end", DateUtils.addSeconds(vo.getEnd(), 1));
		}
		if (vo.getRegisterTimeD() != null) {
			sb.append(" and dd.create_datetime >= :registerTimeD");
			params.put("registerTimeD", vo.getRegisterTimeD());
		}
		if (vo.getRegisterTimeEndD() != null) {
			sb.append(" and dd.create_datetime <=:registerTimeEndD");
			params.put("registerTimeEndD", DateUtils.addSeconds(vo.getRegisterTimeEndD(), 1));
		}
		if (vo.getRecommendId() != null) {
			sb.append(" and a.recommend_id =:recommendId");
			params.put("recommendId", vo.getRecommendId());
		}
		if (vo.getDepositNum() != null && vo.getBegin() != null && vo.getEnd() != null) {
			if (vo.getDepositNum() == 1) {
				sb.append(" and draw.first_time = a.create_datetime");
			} else if (vo.getDepositNum() == 2) {
				sb.append(" and draw.sec_time = a.create_datetime");
			} else if (vo.getDepositNum() == 3) {
				sb.append(" and draw.third_time = a.create_datetime");
			}
		}
		if (StringUtils.isNotEmpty(vo.getIsDeposit())) {
			if (vo.getType() != null) {
				sb.append(" and a.deposit_type=:type");
				params.put("type", vo.getType());
			}
		} else {
			if (vo.getType() != null) {
				sb.append(" and a.deposit_type=:type");
				params.put("type", vo.getType());
			}
		}
		if (vo.getCheckOrderStatus() != null) {
			sb.append(" and a.status in (1,10)");
		} else {
			if (vo.getStatus() != null) {
				sb.append(" and a.status=:status");
				params.put("status", vo.getStatus());
			}
			if (vo.getLockStatus() != 0) {
				sb.append(" and a.lock_flag=:lock");
				params.put("lock", vo.getLockStatus());
			}
		}
		if (StringUtils.isNotEmpty(vo.getPayIds())) {
			sb.append(" and a.pay_id  in (");
			for (String lId : vo.getPayIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (StringUtils.isNotEmpty(vo.getOnlineCode())) {
			sb.append(" and a.pay_name in (");
			for (String lId : vo.getOnlineCode().split(",")) {
				sb.append("'" + lId + "'").append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (vo.getHandlerType() != null) {
			sb.append(" and a.handler_type =:handType");
			params.put("handType", vo.getHandlerType());
		}
		if (vo.getUserId() != null) {
			sb.append(" and a.user_id =:user");
			params.put("user", vo.getUserId());
		}
		if (StringUtils.isNotEmpty(vo.getOrderId())) {
			sb.append(" and a.order_id =:orderId");
			params.put("orderId", vo.getOrderId());
		}
		if (vo.getProxyId() != null) {
			sb.append(" AND (a.parent_ids LIKE :proxyIdCn or a.user_id =:proxyId)");
			params.put("proxyId", vo.getProxyId());
			params.put("proxyIdCn", "%," + vo.getProxyId() + ",%");
		}
		if (StringUtils.isNotEmpty(vo.getBgRemark())) {
			sb.append(" and a.bg_remark ~:bgRemark");
			params.put("bgRemark", vo.getBgRemark());
		}
		if (StringUtils.isNotEmpty(vo.getDegreeIds())) {
			sb.append(" and dd.degree_id in(");
			for (String lId : vo.getDegreeIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (vo.getMinMoney() != null) {
			sb.append(" and a.money >= :minMoney");
			params.put("minMoney", vo.getMinMoney());
		}
		if (vo.getMaxMoney() != null) {
			sb.append(" and a.money <= :maxMoney");
			params.put("maxMoney", vo.getMaxMoney());
		}
		if (StringUtils.isNotEmpty(vo.getOpUsername())) {
			sb.append(" and a.op_username =:op_username");
			params.put("op_username", vo.getOpUsername());
		}
		if (StringUtils.isNotEmpty(vo.getPayBankName())) {
			sb.append(" and a.pay_bank_name ~ :pay_bank_name ");
			params.put("pay_bank_name", vo.getPayBankName());
		}
		if (StringUtils.isNotEmpty(vo.getAgentUser())) {
			sb.append(" and a.agent_username=:agentUser");
			params.put("agentUser", vo.getAgentUser());
		}

		// 试玩账号不查询
		sb.append(" and (a.user_type != :guest and a.user_type != :guestBack)");
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());

		sb.append(vo.isDepositSort() ? " order by a.status Asc,a.create_datetime desc"
				: " order by a.create_datetime desc");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		if (vo.getStatus() != null) {
			aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN a.status =" + vo.getStatus()
					+ " AND (a.user_type !=" + UserTypeEnum.GUEST.getType() + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
					+ " THEN a.money ELSE 0 END ", "totalMoney"));
			aggs.add(
					new Aggregation(AggregationFunction.SUM,
							"CASE WHEN a.status =" + vo.getStatus()
									+ " AND (a.user_type !=" + UserTypeEnum.GUEST.getType() + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
									+ " THEN a.gift_money ELSE 0 END ", "totalGiftMoney"));
		} else if (vo.getCheckOrderStatus() != null) {
			aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN a.status in (1,10) AND (a.user_type !="
					+ UserTypeEnum.GUEST.getType() + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
					+ " THEN a.money ELSE 0 END ", "totalMoney"));
			aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN a.status in (1,10) AND (a.user_type !="
					+ UserTypeEnum.GUEST.getType() + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
					+ " THEN a.gift_money ELSE 0 END ", "totalGiftMoney"));
		} else {
			aggs.add(new Aggregation(
					AggregationFunction.SUM, "CASE WHEN a.status =" + MnyDepositRecord.STATUS_SUCCESS
							+ " AND (a.user_type !=" + UserTypeEnum.GUEST.getType()  + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
					+ " THEN a.money ELSE 0 END ", "totalMoney"));
			aggs.add(new Aggregation(
					AggregationFunction.SUM, "CASE WHEN a.status =" + MnyDepositRecord.STATUS_SUCCESS
							+ " AND (a.user_type !=" + UserTypeEnum.GUEST.getType()  + " AND a.user_type !=" + UserTypeEnum.GUEST_BACK.getType() + ")"
					+ " THEN a.gift_money ELSE 0 END ", "totalGiftMoney"));
		}
		Page<MnyDepositRecord> page = super.queryByPage(sb.toString(), params, aggs);
		if (page.hasRows()) {
			for (MnyDepositRecord r : page.getRows()) {
				r.setRealName(EncryptDataUtil.decryptData(r.getRealName()));
			}
		}
		return page;
	}

	public int countUserDepositNum(Long stationId, Long userId, Date depositDate) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		depositDate = DateUtil.dayFirstTime(depositDate, 0);
		paramMap.put("start", depositDate);
		paramMap.put("end", DateUtil.dayFirstTime(depositDate, 1));
		paramMap.put("stationId", stationId);
		return queryForInt(
				"select count(*) from mny_deposit_record where station_id=:stationId and create_datetime>=:start and create_datetime<:end and user_id=:userId and status=2",
				paramMap);
	}

	/**
	 * 用户中心充值记录获取
	 */
	public Page<MnyDepositRecord> userCenterPage(Date begin, Date end, Long userId, Long stationId, String orderId,
			Boolean include, boolean isMember, Integer status) {
		StringBuilder sb = new StringBuilder("select username,order_id,create_datetime,money,deposit_type,");
		sb.append("pay_name,status,remark,bank_address,bank_way,lock_flag from mny_deposit_record ");
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
			sb.append(" and order_id =:orderId");
			params.put("orderId", orderId);
		}
		sb.append(" order by id DESC");
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM,
				"CASE WHEN status =" + MnyDepositRecord.STATUS_SUCCESS + " THEN money ELSE 0 END ", "totalMoney"));
		return queryByPage(sb.toString(), params, aggs);
	}

	@Override
	public MnyDepositRecord save(MnyDepositRecord t) {
		t = super.save(t);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + t.getStationId());
		return t;
	}

	@Override
	public int update(MnyDepositRecord t) {
		int i = super.update(t);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + t.getStationId());
		return i;
	}

	/**
	 * 获取最后一条充值信息
	 */
	public MnyDepositRecord getNewestRecord(Long stationId, Long userId, Date begin, Date end, Integer status,
			Integer type) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select a.* from mny_deposit_record a");
		sb.append(" where a.station_id =:stationId");
		sb.append(" and a.user_id =:userId");
		map.put("stationId", stationId);
		map.put("userId", userId);
		if (begin != null) {
			sb.append(" and a.create_datetime >= :begin ");
			map.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.create_datetime <= :end");
			map.put("end", end);
		}
		if (status != null) {
			sb.append(" and a.status = :status");
			map.put("status", status);
		}
		if (type != null) {
			sb.append(" and a.deposit_type in (2,3)");
		}
		sb.append(" order by a.create_datetime desc limit 1");
		return findOne(sb.toString(), map);
	}

	public BigDecimal sumDayPay(Long userId, Long stationId, Date begin, Date end, Integer manualDeposit) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);

		paramMap.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select sum(money) from mny_deposit_record");
		sb.append(" where station_id=:stationId and user_id=:userId and status=2");
		if (begin != null) {
			sb.append(" and create_datetime>=:start");
			paramMap.put("start", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime < :end ");
			paramMap.put("end", end);
		}
		if (manualDeposit != null) {
			sb.append(" and deposit_type <> 4");
		}
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	/**
	 * 首充
	 * 
	 * @param userId
	 * @param stationId
	 * @param begin
	 * @param end
	 * @return
	 */
	public BigDecimal firstSumDayPay(Long userId, Long stationId, Date begin, Date end) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);

		paramMap.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select money from mny_deposit_record");
		sb.append(" where station_id=:stationId and user_id=:userId and status=2");
		if (begin != null) {
			sb.append(" and create_datetime>=:start");
			paramMap.put("start", begin);

		}
		if (end != null) {
			sb.append(" and create_datetime < :end ");
			paramMap.put("end", end);
		}
		sb.append(" order by create_datetime asc limit 1");
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	public int updateLockInfo(Long id, Integer lockFlag, Long userId, Date date, Long stationId, Integer oldLockFlag,
			String opUsername, Integer handleType) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("lockFlag", lockFlag);
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		paramMap.put("date", date);
		paramMap.put("oldLock", oldLockFlag);
		paramMap.put("status", MnyDepositRecord.STATUS_UNDO);
		paramMap.put("opUsername", opUsername);
		StringBuilder sb = new StringBuilder("update mny_deposit_record set lock_flag=:lockFlag");
		if (handleType != null) {
			sb.append(",handler_type =:handleType");
			paramMap.put("handleType", handleType);
		}
		sb.append(",op_user_id=:userId,op_datetime=:date,op_username=:opUsername where id=:id and ");
		sb.append(" station_id=:stationId and status=:status and lock_flag=:oldLock");
		int i = update(sb.toString(), paramMap);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + stationId);
		return i;
	}

	public int updateStatus(Long id, Long stationId, Integer status, String remark, String bgRemark, Date date,
			BigDecimal money) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("status", status);
		paramMap.put("stationId", stationId);
		paramMap.put("remark", remark);
		paramMap.put("bgRemark", bgRemark);
		paramMap.put("date", date);
		paramMap.put("oldStatus", MnyDepositRecord.STATUS_UNDO);
		paramMap.put("opUsername", LoginAdminUtil.getUsername());
		StringBuilder sb = new StringBuilder("update mny_deposit_record set status=:status");
		if (money != null) {
			sb.append(",money=:money");
			paramMap.put("money", money);
		}
		sb.append(",remark=:remark,bg_remark=:bgRemark,op_datetime=:date,op_username=:opUsername where id=:id and ");
		sb.append(" station_id=:stationId and status=:oldStatus ");
		int i = update(sb.toString(), paramMap);
		CacheUtil.delCache(CacheKey.STATISTIC, CACHE_PREF + stationId);
		return i;
	}

	public void updateGiftMoney(Long id, BigDecimal giftMoney, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		paramMap.put("stationId", stationId);
		paramMap.put("giftMoney", giftMoney);
		StringBuilder sb = new StringBuilder("update mny_deposit_record set ");
		sb.append("gift_money=:giftMoney where id=:id and station_id=:stationId ");
		update(sb.toString(), paramMap);
	}

	public int countPayCount(Long userId, Date depositDate, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("depositDate", depositDate);
		paramMap.put("stationId", stationId);
		return queryForInt(
				"select count(*) from mny_deposit_record where station_id=:stationId and create_datetime>=:depositDate and user_id=:userId",
				paramMap);
	}

	// 查询充值总人数
	public int countPayTimes(DepositVo vo) {
		StringBuilder sb = new StringBuilder("select count(distinct a.user_id) from mny_deposit_record a");
		if (vo.getDepositNum() != null) {
			sb.append(" left join sys_user_deposit d on a.user_id = d.user_id");
		}
		if (vo.getRegisterTimeD() != null) {
			sb.append(" left join sys_user dd on a.user_id = dd.id");
		}
		Map<String, Object> params = new HashMap<>();
		sb.append(" where a.station_id=:stationId");
		params.put("stationId", vo.getStationId());
		if (vo.getBegin() != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", vo.getBegin());
		}
		if (vo.getEnd() != null) {
			sb.append(" and a.create_datetime <=:end");
			params.put("end", DateUtils.addSeconds(vo.getEnd(), 1));
		}
		if (vo.getRegisterTimeD() != null) {
			sb.append(" and dd.create_datetime >= :registerTimeD");
			params.put("registerTimeD", vo.getRegisterTimeD());
		}
		if (vo.getRegisterTimeEndD() != null) {
			sb.append(" and dd.create_datetime <=:registerTimeEndD");
			params.put("registerTimeEndD", DateUtils.addSeconds(vo.getRegisterTimeEndD(), 1));
		}
		if (vo.getRecommendId() != null) {
			sb.append(" and a.recommend_id =:recommendId");
			params.put("recommendId", vo.getRecommendId());
		}
		if (vo.getDepositNum() != null && vo.getBegin() != null && vo.getEnd() != null) {
			if (vo.getDepositNum() == 1) {
				sb.append(" and d.first_time = a.create_datetime");
			} else if (vo.getDepositNum() == 2) {
				sb.append(" and d.sec_time = a.create_datetime");
			} else if (vo.getDepositNum() == 3) {
				sb.append(" and d.third_time = a.create_datetime");
			}
		}
		if (StringUtils.isNotEmpty(vo.getIsDeposit())) {
			if (vo.getType() != null) {
				sb.append(" and a.deposit_type=:type");
				params.put("type", vo.getType());
			}
		} else {
			if (vo.getType() != null) {
				sb.append(" and a.deposit_type=:type");
				params.put("type", vo.getType());
			}
		}
		sb.append(" and a.status = 2");

		if (StringUtils.isNotEmpty(vo.getPayIds())) {
			sb.append(" and a.pay_id  in (");
			for (String lId : vo.getPayIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (StringUtils.isNotEmpty(vo.getOnlineCode())) {
			sb.append(" and a.pay_name in (");
			for (String lId : vo.getOnlineCode().split(",")) {
				sb.append("'" + lId + "'").append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (vo.getHandlerType() != null) {
			sb.append(" and a.handler_type =:handType");
			params.put("handType", vo.getHandlerType());
		}
		if (vo.getUserId() != null) {
			sb.append(" and a.user_id =:user");
			params.put("user", vo.getUserId());
		}
		if (StringUtils.isNotEmpty(vo.getOrderId())) {
			sb.append(" and a.order_id =:orderId");
			params.put("orderId", vo.getOrderId());
		}
		if (vo.getProxyId() != null) {
			sb.append(" AND (a.parent_ids LIKE :proxyIdCn");
			sb.append(" or a.user_id =:proxyId)");
			params.put("proxyId", vo.getProxyId());
			params.put("proxyIdCn", "%," + vo.getProxyId() + ",%");
		}
		if (StringUtils.isNotEmpty(vo.getWithCode())) {
			sb.append(" and a.with_code =:withCode");
			params.put("withCode", vo.getWithCode());
		}
		if (StringUtils.isNotEmpty(vo.getDegreeIds())) {
			sb.append(" and a.user_id in(select id from sys_user where degree_id in(");
			for (String lId : vo.getDegreeIds().split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			sb.append(" and station_id=:stationId)");
		}
		if (vo.getMinMoney() != null) {
			sb.append(" and a.money >= :minMoney");
			params.put("minMoney", vo.getMinMoney());
		}
		if (vo.getMaxMoney() != null) {
			sb.append(" and a.money <= :maxMoney");
			params.put("maxMoney", vo.getMaxMoney());
		}
		if (StringUtils.isNotEmpty(vo.getOpUsername())) {
			sb.append(" and a.op_username =:op_username");
			params.put("op_username", vo.getOpUsername());
		}
		if (StringUtils.isNotEmpty(vo.getPayBankName())) {
			sb.append(" and a.pay_bank_name ~ :pay_bank_name ");
			params.put("pay_bank_name", vo.getPayBankName());
		}

		// 试玩账号不查询
		sb.append(" and (a.user_type != :guest and a.user_type != :guestBack)");
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());

		return super.queryForInt(sb.toString(), params);

	}

	public BigDecimal getTotalRecordMoney(Long stationId, Integer status, Integer lockFlag, Long limit,
										  Date begin, Date end) {
		return getTotalRecordMoney(stationId, status, lockFlag, limit, begin, end, null);
	}

	public BigDecimal getTotalRecordMoney(Long stationId, Integer status, Integer lockFlag, Long limit,
										  Date begin, Date end,String userIds){

		Map<String, Object> paramMap = new HashMap<>();
		StringBuilder sb = new StringBuilder("select sum(money) from mny_deposit_record where station_id=:stationId");
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
			sb.append(" and create_datetime <=:end");
			paramMap.put("end", end);
		}
		if (StringUtils.isNotEmpty(userIds)) {
			sb.append(" AND user_id in (").append(userIds).append(")");
		}
		if (status != null && status > 0) {
			sb.append(" and status =:status");
			paramMap.put("status", status);
		}
		paramMap.put("stationId", stationId);
		if (limit != null && limit > 0) {
			sb.append(" LIMIT ").append(limit);
		}
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	/**
	 * 获取充值记录
	 *
	 * @param status
	 * @param lockFlag
	 * @param limit
	 * @return
	 */
	public List<MnyDepositRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit,
			Date begin, Date end) {
		StringBuilder sb = new StringBuilder("select * from mny_deposit_record where 1=1");
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
			sb.append(" and create_datetime <=:end");
			paramMap.put("end", end);
		}
		sb.append(" order by create_datetime ASC");
		if (limit != null && limit > 0) {
			sb.append(" LIMIT :count");
			paramMap.put("count", limit);
		}
		return find(sb.toString(), paramMap);
	}

	public int handleTimeoutRecord(Integer status, String remark, Long stationId, Long id, Integer handleType) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		sb.append(
				" update mny_deposit_record set status =:status,bg_remark=:remark,remark=:remark,handler_type=:handleType");
		sb.append(" where id=:id and station_id =:stationId");
		paramMap.put("id", id);
		paramMap.put("handleType", handleType);
		paramMap.put("stationId", stationId);
		paramMap.put("status", status);
		paramMap.put("remark", remark);
		return update(sb.toString(), paramMap);
	}

	/**
	 * 获取最后一条充值信息
	 */
	public MnyDepositRecord findOneByOrderId(String orderId, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select a.* from mny_deposit_record a");
		sb.append(" where 1=1");
		sb.append(" and a.order_id =:orderId and station_id =:stationId");
		map.put("orderId", orderId);
		map.put("stationId", stationId);
		sb.append(" order by a.create_datetime desc limit 1");
		return findOne(sb.toString(), map);
	}

	public int updateOrderStatusByOrderId(String orderId, Integer newStatus, Integer oldStatus, String opDesc,
			Date date, BigDecimal realMoney) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" update mny_deposit_record set status=:newStatus");
		map.put("newStatus", newStatus);
		sb.append(" , handler_type=:handerType");
		map.put("handerType", MnyDepositRecord.HANDLE_TYPE_SYS);
		if (StringUtils.isNotEmpty(opDesc)) {
			sb.append(" , bg_remark=:opDesc");
			map.put("opDesc", opDesc);
		}
		if (date != null) {
			sb.append(" , op_datetime=:date");
			map.put("date", date);
		}
		if (realMoney != null) {
			sb.append(" , money=:realMoney");
			map.put("realMoney", realMoney);
		}
		sb.append(" where order_id =:orderId");
		map.put("orderId", orderId);
		sb.append(" and status=:oldStatus");
		map.put("oldStatus", oldStatus);

		return update(sb.toString(), map);
	}

	public List<MnyDepositRecord> export(Date begin, Date end, Integer type, Integer status, int lock, Long payId,
			Integer handType, String user, String orderId, Long proxyId, String withCode, Long stationId,
			String onlineCode, String opUsername, Integer depositNum, String degreeIds, String payBankName,
			String payIds, String agentUser) {
		StringBuilder sb = new StringBuilder("select a.*,sal.name as degree_name,sa.proxy_name");
		sb.append(" from mny_deposit_record a");
		if (depositNum != null) {
			sb.append(" left join sys_user_deposit d on a.user_id = d.user_id");
		}
		sb.append(" left join sys_user sa on a.user_id = sa.id ");
		sb.append(" left join sys_user_degree sal on sa.degree_id = sal.id ");
		Map<String, Object> params = new HashMap<>();
		sb.append(" where a.station_id=:stationId");
		params.put("stationId", stationId);
		if (depositNum != null && begin != null && end != null) {
			if (depositNum == 1) {
				sb.append(" and d.first_time = a.create_datetime");
			} else if (depositNum == 2) {
				sb.append(" and d.sec_time = a.create_datetime");
			} else if (depositNum == 3) {
				sb.append(" and d.third_time = a.create_datetime");
			}
		}
		if (begin != null) {
			sb.append(" and a.create_datetime >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.create_datetime <=:end");
			params.put("end", end);
		}
		if (type != null) {
			sb.append(" and a.deposit_type=:type");
			params.put("type", type);
		}
		if (status != null) {
			sb.append(" and a.status=:status");
			params.put("status", status);
		}
		if (lock != 0) {
			sb.append(" and a.lock_flag=:lock");
			params.put("lock", lock);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and a.user_id in(select id from sys_user where degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			sb.append(" and station_id=:stationId)");
		}
		if (StringUtils.isNotEmpty(payIds)) {
			sb.append(" and a.pay_id  in (");
			for (String lId : payIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (StringUtils.isNotEmpty(onlineCode)) {
			sb.append(" and a.pay_name in (");
			for (String lId : onlineCode.split(",")) {
				sb.append("'" + lId + "'").append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (handType != null) {
			sb.append(" and a.handler_type =:handType");
			params.put("handType", handType);
		}
		if (StringUtils.isNotEmpty(user)) {
			sb.append(" and a.user_name =:user");
			params.put("user", user);
		}
		if (StringUtils.isNotEmpty(orderId)) {
			sb.append(" and a.order_id =:orderId");
			params.put("orderId", orderId);
		}
		if (proxyId != null) {
			sb.append(" AND (a.parent_ids LIKE :proxyIdCn");
			sb.append(" or a.user_id =:proxyId)");
			params.put("proxyId", proxyId);
			params.put("proxyIdCn", "%," + proxyId + ",%");
		}
		// if (StringUtils.isNotEmpty(withCode)) {
		// sb.append(" and a.with_code =:withCode");
		// params.put("withCode", withCode);
		// }
		if (StringUtils.isNotEmpty(opUsername)) {
			sb.append(" and a.op_username =:op_username");
			params.put("op_username", opUsername);
		}
		if (StringUtils.isNotEmpty(payBankName)) {
			sb.append(" and a.pay_bank_name ~ :pay_bank_name ");
			params.put("pay_bank_name", payBankName);
		}
		if (StringUtils.isNotEmpty(agentUser)) {
			sb.append(" and a.agent_username = :agentUser ");
			params.put("agentUser", agentUser);
		}
		sb.append(" and (a.user_type != :guest and a.user_type != :guestBack)");
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		sb.append(" order by a.status Asc,a.create_datetime desc");
		return find(sb.toString(), params);
	}

	// /**
	// * 统计充值金额
	// *
	// * @param stationId
	// * @param userId
	// * @param beginTime
	// * @param endTime
	// * @return
	// */
	// public List<SysAccountDailyMoney> findSysAccountDailyMoney(Long stationId,
	// Long userId, Date beginTime,
	// Date endTime) {
	// //TODO
	// Map<String, Object> params = new HashMap<>();
	// params.put("stationId", stationId);
	// params.put("userId", userId);
	// params.put("beginTime", beginTime);
	// params.put("endTime", endTime);
	//
	// StringBuilder sb = new StringBuilder();
	// sb.append("select sum(money) as depositAmount, to_char(create_datetime,
	// 'YYYY-MM-DD') as statDateStr from");
	// sb.append(" mny_deposit_record where station_id =:stationId and
	// create_datetime >=:beginTime and");
	// sb.append(" create_datetime <=:endTime and user_id =:userId");
	// sb.append(" GROUP BY to_char(create_datetime, 'YYYY-MM-DD')");
	// return find(sb.toString(), new
	// BeanPropertyRowMapper<>(SysAccountDailyMoney.class), params);
	// }

	// API暂不开放
	// public int countByApiOrderId(Long stationId, String orderId) {
	// Map<String, Object> params = new HashMap<>();
	// params.put("stationId", stationId);
	// params.put("orderId", orderId);
	// StringBuilder sb = new StringBuilder("select count(*) from
	// mny_deposit_record");
	// sb.append(" where station_id =:stationId and api_order_id=:orderId");
	// return count(sb.toString(), params);
	// }

	// public MnyDepositRecord findOneByApiOrderId(Long stationId, String orderId) {
	// Map<String, Object> params = new HashMap<>();
	// params.put("stationId", stationId);
	// params.put("orderId", orderId);
	// StringBuilder sb = new StringBuilder("select * from mny_deposit_record");
	// sb.append(" where station_id =:stationId and api_order_id=:orderId");
	// return findOne(sb.toString(), params);
	// }

	// public void updateApiOrderId(String orderId, Long stationId, String
	// apiOrderId) {
	// StringBuilder sb = new StringBuilder();
	// Map<String, Object> params = new HashMap<>();
	// sb.append(" update mny_deposit_record set api_order_id =:apiOrderId");
	// sb.append(" where station_id =:stationId");
	// sb.append(" and order_id =:orderId");
	// params.put("apiOrderId", apiOrderId);
	// params.put("stationId", stationId);
	// params.put("orderId", orderId);
	// update(sb.toString(), params);
	// }

	public void changeRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update mny_deposit_record set remark =:remark");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("remark", remark);
		update(sb.toString(), params);
	}

	public void changeBgRemark(Long id, Long stationId, String remark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update mny_deposit_record set bg_remark =:remark");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("remark", remark);
		update(sb.toString(), params);
	}

	public MnyDepositRecord getHighestMoneyDepositRecord(Long stationId, Long userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		StringBuilder sb = new StringBuilder("select * from mny_deposit_record");
		sb.append(" where station_id =:stationId and user_id=:userId");
		sb.append(" and status = :status");
		params.put("status", MnyDepositRecord.STATUS_SUCCESS);
		sb.append(" order by money desc limit 1");
		return findOne(sb.toString(), params);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime, Long stationId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" delete from mny_deposit_record");
		sb.append(" where station_id =:stationId ");
		Map<String, Object> map = new HashMap<>();
		sb.append(" and create_datetime <= :createTime");
		map.put("createTime", createTime);
		map.put("stationId", stationId);
		return update(sb.toString(), map);
	}

	public Integer getCountOfFirstDeposit(Long stationId, Integer status, Integer lockFlag, Date begin, Date end, Long userId) {
		StringBuilder sb = new StringBuilder("select count(*) from mny_deposit_record");
		Map<String, Object> map = new HashMap<>();
		sb.append(" where station_id =:stationId ");
		map.put("stationId", stationId);
		if (userId != null) {
			sb.append(" and user_id=:userId");
			map.put("userId", userId);
		}
		if (status != null) {
			sb.append(" and status=:status");
			map.put("status", status);
		}
		if (lockFlag != null) {
			sb.append(" and lock_flag=:lockFlag");
			map.put("lockFlag", lockFlag);
		}
		if (begin != null) {
			sb.append(" and create_datetime >=:begin");
			map.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <=:end");
			map.put("end", end);
		}
		return queryForInt(sb.toString(), map);
	}

	public Integer countDepositTimeAfterLastDraw(Long userId, Long stationId, Date lastDrawDate, Date now) {
		StringBuilder sb = new StringBuilder("select count(*) from mny_deposit_record");
		Map<String, Object> map = new HashMap<>();
		sb.append(" where station_id =:stationId and user_id=:userId");
		sb.append(" and create_datetime >=:lastDrawDate");
		sb.append(" and create_datetime <=:now");
		sb.append(" and status=:status");
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("lastDrawDate", lastDrawDate);
		map.put("now", now);
		map.put("status", MnyDepositRecord.STATUS_SUCCESS);
		return queryForInt(sb.toString(), map);
	}

	public BigDecimal countDepositAmountAfterLastDraw(Long userId, Long stationId, Date lastDrawDate, Date now) {
		StringBuilder sb = new StringBuilder("select COALESCE(sum(money),0) from mny_deposit_record");
		Map<String, Object> map = new HashMap<>();
		sb.append(" where station_id =:stationId and user_id=:userId");
		sb.append(" and create_datetime >=:lastDrawDate");
		sb.append(" and create_datetime <=:now");
		sb.append(" and status=:status");
		sb.append(" group by user_id");
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("lastDrawDate", lastDrawDate);
		map.put("now", now);
		map.put("status", MnyDepositRecord.STATUS_SUCCESS);
		return findObject(sb.toString(), map, BigDecimal.class);
	}

	public List<StationDailyDeposit> getDepositDailyReport(Long stationId, Date begin, Date end, Integer status) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				" SELECT M.deposit_type,M.pay_name,( SELECT COUNT ( * ) FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS deposit_times, ");
		sb.append(
				" ( SELECT COUNT ( * )  FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name and status=:status AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS  success_times, ");
		sb.append(
				" ( SELECT SUM (money) FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS deposit_amount, ");
		sb.append(
				" ( SELECT SUM (money) FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND status=:status AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS success_amount, ");
		sb.append(
				" ( SELECT MAX (money) FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND status=:status AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS max_money, ");
		sb.append(
				" ( SELECT MIN (money)  FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND status=:status AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS min_money, ");
		sb.append(
				" (SELECT COUNT( DISTINCT user ) FROM mny_deposit_record WHERE station_id =:stationId AND pay_name = M.pay_name AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS deposit_user, ");
		sb.append(
				" (SELECT COUNT( DISTINCT user ) FROM mny_deposit_record WHERE station_id =:stationId AND status!=2  AND pay_name = M.pay_name AND (user_type != :guest AND user_type != :guestBack) AND create_datetime >= :begin AND create_datetime <= :end ) AS failed_times ");
		sb.append(
				" FROM mny_deposit_record AS M WHERE station_id =:stationId  AND (user_type != :guest AND user_type != :guestBack) AND M.create_datetime >= :begin AND M.create_datetime <= :end  ");
		sb.append(" GROUP BY M.pay_name, M.deposit_type ORDER BY M.pay_name ASC ");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("status", status);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("begin", begin);
		params.put("end", end);
		return find(sb.toString(), new BeanPropertyRowMapper<>(StationDailyDeposit.class), params);
	}

	public DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("select COALESCE(sum(money),0) as money ,");
		sb.append(" count (*) as times from mny_deposit_record");
		sb.append(" where station_id=:stationId and user_id=:userId and status=2 ");
		sb.append(" and create_datetime>=:start and create_datetime < :end");
		paramMap.put("start", start);
		paramMap.put("end", end);
		return findOne(sb.toString(), paramMap, DailyMoneyVo.class);
	}

	public int updateRecovery(MnyDepositRecord record) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", record.getId());
		params.put("stationId", record.getStationId());
		params.put("status", MnyDepositRecord.STATUS_UNDO);
		params.put("lockFlag", MnyDepositRecord.LOCK_FLAG_UNLOCK);
		return update(
				"update mny_deposit_record set status =:status, lock_flag =:lockFlag  where id =:id and station_id =:stationId",
				params);
	}

	/**
	 * 人工加款是否有效 isHandDeal 充值时间段 startTime endTime
	 */
	public BigDecimal sumRechargeForDate(Date startDate, Date endDate, Long userId, Long stationId,
			boolean isHandDeal) {
		Map<String, Object> paramMap = new HashMap<>();
		StringBuilder sb = new StringBuilder("select sum(money) from mny_deposit_record where station_id=:stationId");
		sb.append(" and user_id = :userId");
		sb.append(" and status=").append(MnyDepositRecord.STATUS_SUCCESS);// 成功的
		if (!isHandDeal) {
			sb.append(" and type <> 1");
		}
		if (startDate != null) {
			sb.append(" and create_datetime >= :startDate");
		}
		if (endDate != null) {
			sb.append(" and create_datetime <= :endDate");
		}
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

	/**
	 * 获取某天最后一条充值信息
	 */
	public MnyDepositRecord getOnedayNewestRecord(Long stationId, Long userId, Date start, Date end, Integer status) {
		StringBuilder sb = new StringBuilder("select * from mny_deposit_record");
		sb.append(" where station_id =:stationId and user_id =:userId");
		sb.append(" and create_datetime >= :start and create_datetime <= :end");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("end", end);
		map.put("start", start);
		if (status != null) {
			sb.append(" and status = :status");
			map.put("status", status);
		}
		sb.append(" order by create_datetime desc limit 1");
		return findOne(sb.toString(), map);
	}

	public BigDecimal oneDayFirstDeposit(Long userId, Date depositDate, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		StringBuilder sb = new StringBuilder("select money from mny_deposit_record where station_id=:stationId");
		if (depositDate != null) {
			sb.append(" and to_char(create_datetime,'YYYY-MM-DD')=:date");
			paramMap.put("date", DateUtil.toDateStr(depositDate));
		}
		sb.append(" and user_id=:userId and status=2");
		sb.append(" order by create_datetime limit 1");
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		return findObject(sb.toString(), paramMap, BigDecimal.class);
	}

}
