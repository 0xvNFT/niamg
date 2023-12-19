package com.play.service;

import java.util.List;

import com.play.model.StationRegisterConfig;

/**
 * 站点注册页面有哪些字段
 *
 * @author admin
 *
 */
public interface StationRegisterConfigService {

	void init(Long stationId);

	List<StationRegisterConfig> initAndGetList(Long stationId, Integer platform);

	void updateProp(Long id, Long stationId, String prop, Integer value);

	StationRegisterConfig findOne(Long id, Long stationId);

	void updateSortNo(Long id, Long stationId, Long sortNo, String name, String tips);

	List<StationRegisterConfig> find(Long stationId, Integer platform, Integer show);

}