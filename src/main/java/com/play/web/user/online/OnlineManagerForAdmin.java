package com.play.web.user.online;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.play.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.play.cache.CacheKey;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.security.MD5Util;
import com.play.model.AdminUser;
import com.play.web.exception.user.NotLoginException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;

public class OnlineManagerForAdmin {
	public static final String USER_ONLINE_SESSION_ID = "admin:sid:";

	public static final int OVERDATETIME = 18000;// 5小时过期

	public static void doOnline(AdminUser user) {
		HttpSession session = ServletUtils.getSession();
		RedisAPI.addCache(getOnlineKey(user.getId(),user.getStationId()), MD5Util.sessionIdMd5(session.getId()), OVERDATETIME,
				CacheKey.ADMIN_ONLINE.getDb());
		session.setAttribute(Constants.SESSION_KEY_ADMIN, user);
	}


	public static void doOffLine() {
		HttpSession session = ServletUtils.getSession();
		AdminUser user = (AdminUser) session.getAttribute(Constants.SESSION_KEY_ADMIN);
		session.removeAttribute(Constants.SESSION_KEY_ADMIN);
		if (user != null) {
			RedisAPI.delCache(CacheKey.ADMIN_ONLINE.getDb(), getOnlineKey(user.getId(),user.getStationId()));
		}
	}

	private static String getOnlineKey(Long uId,Long stationId) {
		return new StringBuilder(USER_ONLINE_SESSION_ID).append(stationId).append(":aid:").append(uId).toString();
	}

	private static String getOnlineKeyProxy(Long uId,Long stationId) {
		return new StringBuilder(USER_ONLINE_SESSION_ID).append(stationId).append(":pid:").append(uId).toString();
	}

	public static void checkLoginError(Long userId,Long stationId) {
		HttpSession session = ServletUtils.getSession();
		String key = getOnlineKey(userId,stationId);
		int db = CacheKey.ADMIN_ONLINE.getDb();
		String err = RedisAPI.getCache(key, db);
		if (StringUtils.isEmpty(err)) {
			session.removeAttribute(Constants.SESSION_KEY_ADMIN);
			throw new NotLoginException(BaseI18nCode.loginTimeout);
		} else if (!err.equals(MD5Util.sessionIdMd5(session.getId()))) {
			session.removeAttribute(Constants.SESSION_KEY_ADMIN);
			throw new NotLoginException(BaseI18nCode.loginInOtherPlace);
		}
		RedisAPI.addCache(key, err, OVERDATETIME, db);
	}

	public static void proxyCheckLoginError(SysUser sysUser) {
		HttpSession session = ServletUtils.getSession();
		String key = OnlineManager.getOnlineSessionId(sysUser);
		String err = RedisAPI.getCache(key, CacheKey.USER_ONLINE.getDb());
		if (StringUtils.isEmpty(err)) {
			session.removeAttribute(Constants.SESSION_KEY_PROXY);
			throw new NotLoginException(BaseI18nCode.loginTimeout);
		} else if (!err.equals(session.getId())) {
			session.removeAttribute(Constants.SESSION_KEY_PROXY);
			throw new NotLoginException(BaseI18nCode.loginInOtherPlace);
		}
		RedisAPI.addCache(key, err, OVERDATETIME, CacheKey.USER_ONLINE.getDb());
	}


	public static boolean checkOnline(Long uId,Long stationId) {
		return StringUtils.isNotEmpty(RedisAPI.getCache(getOnlineKey(uId,stationId),CacheKey.ADMIN_ONLINE.getDb()));
	}

	public static Set<Long> getOnlineUserIds(Long stationId) {
		String key = USER_ONLINE_SESSION_ID + stationId;
		Set<String> keys = RedisAPI.getKeys(key, CacheKey.ADMIN_ONLINE.getDb());
		Set<Long> set = new HashSet<>();
		if (keys != null && !keys.isEmpty()) {
			for (String s : keys) {
				set.add(NumberUtils.toLong(s.substring(s.lastIndexOf(":") + 1)));
			}
			return set;
		}
		return null;
	}
}
