package com.play.web.user.online;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.SpringUtils;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.model.SysUser;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserService;
import com.play.web.exception.user.NotLoginException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;

public class OnlineManager {
	private static Logger logger = LoggerFactory.getLogger(OnlineManager.class);
	public static final String USER_ONLINE_SESSION_ID = "online:sid:";
	public static final String SPRING_SESSION_KEY = "spring:session:sessions:";
	public static final String USER_ONLINE_WARN_SESSION_ID = "ONLINE_WARN_USER:";
	public static final String USER_ONLINE_WARN_VOICE_NOTICE = "USER_ONLINE_WARN_VOICE_NOTICE:";
	public static final String USER_ONLINE_WARN_BETTING_SESSION_ID = "ONLINE_WARN_BETTING_USER:";

	public static final int OVERDATETIME = 7 * 24 * 60 * 60;

	public static void doOnline(SysUser user, String sessionKey) {
		HttpSession session = ServletUtils.getSession();
		SysUser old = (SysUser) session.getAttribute(sessionKey);
		// 该session 已经有用户登录，需要先将他踢下线
		if (old != null && !old.getId().equals(user.getId())) {
			updateAccountOffline(old);
		}
		String key = getOnlineSessionId(user);
		RedisAPI.addCache(key, session.getId(), OVERDATETIME, CacheKey.USER_ONLINE.getDb());
		OnlineUserNumTool.addOnlieNum(user.getStationId(), user.getId());
		session.setAttribute(sessionKey, user);
	}

	public static void doOffLine(String sessionKey) {
		try {
			HttpSession session = ServletUtils.getSession();
			SysUser old = (SysUser) session.getAttribute(sessionKey);
			session.removeAttribute(sessionKey);
			updateAccountOffline(old);
			// 下线时清除会员告警
			delOnlineWarnUser(old, false);
			delOnlineWarnUser(old, true);
		} catch (Exception e) {
			logger.error("注销发生错误", e);
		}
	}

	public static void forcedOffLine(Long userId, Long stationId) {
		if (userId == null || stationId == null) {
			return;
		}
		SysUser old = SpringUtils.getBean(SysUserService.class).findOneById(userId, stationId);
		updateAccountOffline(old);
	}

	public static Integer getOnlineCount(Long stationId, Long proxyId) {
		StringBuilder sb = new StringBuilder(USER_ONLINE_SESSION_ID);
		sb.append(stationId).append(":1[23]0:");// UserTypeEnum的值
		if (proxyId != null) {
			sb.append("*,").append(proxyId).append(",");
		}
		Set<String> keys = RedisAPI.getKeys(sb.toString(), CacheKey.USER_ONLINE.getDb());
		if (keys != null && !keys.isEmpty()) {
			return keys.size();
		}
		return 0;
	}

	public static Set<Long> getOnlineUserIds() {
		int db = CacheKey.USER_ONLINE.getDb();
		Set<String> list = RedisAPI.getKeys(USER_ONLINE_SESSION_ID, db);
		Set<Long> set = null;
		if (list != null && !list.isEmpty()) {
			set = new HashSet<>();
			String sId = null;
			for (String s : list) {
				sId = RedisAPI.getCache(s, db);
				if (RedisAPI.exists(SPRING_SESSION_KEY + sId)) {
					set.add(NumberUtils.toLong(s.substring(s.lastIndexOf(":") + 1)));
				} else {
					RedisAPI.delCache(db, s);
					delOnlineWarnUserBySessionKey(s);
				}
			}
		}
		return set;
	}

	private static void updateAccountOffline(SysUser acc) {
		if (acc != null) {
			RedisAPI.delCacheByExp(getOfflineSessionId(acc), CacheKey.USER_ONLINE.getDb());
			SpringUtils.getBean(SysUserLoginService.class).updateUserOffline(acc.getId(), acc.getStationId());
			delOnlineWarnUser(acc, true);
			delOnlineWarnUser(acc, false);
			// 清楚手机自动登陆信息
			CacheUtil.delCache(CacheKey.DEFAULT,
					EncryptDataUtil.getAutoLoginKey(acc.getUsername(), acc.getStationId()));
		}
	}

	public static void checkLoginError(HttpSession session, SysUser user, String sessionKey) {
		String err = RedisAPI.getCache(getOnlineSessionId(user), CacheKey.USER_ONLINE.getDb());
		if (StringUtils.isEmpty(err)) {
			session.removeAttribute(sessionKey);
			SpringUtils.getBean(SysUserLoginService.class).updateUserOffline(user.getId(), user.getStationId());
			removeOldParentSession(user);
			throw new NotLoginException(BaseI18nCode.loginTimeout);

		} else if (!err.equals(session.getId())) {
			session.removeAttribute(sessionKey);
			SpringUtils.getBean(SysUserLoginService.class).updateUserOffline(user.getId(), user.getStationId());
			// removeOldParentSession(acc);
			throw new NotLoginException(BaseI18nCode.loginInOtherPlace);
		}
	}

	private static void removeOldParentSession(SysUser user) {
		RedisAPI.delCacheByExp(getOfflineSessionId(user), CacheKey.USER_ONLINE.getDb());
	}

	private static String getOfflineSessionId(SysUser user) {
		StringBuilder sb = new StringBuilder(USER_ONLINE_SESSION_ID);
		sb.append(user.getStationId()).append(":*:").append(user.getId());
		return sb.toString();
	}

	public static String getOnlineSessionId(SysUser user) {
		StringBuilder sb = new StringBuilder(USER_ONLINE_SESSION_ID);
		sb.append(user.getStationId()).append(":").append(user.getType());
		sb.append(":").append(StringUtils.trimToEmpty(user.getParentIds()));
		sb.append(":,").append(user.getRecommendId() == null ? "" : user.getRecommendId());
		sb.append(",:").append(user.getId());
		return sb.toString();
	}

	/**
	 * 第三方体育回调会员是否在线。
	 *
	 * @param sysAccount
	 * @return
	 */
	public static boolean returnUserOnlineBoolean(SysUser user) {
		String session = getOnlineSessionId(user);
		String sId = RedisAPI.getCache(session, CacheKey.USER_ONLINE.getDb());
		if (RedisAPI.exists(SPRING_SESSION_KEY + sId)) {
			return true;
		}
		return false;
	}

	public static void doOnlineWarnUser(SysUser user, boolean isBet) {
		if (user.getOnlineWarn() != null && user.getOnlineWarn() == Constants.STATUS_ENABLE) {
			String key = (isBet ? USER_ONLINE_WARN_BETTING_SESSION_ID : USER_ONLINE_WARN_SESSION_ID)
					+ user.getStationId() + ":aid:" + user.getId();
			RedisAPI.addCache(key, user.getId().toString(), CacheKey.ONLINE_WARN_USER.getTimeout(),
					CacheKey.ONLINE_WARN_USER.getDb());
			// 登陆加入声音提示
			if (!isBet) {
				key = USER_ONLINE_WARN_VOICE_NOTICE + ":sid:" + user.getStationId();
				RedisAPI.addCache(key, "true", CacheKey.ONLINE_WARN_USER.getTimeout(),
						CacheKey.ONLINE_WARN_USER.getDb());
			}
		}
	}

	public static void delOnlineWarnUser(SysUser user, boolean isBet) {
		if (user != null && user.getOnlineWarn() != null && user.getOnlineWarn() == Constants.STATUS_ENABLE) {
			String key = (isBet ? USER_ONLINE_WARN_BETTING_SESSION_ID : USER_ONLINE_WARN_SESSION_ID)
					+ user.getStationId() + ":aid:" + user.getId();
			RedisAPI.delCache(CacheKey.ONLINE_WARN_USER.getDb(), key);
		}
	}

	public static int getOnlineWarnNum(Long stationId, boolean isBet) {
		String key = (isBet ? USER_ONLINE_WARN_BETTING_SESSION_ID : USER_ONLINE_WARN_SESSION_ID) + stationId + ":";
		Set<String> keys = RedisAPI.getKeys(key, CacheKey.ONLINE_WARN_USER.getDb());
		if (keys != null && !keys.isEmpty()) {
			return keys.size();
		}
		return 0;
	}

	public static void delOnlineWarnUserBySessionKey(String sessionKey) {
		// 清除告警缓存
		String[] sIds = sessionKey.split(":");
		if (sIds.length > 5) {
			String key = USER_ONLINE_WARN_SESSION_ID + sIds[2] + ":aid:" + sIds[6];
			RedisAPI.delCache(CacheKey.ONLINE_WARN_USER.getDb(), key);
			// 清除下注告警
			key = USER_ONLINE_WARN_BETTING_SESSION_ID + sIds[2] + ":aid:" + sIds[6];
			RedisAPI.delCache(CacheKey.ONLINE_WARN_USER.getDb(), key);
		}
	}
}
