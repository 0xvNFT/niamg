package com.play.common.utils;

import org.apache.commons.lang3.math.NumberUtils;

import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;

/**
 * 代理模式工具类
 * 
 * <pre>
 * StationConfigEnum.proxy_model
 *     值 1=全部代理，2=多级代理+会员，3=一级代理+会员，4=全部会员
 * </pre>
 * 
 * @author macair
 *
 */
public class ProxyModelUtil {
	public static final int all_proxy = 1;// 全部代理
	public static final int multi_proxy = 2;// 多级代理+会员
	public static final int one_proxy = 3;// 一级代理+会员
	public static final int all_member = 4;// 全部会员

	public static int getProxyModel(Long stationId) {
		String v = StationConfigUtil.get(stationId, StationConfigEnum.proxy_model);
		return NumberUtils.toInt(v, all_proxy);
	}

	public static boolean isAllProxy(Long stationId) {
		return getProxyModel(stationId) == all_proxy;
	}

	public static boolean isAllProxy(int proxyModel) {
		return proxyModel == all_proxy;
	}

	public static boolean isMultiProxy(Long stationId) {
		return getProxyModel(stationId) == multi_proxy;
	}

	public static boolean isMultiProxy(int proxyModel) {
		return proxyModel == multi_proxy;
	}

	public static boolean isMultiOrAllProxy(Long stationId) {
		int proxyModel = getProxyModel(stationId);
		return isMultiOrAllProxy(proxyModel);
	}

	public static boolean isMultiOrAllProxy(int proxyModel) {
		return proxyModel == multi_proxy || proxyModel == all_proxy;
	}

	public static boolean isOneProxy(Long stationId) {
		return getProxyModel(stationId) == one_proxy;
	}

	public static boolean isOneProxy(int proxyModel) {
		return proxyModel == one_proxy;
	}

	public static boolean isAllMember(Long stationId) {
		return getProxyModel(stationId) == all_member;
	}

	public static boolean isAllMember(int proxyModel) {
		return proxyModel == all_member;
	}

	/**
	 * 代理是否可以创建推广链接 有代理的才能创建代理推广
	 * 
	 * @param proxyModel
	 * @return
	 */
	public static boolean canBePromo(Long stationId) {
		return getProxyModel(stationId) != all_member;
	}

	/**
	 * 代理是否可以创建推广链接 有代理的才能创建代理推广
	 * 
	 * @param proxyModel
	 * @return
	 */
	public static boolean canBePromo(int proxyModel) {
		return proxyModel != all_member;
	}

	/**
	 * 是否可以创建会员推荐好友 有会员的才能创建，且站点开启 会员推荐好友开关 这个开关
	 * 
	 * @param stationId
	 * @param proxyModel
	 * @return
	 */
	public static boolean canBeRecommend(Long stationId, int proxyModel) {
		return proxyModel != all_proxy && StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_member_recommend);
	}

	/**
	 * 是否可以创建会员推荐好友 有会员的才能创建，且站点开启 会员推荐好友开关 这个开关
	 * 
	 * @param stationId
	 * @return
	 */
	public static boolean canBeRecommend(Long stationId) {
		return StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_member_recommend)
				&& getProxyModel(stationId) != all_proxy;
	}
}
