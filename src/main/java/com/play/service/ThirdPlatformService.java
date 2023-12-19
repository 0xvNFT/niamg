package com.play.service;

import java.util.List;

import com.play.model.ThirdPlatform;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.ThirdPlatformVo;
import com.play.orm.jdbc.page.Page;

/**
 * 第三方游戏开关
 *
 * @author admin
 *
 */
public interface ThirdPlatformService {
	// 这个接口只是去初始化 Evolution而已（临时接口..）
	void initEvolution(Long stationId, Long partnerId,Integer p);
	
	void init(Long stationId, Long partnerId);

	Page<ThirdPlatform> page(Long stationId, Integer platform);

	void changeStatus(Long id, Integer status);

	List<ThirdPlatformVo> find(Long stationId);
	List<ThirdPlatformVo> findValid(Long stationId);

	ThirdPlatformSwitch getPlatformSwitch(Long stationId);

	ThirdPlatform findOne(Long stationId, Integer platform);

}