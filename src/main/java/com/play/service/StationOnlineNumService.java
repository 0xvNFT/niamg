package com.play.service;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 站点每日最高在线统计
 *
 * @author admin
 *
 */
public interface StationOnlineNumService {

	/**
	 * 站点在线人数处理
	 *
	 * @return
	 */
	void handleOnlineNum(List<Long> stationIds);

	/**
	 * 获取站点最高在线人数
	 * 
	 * @param stationId
	 * @param now
	 * @return
	 */
	int getOnlineNum(Long stationId, Date date);

	/**
	 * 租户后台图表数据获取
	 *
	 * @param stationId
	 * @param begin
	 * @param end
	 * @return
	 */
	JSONObject adminEchartsData(Long stationId, Date begin, Date end);

	/**
	 * 总控图标数据获取
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	JSONObject managerEchartsData(Date begin, Date end);
}