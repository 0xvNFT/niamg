package com.play.service;

import java.util.List;
import java.util.Set;

import com.play.model.StationConfig;
import com.play.model.dto.StationFakeData;

/**
 * 站点配置信息
 *
 * @author admin
 *
 */
public interface StationConfigService {

	void initStation(Long stationId, Long partnerId);

	Set<String> getAllSet(Long stationId);

	void saveSettings(Long stationId, String keys);

	List<StationConfig> getAll(Long stationId, Integer visible);

	void saveConfig(Long stationId, String key, String value);

	/**
	 * 获取站点假数据
	 * @param stationId
	 * @return
	 */
	StationFakeData getStationFakeData(Long stationId);

}