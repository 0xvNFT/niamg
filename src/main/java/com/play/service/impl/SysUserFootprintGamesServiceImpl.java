package com.play.service.impl;

import java.util.ArrayList;
import java.util.List;


import com.alibaba.fastjson.JSONArray;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.app.GameItemResult;
import com.play.model.dto.SysUserFootprintGamesDto;
import com.play.model.vo.AppThirdGameVo;
import com.play.service.StationHomepageGameService;
import com.play.web.utils.app.NativeUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserFootprintGamesDao;
import com.play.model.SysUserFootprintGames;
import com.play.service.SysUserFootprintGamesService;
import org.springframework.util.CollectionUtils;

/**
 *
 */
@Service
public class SysUserFootprintGamesServiceImpl implements SysUserFootprintGamesService {
	//private Logger logger = LoggerFactory.getLogger(SysUserFootprintGamesServiceImpl.class);

	@Autowired
	private SysUserFootprintGamesDao sysUserFootprintGamesDao;

	@Autowired
	private StationHomepageGameService stationHomepageGameService;

	@Override
	public List<SysUserFootprintGames> find(Long stationId, Long userId, Integer gameType, String platform) {
		// TODO Auto-generated method stub
		return sysUserFootprintGamesDao.find(stationId, userId, gameType, platform);
	}

	@Override
	public void tryUpdate(Long stationId, Long userId, Integer gameType, String platform, String gameCode, Integer isSubGame) {
		// TODO Auto-generated method stub
		if (stationId == null || userId == null || gameType == null || platform == null || StringUtils.isBlank(gameCode)) {
			return;
		}
		SysUserFootprintGames record = sysUserFootprintGamesDao.findOne(stationId, userId, gameType, platform, gameCode);
		if (record == null) {
			sysUserFootprintGamesDao.insert(stationId, userId, gameType, platform, gameCode, isSubGame);
		} else {
			sysUserFootprintGamesDao.update(stationId, userId, gameType, platform, gameCode, 1);
		}
	}

	@Override
	public List<SysUserFootprintGamesDto> getGameList(Long stationId, Long userId) {
		// 先从缓存取
		String key = new StringBuilder("userFootGameList:").append(stationId).append(":").append(userId).toString();
		String cacheStr = CacheUtil.getCache(CacheKey.USER_FOOT_GAME_LIST, key, String.class);
		if (StringUtils.isNotBlank(cacheStr)) {
			return JSONArray.parseArray(cacheStr, SysUserFootprintGamesDto.class);
		}

		// 若缓存未取到数据，再从DB查询
		List<SysUserFootprintGames> printGamesList = this.find(stationId, userId, null, null);
		if (CollectionUtils.isEmpty(printGamesList)) {
			return new ArrayList<>();
		}

		List<SysUserFootprintGamesDto> tempList = new ArrayList<>();
		for (SysUserFootprintGames games : printGamesList) {
			SysUserFootprintGamesDto dto = new SysUserFootprintGamesDto();
			BeanUtils.copyProperties(games, dto);
			tempList.add(dto);
		}

		List<SysUserFootprintGamesDto> list = new ArrayList<>();
		for (SysUserFootprintGamesDto dto : tempList) {
			//如果不是二级子游戏，需要获取游戏的图标和跳转地址
			if (dto.getIsSubGame() != 1 && StringUtils.isEmpty(dto.getThirdGameUrl())) {
				Integer tabGameType = NativeUtils.convertTabGameType2HotGameType(dto.getGameType());
				List<AppThirdGameVo> games = NativeUtils.getOtherGame(tabGameType,null);
				if (games != null && !games.isEmpty()) {
					for (AppThirdGameVo game : games) {
						if (game.getGameType() != null && !org.springframework.util.StringUtils.isEmpty(game.getCzCode())) {
							if (game.getGameType().equalsIgnoreCase(dto.getPlatform()) ||
									game.getCzCode().equalsIgnoreCase(dto.getPlatform())) {
								dto.setGameName(game.getName());
								dto.setImageUrl(game.getImgUrl());
								dto.setThirdGameUrl(game.getForwardUrl());
								break;
							}
						}
					}
				}
			}

			// 如果新增游戏为二级子游戏，如：MG电子、PT电子。通过Json配置文件获取游戏URL信息
			if (dto.getIsSubGame() == 1) {
				List<GameItemResult> gameList = stationHomepageGameService.getGameDataList(dto.getPlatform());

				for(int i = 0; i < gameList.size(); i++) {
					if(dto.getGameCode().equals(gameList.get(i).getType())) {
						dto.setGameName(gameList.get(i).getName());
						dto.setImageUrl(gameList.get(i).getButtonImagePath());
						dto.setThirdGameUrl(gameList.get(i).getFinalRelatveUrl());
						break;
					}
				}

			}

			list.add(dto);
		}

		// 写入缓存
		CacheUtil.addCache(CacheKey.USER_FOOT_GAME_LIST, key, list, 60);

		return list;
	}
}
