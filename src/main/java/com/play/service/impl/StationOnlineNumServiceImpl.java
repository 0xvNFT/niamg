package com.play.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.play.dao.StationOnlineNumDao;
import com.play.model.Station;
import com.play.model.StationOnlineNum;
import com.play.service.StationOnlineNumService;
import com.play.service.StationService;
import com.play.web.user.online.OnlineManager;
import com.play.web.user.online.OnlineUserNumTool;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 *
 */
@Service
public class StationOnlineNumServiceImpl implements StationOnlineNumService {

	@Autowired
	private StationOnlineNumDao stationOnlineNumDao;

	@Autowired
	private StationService stationService;

	/**
	 * 每次保存上一分钟的数据
	 */
	@Override
	public void handleOnlineNum(List<Long> stationIds) {
		int minuteNum = 0;
		int dayNum = 0;
		Integer onlineNum = 0;
		Calendar c = Calendar.getInstance();
		int second = c.get(Calendar.SECOND);
		Date date = c.getTime();
		c.add(Calendar.MINUTE, -1);
		Date last=c.getTime();
		for (Long stationId : stationIds) {
			if (second < 11) {
				// 上一分钟 登录人数
				minuteNum = OnlineUserNumTool.getMinuteOnlineNum(stationId, last);
				// 今天总登录人数
				dayNum = OnlineUserNumTool.getDayOnlineNum(stationId, last);
				onlineNum = OnlineManager.getOnlineCount(stationId, null);
				stationOnlineNumDao.updateNum(last, stationId, onlineNum, minuteNum, dayNum);
			}
			// 当前这分钟登录人数
			minuteNum = OnlineUserNumTool.getMinuteOnlineNum(stationId, date);
			// 今天总登录人数
			dayNum = OnlineUserNumTool.getDayOnlineNum(stationId, date);
			onlineNum = OnlineManager.getOnlineCount(stationId, null);
			stationOnlineNumDao.updateNum(date, stationId, onlineNum, minuteNum, dayNum);
		}
	}

	@Override
	public int getOnlineNum(Long stationId, Date date) {
		return stationOnlineNumDao.getOnlineNum(date, stationId);
	}

	@Override
	public JSONObject adminEchartsData(Long stationId, Date begin, Date end) {
		List<StationOnlineNum> onlineNums = null;
		boolean day = false;// false按分钟统计
		if ((end.getTime() - begin.getTime()) / 1000 >= 86400) {
			day = true;// 按天统计
			onlineNums = stationOnlineNumDao.getDataByDay(stationId, begin, end);
		} else {// 按分钟，只统计一天的数据
			onlineNums = stationOnlineNumDao.getDataByMinute(stationId, begin, end);
		}
		JSONObject result = new JSONObject();
		String[] dayData = new String[onlineNums.size()];
		int[] onlineNum = new int[onlineNums.size()];
		int[] loginNum = new int[onlineNums.size()];
		int i = 0;
		for (StationOnlineNum n : onlineNums) {
			onlineNum[i] = onlineNums.get(i).getOnlineNum();
			if (day) {
				dayData[i] = n.getStatDate();
				loginNum[i] = onlineNums.get(i).getDayLoginNum();
			} else {
				dayData[i] = n.getStatMinute();
				loginNum[i] = onlineNums.get(i).getMinuteLoginNum();
			}
			i++;
		}
		result.put("dayDate", dayData);
		result.put("onlineNum", onlineNum);
		result.put("loginNum", loginNum);
		return result;
	}

	@Override
	public JSONObject managerEchartsData(Date begin, Date end) {
		JSONObject result = new JSONObject();
		List<Station> stations = stationService.getAll();
		List<StationOnlineNum> onlineNums = null;
		boolean day = false;// false按分钟统计
		if ((end.getTime() - begin.getTime()) / 1000 >= 86400) {
			day = true;// 按天统计
			onlineNums = stationOnlineNumDao.getDataByDay(null, begin, end);
		} else {
			onlineNums = stationOnlineNumDao.getDataByMinute(null, begin, end);
		}
		List<String> days = new ArrayList<>();
		Map<String, Integer> numMap = filterByStation(day, days, onlineNums);
		List<Integer> dayOnline = null;
		for (Station s : stations) {
			dayOnline = new ArrayList<>();
			for (String d : days) {
				dayOnline.add(numMap.getOrDefault(getMapKey(d, s.getId()), 0));
			}
			result.put(s.getCode(), dayOnline);
		}
		result.put("dayData", days.toArray());
		result.put("stations", stations);
		return result;
	}

	private Map<String, Integer> filterByStation(boolean day, List<String> days, List<StationOnlineNum> onlineNums) {
		Map<String, Integer> map = new HashMap<>();
		String key = null;
		Integer nums = null;
		for (StationOnlineNum num : onlineNums) {
			if (day) {
				if (!days.contains(num.getStatDate())) {
					days.add(num.getStatDate());
				}
				key = getMapKey(num.getStatDate(), num.getStationId());
			} else {
				if (!days.contains(num.getStatMinute())) {
					days.add(num.getStatMinute());
				}
				key = getMapKey(num.getStatMinute(), num.getStationId());
			}
			nums = map.get(key);
			if (nums == null) {
				map.put(key, 0);
				nums = 0;
			}
			if (num.getOnlineNum().compareTo(nums) > 0) {
				map.put(key, num.getOnlineNum());
			}
		}
		return map;
	}

	private String getMapKey(String stat, Long stationId) {
		return new StringBuilder(stat).append("__").append(stationId).toString();
	}
}
