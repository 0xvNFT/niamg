package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.core.UserPermEnum;
import com.play.core.UserTypeEnum;
import com.play.model.SysUserPerm;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 会员权限
 *
 * @author admin
 *
 */
@Repository
public class SysUserPermDao extends JdbcRepository<SysUserPerm> {

	public Page<SysUserPerm> page(Long stationId, String username, String proxyUsername, Integer userType,
			Integer type) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select * from sys_user_perm where station_id =:stationId");
		map.put("stationId", stationId);
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and username=:username");
			map.put("username", username);
		}
		if (StringUtils.isNotEmpty(proxyUsername)) {
			sb.append(" AND user_id in(select id from sys_user where ");
			sb.append("station_id =:stationId and parent_names like :proxyName)");
			map.put("proxyName", "%," + proxyUsername.trim().toLowerCase() + ",%");
		}
		// 试玩账号不显示
		if (userType != null) {
			if(GuestTool.isGuest(userType)) {
				// 只要为试玩类型，都查前后台试玩账户
				sb.append(" AND (user_type = :guest OR user_type = :guestBack) ");
				map.put("guest", UserTypeEnum.GUEST.getType());
				map.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
			} else {
				sb.append(" AND user_type =:userType");
				map.put("userType", userType);
			}
		} else {
			sb.append(" AND (user_type != :guest AND user_type != :guestBack) ");
			map.put("guest", UserTypeEnum.GUEST.getType());
			map.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		}
		if (type != null) {
			sb.append(" and type = :type");
			map.put("type", type);
		}
		sb.append(" order by user_id desc");
		return queryByPage(sb.toString(), map);
	}

	public void updateStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE sys_user_perm SET status=:status WHERE id =:id and station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_PERM);
	}

	public UserPermVo getPerm(Long userId, Long stationId) {
		String key = new StringBuilder("sysPerm").append(String.valueOf(stationId)).append(":uid:").append(userId).toString();
		UserPermVo p = CacheUtil.getCache(CacheKey.USER_PERM, key, UserPermVo.class);
		if (p != null) {
			return p;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		List<SysUserPerm> l = find("select type,status from sys_user_perm where user_id=:userId and station_id=:stationId", map);
		p = new UserPermVo();
		if (l != null) {
			for (SysUserPerm up : l) {
				UserPermEnum perm = UserPermEnum.getPerm(up.getType());
				if (perm == null) {
					continue;
				}
				switch (perm) {
				case deposit:
					p.setDeposit(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case withdraw:
					p.setWithdraw(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case thirdConvert:
					p.setThirdConvert(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case liveBet:
					p.setLiveBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case egameBet:
					p.setEgameBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case chessBet:
					p.setChessBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case sportBet:
					p.setSportBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case fishingBet:
					p.setFishingBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case esportBet:
					p.setEsportBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case lotteryBet:
					p.setLotteryBet(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case rebate:
					p.setRebate(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case proxyRebate:
					p.setProxyRebate(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case signIn:
					p.setSignIn(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case turntable:
					p.setTurntable(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case redEnvelope:
					p.setRedEnvelope(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case exchangeScore:
					p.setExchangeScore(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case activeApply:
					p.setActiveApply(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case depositGift:
					p.setDepositGift(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case upgradeGift:
					p.setUpgradeGift(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				case moneyIncome:
					p.setMoneyIncome(Objects.equals(up.getStatus(), Constants.STATUS_ENABLE));
					break;
				}
			}
			CacheUtil.addCache(CacheKey.THIRD_PLATFORM, key, p);
		}
		return p;
	}

	public SysUserPerm findOne(Long userId, Long stationId, int type) {
		String key = new StringBuilder("uid:").append(userId).append(":").append(stationId).append(":").append(type)
				.toString();
		SysUserPerm p = CacheUtil.getCache(CacheKey.USER_PERM, key, SysUserPerm.class);
		if (p != null) {
			return p;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		map.put("type", type);
		p = findOne("select * from sys_user_perm where user_id=:userId and station_id=:stationId and type=:type", map);
		if (p != null) {
			CacheUtil.addCache(CacheKey.USER_PERM, key, p);
		}
		return p;
	}

	public int changeUserType(Long userId, Long stationId, int userType) {
		Map<String, Object> map = new HashMap<>();
		map.put("userType", userType);
		map.put("stationId", stationId);
		map.put("userId", userId);
		int count = update("UPDATE sys_user_perm SET user_type = :userType WHERE user_id = :userId and station_id = :stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_PERM);
		return count;
	}
}
