package com.play.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.core.UserTypeEnum;
import com.play.model.SysUserMoney;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员余额信息表
 *
 * @author admin
 *
 */
@Repository
public class SysUserMoneyDao extends JdbcRepository<SysUserMoney> {

	public void init(Long userId, BigDecimal money) {
		update("INSERT INTO sys_user_money(user_id, money) VALUES (" + userId + "," + money + ")");
	}

	public SysUserMoney findOneForUpdate(Long userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		return findOne("select * from sys_user_money where user_id=:userId for update", map);
	}

	public BigDecimal getMoney(Long userId) {
		if (userId == null || userId == 0) {
			return null;
		}
		String key = getKey(userId);
		BigDecimal money = CacheUtil.getCache(CacheKey.MONEY, key, BigDecimal.class);
		if (money == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			money = findObject("select money from sys_user_money where user_id=:userId", map, BigDecimal.class);
			if (money != null) {
				CacheUtil.addCache(CacheKey.MONEY, key, money);
			}
		}
		return money;
	}

	private String getKey(Long userId) {
		return new StringBuilder("id:").append(userId).toString();
	}

	public void updateMoney(SysUserMoney sym) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", sym.getUserId());
		map.put("money", sym.getMoney());
		update("update sys_user_money set money=:money where user_id=:id ", map);
		CacheUtil.delCache(CacheKey.MONEY, getKey(sym.getUserId()));
	}

	public BigDecimal teamMoneyCount(Long userId, Long stationId, boolean isRecommend) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sb = new StringBuilder("select sum(a.money) from sys_user_money a");
		sb.append(" left join sys_user b on b.id =a.user_id where b.station_id =:stationId");
		if (userId != null) {
			map.put("userId", userId);
			if (isRecommend) {// 会员推荐
				sb.append(" and (a.user_id=:userId or b.recommend_id=:userId)");
			} else {
				sb.append(" and (a.user_id=:userId OR b.parent_ids like :userIdCn)");
				map.put("userIdCn", "%," + userId + ",%");
			}
		}
		sb.append(" and b.status=:status and (b.type != :guest and b.type != :guestBack)");
		map.put("status", Constants.STATUS_ENABLE);
		map.put("guest", UserTypeEnum.GUEST.getType());
		map.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		map.put("stationId", stationId);
		return findObject(sb.toString(), map, BigDecimal.class);
	}

	public BigDecimal stationMoneyCount(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("status", Constants.STATUS_ENABLE);
		map.put("guest", UserTypeEnum.GUEST.getType());
		map.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		StringBuilder sb = new StringBuilder("select sum(a.money) from sys_user_money a");
		sb.append(" left join sys_user b on b.id =a.user_id where b.station_id =:stationId");
		sb.append(" and b.status=:status and (b.type != :guest and b.type != :guestBack)");
		return findObject(sb.toString(), map, BigDecimal.class);
	}

	public Object teamMoneyCount(Long id, Long stationId) {
		// TODO Auto-generated method stub
		return null;
	}

}
