package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.ThirdGame;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 游戏大开关
 *
 * @author admin
 *
 */
@Repository
public class ThirdGameDao extends JdbcRepository<ThirdGame> {

	public ThirdGame findOne(Long stationId) {
		String key = getKey(stationId);
		ThirdGame tg = CacheUtil.getCache(CacheKey.THIRD_PLATFORM, key, ThirdGame.class);
		if (tg != null) {
			return tg;
		}

		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		tg = findOne("select * from third_game where station_id=:stationId", map);
		if (tg != null) {
			CacheUtil.addCache(CacheKey.THIRD_PLATFORM, key, tg);
		}
		return tg;
	}

	private String getKey(Long stationId) {
		return new StringBuilder("g:").append(stationId).toString();
	}

	public Page<ThirdGame> page(Long stationId) {
		if (stationId != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("stationId", stationId);
			return queryByPage("select * from third_game where station_id=:stationId", map);
		}
		return queryByPage("select * from third_game order by station_id desc");
	}

	public void changeStatus(Long stationId, String type, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("status", status);
		switch (type) {
		case "live":
			update("update third_game set live=:status where station_id=:stationId", map);
			break;
		case "egame":
			update("update third_game set egame=:status where station_id=:stationId", map);
			break;
		case "chess":
			update("update third_game set chess=:status where station_id=:stationId", map);
			break;
		case "fishing":
			update("update third_game set fishing=:status where station_id=:stationId", map);
			break;
		case "esport":
			update("update third_game set esport=:status where station_id=:stationId", map);
			break;
		case "sport":
			update("update third_game set sport=:status where station_id=:stationId", map);
			break;
		case "lottery":
			update("update third_game set lottery=:status where station_id=:stationId", map);
			break;
		}
		CacheUtil.delCache(CacheKey.THIRD_PLATFORM, getKey(stationId));
	}

}
