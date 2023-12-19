package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.SysUserFootprintGames;
import com.play.orm.jdbc.JdbcRepository;

@Repository
public class SysUserFootprintGamesDao extends JdbcRepository<SysUserFootprintGames> {
	public List<SysUserFootprintGames> find(Long stationId, Long userId, Integer gameType, String platform) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("gameType", gameType);
		map.put("platform", platform);
		StringBuilder sql = new StringBuilder("select * from sys_user_footprint_games");
		sql.append(" where station_id=:stationId AND user_id=:userId");
		if (gameType != null) {
			sql.append(" AND game_type=:gameType ");
		}
		if (StringUtils.isNoneBlank(platform)) {
			sql.append(" AND platform=:platform ");
		}
		sql.append(" order by login_times desc limit 10");
		return find(sql.toString(), map);
	}

	public SysUserFootprintGames findOne(Long stationId, Long userId, Integer gameType, String platform, String gameCode) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("gameType", gameType);
		map.put("platform", platform);
		map.put("gameCode", gameCode);
		StringBuilder sql = new StringBuilder("select * from sys_user_footprint_games");
		sql.append(" where station_id=:stationId AND  user_id=:userId AND  game_type=:gameType");
		sql.append(" AND  platform=:platform AND game_code=:gameCode");
		return findOne(sql.toString(), map);
	}

	public void insert(Long stationId, Long userId, Integer gameType, String platform, String gameCode, Integer isSubGame) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("gameType", gameType);
		map.put("platform", platform);
		map.put("gameCode", gameCode);
		map.put("isSubGame", isSubGame);
		StringBuilder sql = new StringBuilder("INSERT INTO sys_user_footprint_games");
		sql.append(" (station_id, user_id, game_type, platform, game_code, is_sub_game, login_times) ");
		sql.append(" values(:stationId, :userId, :gameType, :platform, :gameCode, :isSubGame, 1)");
		find(sql.toString(), map);
	}

	public void update(Long stationId, Long userId, Integer gameType, String platform, String gameCode, Integer addTimes) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("gameType", gameType);
		map.put("platform", platform);
		map.put("gameCode", gameCode);
		map.put("addTimes", addTimes);
		StringBuilder sql = new StringBuilder("UPDATE sys_user_footprint_games SET");
		sql.append(" login_times=COALESCE(login_times,0) + :addTimes ");
		sql.append(" where station_id=:stationId AND user_id=:userId AND game_type=:gameType");
		sql.append(" AND platform=:platform AND game_code=:gameCode");
		update(sql.toString(), map);
	}
}
