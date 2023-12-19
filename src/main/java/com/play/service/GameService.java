package com.play.service;

import com.play.model.app.LobbyGame;
import com.play.model.vo.AppThirdGameVo;

import java.util.List;

public interface GameService {

    public List<LobbyGame> getLobbyGames(Integer type, Integer limitNum, Integer ver);
    public List<AppThirdGameVo> getLotGames(Integer type,Integer limitNum,Integer ver, String lan);
    public List<LobbyGame> getLobbyGamesNew(Integer type, Integer limitNum, Integer ver);
    public List<LobbyGame> getLobbyGamesNew(Integer type, Integer limitNum, Integer ver,String lan);

    public List<AppThirdGameVo> getTabGames(Integer type, Integer limitNum, Integer ver);

    public List<AppThirdGameVo> getTabGamesNew(Long stationId, Integer type, Integer limitNum, Integer ver);
    public List<AppThirdGameVo> getTabGamesNew(Long stationId, Integer type, Integer limitNum, Integer ver,String lan);
    public List<AppThirdGameVo> getFactoryGames(String platformType,Integer limitNum,Integer ver,String lan,Integer pageSize,Integer pageIndex);

    public List<AppThirdGameVo> searchForGames(String keyword, Integer pageIndex, Integer pageSize);

}
