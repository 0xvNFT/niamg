package com.play.service;

import com.play.model.ThirdGame;
import com.play.orm.jdbc.page.Page;

/**
 * 游戏大开关
 *
 * @author admin
 *
 */
public interface ThirdGameService {
	void init(Long stationId, Long partnerId);

	ThirdGame findOne(Long stationId);

	Page<ThirdGame> page(Long stationId);

	void changeStatus(Long stationId, String type, Integer status);

}