package com.play.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.core.HomepageGameTabEnum;
import com.play.core.LanguageEnum;
import com.play.core.ModuleEnum;
import com.play.core.PlatformType;
import com.play.model.AppTab;
import com.play.model.StationHomepageGame;
import com.play.model.ThirdGame;
import com.play.model.app.GameItemResult;
import com.play.model.app.LobbyGame;
import com.play.model.dto.StationHomepageGameDto;
import com.play.model.vo.AppThirdGameVo;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.app.NativeJsonUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

import java.util.List;




@Service
public class GameServiceImpl implements GameService {

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    AppTabService appTabService;
    @Autowired
    ThirdGameService thirdGameService;
    @Autowired
    YGCenterService ygCenterService;
    @Autowired
    ThirdCenterService thirdCenterService;
    @Autowired
    StationHomepageGameService stationHomepageGameService;

    public List<LobbyGame> getLobbyGamesNew(Integer type, Integer limitNum, Integer ver) {
        return getLobbyGamesNew(type, limitNum, ver, null);
    }
    @Override
    public List<LobbyGame> getLobbyGamesNew(Integer type, Integer limitNum, Integer ver,String lan) {
        List<LobbyGame> lobbys = new ArrayList<>();
        List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),lan);
        if (limitNum == null) {
            limitNum = 6;
        }
        for (int i = 0; i < tabs.size(); i++) {
            LobbyGame lobby = new LobbyGame();
            AppTab tab = tabs.get(i);
            tab.setIndex(i);
            lobby.setTab(tab);
            List<AppThirdGameVo> otherGame = getTabGamesNew(tab.getStationId(), tab.getType(), limitNum, ver, lan);
            lobby.setGames(otherGame);
            lobbys.add(lobby);
        }
        return lobbys;
    }

    @Override
    public List<LobbyGame> getLobbyGames(Integer type, Integer limitNum, Integer ver) {
        List<LobbyGame> lobbys = new ArrayList<>();
        List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),null);
        ThirdGame thirdGame = thirdGameService.findOne(SystemUtil.getStationId());
        for (int i = 0; i < tabs.size(); i++) {
            LobbyGame lobby = new LobbyGame();
            AppTab tab = tabs.get(i);
            tab.setIndex(i);
            lobby.setTab(tab);
            List<AppThirdGameVo> otherGame = null;
            if (tab.getType() == 3 && thirdGame.getLottery() == 2) {//获取彩票
                try{
                    otherGame = getLotGames(tab.getType(), 6, ver);
                }catch (Exception e){
                    otherGame = new ArrayList<>();
                    logger.error("get lottery error = ",e);
                }
            } else if (tab.getType() == ModuleEnum.recommend.getType()) {//平台推荐
                tab.setGameType(PlatformType.PG.name());
                otherGame = getOtherGames(tab.getType(), PlatformType.PG.name(), 6, 1);
            } else if (tab.getType() == ModuleEnum.featured.getType()) {//精选游戏
                tab.setGameType(PlatformType.JDB.name());
                otherGame = getOtherGames(tab.getType(), PlatformType.JDB.name(), 6, 1);
            } else if (tab.getType() == ModuleEnum.inhouse.getType()) {//自研游戏 in-house
                tab.setGameType(PlatformType.BS.name());
                otherGame = getOtherGames(tab.getType(), PlatformType.BS.name(), 6, 1);
            } else if (tab.getType() == ModuleEnum.favorite.getType()) {// 我的收藏
                tab.setGameType(PlatformType.TADA.name());
                otherGame = getOtherGames(tab.getType(), PlatformType.TADA.name(), 6, 1);
            } else if (tab.getType() == ModuleEnum.hotsport.getType()) {// 精选体育
                tab.setGameType(PlatformType.PP.name());
                otherGame = getOtherGames(tab.getType(), PlatformType.PP.name(), 6, 1);
            } else {
                otherGame = NativeUtils.getOtherGame(tab.getType(), ver);
                if (otherGame != null && otherGame.size() > 6) {
                    otherGame = otherGame.subList(0, 6);
                }
            }
            if (otherGame != null) {
                lobby.setGames(otherGame);
            }
            lobbys.add(lobby);
        }
        return lobbys;
    }

    @Override
    public List<AppThirdGameVo> getLotGames(Integer type, Integer limitNum, Integer ver, String lan) {
        return getLotGames(type, limitNum, ver);
    }
    private List<AppThirdGameVo> getLotGames(Integer type,Integer limitNum,Integer ver) {
        List<AppThirdGameVo> lotGames = NativeUtils.getOtherGame(type,ver);
        List<AppThirdGameVo> datas = new ArrayList<>();
        if(lotGames != null && !lotGames.isEmpty()){
            for (AppThirdGameVo lot : lotGames) {
                if (lot.getGameType().equalsIgnoreCase(PlatformType.IYG.name())) {
                    JSONObject lotteryList = thirdCenterService.getLotteryList(PlatformType.IYG.getValue(), SystemUtil.getStationId(), 2);
                    if (lotteryList != null) {
                        JSONArray lots = lotteryList.getJSONArray("lotInfo");
                        for (int i = 0; i < lots.size(); i++) {
                            AppThirdGameVo data = new AppThirdGameVo();
                            JSONObject lotGame = lots.getJSONObject(i);
                            String lotCode = lotGame.getString("lotCode");
                            String groupCode = lotGame.getString("lotGroup");
                            Integer lotVersion = lotGame.getInteger("lotVersion");
                            String lotIcon = lotGame.getString("lotIcon");
                            String lotName = lotGame.getString("lotName");
                            StringBuilder sb = new StringBuilder();
                            sb.append(lot.getForwardUrl());
                            if (sb.toString().contains("?")) {
                                sb.append("&lotCode=").append(lotCode);
                            } else {
                                sb.append("?lotCode=").append(lotCode);
                            }
                            sb.append("&lotVersion=V").append(lotVersion);
                            sb.append("&groupCode=").append(groupCode);
                            data.setForwardUrl(sb.toString());
                            data.setLotVersion(lotVersion);
                            data.setName(lotName);
                            String lotImg = "/common/third/images/lottery/iyg/" + lotCode + ".png";
                            data.setImgUrl(StringUtils.isNotEmpty(lotIcon) ? lotIcon : lotImg);
                            data.setGameType(lot.getGameType());
                            data.setCzCode(lot.getCzCode());
                            data.setLotCode(lotCode);
                            datas.add(data);
                        }
                    }
                } else if (lot.getGameType().equalsIgnoreCase(PlatformType.YG.name())) {
                    JSONObject lotteryList = thirdCenterService.getLotteryList(PlatformType.YG.getValue(), SystemUtil.getStationId(), 2);
                    if (lotteryList != null) {
                        JSONArray lots = lotteryList.getJSONArray("lotInfo");
                        for (int i = 0; i < lots.size(); i++) {
                            AppThirdGameVo data = new AppThirdGameVo();
                            JSONObject lotGame = lots.getJSONObject(i);
                            String lotCode = lotGame.getString("lotCode");
                            Integer lotVersion = lotGame.getInteger("lotVersion");
                            String lotIcon = lotGame.getString("lotIcon");
                            String lotName = lotGame.getString("lotName");
                            StringBuilder sb = new StringBuilder();
                            sb.append(lot.getForwardUrl());
                            if (sb.toString().contains("?")) {
                                sb.append("&lotCode=").append(lotCode);
                            } else {
                                sb.append("?lotCode=").append(lotCode);
                            }
                            sb.append("&lotVersion=V").append(lotVersion);
                            data.setForwardUrl(sb.toString());
                            data.setLotVersion(lotVersion);
                            data.setName(lotName);
                            String lotImg = "/common/third/images/lottery/yg/" + lotCode + ".png";
                            data.setImgUrl(!StringUtils.isEmpty(lotIcon) ? lotIcon : lotImg);
                            data.setGameType(lot.getGameType());
                            data.setCzCode(lot.getCzCode());
                            data.setLotCode(lotCode);
                            datas.add(data);
                        }
                    }
                }
            }
        }
        if (limitNum != null && datas.size() > limitNum) {
            datas = datas.subList(0, limitNum);
        }
        return datas;
    }
    private List<AppThirdGameVo> getOtherGames(Integer tabType,String gameType,Integer pageSize,Integer pageIndex){
        List<GameItemResult> pp = NativeUtils.gameDatas(ServletUtils.getRequest(), gameType, pageSize, pageIndex);
        List<AppThirdGameVo> datas = new ArrayList<>();
        for (GameItemResult data : pp) {
            AppThirdGameVo d = new AppThirdGameVo();
            d.setName(data.getDisplayName());
            d.setCzCode(gameType);
            d.setImgUrl(data.getButtonImagePath());
            d.setModuleCode(1);
            d.setGameType(gameType);
            d.setGameTabType(tabType);
            d.setForwardUrl(data.getFinalRelatveUrl());
            d.setIsListGame(0);
            datas.add(d);
        }
        return datas;
    }

    @Override
    public List<AppThirdGameVo> getTabGames(Integer type, Integer limitNum, Integer ver) {
        List<AppThirdGameVo> goodGames = new ArrayList<>();
        if (type == ModuleEnum.featured.getType()) {
            goodGames = getOtherGames(type, PlatformType.JDB.name(), null, null);
        }else if (type == ModuleEnum.inhouse.getType()) {
            goodGames = getOtherGames(type, PlatformType.BS.name(), null, null);
        }else if (type == ModuleEnum.recommend.getType()) {
            goodGames = getOtherGames(type, PlatformType.PG.name(), null, null);
        }else if (type == ModuleEnum.hotsport.getType()) {
            goodGames = getOtherGames(type, PlatformType.PP.name(), null, null);
        }else if (type == ModuleEnum.favorite.getType()) {
            goodGames = getOtherGames(type, PlatformType.TADA.name(), null, null);
        }else if (type == ModuleEnum.lottery.getType()) {
            goodGames = getLotGames(type,6,ver);//彩票目前固定使用内部的YG彩票
        }else{
            goodGames = NativeUtils.getOtherGame(type,ver);
        }
        return goodGames;
    }

    public List<AppThirdGameVo> getTabGamesNew(Long stationId, Integer type, Integer limitNum, Integer ver) {
        return getTabGamesNew(stationId, type, limitNum, ver, null);
    }
    public List<AppThirdGameVo> getTabGamesNew(Long stationId, Integer type, Integer limitNum, Integer ver,String lan) {
        List<AppThirdGameVo> goodGames = null;
        AppTab appTab = appTabService.getAppTab(stationId, type);
        if (!ObjectUtils.isEmpty(appTab) && HomepageGameTabEnum.isExist(type)) {
            StationHomepageGameDto dto = new StationHomepageGameDto();
            dto.setStationId(stationId);
            dto.setGameTabId(appTab.getId());
            List<StationHomepageGame> list = stationHomepageGameService.getList(dto);
            goodGames = new ArrayList<>();
            for (StationHomepageGame game : list) {
                AppThirdGameVo vo = new AppThirdGameVo();
                vo.setCzCode(game.getParentGameCode());
                vo.setImgUrl(game.getImageUrl());
                vo.setModuleCode(1);
                vo.setGameType(game.getParentGameCode());
                vo.setName(game.getGameName());
                vo = getNameByLan(vo, lan);
                vo.setGameTabType(type);
                vo.setForwardUrl(game.getThirdGameUrl());
                vo.setIsListGame(0);
                goodGames.add(vo);
            }
            if (limitNum != null && goodGames.size() > limitNum) {
                goodGames = goodGames.subList(0, limitNum);
            }
        } else if (type == ModuleEnum.lottery.getType()) {
            goodGames = getLotGames(type, /*limitNum != null ? limitNum : 9*/limitNum, ver, lan);
        } else {
            goodGames = NativeUtils.getOtherGame(type, ver,lan);
        }
        return goodGames;
    }

    public List<AppThirdGameVo> getFactoryGames(String platformType,Integer limitNum,Integer ver,String lan,Integer pageSize,Integer pageIndex){
        List<GameItemResult> gameItemResults = NativeUtils.gameDatas(ServletUtils.getRequest(), platformType, pageSize, pageIndex);
        List<AppThirdGameVo> goodGames = new ArrayList<>();
        for (GameItemResult game : gameItemResults) {
            AppThirdGameVo vo = new AppThirdGameVo();
            vo.setCzCode(platformType);
            vo.setImgUrl(game.getButtonImagePath());
            vo.setModuleCode(1);
            vo.setGameType(platformType);
            vo.setName(game.getDisplayName());
//            vo = getNameByLan(vo, lan);
//            vo.setGameTabType(type);
            vo.setForwardUrl(game.getFinalRelatveUrl());
            vo.setIsListGame(0);
            vo.setIsApp(game.getIsApp()==null?"":game.getIsApp());// 1表示仅支持APP,2 不写表示什么都支持 APP和PC都支持,3表示仅pc
            goodGames.add(vo);
        }
        if (limitNum != null && goodGames.size() > limitNum) {
            goodGames = goodGames.subList(0, limitNum);
        }
        return goodGames;
    }

    private AppThirdGameVo getNameByLan(AppThirdGameVo vo,String lan){
//        logger.error("getname by lan,lan === " + lan);
        if (StringUtils.isEmpty(lan)) {
            return vo;
        }
        LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
        if (lanEnum == null) {
            return vo;
        }
        if (StringUtils.isEmpty(vo.getType())) {
            return vo;
        }
        if (vo.getType().equals("3")) {//电子
            if (vo.getGameType().equals("pt")) {
                vo.setName(I18nTool.getMessage("admin." + vo.getGameType() + ".ele",lanEnum.getLocale()));
            }else if (vo.getGameType().equals("cp9")) {
                vo.setName(I18nTool.getMessage("admin.nine.ele",lanEnum.getLocale()));
            }else{
                vo.setName(I18nTool.getMessage("admin." + vo.getGameType() + ".electronic",lanEnum.getLocale()));
            }
        }else if (vo.getType().equals("2")) {//真人
            vo.setName(I18nTool.getMessage("admin." + vo.getGameType() + ".hall",lanEnum.getLocale()));
        }else if (vo.getType().equals("5")) {//电竞
            if (vo.getGameType().equals("avia")) {
                vo.setName(I18nTool.getMessage("admin.asia.esport",lanEnum.getLocale()));
            }
        }else if (vo.getType().equals("6")) {//捕鱼
            if (vo.getGameType().equals("bbin")) {
                vo.setName(I18nTool.getMessage("admin.bbin.fish.game.hall",lanEnum.getLocale()));
            }else if (vo.getGameType().equals("cq9")) {
                vo.setName(I18nTool.getMessage("admin.nine.fish.game",lanEnum.getLocale()));
            }
        }else if (vo.getType().equals("7")) {//棋牌
            if (vo.getGameType().equals("ky")) {
                vo.setName(I18nTool.getMessage("PlatformVo.KY",lanEnum.getLocale()));
            }else if (vo.getGameType().equals("leg")) {
                vo.setName(I18nTool.getMessage("PlatformVo.LEG",lanEnum.getLocale()));
            }
        }else if (vo.getType().equals("4")) {//体育
            if (vo.getGameType().equals("ppSport")) {
                vo.setName(I18nTool.getMessage("admin.pp.sports",lanEnum.getLocale()));
            } else if (vo.getGameType().equals("tysb")) {
                vo.setName(I18nTool.getMessage("admin.sad.sports",lanEnum.getLocale()));
            } else {
                vo.setName(I18nTool.getMessage("admin." + vo.getGameType() + ".sports",lanEnum.getLocale()));
            }
        }
        return vo;
    }

    @Override
    public List<AppThirdGameVo> searchForGames(String keyword, Integer pageIndex, Integer pageSize) {
        if (StringUtils.isEmpty(keyword)) {
            throw new BaseException("Keyword can not empty");
        }
        if (keyword.length() < 3) {
            throw new BaseException("Search requires at least 3 characters");
        }
        keyword = keyword.toLowerCase();
        List<AppThirdGameVo> searchResults = null;
        String key = String.format("%s_%s_%s_%s", "native_", SystemUtil.getStationId(), "/v2/getRelativeGames",
                keyword);
        String searchResultString = "";
      //  String searchResultString = CacheUtil.getCache(CacheKey.SEARCH_GAMES, key);
        if (StringUtils.isNotBlank(searchResultString)) {//不能使用缓存，否则前台切换语种的时候，内容没有根据语种变化
            searchResults = NativeJsonUtil.toList(searchResultString, AppThirdGameVo.class);
        } else {
            searchResults = new ArrayList<>();
            List<Integer> types = handleSwitch();
            for (Integer type : types) {
                List<AppThirdGameVo> games = NativeUtils.getOtherGame(type,null);
                if (games != null && !games.isEmpty()) {
                    for (AppThirdGameVo data : games) {
                        if (data.getIsListGame() != null && data.getIsListGame() == 1) {
                            List<GameItemResult> gameItemResults = NativeUtils.gameDatas(ServletUtils.getRequest(), data.getCzCode(), null, null);
                            for (GameItemResult result : gameItemResults) {
                                AppThirdGameVo vo = new AppThirdGameVo();
                                vo.setName(result.getDisplayName());
                                vo.setImgUrl(result.getButtonImagePath());
                                vo.setModuleCode(1);
                                vo.setGameType(data.getCzCode());
                                vo.setGameTabType(type);
                                vo.setForwardUrl(result.getFinalRelatveUrl());
                                vo.setIsListGame(0);
                                searchResults.add(vo);
                            }
                        } else {
                            AppThirdGameVo vo = new AppThirdGameVo();
                            vo.setName(data.getName());
                            vo.setImgUrl(data.getImgUrl());
                            vo.setModuleCode(1);
                            vo.setGameType(data.getCzCode());
                            vo.setGameTabType(type);
                            vo.setForwardUrl(data.getForwardUrl());
                            vo.setIsListGame(0);
                            searchResults.add(vo);
                        }
                    }
                }
            }
        }
        if (!searchResults.isEmpty()){
            CacheUtil.addCache(CacheKey.SEARCH_GAMES, key, searchResults);
        }
        List<AppThirdGameVo> filterList = new ArrayList<>();
        for (AppThirdGameVo game : searchResults) {
            if (StringUtils.isNotEmpty(game.getName())) {
                String gameName = game.getName().toLowerCase();
                if (gameName.contains(keyword)){
                    filterList.add(game);
                }
            }
        }
        Integer gameSize = filterList.size();
        if (pageIndex != null && pageSize != null && gameSize > 0) {
            if (gameSize > (pageSize * pageIndex)) {
                filterList = filterList.subList((pageIndex - 1) * pageSize, pageIndex * pageSize);
            } else {
                filterList = filterList.subList(pageSize * (pageIndex-1), gameSize - 1);
            }
        }
        return filterList;
    }

    // 种类代码 0--体育 1-真人 2-电子 3--彩票 4--棋牌 5--红包 6--电竞 7--捕鱼
    private List<Integer> handleSwitch() {

        List<Integer> gameTypes = new ArrayList<>();
        ThirdGame thirdGame = thirdGameService.findOne(SystemUtil.getStationId());
        if (thirdGame.getSport() == 2) {
            gameTypes.add(0);
        }
        if (thirdGame.getEsport() == 2) {
            gameTypes.add(6);
        }
        if (thirdGame.getChess() == 2) {
            gameTypes.add(4);
        }
        if (thirdGame.getLive() == 2) {
            gameTypes.add(1);
        }
        if (thirdGame.getEgame() == 2) {
            gameTypes.add(2);
        }
        if (thirdGame.getFishing() == 2) {
            gameTypes.add(7);
        }
        return gameTypes;
    }
}