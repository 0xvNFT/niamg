package com.play.cache;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public enum CacheKey {
	DEFAULT("默认", 0, 0),

	MANAGER("总控权限", 3600, 1),

	PARTNER("合作商", 3600, 1),

	ADMIN("站点管理员", 3600, 1),

	AGENT("代理商", 3600, 1),

	// 保存一星期
	USER_ONLINE("在线会员", 604800, 2),

	ADMIN_ONLINE("在线站点管理员", 604800, 2),

	ONLINE_USER_ID_SET("在线会员ID Set", 87000, 2),

	USER_PROMO_CODE("推广码",87000,2),

	ONLINE_WARN_USER("告警会员", 604800, 3),

	SYS_CONFIG("系统配置", 3600, 3),

	STATION_CONFIG("站点配置", 10800, 3),

	THIRD_PLATFORM("第三方游戏开关", 10800, 3),

	REGISTER_CONFIG("注册配置", 3600, 4),

	STATION_DOMAIN("站点域名", 3600, 5),

	USER_INFO("会员信息", 3600, 7),
	ACTIVATE_DATA("激活码信息", 3600, 7),

	USER_DEGREE("会员等级", 10, 7),

	USER_GROUP("会员组别", 10, 7),

	USER_PERM("会员权限", 3600, 7),

	REBATE_SCALE("反水返点信息", 3600, 7),

	MONEY("余额", 20, 8),

	SCORE("积分", 20, 8),

	STATISTIC("报表统计", 86400, 11),

	RED_PACKET("红包", 0, 12),
	INVITE_DATA("邀请数据", 60, 12),

	PARTNER_IPS("合作商后台IP白名单", 86400, 13),

	STATION_IPS("站点后台IP白名单", 864000, 13),

	AGENT_IPS("代理商后台的IP白名单", 86400, 13),

	FRONT_STATIOIN_IPS("站点前台IP白名单", 86400, 13),

	LOGIN_FAIL_IP("登陆失败账号IP", 604800, 13),

	TURNTABLE("大转盘", 86400, 15),

	STATION_REGION_IP_NETS("当前站点国家IP网络段", 86400, 13),

	/**
	 * 原生相关
	 */
	NATIVE("原生数据", 3600, 6),
	SEARCH_GAMES("搜索数据", 86400, 6),

	KICKBACK_STRATEGY("返水策略", 86400, 15),

	THIRD_AUTO_EXCHANGE("第三方游戏额度自动转换", 86400, 14),

	USER_FOOT_GAME_LIST("用户足迹游戏列表", 60, 8),

	STATION_FAKE_DATA("站点假数据", 15, 8),

	;

	private String title;
	private int timeout;// 超时时间单位（秒），0：永不超时
	private int db;

	public String getTitle() {
		return title;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getDb() {
		return db;
	}

	/**
	 * 自定义过期时间和库
	 *
	 * @param _timeout
	 * @param _db
	 */
	private CacheKey(String title, int _timeout, int _db) {
		this.title = title;
		this.timeout = _timeout;
		this.db = _db;
	}

	public static List<CacheKey> getList() {
		List<CacheKey> list = new ArrayList<>();
		for (CacheKey r : values()) {
			list.add(r);
		}
		list.sort(new Comparator<CacheKey>() {
			@Override
			public int compare(CacheKey o1, CacheKey o2) {
				return o1.getDb() - o2.getDb();
			}
		});
		return list;
	}
}
