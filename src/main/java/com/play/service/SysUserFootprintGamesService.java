package com.play.service;

import java.util.List;

import com.play.model.SysUserFootprintGames;
import com.play.model.dto.SysUserFootprintGamesDto;

/**
 *
 */
public interface SysUserFootprintGamesService {

	public List<SysUserFootprintGames> find(Long stationId, Long userId, Integer gameType, String platform);

	public void tryUpdate(Long stationId, Long userId, Integer gameType, String platform, String gameCode, Integer isSubGame);

	public List<SysUserFootprintGamesDto> getGameList(Long stationId, Long userId);
}
