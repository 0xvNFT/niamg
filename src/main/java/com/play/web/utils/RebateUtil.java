package com.play.web.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.SpringUtils;
import com.play.core.StationConfigEnum;
import com.play.model.StationRebate;
import com.play.model.SysUserRebate;
import com.play.service.StationRebateService;
import com.play.service.SysUserRebateService;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 返点工具类
 */
public class RebateUtil {

	private final static BigDecimal FIXED_KICK_BACK_DIFF = new BigDecimal("0.1");

	/**
	 * 获取其他返点值例表
	 *
	 * @param proxyRebate 代理返点
	 */
	public static JSONArray getRebateArray(BigDecimal proxyRebate, BigDecimal diff, BigDecimal maxDiff) {
		Long stationId = SystemUtil.getStationId();
		BigDecimal rebate = proxyRebate.subtract(diff);
		BigDecimal minRebate = BigDecimal.ZERO;
		if (maxDiff != null) {
			minRebate = proxyRebate.subtract(maxDiff).stripTrailingZeros();
		}
		JSONArray array = new JSONArray();
		JSONObject jsonObject;
		if (rebate.compareTo(minRebate) < 0) {
			jsonObject = new JSONObject();
			jsonObject.put("value", 0);
			jsonObject.put("label", "0‰");// 千分比
			array.add(jsonObject);
			return array;
		}
		String chooseList = StationConfigUtil.get(stationId, StationConfigEnum.rebate_choose_list);

		if (StringUtils.isNotEmpty(chooseList)) {
			String[] chooses = chooseList.split(",");
			BigDecimal b = null;
			for (String i : chooses) {
				b = new BigDecimal(i);
				if (b.compareTo(rebate) > 0 || b.compareTo(minRebate) < 0) {
					continue;
				}
				jsonObject = new JSONObject();
				jsonObject.put("value", i);
				jsonObject.put("label", i + "‰");
				array.add(jsonObject);
			}
		} else {
			if (diff.compareTo(BigDecimal.ZERO) <= 0) {
				diff = FIXED_KICK_BACK_DIFF;
			}
			int num = BigDecimalUtil.divide(rebate.subtract(minRebate), diff).intValue();
			BigDecimal normalValue = minRebate;
			if (num > 0) {
				for (int i = num; i >= 0; i--) {
					jsonObject = new JSONObject();
					jsonObject.put("value", normalValue);
					jsonObject.put("label", normalValue + "‰");// 千分比
					normalValue = normalValue.add(diff).stripTrailingZeros();
					array.add(jsonObject);
				}
			} else {
				jsonObject = new JSONObject();
				jsonObject.put("value", 0);
				jsonObject.put("label", "0‰");// 千分比
				array.add(jsonObject);
			}
		}
		array.sort((x, y) -> JSONObject.parseObject(x.toString()).getDouble("value") >= JSONObject
				.parseObject(y.toString()).getDouble("value") ? -1 : 1);
		return array;
	}

	public static void verifyThirdRebate(BigDecimal rebate, BigDecimal proxyRebate, BigDecimal diff, BigDecimal maxDiff,
			String thirdName) {
		if (rebate == null || rebate.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if (proxyRebate.compareTo(rebate.add(diff)) < 0) {
			throw new BaseException(thirdName + "返点值必须小于代理的" + diff + "‰");
		}
		if (maxDiff != null && maxDiff.compareTo(BigDecimal.ZERO) > 0
				&& proxyRebate.compareTo(rebate.add(maxDiff)) > 0) {
			throw new BaseException("当前拿取的下级" + thirdName + "返点超过平台允许的" + maxDiff + "‰返点差值");
		}
	}

	/**
	 * map的key不能修改 获取体育，真人，电子奖金组
	 */
	public static void getRebateMap(Long stationId, Long proxyId, Map<String, Object> map) {
		StationRebate sr = SpringUtils.getBean(StationRebateService.class).findOne(stationId,
				StationRebate.USER_TYPE_PROXY);
		if (sr == null) {
			return;
		}
		if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {
			map.put("fixRebate", true);// key不能修改
			map.put("sr", sr);// key不能修改
			return;
		}
		map.put("fixRebate", false);// key不能修改
		BigDecimal diff = sr.getLevelDiff();
		BigDecimal maxDiff = sr.getMaxDiff();
		if (proxyId == null) {
			putDefaultProxyRebateArray(map, sr, diff, maxDiff);
		} else {
			SysUserRebate ps = SpringUtils.getBean(SysUserRebateService.class).findOne(proxyId, stationId);
			if (ps == null) {
				putDefaultProxyRebateArray(map, sr, diff, maxDiff);
			} else {
				map.put("curRebate", ps);
				// key不能修改
				map.put("liveArray", getRebateArray(ps.getLive(), diff, maxDiff));
				map.put("egameArray", getRebateArray(ps.getEgame(), diff, maxDiff));
				map.put("sportArray", getRebateArray(ps.getSport(), diff, maxDiff));
				map.put("chessArray", getRebateArray(ps.getChess(), diff, maxDiff));
				map.put("esportArray", getRebateArray(ps.getEsport(), diff, maxDiff));
				map.put("fishingArray", getRebateArray(ps.getFishing(), diff, maxDiff));
				map.put("lotteryArray", getRebateArray(ps.getLottery(), diff, maxDiff));
			}
		}
	}

	private static void putDefaultProxyRebateArray(Map<String, Object> map, StationRebate sr, BigDecimal diff,
			BigDecimal maxDiff) {
		// key不能修改
		map.put("liveArray", getRebateArray(sr.getLive(), diff, maxDiff));
		map.put("egameArray", getRebateArray(sr.getEgame(), diff, maxDiff));
		map.put("sportArray", getRebateArray(sr.getSport(), diff, maxDiff));
		map.put("chessArray", getRebateArray(sr.getChess(), diff, maxDiff));
		map.put("esportArray", getRebateArray(sr.getEsport(), diff, maxDiff));
		map.put("fishingArray", getRebateArray(sr.getFishing(), diff, maxDiff));
		map.put("lotteryArray", getRebateArray(sr.getLottery(), diff, maxDiff));
	}



	/**
	 * 功能：随机指定范围内N个不重复的数
	 *
	 * @param max 指定范围最大值
	 * @param min 指定范围最小值
	 * @param n 随机数个数
	 */
	public static int[] randomArray(int min, int max, int n) {
		int len = max - min + 1;

		if (max < min || n > len) {
			return null;
		}
		// 初始化给定范围的待选数组
		int[] source = new int[len];
		for (int i = min; i < min + len; i++) {
			source[i - min] = i;
		}
		int[] result = new int[n];
		Random rd = new Random();
		int index = 0;
		for (int i = 0; i < result.length; i++) {
			// 待选数组0到(len-2)随机一个下标
			index = Math.abs(rd.nextInt() % len--);
			// 将随机到的数放入结果集
			result[i] = source[index];
			// 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
			source[index] = source[len];
		}
		return result;
	}
}
